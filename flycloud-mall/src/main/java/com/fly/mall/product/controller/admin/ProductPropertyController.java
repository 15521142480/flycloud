package com.fly.mall.product.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.mall.api.product.domain.bo.ProductPropertyBo;
import com.fly.mall.api.product.domain.vo.ProductPropertyRespVo;
import com.fly.mall.api.product.domain.vo.ProductPropertyVo;
import com.fly.mall.product.service.IProductPropertyService;
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
 * 管理后台 - 商品属性 控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/product/property")
public class ProductPropertyController extends BaseController {

    private final IProductPropertyService productPropertyService;

    /**
     * 查询商品属性分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:product:property:list')")
    @GetMapping("/list")
    public R<PageVo<ProductPropertyRespVo>> list(ProductPropertyBo bo, PageBo page) {
        return R.ok(convertPage(productPropertyService.queryPageList(bo, page)));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:product:property:list')")
    @GetMapping("/page")
    public R<PageVo<ProductPropertyRespVo>> page(ProductPropertyBo bo, PageBo page) {
        return R.ok(convertPage(productPropertyService.queryPageList(bo, page)));
    }

    /**
     * 查询所有商品属性。
     */
    @GetMapping({"/getList", "/simple-list"})
    public R<List<ProductPropertyRespVo>> allList(ProductPropertyBo bo) {
        return R.ok(BeanUtil.copyToList(productPropertyService.queryList(bo), ProductPropertyRespVo.class));
    }

    /**
     * 获取商品属性详情。
     */
    @GetMapping("/get/{id}")
    public R<ProductPropertyRespVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(BeanUtil.toBean(productPropertyService.queryById(id), ProductPropertyRespVo.class));
    }

    /**
     * 获得详情。
     */
    @GetMapping("/get-detail")
    public R<ProductPropertyRespVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(BeanUtil.toBean(productPropertyService.queryById(id), ProductPropertyRespVo.class));
    }

    /**
     * 获得商品属性详情，兼容 yudao 前端接口。
     */
    @GetMapping("/get")
    public R<ProductPropertyRespVo> get(@RequestParam("id") Long id) {
        return R.ok(BeanUtil.toBean(productPropertyService.queryById(id), ProductPropertyRespVo.class));
    }

    /**
     * 新增或修改商品属性。
     */
    @Log(title = "商品属性", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:product:property:saveOrUpdate')")
    @PostMapping({"/saveOrUpdate", "/create"})
    public R<Long> saveOrUpdate(@RequestBody ProductPropertyBo bo) {
        if (bo.getId() == null) {
            return R.ok(productPropertyService.createProperty(bo));
        }
        productPropertyService.saveOrUpdate(bo);
        return R.ok(bo.getId());
    }

    /**
     * 更新数据，兼容 yudao 前端接口。
     */
    @PutMapping("/update")
    public R<Boolean> yudaoUpdate(@RequestBody ProductPropertyBo bo) {
        return R.ok(productPropertyService.saveOrUpdate(bo));
    }

    /**
     * 删除商品属性。
     */
    @Log(title = "商品属性", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:product:property:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Boolean> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(productPropertyService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     * 删除数据，兼容 yudao 前端接口。
     */
    @DeleteMapping("/delete")
    public R<Boolean> yudaoDelete(@RequestParam("id") Long id) {
        return R.ok(productPropertyService.deleteWithValidByIds(java.util.List.of(id), true));
    }

    /**
     * 转换商品属性分页响应对象。
     */
    private PageVo<ProductPropertyRespVo> convertPage(PageVo<ProductPropertyVo> page) {
        PageVo<ProductPropertyRespVo> respPage = new PageVo<>();
        respPage.setList(BeanUtil.copyToList(page.getList(), ProductPropertyRespVo.class));
        respPage.setTotal(page.getTotal());
        respPage.setPages(page.getPages());
        return respPage;
    }

}
