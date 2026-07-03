package com.fly.mall.product.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.common.utils.ExcelUtil;
import com.fly.mall.api.product.domain.bo.ProductSpuBo;
import com.fly.mall.api.product.domain.vo.ProductSpuRespVo;
import com.fly.mall.api.product.domain.vo.ProductSpuSimpleRespVo;
import com.fly.mall.api.product.domain.vo.ProductSpuVo;
import com.fly.mall.product.service.IProductSpuService;
import cn.hutool.core.bean.BeanUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Collection;
import java.util.Map;

/**
 * 管理后台 - 商品 SPU 控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/product/spu")
public class ProductSpuController extends BaseController {

    private final IProductSpuService productSpuService;

    /**
     * 忽略 BaseEntity 单值创建时间绑定，避免 yudao 前端 createTime[0]/createTime[1] 查询参数绑定失败。
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("createTime", "createTime[0]", "createTime[1]");
    }

    /**
     * 查询商品 SPU 分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:product:spu:list')")
    @GetMapping(value = "/list", params = "!spuIds")
    public R<PageVo<ProductSpuRespVo>> list(ProductSpuBo bo, PageBo page,
                                            @RequestParam(value = "createTime[0]", required = false)
                                            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime beginTime,
                                            @RequestParam(value = "createTime[1]", required = false)
                                            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        setCreateTimeRange(bo, beginTime, endTime);
        return R.ok(convertPage(productSpuService.queryPageList(bo, page)));
    }

    /**
     * 获得商品 SPU 分页。
     */
    @PreAuthorize("@pms.hasPermission('mall:product:spu:list')")
    @GetMapping("/page")
    public R<PageVo<ProductSpuRespVo>> page(ProductSpuBo bo, PageBo page,
                                            @RequestParam(value = "createTime[0]", required = false)
                                            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime beginTime,
                                            @RequestParam(value = "createTime[1]", required = false)
                                            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        setCreateTimeRange(bo, beginTime, endTime);
        return R.ok(convertPage(productSpuService.queryPageList(bo, page)));
    }

    /**
     * 获得商品 SPU 详情列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:product:spu:list')")
    @GetMapping(value = {"/detail-list", "/list"}, params = "spuIds")
    public R<List<ProductSpuRespVo>> getSpuList(@RequestParam("spuIds") Collection<Long> spuIds) {
        return R.ok(BeanUtil.copyToList(productSpuService.queryListByIds(spuIds), ProductSpuRespVo.class));
    }

    /**
     * 获得商品 SPU 精简列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:product:spu:list')")
    @GetMapping("/list-all-simple")
    public R<List<ProductSpuSimpleRespVo>> getSpuSimpleList() {
        ProductSpuBo bo = new ProductSpuBo();
        bo.setStatus(com.fly.common.enums.StatusEnum.ENABLE.getStatus());
        return R.ok(BeanUtil.copyToList(productSpuService.queryList(bo), ProductSpuSimpleRespVo.class));
    }

    /**
     * 获得商品 SPU 状态数量。
     */
    @PreAuthorize("@pms.hasPermission('mall:product:spu:list')")
    @GetMapping("/get-count")
    public R<Map<Integer, Long>> getSpuCount(ProductSpuBo bo,
                                             @RequestParam(value = "createTime[0]", required = false)
                                             @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime beginTime,
                                             @RequestParam(value = "createTime[1]", required = false)
                                             @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        setCreateTimeRange(bo, beginTime, endTime);
        return R.ok(productSpuService.queryStatusCount(bo));
    }

    /**
     * 获取商品 SPU 详情。
     */
    @GetMapping("/get/{id}")
    public R<ProductSpuRespVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(BeanUtil.toBean(productSpuService.queryDetailById(id), ProductSpuRespVo.class));
    }

    /**
     * 获得商品 SPU 明细。
     */
    @GetMapping("/get-detail")
    public R<ProductSpuRespVo> getSpuDetail(@RequestParam("id") Long id) {
        return R.ok(BeanUtil.toBean(productSpuService.queryDetailById(id), ProductSpuRespVo.class));
    }

    /**
     * 新增或修改商品 SPU。
     */
    @Log(title = "商品SPU", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:product:spu:saveOrUpdate')")
    @PostMapping({"/saveOrUpdate", "/create"})
    public R<Long> saveOrUpdate(@RequestBody ProductSpuBo bo) {
        if (bo.getId() == null) {
            return R.ok(productSpuService.createSpu(bo));
        }
        productSpuService.saveOrUpdate(bo);
        return R.ok(bo.getId());
    }

    /**
     * 更新数据，兼容 yudao 前端接口。
     */
    @PutMapping("/update")
    public R<Boolean> yudaoUpdate(@RequestBody ProductSpuBo bo) {
        return R.result(productSpuService.saveOrUpdate(bo));
    }

    /**
     * 更新商品 SPU 状态。
     */
    @Log(title = "商品SPU", businessType = BusinessType.UPDATE)
    @PreAuthorize("@pms.hasPermission('mall:product:spu:saveOrUpdate')")
    @PutMapping("/update-status")
    public R<Boolean> updateStatus(@RequestBody ProductSpuBo bo) {
        return R.result(productSpuService.updateStatus(bo.getId(), bo.getStatus()));
    }

    /**
     * 删除商品 SPU。
     */
    @Log(title = "商品SPU", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:product:spu:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Boolean> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.result(productSpuService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     * 删除数据，兼容 yudao 前端接口。
     */
    @DeleteMapping("/delete")
    public R<Boolean> yudaoDelete(@RequestParam("id") Long id) {
        return R.result(productSpuService.deleteWithValidByIds(java.util.List.of(id), true));
    }

    /**
     * 转换商品 SPU 分页响应对象。
     */
    private PageVo<ProductSpuRespVo> convertPage(PageVo<ProductSpuVo> page) {
        PageVo<ProductSpuRespVo> respPage = new PageVo<>();
        respPage.setList(BeanUtil.copyToList(page.getList(), ProductSpuRespVo.class));
        respPage.setTotal(page.getTotal());
        respPage.setPages(page.getPages());
        return respPage;
    }

    /**
     * 设置商品 SPU 创建时间查询范围。
     */
    private void setCreateTimeRange(ProductSpuBo bo, LocalDateTime beginTime, LocalDateTime endTime) {
        if (beginTime != null) {
            bo.getParams().put("beginCreateTime", beginTime);
        }
        if (endTime != null) {
            bo.getParams().put("endCreateTime", endTime);
        }
    }

    /**
     * 导出商品 SPU。
     */
    @Log(title = "商品SPU", businessType = BusinessType.EXPORT)
    @PreAuthorize("@pms.hasPermission('mall:product:spu:download')")
    @PostMapping("/export")
    public void export(ProductSpuBo bo, HttpServletResponse response) {
        List<ProductSpuVo> list = productSpuService.queryList(bo);
        ExcelUtil.exportExcel(list, "商品SPU", ProductSpuVo.class, response);
    }

    /**
     * 导出商品 SPU，兼容 yudao 前端接口。
     */
    @GetMapping("/export-excel")
    public void exportExcel(ProductSpuBo bo, HttpServletResponse response) {
        export(bo, response);
    }

}
