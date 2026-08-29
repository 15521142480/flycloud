package com.fly.ai.common.tool.tool;

import com.fly.ai.common.tool.model.AiToolAuthorizationTrace;
import com.fly.ai.common.tool.model.AiToolOrderSummary;
import com.fly.ai.common.tool.model.AiToolPublicUserInfo;
import com.fly.common.domain.model.R;
import com.fly.mall.api.trade.domain.vo.TradeOrderVo;
import com.fly.mall.api.trade.feign.ITradeOrderApi;
import com.fly.system.api.system.domain.vo.SysUserVo;
import com.fly.system.api.system.feign.ISysRoleApi;
import com.fly.system.api.system.feign.ISysUserApi;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;

import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * 验证业务工具的服务端授权规则。
 */
class AiBusinessToolsTest {

    /**
     * 用户查询属于公共工具，但必须只返回脱敏字段。
     */
    @Test
    void shouldReturnSanitizedPublicUserInfo() {
        ISysUserApi sysUserApi = mock(ISysUserApi.class);
        SysUserVo user = new SysUserVo();
        user.setId(10L);
        user.setAccount("fly-user");
        user.setName("飞翔用户");
        user.setPassword("must-not-leak");
        when(sysUserApi.getUserById(10L)).thenReturn(R.ok(user));
        AiBusinessTools tools = tools(sysUserApi, mock(ISysRoleApi.class), mock(ITradeOrderApi.class));
        AiToolAuthorizationTrace trace = new AiToolAuthorizationTrace();

        Object result = tools.querySystemUserById(10L, toolContext(1L, trace));

        AiToolPublicUserInfo userInfo = assertInstanceOf(AiToolPublicUserInfo.class, result);
        assertEquals(10L, userInfo.userId());
        assertEquals("fly-user", userInfo.account());
        assertEquals("检查到您有该模块权限（公共模块）", trace.permissionMessage());
    }

    /**
     * 超级管理员可以读取任意订单的脱敏摘要。
     */
    @Test
    void shouldAllowSuperAdminToQueryAnyOrder() {
        ISysRoleApi sysRoleApi = mock(ISysRoleApi.class);
        ITradeOrderApi tradeOrderApi = mock(ITradeOrderApi.class);
        when(sysRoleApi.isSuperAdmin(1L)).thenReturn(R.ok(true));
        TradeOrderVo order = order(20L, 2L);
        when(tradeOrderApi.getOrderByIdOrNo("20")).thenReturn(R.ok(order));
        AiBusinessTools tools = tools(mock(ISysUserApi.class), sysRoleApi, tradeOrderApi);
        AiToolAuthorizationTrace trace = new AiToolAuthorizationTrace();

        Object result = tools.queryMallOrderByIdOrNo("20", toolContext(1L, trace));

        AiToolOrderSummary summary = assertInstanceOf(AiToolOrderSummary.class, result);
        assertEquals(20L, summary.orderId());
        assertEquals("检查到您有该模块权限（超级管理员）", trace.permissionMessage());
    }

    /**
     * 订单流水号必须按字符串传递，避免超出 Long 范围时在 Spring AI 参数转换阶段发生溢出。
     */
    @Test
    void shouldAllowSuperAdminToQueryOrderByStringOrderNo() {
        String orderNo = "M202607040355023193520";
        ISysRoleApi sysRoleApi = mock(ISysRoleApi.class);
        ITradeOrderApi tradeOrderApi = mock(ITradeOrderApi.class);
        when(sysRoleApi.isSuperAdmin(1L)).thenReturn(R.ok(true));
        when(tradeOrderApi.getOrderByIdOrNo(orderNo)).thenReturn(R.ok(order(20L, 2L)));
        AiBusinessTools tools = tools(mock(ISysUserApi.class), sysRoleApi, tradeOrderApi);
        AiToolAuthorizationTrace trace = new AiToolAuthorizationTrace();

        Object result = tools.queryMallOrderByIdOrNo(orderNo, toolContext(1L, trace));

        AiToolOrderSummary summary = assertInstanceOf(AiToolOrderSummary.class, result);
        assertEquals(20L, summary.orderId());
        assertEquals("检查到您有该模块权限（超级管理员）", trace.permissionMessage());
    }

    /**
     * 雪花算法生成的大整数订单主键必须以字符串传入工具，避免模型 JSON 数字精度或 Long 转换问题。
     */
    @Test
    void shouldAcceptLargeNumericOrderIdFromToolJson() {
        Long orderId = 2073133434168320001L;
        ISysRoleApi sysRoleApi = mock(ISysRoleApi.class);
        ITradeOrderApi tradeOrderApi = mock(ITradeOrderApi.class);
        when(sysRoleApi.isSuperAdmin(1L)).thenReturn(R.ok(true));
        when(tradeOrderApi.getOrderByIdOrNo(orderId.toString())).thenReturn(R.ok(order(orderId, 2L)));
        AiBusinessTools tools = tools(mock(ISysUserApi.class), sysRoleApi, tradeOrderApi);
        AiToolAuthorizationTrace trace = new AiToolAuthorizationTrace();
        ToolCallback callback = Arrays.stream(ToolCallbacks.from(tools))
                .filter(item -> "query_mall_order_by_id_or_no".equals(item.getToolDefinition().name()))
                .findFirst()
                .orElseThrow();

        String result = callback.call("{\"idOrNo\":\"2073133434168320001\"}", toolContext(1L, trace));

        assertTrue(result.contains("2073133434168320001"));
        assertEquals("检查到您有该模块权限（超级管理员）", trace.permissionMessage());
    }

    /**
     * 普通用户只能读取本人订单。
     */
    @Test
    void shouldAllowOrderOwnerToQueryOwnOrder() {
        ISysRoleApi sysRoleApi = mock(ISysRoleApi.class);
        ITradeOrderApi tradeOrderApi = mock(ITradeOrderApi.class);
        when(sysRoleApi.isSuperAdmin(1L)).thenReturn(R.ok(false));
        when(tradeOrderApi.getOrderByIdOrNo("20")).thenReturn(R.ok(order(20L, 1L)));
        AiBusinessTools tools = tools(mock(ISysUserApi.class), sysRoleApi, tradeOrderApi);
        AiToolAuthorizationTrace trace = new AiToolAuthorizationTrace();

        Object result = tools.queryMallOrderByIdOrNo("20", toolContext(1L, trace));

        assertInstanceOf(AiToolOrderSummary.class, result);
        assertEquals("检查到您有该模块权限（当前用户为订单用户）", trace.permissionMessage());
    }

    /**
     * 非订单创建人且非超级管理员时，不向模型返回任何订单字段。
     */
    @Test
    void shouldDenyNonOwnerOrderQuery() {
        ISysRoleApi sysRoleApi = mock(ISysRoleApi.class);
        ITradeOrderApi tradeOrderApi = mock(ITradeOrderApi.class);
        when(sysRoleApi.isSuperAdmin(1L)).thenReturn(R.ok(false));
        when(tradeOrderApi.getOrderByIdOrNo("20")).thenReturn(R.ok(order(20L, 2L)));
        AiBusinessTools tools = tools(mock(ISysUserApi.class), sysRoleApi, tradeOrderApi);
        AiToolAuthorizationTrace trace = new AiToolAuthorizationTrace();

        Object result = tools.queryMallOrderByIdOrNo("20", toolContext(1L, trace));

        assertEquals("当前登录用户无权查询该订单。", result);
        assertTrue(trace.isDenied());
        assertEquals("检查到您没有该模块权限", trace.permissionMessage());
    }

    /**
     * 注解工具必须能被 Spring AI 正确解析为 ToolCallback。
     */
    @Test
    void shouldExposeAnnotatedMethodsAsSpringAiTools() {
        AiBusinessTools tools = tools(mock(ISysUserApi.class), mock(ISysRoleApi.class), mock(ITradeOrderApi.class));

        ToolCallback[] callbacks = ToolCallbacks.from(tools);

        assertEquals(2, callbacks.length);
        assertFalse(callbacks[0].getToolDefinition().name().isBlank());
        assertFalse(callbacks[1].getToolDefinition().name().isBlank());
    }

    /**
     * 创建工具实例。
     */
    private AiBusinessTools tools(ISysUserApi sysUserApi, ISysRoleApi sysRoleApi, ITradeOrderApi tradeOrderApi) {
        return new AiBusinessTools(sysUserApi, sysRoleApi, tradeOrderApi);
    }

    /**
     * 创建仅包含服务端登录信息的工具上下文。
     */
    private ToolContext toolContext(Long loginUserId, AiToolAuthorizationTrace trace) {
        return new ToolContext(Map.of(
                AiBusinessTools.LOGIN_USER_ID_CONTEXT_KEY, loginUserId,
                AiBusinessTools.AUTHORIZATION_TRACE_CONTEXT_KEY, trace));
    }

    /**
     * 创建订单测试数据。
     */
    private TradeOrderVo order(Long orderId, Long ownerUserId) {
        TradeOrderVo order = new TradeOrderVo();
        order.setId(orderId);
        order.setNo("ORDER-" + orderId);
        order.setUserId(ownerUserId);
        order.setStatus(10);
        order.setProductCount(1);
        order.setPayPrice(100);
        return order;
    }
}
