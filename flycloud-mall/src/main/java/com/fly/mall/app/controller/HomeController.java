package com.fly.mall.app.controller;

import com.fly.common.domain.model.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * app端首页控制层。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RestController
@RequestMapping("/app/home")
public class HomeController {

    /**
     * 获取推荐商品列表。
     *
     * @return 推荐商品列表
     */
    @GetMapping("/getRecommendGoodsList")
    public R<List<String>> getRecommendGoodsList() {
        List<String> goodsList = new ArrayList<>();
        goodsList.add("商品1");
        goodsList.add("商品2");
        goodsList.add("商品3");
        return R.ok(goodsList);
    }

}
