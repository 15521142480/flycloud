package com.fly.test.member.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 会员用户搜索文档。
 *
 * <p>该对象只描述搜索模型，和 MySQL 的 {@code MemberUser} DO 严格分离，不保存密码等敏感字段。</p>
 */
@Data
@Document(indexName = "member_user", createIndex = false)
public class MemberUserDocument {

    @Id
    private Long id;
    @Field(type = FieldType.Keyword)
    private String mobile;
    @Field(type = FieldType.Keyword)
    private String email;
    @Field(type = FieldType.Integer)
    private Integer status;
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String nickname;
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String name;
    @Field(type = FieldType.Keyword)
    private String avatar;
    @Field(type = FieldType.Keyword)
    private List<Long> tagIds;
    @Field(type = FieldType.Keyword)
    private List<Long> postIds;
    @Field(type = FieldType.Long)
    private Long levelId;
    @Field(type = FieldType.Long)
    private Long groupId;
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String mark;
    @Field(type = FieldType.Date)
    private LocalDateTime createTime;
    @Field(type = FieldType.Date)
    private LocalDateTime updateTime;
}
