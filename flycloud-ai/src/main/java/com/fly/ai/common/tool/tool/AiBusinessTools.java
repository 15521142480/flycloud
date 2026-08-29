package com.fly.ai.common.tool.tool;

import com.fly.ai.common.tool.model.AiToolAuthorizationTrace;
import com.fly.ai.common.tool.model.AiToolOrderSummary;
import com.fly.ai.common.tool.model.AiToolPublicUserInfo;
import com.fly.common.domain.model.R;
import com.fly.common.exception.AiProviderException;
import com.fly.mall.api.trade.domain.vo.TradeOrderVo;
import com.fly.mall.api.trade.feign.ITradeOrderApi;
import com.fly.system.api.system.domain.vo.SysUserVo;
import com.fly.system.api.system.feign.ISysRoleApi;
import com.fly.system.api.system.feign.ISysUserApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * AI 可调用的业务工具集合。
 * <p>
 * 工具参数只能描述要查询的业务资源；当前登录用户、角色和授权状态只从服务端 {@link ToolContext} 获取，
 * 绝不信任模型提供的用户身份或权限信息。
 *
 * @author lxs
 * @date 2026-08-27
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AiBusinessTools {

    /** Spring AI ToolContext 中保存当前登录用户编号的键。 */
    public static final String LOGIN_USER_ID_CONTEXT_KEY = "aiToolLoginUserId";

    /** Spring AI ToolContext 中保存授权轨迹的键。 */
    public static final String AUTHORIZATION_TRACE_CONTEXT_KEY = "aiToolAuthorizationTrace";

    private static final String QUERY_SYSTEM_USER_TOOL = "query_system_user_by_id";

    private static final String QUERY_MALL_ORDER_TOOL = "query_mall_order_by_id_or_no";

    private final ISysUserApi sysUserApi;

    private final ISysRoleApi sysRoleApi;

    private final ITradeOrderApi tradeOrderApi;

    /**
     * 根据用户编号查询公共用户信息。
     *
     * @param userId 用户编号
     * @param toolContext 服务端工具上下文
     * @return 已脱敏的公共用户信息，用户不存在时返回说明文本
     */
    @Tool(name = QUERY_SYSTEM_USER_TOOL, description = "根据用户ID查询系统用户的公共信息。该工具不返回密码、手机号、邮箱等敏感字段。")
    public Object querySystemUserById(
            @ToolParam(description = "需要查询的用户ID") Long userId,
            ToolContext toolContext) {
        requireResourceId(userId, "用户ID");
        AiToolAuthorizationTrace trace = authorizationTrace(toolContext);
        trace.grantPublic(QUERY_SYSTEM_USER_TOOL);
        log.info("AI 工具调用，tool={}, userId={}", QUERY_SYSTEM_USER_TOOL, userId);
        SysUserVo user = checkedData(sysUserApi.getUserById(userId), "系统用户");
        if (user == null) {
            return "未查询到该用户。";
        }
        return new AiToolPublicUserInfo(user.getId(), user.getAccount(), user.getName(), user.getRealName(),
                user.getDeptId(), user.getDeptName(), user.getStatus(), user.getCreateTime());
    }

    /**
     * 根据订单数据库主键或订单流水号查询订单摘要。
     * <p>
     * 超级管理员可查询全部订单；普通用户仅能查询本人订单。未授权时不向模型返回订单是否存在以及任何订单内容。
     *
     * @param idOrNo 订单数据库主键或订单流水号
     * @param toolContext 服务端工具上下文
     * @return 已脱敏的订单摘要，或无权限/不存在说明
     */
    @Tool(name = QUERY_MALL_ORDER_TOOL, description = "根据商城订单数据库主键或订单流水号查询订单摘要。参数统一传入用户提供的订单 ID、订单编号或订单流水号，例如 2073133434168320001 或 M202607040355023193520。仅在当前登录用户是订单创建人或拥有超级管理员角色时返回订单信息。工具成功返回订单摘要即表示后端授权已通过；结果中的 buyerUserId 可用于继续调用“根据用户ID查询系统用户的公共信息”工具，以回答订单由谁购买。")
    public Object queryMallOrderByIdOrNo(
            @ToolParam(description = "需要查询的订单数据库主键或订单流水号，必须保持原始字符串，例如 2073133434168320001 或 M202607040355023193520") String idOrNo,
            ToolContext toolContext) {
        requireOrderIdentifier(idOrNo);
        AiToolAuthorizationTrace trace = authorizationTrace(toolContext);
        Long loginUserId = loginUserId(toolContext);
        TradeOrderVo order = checkedData(tradeOrderApi.getOrderByIdOrNo(idOrNo), "商城订单");
        return authorizeOrderQuery(order, loginUserId, trace, QUERY_MALL_ORDER_TOOL, "idOrNo=" + idOrNo);
    }

    /**
     * 执行订单资源的服务端授权并返回最小化订单摘要。
     *
     * @param order 已查询到的订单，可为 {@code null}
     * @param loginUserId 当前登录用户编号
     * @param trace 本次工具调用授权轨迹
     * @param toolName 当前工具名称
     * @param queryCondition 已脱敏的查询条件日志文本
     * @return 已授权的订单摘要，或不泄露资源状态的拒绝说明
     */
    private Object authorizeOrderQuery(TradeOrderVo order, Long loginUserId, AiToolAuthorizationTrace trace,
            String toolName, String queryCondition) {
        boolean superAdmin = Boolean.TRUE.equals(checkedData(sysRoleApi.isSuperAdmin(loginUserId), "用户角色"));
        if (superAdmin) {
            trace.grantSuperAdmin(toolName);
            log.info("AI 工具调用，tool={}, {}, authorization=super-admin", toolName, queryCondition);
            return order == null ? "未查询到该订单。" : toOrderSummary(order);
        }
        if (order != null && Objects.equals(order.getUserId(), loginUserId)) {
            trace.grantResourceOwner(toolName);
            log.info("AI 工具调用，tool={}, {}, authorization=order-owner", toolName, queryCondition);
            return toOrderSummary(order);
        }
        trace.deny(toolName);
        log.warn("AI 工具调用被拒绝，tool={}, {}, loginUserId={}", toolName, queryCondition, loginUserId);
        return "当前登录用户无权查询该订单。";
    }

    /**
     * 从工具上下文取得服务端创建的授权轨迹。
     *
     * @param toolContext 服务端工具上下文
     * @return 授权轨迹
     */
    private AiToolAuthorizationTrace authorizationTrace(ToolContext toolContext) {
        Object trace = toolContext.getContext().get(AUTHORIZATION_TRACE_CONTEXT_KEY);
        if (trace instanceof AiToolAuthorizationTrace authorizationTrace) {
            return authorizationTrace;
        }
        throw new AiProviderException(500, "AI Tool Calling 授权上下文缺失");
    }

    /**
     * 从工具上下文取得当前登录用户编号。
     *
     * @param toolContext 服务端工具上下文
     * @return 当前登录用户编号
     */
    private Long loginUserId(ToolContext toolContext) {
        Object userId = toolContext.getContext().get(LOGIN_USER_ID_CONTEXT_KEY);
        if (userId instanceof Long loginUserId) {
            return loginUserId;
        }
        throw new AiProviderException(401, "未获取到当前登录用户信息");
    }

    /**
     * 校验模型提供的业务资源编号。
     *
     * @param resourceId 资源编号
     * @param resourceName 资源名称
     */
    private void requireResourceId(Long resourceId, String resourceName) {
        if (resourceId == null || resourceId <= 0) {
            throw new AiProviderException(400, resourceName + "必须为正整数");
        }
    }

    /**
     * 校验模型提供的订单标识。
     *
     * @param idOrNo 订单数据库主键或订单流水号
     */
    private void requireOrderIdentifier(String idOrNo) {
        if (!StringUtils.hasText(idOrNo)) {
            throw new AiProviderException(400, "订单ID或订单流水号不能为空");
        }
    }

    /**
     * 读取并校验 Feign 调用响应。
     *
     * @param response Feign 调用响应
     * @param resourceName 资源名称
     * @param <T> 响应数据类型
     * @return 响应数据
     */
    private <T> T checkedData(R<T> response, String resourceName) {
        if (response == null) {
            throw new AiProviderException(502, "查询" + resourceName + "时未收到服务响应");
        }
        return response.getCheckedData();
    }

    /**
     * 转换为允许传递给模型的最小订单摘要。
     *
     * @param order 订单详情
     * @return 脱敏订单摘要
     */
    private AiToolOrderSummary toOrderSummary(TradeOrderVo order) {
        return new AiToolOrderSummary(order.getId(), order.getNo(), order.getUserId(), order.getStatus(), order.getProductCount(),
                order.getPayStatus(), order.getTotalPrice(), order.getDiscountPrice(), order.getDeliveryPrice(),
                order.getPayPrice(), order.getCreateTime());
    }
}
