package com.fly.member.search.repository;

import com.fly.common.elasticsearch.projection.ElasticsearchProjectionWriter;
import com.fly.member.search.document.MemberUserDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 会员用户 ES 投影数据访问仓库。
 *
 * <p>统一封装文档的 upsert/delete，并复用公共双写能力；业务 Service 不直接操作 ES 客户端或写入器。</p>
 */
@Repository
@RequiredArgsConstructor
public class MemberUserSearchRepository {

    private final ElasticsearchProjectionWriter projectionWriter;

    /**
     * 按稳定会员主键幂等写入当前索引及升级窗口中的新索引。
     */
    public void upsert(String alias, Long memberUserId, MemberUserDocument document, String rebuildingIndex) {
        projectionWriter.upsert(alias, String.valueOf(memberUserId), document, rebuildingIndex);
    }

    /**
     * 按稳定会员主键删除当前索引及升级窗口中的新索引文档。
     */
    public void delete(String alias, Long memberUserId, String rebuildingIndex) {
        projectionWriter.delete(alias, String.valueOf(memberUserId), rebuildingIndex);
    }
}
