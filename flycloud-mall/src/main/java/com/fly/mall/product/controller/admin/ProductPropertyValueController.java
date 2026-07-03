package com.fly.mall.product.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.mall.api.product.domain.bo.ProductPropertyValueBo;
import com.fly.mall.api.product.domain.vo.ProductPropertyValueRespVo;
import com.fly.mall.api.product.domain.vo.ProductPropertyValueVo;
import com.fly.mall.product.service.IProductPropertyValueService;
import cn.hutool.core.bean.BeanUtil;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 管理后台 - 商品属性值 控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping( "/admin/product/property/value")
public class ProductPropertyValueController extends BaseController {

    private final IProductPropertyValueService productPropertyValueService;

    /**
     * 查询商品属性值分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:product:property:list')")
    @GetMapping("/list")
    public R<PageVo<ProductPropertyValueRespVo>> list(ProductPropertyValueBo bo, PageBo page) {
        return R.ok(convertPage(productPropertyValueService.queryPageList(bo, page)));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:product:property:list')")
    @GetMapping("/page")
    public R<PageVo<ProductPropertyValueRespVo>> page(ProductPropertyValueBo bo, PageBo page) {
        return R.ok(convertPage(productPropertyValueService.queryPageList(bo, page)));
    }

    /**
     * 查询所有商品属性值。
     */
    @GetMapping({"/getList", "/simple-list"})
    public R<List<ProductPropertyValueRespVo>> allList(ProductPropertyValueBo bo) {
        return R.ok(BeanUtil.copyToList(productPropertyValueService.queryList(bo), ProductPropertyValueRespVo.class));
    }

    /**
     * 获取商品属性值详情。
     */
    @GetMapping("/get/{id}")
    public R<ProductPropertyValueRespVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(BeanUtil.toBean(productPropertyValueService.queryById(id), ProductPropertyValueRespVo.class));
    }

    /**
     * 获得详情。
     */
    @GetMapping("/get-detail")
    public R<ProductPropertyValueRespVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(BeanUtil.toBean(productPropertyValueService.queryById(id), ProductPropertyValueRespVo.class));
    }

    /**
     * 获得商品属性值详情，兼容 yudao 前端接口。
     */
    @GetMapping("/get")
    public R<ProductPropertyValueRespVo> get(@RequestParam("id") Long id) {
        return R.ok(BeanUtil.toBean(productPropertyValueService.queryById(id), ProductPropertyValueRespVo.class));
    }

    /**
     * 新增或修改商品属性值。
     */
    @Log(title = "商品属性值", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:product:property:saveOrUpdate')")
    @PostMapping({"/saveOrUpdate", "/create"})
    public R<Long> saveOrUpdate(@RequestBody ProductPropertyValueBo bo) {
        if (bo.getId() == null) {
            return R.ok(productPropertyValueService.createPropertyValue(bo));
        }
        productPropertyValueService.saveOrUpdate(bo);
        return R.ok(bo.getId());
    }

    /**
     * 更新数据，兼容 yudao 前端接口。
     */
    @PutMapping("/update")
    public R<Boolean> yudaoUpdate(@RequestBody ProductPropertyValueBo bo) {
        return R.result(productPropertyValueService.saveOrUpdate(bo));
    }

    /**
     * 删除商品属性值。
     */
    @Log(title = "商品属性值", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:product:property:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Boolean> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.result(productPropertyValueService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     * 删除数据，兼容 yudao 前端接口。
     */
    @DeleteMapping("/delete")
    public R<Boolean> yudaoDelete(@RequestParam("id") Long id) {
        return R.result(productPropertyValueService.deleteWithValidByIds(java.util.List.of(id), true));
    }

    /**
     * 转换商品属性值分页响应对象。
     */
    private PageVo<ProductPropertyValueRespVo> convertPage(PageVo<ProductPropertyValueVo> page) {
        PageVo<ProductPropertyValueRespVo> respPage = new PageVo<>();
        respPage.setList(BeanUtil.copyToList(page.getList(), ProductPropertyValueRespVo.class));
        respPage.setTotal(page.getTotal());
        respPage.setPages(page.getPages());
        return respPage;
    }

}
