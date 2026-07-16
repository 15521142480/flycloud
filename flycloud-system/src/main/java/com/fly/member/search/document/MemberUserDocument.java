package com.fly.member.search.document;

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

    /**
     * 会员用户主键，同时作为 ES 文档 ID。
     */
    @Id
    private Long id;

    /**
     * 会员手机号，用于精确匹配。
     */
    @Field(type = FieldType.Keyword)
    private String mobile;

    /**
     * 会员邮箱，用于精确匹配。
     */
    @Field(type = FieldType.Keyword)
    private String email;

    /**
     * 会员账号状态。
     */
    @Field(type = FieldType.Integer)
    private Integer status;

    /**
     * 会员昵称，使用 IK 分词检索。
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String nickname;

    /**
     * 会员真实姓名，使用 IK 分词检索。
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String name;

    /**
     * 会员头像地址。
     */
    @Field(type = FieldType.Keyword)
    private String avatar;

    /**
     * 会员标签编号集合。
     */
    @Field(type = FieldType.Keyword)
    private List<Long> tagIds;

    /**
     * 会员岗位编号集合。
     */
    @Field(type = FieldType.Keyword)
    private List<Long> postIds;

    /**
     * 会员等级编号。
     */
    @Field(type = FieldType.Long)
    private Long levelId;

    /**
     * 会员分组编号。
     */
    @Field(type = FieldType.Long)
    private Long groupId;

    /**
     * 会员备注，使用 IK 分词检索。
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String mark;

    /**
     * 会员创建时间。
     */
    @Field(type = FieldType.Date)
    private LocalDateTime createTime;

    /**
     * 会员最后修改时间。
     */
    @Field(type = FieldType.Date)
    private LocalDateTime updateTime;
}
