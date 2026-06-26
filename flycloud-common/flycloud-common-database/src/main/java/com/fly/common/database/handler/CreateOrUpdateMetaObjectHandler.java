package com.fly.common.database.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.fly.common.security.util.UserUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 新增或修改的监听器
 *
 * 由于本项目使用的是逻辑删除(字段isDeleted：@TableLogic)，
 * 并且createBy、createTime设置为新增语句会加上该字段（@TableField(fill = FieldFill.INSERT)），updateBy、updateTime新增或修改都会加上该字段（@TableField(fill = FieldFill.INSERT_UPDATE)）
 * 此处解决的就是逻辑删除是update语句，一般只传id不会传updateBy或updateTime，但是有些表这两个字段是必填，所以此处会默认赋值
 *
 * @author: lxs
 * @date: 2026/6/26
 */
@Component
public class CreateOrUpdateMetaObjectHandler {
//public class CreateOrUpdateMetaObjectHandler implements MetaObjectHandler {

    // todo 会有时间字段数据变成 1970-01-01 08:00:00 bug，暂且终止
//    @Override
//    public void insertFill(MetaObject metaObject) {
//
//        this.strictInsertFill(metaObject, "createBy", () -> String.valueOf(UserUtils.getCurUserId()), String.class);
//        this.strictInsertFill(metaObject, "createTime", LocalDateTime::now, LocalDateTime.class);
//        this.strictInsertFill(metaObject, "updateBy", () -> String.valueOf(UserUtils.getCurUserId()), String.class);
//        this.strictInsertFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
//    }
//
//    @Override
//    public void updateFill(MetaObject metaObject) {
//
//        this.strictUpdateFill(metaObject, "updateBy", () -> String.valueOf(UserUtils.getCurUserId()), String.class);
//        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
//    }
}
