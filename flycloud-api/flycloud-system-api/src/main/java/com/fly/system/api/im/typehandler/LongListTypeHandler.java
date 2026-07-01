package com.fly.system.api.im.typehandler;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;

import java.util.List;

/**
 * Long 列表 JSON 类型处理器。
 *
 * @author lxs
 * @date 2026-07-02
 */
public class LongListTypeHandler extends JacksonTypeHandler {

    public LongListTypeHandler(Class<?> type) {
        super(type);
    }

    @Override
    public Object parse(String json) {
        Object value = super.parse(json);
        return value instanceof List<?> ? value : null;
    }

}
