package com.fly.mall.app.controller;

import com.fly.common.domain.model.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 'app端'首页-控制层
 *
 * @author lxs
 * @date 2023/4/24
 */
@RestController
@RequestMapping("/app/home")
public class HomeController {


    /**
     * 获取推荐商品列表
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
