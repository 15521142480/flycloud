package com.fly.test.member.repository;

import com.fly.test.member.document.MemberUserDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/** 会员用户索引的简单 CRUD Repository；复杂检索由官方 Java Client 实现。 */
public interface MemberUserSearchRepository extends ElasticsearchRepository<MemberUserDocument, Long> {
}
