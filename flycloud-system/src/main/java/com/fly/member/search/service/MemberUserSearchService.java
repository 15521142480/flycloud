package com.fly.member.search.service;

import com.fly.common.domain.vo.PageVo;
import com.fly.common.elasticsearch.index.model.ElasticsearchAliasIndexGroup;
import com.fly.member.search.model.MemberUserSearchInsertBo;
import com.fly.member.search.model.MemberUserSearchVo;
import com.fly.member.search.model.MemberUserSearchUpdateBo;
import com.fly.member.search.model.MemberUserSearchPageBo;

import java.util.List;

/**
 * 会员用户搜索业务服务。
 *
 * <p>接口只暴露会员搜索用例；ES 索引版本、Alias、Mapping 和通用分页实现均下沉至相应职责组件。</p>
 */
public interface MemberUserSearchService {

    /**
     * @return 当前 Alias 指向的真实索引名称。
     */
    String fullSynchronize();

    /**
     * 新增会员主数据，并在同一事务中创建 ES 投影消息。
     *
     * @param command 新增会员请求
     * @return 新增会员主键
     */
    Long insertMemberUser(MemberUserSearchInsertBo command);

    /**
     * 执行会员用户条件分页检索。
     */
     PageVo<MemberUserSearchVo> searchPage(MemberUserSearchPageBo query);

    /**
     * 更新会员主数据，并在同一事务中创建 ES 投影消息。
     */
     void updateMemberUser(MemberUserSearchUpdateBo command);

    /**
     * @return 升级后的真实索引名称。
     */
     String upgradeIndexToNextVersion();

    /**
     * 在观察期内回滚指定索引升级记录。
     */
    void rollbackIndex(Long recordId);

    /**
     * 查询 Elasticsearch 集群中全部业务别名及其真实版本索引。
     *
     * @return Alias 与版本索引分组
     */
    List<ElasticsearchAliasIndexGroup> listIndexAliases();

    /**
     * 删除未被 Alias 引用的会员用户历史版本索引。
     *
     * @param index 待删除真实版本索引
     */
    void deleteHistoricalIndex(String index);

    /**
     * 按会员主键回查 MySQL 并幂等写入 ES 投影。
     */
     void upsertByMemberUserId(Long memberUserId);

    /**
     * 按会员主键删除 ES 投影。
     */
     void deleteByMemberUserId(Long memberUserId);
}
