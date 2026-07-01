package com.fly.mall.product.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.product.domain.bo.ProductCommentBo;
import com.fly.mall.api.product.domain.vo.ProductCommentVo;

import java.util.Collection;
import java.util.List;

/**
 * 商品评价 Service 接口。
 *
 * @author lxs
 * @date 2026-06-28
 */
public interface IProductCommentService {

    /**
     * 查询商品评价详情。
     */
    ProductCommentVo queryById(Long id);

    /**
     * 分页查询商品评价。
     */
    PageVo<ProductCommentVo> queryPageList(ProductCommentBo bo, PageBo pageBo);

    /**
     * 查询商品评价列表。
     */
    List<ProductCommentVo> queryList(ProductCommentBo bo);

    /**
     * 新增或修改商品评价。
     */
    Boolean saveOrUpdate(ProductCommentBo bo);

    /**
     * 回复商品评价。
     */
    Boolean replyComment(ProductCommentBo bo);

    /**
     * 更新商品评价可见状态。
     */
    Boolean updateCommentVisible(ProductCommentBo bo);

    /**
     * 校验并批量删除商品评价。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
