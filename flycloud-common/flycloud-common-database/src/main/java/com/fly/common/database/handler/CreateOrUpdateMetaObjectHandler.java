package com.fly.common.database.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.fly.common.domain.BaseEntity;
import com.fly.common.security.util.UserUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 通用参数填充实现类 (如：创建人/时间、修改人/时间)
 *
 * @author: lxs
 * @date: 2026/6/26
 */
@Component
public class CreateOrUpdateMetaObjectHandler implements MetaObjectHandler {

    // NOTE 会有时间字段数据变成 1970-01-01 08:00:00 bug ？

    
    /**
     * 新增语句
     */
    @Override
    @SuppressWarnings("PatternVariableCanBeUsed")
    public void insertFill(MetaObject metaObject) {
        
        if (Objects.nonNull(metaObject) && metaObject.getOriginalObject() instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity) metaObject.getOriginalObject();

            LocalDateTime current = LocalDateTime.now();
            // 创建时间为空，则以当前时间为插入时间
            if (Objects.isNull(baseEntity.getCreateTime())) {
                baseEntity.setCreateTime(current);
            }
            // 更新时间为空，则以当前时间为更新时间
            if (Objects.isNull(baseEntity.getUpdateTime())) {
                baseEntity.setUpdateTime(current);
            }

            String curUserId = UserUtils.getCurUserIdStr();
            // 当前登录用户不为空，创建人为空，则当前登录用户为创建人
            if (Objects.nonNull(curUserId) && Objects.isNull(baseEntity.getCreateBy())) {
                baseEntity.setCreateBy(curUserId);
            }
            // 当前登录用户不为空，更新人为空，则当前登录用户为更新人
            if (Objects.nonNull(curUserId) && Objects.isNull(baseEntity.getUpdateTime())) {
                baseEntity.setUpdateBy(curUserId);
            }
        }
    }

    
    /**
     * 修改
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        
        // 更新时间为空，则以当前时间为更新时间
        Object modifyTime = getFieldValByName("updateTime", metaObject);
        if (Objects.isNull(modifyTime)) {
            setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        }

        // 当前登录用户不为空，更新人为空，则当前登录用户为更新人
        Object modifier = getFieldValByName("updateBy", metaObject);
        String curUserId = UserUtils.getCurUserIdStr();
        if (Objects.nonNull(curUserId) && Objects.isNull(modifier)) {
            setFieldValByName("updateBy", curUserId, metaObject);
        }
    }
    
    
}
