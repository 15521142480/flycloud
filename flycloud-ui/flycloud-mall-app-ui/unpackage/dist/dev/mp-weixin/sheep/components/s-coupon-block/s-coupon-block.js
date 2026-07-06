"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../index.js");
const sheep_api_promotion_coupon = require("../../api/promotion/coupon.js");
const sheep_helper_const = require("../../helper/const.js");
const sheep_helper_utils = require("../../helper/utils.js");
const sheep_hooks_useGoods = require("../../hooks/useGoods.js");
if (!Array) {
  const _easycom_su_coupon2 = common_vendor.resolveComponent("su-coupon");
  _easycom_su_coupon2();
}
const _easycom_su_coupon = () => "../../ui/su-coupon/su-coupon.js";
if (!Math) {
  _easycom_su_coupon();
}
const _sfc_main = {
  __name: "s-coupon-block",
  props: {
    data: {
      type: Object,
      default: () => ({})
    },
    styles: {
      type: Object,
      default: () => ({})
    }
  },
  setup(__props) {
    const props = __props;
    const { columns, button } = props.data;
    const SIZE_LIST = ["lg", "md", "xs"];
    const couponBg = {
      background: `url(${sheep_index.sheep.$url.cdn(props.data.bgImg)}) no-repeat top center / 100% 100%`
    };
    const btnStyles = {
      background: button.bgColor,
      color: button.color
    };
    const couponBoxStyleNormal = {
      display: "flex",
      "justify-content": "space-between"
    };
    const couponBoxStyleTwo = {
      display: "flex",
      "justify-content": "space-around"
    };
    const bgStyle = common_vendor.computed(() => {
      const { bgType, bgImg, bgColor } = props.styles;
      return {
        background: bgType === "img" ? `url(${bgImg}) no-repeat top center / 100% 100%` : bgColor
      };
    });
    const formatCouponDiscountType = (coupon) => {
      if (coupon.discountType === sheep_helper_const.PromotionDiscountTypeEnum.PRICE.type) {
        return "reduce";
      }
      if (coupon.discountType === sheep_helper_const.PromotionDiscountTypeEnum.PERCENT.type) {
        return "percent";
      }
      return `未知【${coupon.discountType}】`;
    };
    const formatCouponDiscountValue = (coupon) => {
      if (coupon.discountType === sheep_helper_const.PromotionDiscountTypeEnum.PRICE.type) {
        return sheep_helper_utils.floatToFixed2(coupon.discountPrice);
      }
      if (coupon.discountType === sheep_helper_const.PromotionDiscountTypeEnum.PERCENT.type) {
        return sheep_hooks_useGoods.formatDiscountPercent(coupon.discountPercent);
      }
      return `未知【${coupon.discountType}】`;
    };
    const formatValidityType = (row) => {
      if (row.validityType === sheep_helper_const.CouponTemplateValidityTypeEnum.DATE.type) {
        return `${sheep_helper_utils.formatDate(row.validStartTime)} 至 ${sheep_helper_utils.formatDate(row.validEndTime)}`;
      }
      if (row.validityType === sheep_helper_const.CouponTemplateValidityTypeEnum.TERM.type) {
        return `领取后第 ${row.fixedStartTerm} - ${row.fixedEndTerm} 天内可用`;
      }
      return "未知【" + row.validityType + "】";
    };
    const formatCouponTitle = (coupon) => {
      return `满${sheep_helper_utils.floatToFixed2(coupon.usePrice)}元${coupon.discountType === sheep_helper_const.PromotionDiscountTypeEnum.PRICE.type ? ",减" + sheep_helper_utils.floatToFixed2(coupon.discountPrice) + "元" : ",打" + sheep_hooks_useGoods.formatDiscountPercent(coupon.discountPercent) + "折"}`;
    };
    const couponList = common_vendor.ref([]);
    async function onGetCoupon(id) {
      const { error, msg } = await sheep_api_promotion_coupon.CouponApi.takeCoupon(id);
      if (error === 0) {
        common_vendor.index.showToast({
          title: msg,
          icon: "none"
        });
        return;
      }
      await getCouponTemplateList();
    }
    const getCouponTemplateList = async () => {
      const { data } = await sheep_api_promotion_coupon.CouponApi.getCouponTemplateListByIds(props.data.couponIds.join(","));
      couponList.value = data;
    };
    common_vendor.onMounted(() => {
      getCouponTemplateList();
    });
    return (_ctx, _cache) => {
      return {
        a: common_vendor.f(couponList.value, (item, index, i0) => {
          return common_vendor.e(common_vendor.unref(columns) === 2 ? {
            a: common_vendor.o(($event) => onGetCoupon(item.id), index),
            b: common_vendor.s(btnStyles)
          } : {
            c: common_vendor.s(btnStyles),
            d: common_vendor.o(($event) => onGetCoupon(item.id), index)
          }, {
            e: "6b61b8da-0-" + i0,
            f: common_vendor.p({
              size: SIZE_LIST[common_vendor.unref(columns) - 1],
              textColor: __props.data.textColor,
              background: "",
              couponId: item.id,
              title: formatCouponTitle(item),
              type: formatCouponDiscountType(item),
              value: formatCouponDiscountValue(item),
              sellBy: formatValidityType(item),
              surplus: item.totalCount === -1 ? -1 : item.totalCount - item.takeCount
            }),
            g: index
          });
        }),
        b: common_vendor.unref(columns) === 2,
        c: common_vendor.s(couponBg),
        d: common_vendor.s({
          marginLeft: `${__props.data.space}px`
        }),
        e: common_vendor.s(couponList.value.length === 2 ? couponBoxStyleTwo : couponBoxStyleNormal),
        f: common_vendor.s(bgStyle.value),
        g: common_vendor.s({
          marginLeft: `${__props.data.space}px`
        })
      };
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-6b61b8da"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/components/s-coupon-block/s-coupon-block.js.map
