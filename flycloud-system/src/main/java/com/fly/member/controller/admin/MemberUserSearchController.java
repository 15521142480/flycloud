package com.fly.member.controller.admin;

import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.member.search.model.MemberUserSearchInsertBo;
import com.fly.member.search.model.MemberUserSearchPageBo;
import com.fly.member.search.model.MemberUserSearchUpdateBo;
import com.fly.member.search.model.MemberUserSearchVo;
import com.fly.member.search.service.MemberUserSearchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 会员用户 Elasticsearch 演示控制器。
 *
 * <p>仅用于验证搜索、异步投影和版本索引升级链路。正式业务接口仍应收敛在 {@code MemberUserController}，
 * 并以独立的 {@code search-page} 等路径区分搜索语义。</p>
 *
 * @author: lxs
 * @date: 2026/7/15
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/member/user/search")
@ConditionalOnExpression("'${flycloud.elasticsearch.enabled:false}' == 'true' and '${flycloud.rocketmq.enabled:false}' == 'true'")
public class MemberUserSearchController {

    private final MemberUserSearchService memberUserSearchService;

    /**
     * 接口 1：创建/复用会员索引别名并分页全量同步 MySQL 数据。
     */
    @PostMapping("/synchronize")
    public R<String> synchronizeMemberUser() {
        return R.ok(memberUserSearchService.fullSynchronize());
    }

    /**
     * 接口 2：新增会员用户，并在同一事务中写入本地消息表。
     *
     * @param bo 新增会员请求
     * @return 新增会员主键
     */
    @PostMapping("/insert")
    public R<Long> insertMemberUser(@Valid @RequestBody MemberUserSearchInsertBo bo) {
        return R.ok(memberUserSearchService.insertMemberUser(bo));
    }

    /**
     * 接口 3：使用 ES 官方 Java Client 按条件分页检索会员用户。
     */
    @GetMapping("/page")
    public R<PageVo<MemberUserSearchVo>> memberUserPage(MemberUserSearchPageBo bo) {
        return R.ok(memberUserSearchService.searchPage(bo));
    }

    /**
     * 接口 4：更新 MySQL，写本地消息表，由 RocketMQ 异步更新 ES。
     */
    @PutMapping("/update")
    public R<Void> updateMemberUser(@Valid @RequestBody MemberUserSearchUpdateBo bo) {
        memberUserSearchService.updateMemberUser(bo);
        return R.ok();
    }

    /**
     * 接口 5：升级索引并原子切换别名；post_ids 必须先由升级脚本添加。
     */
    @PostMapping("/upgrade-index")
    public R<String> upgradeMemberUserIndex() {
        return R.ok(memberUserSearchService.upgradeIndexToNextVersion());
    }

    /**
     * 接口 6：索引升级观察期内的一键 Alias 回滚。
     */
    @PostMapping("/rollback-index/{recordId}")
    public R<Void> rollbackMemberUserIndex(@PathVariable Long recordId) {
        memberUserSearchService.rollbackIndex(recordId);
        return R.ok();
    }
}
