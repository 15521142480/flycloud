package com.fly.common.domain.bo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.utils.PrincipalUtils;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.Arrays;

/**
 * 分页-bo
 *
 * @author lxs
 * @date 2023/3/22
 */
@Data
public class PageBo implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * 升序
     */
    public static final String ASC = "ASC";

    /**
     * 倒序
     */
    public static final String DESC = "DESC";

    /**
     * 最小当前页
     */
    public static final Integer MIN_PAGE_NUM = 1;


    /**
     * 最大分页大小，如果分页大小大于1000，则用1000作为分页的大小。防止有人直接传入一个较大的数，导致服务器内存溢出宕机
     */
    public static final Integer MAX_PAGE_SIZE = 1000;

    /**
     * 当前页
     */
    // @NotNull(message = "pageNum 不能为空")
    @Min(value = 1, message = "页码最小值为 1")
    private Integer pageNum;

    /**
     * 每页大小
     */
    // @NotNull(message = "pageSize 不能为空")
    @Range(min = 1, max = 1000, message = "每页条数，取值范围 1-1000") // 防止有人直接传入一个较大的数，导致服务器内存溢出宕机
    private Integer pageSize;

    /**
     * 排序字段数组，用逗号分割
     */
    private String[] columns;

    /**
     * 排序字段方式，用逗号分割，ASC正序，DESC倒序
     */
    private String[] orders;

    /**
     * 排序
     */
    private String orderBy;


    /**
     * build成iPage
     *
     * @author lxs
     * @date 2023/3/22
     */
    public <T> Page<T> build() {

        Integer pageNum = getPageNum();
        Integer pageSize = getPageSize();
        if (pageNum <= 0) {
            pageNum = MIN_PAGE_NUM;
        }
        if (pageSize > MAX_PAGE_SIZE) {
            pageSize = MAX_PAGE_SIZE;
        }
        Page<T> page = new Page<>(pageNum, pageSize);

//        OrderItem orderItem = buildOrderItem();
//        if (ObjectUtil.isNotNull(orderItem)) {
//            page.addOrder(orderItem);
//        }
        return page;
    }


    /**
     * 构建返回排序
     *
     * @author lxs
     * @date 2023/3/22
     */
//    public List<OrderItem> buildOrderItem(){
//
//
//    }


    /**
     * 获取排序字符字符串
     *
     * @author lxs
     * @date 2023/3/22
     */
    public String getOrderBy() {
        return getOrderByColumnsAndOrders(this.columns, this.orders);
    }


    /**
     * 根据字段和排序标识获取排序字符串
     *
     * @author lxs
     * @date 2023/3/22
     */
    public static String getOrderByColumnsAndOrders(String[] columns, String[] orders) {

        if (columns == null || columns.length == 0) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();

        for (int x = 0; x < columns.length; x++) {

            String column = columns[x];
            String order;

            if (orders != null && orders.length > x) {
                order = orders[x].toUpperCase();
                if (!(order.equals(ASC) || order.equals(DESC))) {
                    throw new IllegalArgumentException("非法的排序策略：" + column);
                }
            } else {
                order = ASC;
            }

            // 判断列名称的合法性，防止SQL注入。只能是【字母，数字，下划线】
            if (PrincipalUtils.isField(column)) {
                throw new IllegalArgumentException("非法的排序字段名称：" + column);
            }

            // 驼峰转换为下划线
            column = humpConversionUnderscore(column);

            if (x != 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append("`").append(column).append("` ").append(order);
        }
        return stringBuilder.toString();
    }


    public static String humpConversionUnderscore(String value) {
        StringBuilder stringBuilder = new StringBuilder();
        char[] chars = value.toCharArray();
        for (char character : chars) {
            if (Character.isUpperCase(character)) {
                stringBuilder.append("_");
                character = Character.toLowerCase(character);
            }
            stringBuilder.append(character);
        }
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return "PageDTO{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", columns=" + Arrays.toString(columns) +
                ", orders=" + Arrays.toString(orders) +
                '}';
    }
}
