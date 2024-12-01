package com.fly.common.database.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.entity.Search;
import com.fly.common.domain.bo.PageBo;

/**
 * 分页工具类
 *
 */
public class PageUtils {

	public static <T> IPage<T> getPage(Search search) {
		return new Page<T>(search.getCurrent(), search.getSize());
	}

	public static int getStart(PageBo pageBo) {
		return (pageBo.getPageNum() - 1) * pageBo.getPageSize();
	}
}
