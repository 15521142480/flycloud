package com.fly.member.search.converter;

import com.fly.system.api.member.domain.MemberUser;
import com.fly.member.search.document.MemberUserDocument;
import com.fly.member.search.model.MemberUserSearchVo;
import org.springframework.stereotype.Component;

/**
 * 显式会员 DO / ES Document / VO 转换，避免弱类型 Bean 复制泄露敏感字段。
 */
@Component
public class MemberUserDocumentConverter {

    /**
     * 将 MySQL 会员 DO 显式转换为 ES Document。
     *
     * <p>不复制密码等敏感字段，避免实体直接作为 MQ 或 ES 数据结构。</p>
     */
    public MemberUserDocument toDocument(MemberUser source) {
        MemberUserDocument target = new MemberUserDocument();
        target.setId(source.getId());
        target.setMobile(source.getMobile());
        target.setEmail(source.getEmail());
        target.setStatus(source.getStatus());
        target.setNickname(source.getNickname());
        target.setName(source.getName());
        target.setAvatar(source.getAvatar());
        target.setTagIds(source.getTagIds());
        target.setPostIds(source.getPostIds());
        target.setLevelId(source.getLevelId());
        target.setGroupId(source.getGroupId());
        target.setMark(source.getMark());
        target.setCreateTime(source.getCreateTime());
        target.setUpdateTime(source.getUpdateTime());
        return target;
    }

    /**
     * 将 ES Document 显式转换为接口响应 VO。
     */
     public MemberUserSearchVo toVo(MemberUserDocument source) {
        MemberUserSearchVo target = new MemberUserSearchVo();
        target.setId(source.getId());
        target.setMobile(source.getMobile());
        target.setEmail(source.getEmail());
        target.setStatus(source.getStatus());
        target.setNickname(source.getNickname());
        target.setName(source.getName());
        target.setAvatar(source.getAvatar());
        target.setTagIds(source.getTagIds());
        target.setPostIds(source.getPostIds());
        target.setLevelId(source.getLevelId());
        target.setGroupId(source.getGroupId());
        target.setMark(source.getMark());
        target.setCreateTime(source.getCreateTime());
        target.setUpdateTime(source.getUpdateTime());
        return target;
    }
}
