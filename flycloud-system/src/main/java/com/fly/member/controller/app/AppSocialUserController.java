package com.fly.member.controller.app;

import com.fly.common.domain.model.R;
import com.fly.common.security.util.UserUtils;
import com.fly.member.service.ISocialUserService;
import com.fly.system.api.member.domain.bo.AppSocialUserBindReqBo;
import com.fly.system.api.member.domain.bo.AppSocialUserUnbindReqBo;
import com.fly.system.api.member.domain.bo.AppSocialWxaQrcodeReqBo;
import com.fly.system.api.member.domain.vo.AppSocialUserRespVo;
import com.fly.system.api.member.domain.vo.AppSocialWxaSubscribeTemplateRespVo;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

/**
 * 移动端 - 会员社交用户控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping({"/app/member/social-user", "/member/social-user"})
public class AppSocialUserController {

    private static final int USER_TYPE_MEMBER = 2;

    private final ISocialUserService socialUserService;

    /**
     * 社交绑定。
     */
    @PostMapping("/bind")
    @PermitAll
    public R<String> socialBind(@Valid @RequestBody AppSocialUserBindReqBo reqBo) {
        return R.ok(socialUserService.bindSocialUser(UserUtils.getCurUserId(), USER_TYPE_MEMBER, reqBo));
    }

    /**
     * 取消社交绑定。
     */
    @DeleteMapping("/unbind")
    public R<Boolean> socialUnbind(@RequestBody AppSocialUserUnbindReqBo reqBo) {
        return R.ok(socialUserService.unbindSocialUser(UserUtils.getCurUserId(), USER_TYPE_MEMBER, reqBo));
    }

    /**
     * 获得社交用户。
     */
    @GetMapping("/get")
    public R<AppSocialUserRespVo> getSocialUser(@RequestParam("type") Integer type) {
        return R.ok(socialUserService.getSocialUserByUserId(USER_TYPE_MEMBER, UserUtils.getCurUserId(), type));
    }

    /**
     * 获得微信小程序码。
     */
    @PostMapping("/wxa-qrcode")
    @PermitAll
    public R<String> getWxaQrcode(@Valid @RequestBody AppSocialWxaQrcodeReqBo reqBo) {
        return R.ok(createLocalQrcode(reqBo));
    }

    /**
     * 获得微信小程序订阅模板列表。
     */
    @GetMapping("/get-subscribe-template-list")
    @PermitAll
    public R<List<AppSocialWxaSubscribeTemplateRespVo>> getSubscribeTemplateList() {
        AppSocialWxaSubscribeTemplateRespVo template = new AppSocialWxaSubscribeTemplateRespVo();
        template.setId("ORDER_DELIVERY");
        template.setTitle("订单发货通知");
        template.setContent("订单状态、发货时间、快递单号、收货地址");
        template.setExample("您的订单已发货，请留意物流信息");
        template.setType(USER_TYPE_MEMBER);
        return R.ok(List.of(template));
    }

    /**
     * 生成本地小程序码占位图片，便于未接微信开放平台时完成流程联调。
     */
    private String createLocalQrcode(AppSocialWxaQrcodeReqBo reqBo) {
        int width = reqBo.getWidth() == null || reqBo.getWidth() <= 0 ? 280 : reqBo.getWidth();
        try {
            BufferedImage image = new BufferedImage(width, width, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = image.createGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, width, width);
            graphics.setColor(Color.BLACK);
            byte[] bytes = (reqBo.getPath() + "|" + reqBo.getScene()).getBytes(StandardCharsets.UTF_8);
            int cell = Math.max(4, width / 35);
            for (int y = 0; y < width / cell; y++) {
                for (int x = 0; x < width / cell; x++) {
                    int index = (x + y * 31) % bytes.length;
                    if (((bytes[index] + x * 13 + y * 17) & 1) == 0) {
                        graphics.fillRect(x * cell, y * cell, cell, cell);
                    }
                }
            }
            graphics.dispose();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", outputStream);
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (Exception ex) {
            return Base64.getEncoder().encodeToString((reqBo.getPath() + "|" + reqBo.getScene()).getBytes(StandardCharsets.UTF_8));
        }
    }

}
