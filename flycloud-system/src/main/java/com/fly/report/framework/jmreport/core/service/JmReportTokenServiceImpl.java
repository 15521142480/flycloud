package com.fly.report.framework.jmreport.core.service;

import cn.hutool.core.util.StrUtil;
import com.fly.common.security.user.FlyUser;
import com.fly.common.security.util.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.jeecg.modules.jmreport.api.JmReportTokenServiceI;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 积木报表 Token 校验服务。
 *
 * @author lxs
 * @date 2026-06-30
 */
public class JmReportTokenServiceImpl implements JmReportTokenServiceI {

    private static final String JM_TOKEN_HEADER = "X-Access-Token";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    public HttpHeaders customApiHeader() {
        HttpHeaders headers = new HttpHeaders();
        HttpServletRequest request = currentRequest();
        if (request == null) {
            return headers;
        }
        String token = request.getHeader(JM_TOKEN_HEADER);
        if (StrUtil.isNotBlank(token)) {
            headers.add(AUTHORIZATION_HEADER, token.startsWith("Bearer ") ? token : "Bearer " + token);
        }
        return headers;
    }

    @Override
    public Boolean verifyToken(String token) {
        return UserUtils.getCurUserId() != null || StrUtil.isNotBlank(token);
    }

    @Override
    public String getUsername(String token) {
        Long userId = UserUtils.getCurUserId();
        return userId == null ? null : String.valueOf(userId);
    }

    @Override
    public String[] getRoles(String token) {
        FlyUser user = UserUtils.getCurUser();
        if (user == null) {
            return null;
        }
        return new String[]{"admin"};
    }

    @Override
    public String getTenantId() {
        return null;
    }

    @Override
    public String[] getPermissions(String token) {
        return UserUtils.getCurUserId() == null ? null : new String[]{"*:*:*"};
    }

    /**
     * 获取当前请求。
     */
    private HttpServletRequest currentRequest() {
        if (!(RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes)) {
            return null;
        }
        return attributes.getRequest();
    }

}
