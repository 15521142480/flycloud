package com.fly.test.member.service;

import com.fly.common.elasticsearch.index.ElasticsearchAliasService;
import com.fly.common.elasticsearch.index.ElasticsearchIndexName;
import com.fly.common.elasticsearch.index.ElasticsearchIndexService;
import com.fly.common.elasticsearch.index.ElasticsearchMappingService;
import com.fly.common.elasticsearch.page.ElasticsearchPageExecutor;
import com.fly.common.elasticsearch.page.ElasticsearchPageRequest;
import com.fly.common.elasticsearch.projection.ElasticsearchProjectionWriter;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.exception.ServiceException;
import com.fly.common.rocketmq.constant.RocketMqTagConstants;
import com.fly.common.rocketmq.constant.RocketMqTopicConstants;
import com.fly.common.rocketmq.service.MqOutboxService;
import com.fly.member.mapper.MemberUserMapper;
import com.fly.system.api.member.domain.MemberUser;
import com.fly.test.member.converter.MemberUserDocumentConverter;
import com.fly.test.member.definition.MemberUserIndexDefinition;
import com.fly.test.member.domain.bo.MemberUserSearchPageBo;
import com.fly.test.member.domain.bo.MemberUserSearchUpdateBo;
import com.fly.test.member.domain.event.MemberUserIndexEvent;
import com.fly.test.member.domain.vo.MemberUserSearchVo;
import com.fly.test.member.document.MemberUserDocument;
import com.fly.test.member.field.MemberUserIndexFields;
import com.fly.test.member.query.MemberUserSearchQueryBuilder;
import com.fly.test.member.sync.MemberUserIndexSyncService;
import com.fly.test.member.upgrade.MemberUserIndexUpgradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/** 会员用户搜索业务编排；通用索引、查询与同步细节均已下沉公共/专属组件。 */
@Service
@RequiredArgsConstructor
@ConditionalOnExpression("'${flycloud.elasticsearch.enabled:false}' == 'true' and '${flycloud.rocketmq.enabled:false}' == 'true'")
public class MemberUserSearchService {

    private static final Map<String, String> SORT_FIELDS = Map.of(
            MemberUserIndexFields.ID, MemberUserIndexFields.ID,
            MemberUserIndexFields.CREATE_TIME, MemberUserIndexFields.CREATE_TIME,
            MemberUserIndexFields.UPDATE_TIME, MemberUserIndexFields.UPDATE_TIME);

    private final MemberUserIndexDefinition definition;
    private final ElasticsearchAliasService aliasService;
    private final ElasticsearchIndexService indexService;
    private final ElasticsearchMappingService mappingService;
    private final ElasticsearchPageExecutor pageExecutor;
    private final ElasticsearchProjectionWriter projectionWriter;
    private final MemberUserSearchQueryBuilder queryBuilder;
    private final MemberUserDocumentConverter converter;
    private final MemberUserIndexSyncService syncService;
    private final MemberUserIndexUpgradeService upgradeService;
    private final MemberUserMapper memberUserMapper;
    private final MqOutboxService outboxService;

    /**
     * 初始化真实版本索引（如不存在）并执行全量同步。
     *
     * @return 当前 Alias 指向的真实索引名称
     */
    public String fullSynchronize() {
        String index = aliasService.getCurrentIndex(definition.alias());
        if (index == null) {
            index = ElasticsearchIndexName.buildVersionedIndex(definition.alias(), definition.initialVersion());
            indexService.create(index, mappingService.load(definition.mappingResource()));
            aliasService.switchAlias(definition.alias(), null, index);
        }
        syncService.synchronize(index);
        return index;
    }

    /**
     * 执行会员用户 ES 条件分页查询。
     *
     * @param bo 业务查询条件
     * @return 标准分页响应
     */
    public PageVo<MemberUserSearchVo> searchPage(MemberUserSearchPageBo bo) {
        return pageExecutor.search(definition.alias(), new ElasticsearchPageRequest(bo.getPageNum(), bo.getPageSize(),
                bo.getSortField(), bo.getSortOrder(), SORT_FIELDS, null), queryBuilder.build(bo), MemberUserDocument.class, converter::toVo);
    }

    /**
     * 更新会员主数据，并在同一数据库事务内创建 ES 投影更新事件。
     *
     * @param bo 允许更新的会员字段
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateMemberUser(MemberUserSearchUpdateBo bo) {
        MemberUser user = requiredUser(bo.getId());
        if (bo.getNickname() != null) {
            user.setNickname(bo.getNickname());
        }
        if (bo.getName() != null) {
            user.setName(bo.getName());
        }
        if (bo.getMark() != null) {
            user.setMark(bo.getMark());
        }
        if (bo.getStatus() != null) {
            user.setStatus(bo.getStatus());
        }
        if (bo.getPostIds() != null) {
            user.setPostIds(bo.getPostIds());
        }
        memberUserMapper.updateById(user);
        MemberUserIndexEvent event = new MemberUserIndexEvent();
        event.setMemberUserId(user.getId());
        event.setAction(RocketMqTagConstants.UPDATE);
        outboxService.save(
                RocketMqTopicConstants.SYSTEM_USER_EVENT,
                RocketMqTagConstants.UPDATE,
                String.valueOf(user.getId()),
                "MEMBER_USER_INDEX_UPDATE",
                event);
    }

    /**
     * 将会员索引升级至下一个真实版本并原子切换 Alias。
     *
     * @return 新真实索引名称
     */
    public String upgradeIndexToNextVersion() {
        return upgradeService.upgrade();
    }

    /**
     * 在观察期内将 Alias 回滚到升级前索引。
     *
     * @param recordId 升级审计记录编号
     */
    public void rollbackIndex(Long recordId) {
        upgradeService.rollback(recordId);
    }

    /**
     * 按业务主键回查 MySQL 并幂等写入 ES 投影；升级窗口会自动双写。
     *
     * @param memberUserId 会员用户主键
     */
    public void upsertByMemberUserId(Long memberUserId) {
        MemberUser user = memberUserMapper.selectById(memberUserId);
        if (user == null || Boolean.TRUE.equals(user.getIsDeleted())) {
            deleteByMemberUserId(memberUserId);
            return;
        }
        projectionWriter.upsert(
                definition.alias(),
                String.valueOf(memberUserId),
                converter.toDocument(user),
                upgradeService.rebuildingIndex());
    }

    /**
     * 按业务主键删除 ES 投影；升级窗口会同时删除 rebuilding 索引中的文档。
     *
     * @param memberUserId 会员用户主键
     */
    public void deleteByMemberUserId(Long memberUserId) {
        projectionWriter.delete(definition.alias(), String.valueOf(memberUserId), upgradeService.rebuildingIndex());
    }

    /** 根据主键获取有效会员用户。 */
    private MemberUser requiredUser(Long id) {
        MemberUser user = memberUserMapper.selectById(id);
        if (user == null || Boolean.TRUE.equals(user.getIsDeleted())) {
            throw new ServiceException("会员用户不存在");
        }
        return user;
    }
}
