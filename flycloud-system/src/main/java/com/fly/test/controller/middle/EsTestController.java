package com.fly.test.controller.middle;

import com.fly.common.domain.model.R;
import com.fly.test.service.ITestService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Elasticsearch 历史联调接口。
 *
 * <p>生产会员检索请使用 {@code MemberUserSearchController}，本接口不承载正式搜索业务。</p>
 *
 * @author: lxs
 * @date: 2025/8/15
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/test/es")
public class EsTestController {

    private final ITestService testService;

    /**
     * 执行历史 ES 增、删、改、查联调操作。
     *
     * @param type 操作类型：1 到 4 分别表示增、删、改、查
     * @param indexName 索引名称
     * @return 操作结果
     * @throws IOException ES 调用异常
     */
    @GetMapping("/esTest/{type}/{indexName}")
    public R<Void> seataTest(@NotNull(message = "type不能为空(1到4分别是增删改查)") @PathVariable Integer type, @NotNull(message = "indexName不能为空") @PathVariable String indexName) throws IOException {
        return R.result(testService.esTest(type, indexName));
    }

}
