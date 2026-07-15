package com.fly.member.search.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fly.common.elasticsearch.page.ElasticsearchPageExecutor;
import com.fly.common.elasticsearch.page.model.ElasticsearchPageRequest;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.exception.ServiceException;
import com.fly.common.rocketmq.constant.RocketMqTagConstants;
import com.fly.common.rocketmq.constant.RocketMqTopicConstants;
import com.fly.common.rocketmq.outbox.service.MqOutboxService;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.StringUtils;
import com.fly.member.mapper.MemberUserMapper;
import com.fly.member.search.model.MemberUserSearchInsertBo;
import com.fly.member.search.model.MemberUserSearchUpdateBo;
import com.fly.system.api.member.domain.MemberUser;
import com.fly.member.search.converter.MemberUserDocumentConverter;
import com.fly.member.search.document.MemberUserDocument;
import com.fly.member.search.model.MemberUserSearchVo;
import com.fly.member.search.index.MemberUserIndexDefinition;
import com.fly.member.search.index.MemberUserIndexEvent;
import com.fly.member.search.index.MemberUserIndexFields;
import com.fly.member.search.index.MemberUserIndexSyncService;
import com.fly.member.search.index.MemberUserIndexUpgradeService;
import com.fly.member.search.model.MemberUserSearchPageBo;
import com.fly.member.search.query.MemberUserSearchQueryBuilder;
import com.fly.member.search.repository.MemberUserSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 会员用户搜索业务编排；通用索引、查询与同步细节均已下沉公共/专属组件。
 */
@Service
@RequiredArgsConstructor
@ConditionalOnExpression("'${flycloud.elasticsearch.enabled:false}' == 'true' and '${flycloud.rocketmq.enabled:false}' == 'true'")
public class MemberUserSearchServiceImpl implements MemberUserSearchService {

    private static final Map<String, String> SORT_FIELDS = Map.of(
            MemberUserIndexFields.ID, MemberUserIndexFields.ID,
            MemberUserIndexFields.CREATE_TIME, MemberUserIndexFields.CREATE_TIME,
            MemberUserIndexFields.UPDATE_TIME, MemberUserIndexFields.UPDATE_TIME);

    private final MemberUserIndexDefinition definition;
    private final ElasticsearchPageExecutor pageExecutor;
    private final MemberUserSearchRepository repository;
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
    @Override
    public String fullSynchronize() {
        return syncService.initializeAndSynchronize();
    }

    /**
     * 新增会员主数据，并在同一数据库事务内创建 ES 投影更新事件。
     *
     * <p>消息只传递新增后的会员主键；消费者收到消息后回查 MySQL，再写入 ES，
     * 从而保证 MySQL 是唯一权威数据源。</p>
     *
     * @param bo 新增会员请求
     * @return 新增会员主键
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long insertMemberUser(MemberUserSearchInsertBo bo) {
        validateMobileUnique(bo.getMobile());

        MemberUser user = new MemberUser();
        user.setMobile(bo.getMobile());
        user.setEmail(bo.getEmail());
        user.setNickname(bo.getNickname());
        user.setName(bo.getName());
        user.setStatus(bo.getStatus() == null ? 0 : bo.getStatus());
        user.setMark(bo.getMark());
        user.setTagIds(bo.getTagIds());
        user.setPostIds(bo.getPostIds());
        user.setLevelId(bo.getLevelId());
        user.setGroupId(bo.getGroupId());
        user.setPoint(0);
        user.setExperience(0);
        user.setIsDeleted(false);
        LocalDateTime now = LocalDateTime.now();
        String operator = String.valueOf(UserUtils.getCurUserId());
        user.setCreateBy(operator);
        user.setCreateTime(now);
        user.setUpdateBy(operator);
        user.setUpdateTime(now);
        if (memberUserMapper.insert(user) != 1 || user.getId() == null) {
            throw new ServiceException("新增会员用户失败");
        }

        saveIndexUpsertEvent(user.getId(), RocketMqTagConstants.CREATE, "MEMBER_USER_INDEX_CREATE");
        return user.getId();
    }

    /**
     * 执行会员用户 ES 条件分页查询。
     *
     * @param bo 业务查询条件
     * @return 标准分页响应
     */
    @Override
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
    @Override
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
        saveIndexUpsertEvent(user.getId(), RocketMqTagConstants.UPDATE, "MEMBER_USER_INDEX_UPDATE");
    }

    /**
     * 将会员索引升级至下一个真实版本并原子切换 Alias。
     *
     * @return 新真实索引名称
     */
    @Override
    public String upgradeIndexToNextVersion() {
        return upgradeService.upgrade();
    }

    /**
     * 在观察期内将 Alias 回滚到升级前索引。
     *
     * @param recordId 升级审计记录编号
     */
    @Override
    public void rollbackIndex(Long recordId) {
        upgradeService.rollback(recordId);
    }

    /**
     * 按业务主键回查 MySQL 并幂等写入 ES 投影；升级窗口会自动双写。
     *
     * @param memberUserId 会员用户主键
     */
    @Override
    public void upsertByMemberUserId(Long memberUserId) {
        MemberUser user = memberUserMapper.selectById(memberUserId);
        if (user == null || Boolean.TRUE.equals(user.getIsDeleted())) {
            deleteByMemberUserId(memberUserId);
            return;
        }
        repository.upsert(definition.alias(), memberUserId, converter.toDocument(user), upgradeService.rebuildingIndex());
    }

    /**
     * 按业务主键删除 ES 投影；升级窗口会同时删除 rebuilding 索引中的文档。
     *
     * @param memberUserId 会员用户主键
     */
    @Override
    public void deleteByMemberUserId(Long memberUserId) {
        repository.delete(definition.alias(), memberUserId, upgradeService.rebuildingIndex());
    }

    /**
     * 根据主键获取有效会员用户。
     */
    private MemberUser requiredUser(Long id) {
        MemberUser user = memberUserMapper.selectById(id);
        if (user == null || Boolean.TRUE.equals(user.getIsDeleted())) {
            throw new ServiceException("会员用户不存在");
        }
        return user;
    }

    /**
     * 校验手机号在有效会员中唯一。
     *
     * @param mobile 待校验手机号
     */
    private void validateMobileUnique(String mobile) {
        if (StringUtils.isBlank(mobile)) {
            return;
        }
        Long count = memberUserMapper.selectCount(Wrappers.<MemberUser>lambdaQuery()
                .eq(MemberUser::getIsDeleted, false)
                .eq(MemberUser::getMobile, mobile));
        if (count != null && count > 0) {
            throw new ServiceException("手机号已被会员使用");
        }
    }

    /**
     * 在当前业务事务内保存会员 ES 投影事件。
     *
     * <p>新增和修改分别使用 {@code create}、{@code update} Tag；ES 消费者订阅两者，
     * 并统一按主键执行投影 upsert。</p>
     *
     * @param memberUserId 会员用户主键
     * @param action 业务动作
     * @param eventType 事件类型
     */
    private void saveIndexUpsertEvent(Long memberUserId, String action, String eventType) {
        MemberUserIndexEvent event = new MemberUserIndexEvent();
        event.setMemberUserId(memberUserId);
        event.setAction(action);
        outboxService.save(
                RocketMqTopicConstants.SYSTEM_USER_EVENT,
                action,
                String.valueOf(memberUserId),
                eventType,
                event);
    }
}
