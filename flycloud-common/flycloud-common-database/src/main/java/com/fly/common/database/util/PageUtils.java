package com.fly.common.database.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.entity.Search;

/**
 * 分页工具类
 *
 */
public class PageUtils {

	public static <T> IPage<T> getPage(Search search) {
		return new Page<T>(search.getCurrent(), search.getSize());
	}
}
