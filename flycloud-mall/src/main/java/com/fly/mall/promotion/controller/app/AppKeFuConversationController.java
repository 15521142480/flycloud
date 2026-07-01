package com.fly.mall.promotion.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.promotion.domain.bo.KeFuConversationBo;
import com.fly.mall.api.promotion.domain.vo.KeFuConversationVo;
import com.fly.mall.promotion.service.IKeFuConversationService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 移动端 - 客服会话 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/promotion/ke-fu-conversation")
public class AppKeFuConversationController {

    private final IKeFuConversationService keFuConversationService;

    /**
     * 查询移动端客服会话分页列表。
     */
    @GetMapping("/list")
    public R<PageVo<KeFuConversationVo>> list(KeFuConversationBo bo, PageBo page) {
        return R.ok(keFuConversationService.queryPageList(bo, page));
    }

    /**
     * 获得分页列表。
     */
    @GetMapping("/page")
    public R<PageVo<KeFuConversationVo>> page(KeFuConversationBo bo, PageBo page) {
        return R.ok(keFuConversationService.queryPageList(bo, page));
    }

    /**
     * 获取移动端客服会话详情。
     */
    @GetMapping("/get/{id}")
    public R<KeFuConversationVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(keFuConversationService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping({"/get-detail", "/get"})
    public R<KeFuConversationVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(keFuConversationService.queryById(id));
    }

}
