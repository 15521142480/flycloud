"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../index.js");
const sheep_api_product_spu = require("../../api/product/spu.js");
const sheep_api_promotion_point = require("../../api/promotion/point.js");
const sheep_helper_const = require("../../helper/const.js");
if (!Array) {
  const _easycom_s_goods_column2 = common_vendor.resolveComponent("s-goods-column");
  _easycom_s_goods_column2();
}
const _easycom_s_goods_column = () => "../s-goods-column/s-goods-column.js";
if (!Math) {
  _easycom_s_goods_column();
}
const _sfc_main = {
  __name: "s-point-block",
  props: {
    data: {
      type: Object,
      default() {
      }
    },
    styles: {
      type: Object,
      default() {
      }
    }
  },
  setup(__props) {
    const LayoutTypeEnum = {
      // 单列大图
      ONE_COL_BIG_IMG: "oneColBigImg",
      // 双列
      TWO_COL: "twoCol",
      // 单列小图
      ONE_COL_SMALL_IMG: "oneColSmallImg"
    };
    const state = common_vendor.reactive({
      spuList: [],
      leftSpuList: [],
      rightSpuList: []
    });
    const props = __props;
    const { layoutType, btnBuy, activityIds } = props.data || {};
    const { marginLeft, marginRight } = props.styles || {};
    const buyStyle = common_vendor.computed(() => {
      if (btnBuy.type === "text") {
        return {
          background: `linear-gradient(to right, ${btnBuy.bgBeginColor}, ${btnBuy.bgEndColor})`
        };
      }
      if (btnBuy.type === "img") {
        return {
          width: "54rpx",
          height: "54rpx",
          background: `url(${sheep_index.sheep.$url.cdn(btnBuy.imgUrl)}) no-repeat`,
          backgroundSize: "100% 100%"
        };
      }
    });
    let count = 0;
    let leftHeight = 0;
    let rightHeight = 0;
    function calculateGoodsColumn(height = 0, where = "left") {
      if (!state.spuList[count])
        return;
      if (where === "left")
        leftHeight += height;
      if (where === "right")
        rightHeight += height;
      if (leftHeight <= rightHeight) {
        state.leftSpuList.push(state.spuList[count]);
      } else {
        state.rightSpuList.push(state.spuList[count]);
      }
      count++;
    }
    async function getPointActivityDetailList(ids) {
      const { data } = await sheep_api_promotion_point.PointApi.getPointActivityListByIds(ids);
      return data;
    }
    async function getSpuDetail(ids) {
      const { data: spu } = await sheep_api_product_spu.SpuApi.getSpuDetail(ids);
      return spu;
    }
    common_vendor.onMounted(async () => {
      const activityList = await getPointActivityDetailList(activityIds.join(","));
      for (const activity of activityList) {
        state.spuList.push(await getSpuDetail(activity.spuId));
      }
      activityList.forEach((activity) => {
        const spu = state.spuList.find((spu2) => activity.spuId === spu2.id);
        if (spu) {
          spu.pointStock = activity.stock;
          spu.pointTotalStock = activity.totalStock;
          spu.point = activity.point;
          spu.pointPrice = activity.price;
          spu.activityId = activity.id;
          spu.activityType = sheep_helper_const.PromotionActivityTypeEnum.POINT.type;
        }
      });
      if (layoutType === LayoutTypeEnum.TWO_COL) {
        calculateGoodsColumn();
      }
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.unref(layoutType) === LayoutTypeEnum.ONE_COL_BIG_IMG && state.spuList.length
      }, common_vendor.unref(layoutType) === LayoutTypeEnum.ONE_COL_BIG_IMG && state.spuList.length ? {
        b: common_vendor.f(state.spuList, (item, k0, i0) => {
          var _a;
          return {
            a: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/goods/point", {
              id: item.activityId
            }), item.id),
            b: "273ad0c0-0-" + i0,
            c: common_vendor.p({
              size: "sl",
              goodsFields: __props.data.fields,
              tagStyle: __props.data.badge,
              data: item,
              titleColor: (_a = __props.data.fields.name) == null ? void 0 : _a.color,
              subTitleColor: __props.data.fields.introduction.color,
              topRadius: __props.data.borderRadiusTop,
              bottomRadius: __props.data.borderRadiusBottom
            }),
            d: item.id
          };
        }),
        c: common_vendor.t(common_vendor.unref(btnBuy).type === "text" ? common_vendor.unref(btnBuy).text : ""),
        d: common_vendor.s(buyStyle.value),
        e: common_vendor.s({
          marginBottom: __props.data.space * 2 + "rpx"
        })
      } : {}, {
        f: common_vendor.unref(layoutType) === LayoutTypeEnum.ONE_COL_SMALL_IMG && state.spuList.length
      }, common_vendor.unref(layoutType) === LayoutTypeEnum.ONE_COL_SMALL_IMG && state.spuList.length ? {
        g: common_vendor.f(state.spuList, (item, k0, i0) => {
          var _a;
          return {
            a: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/goods/point", {
              id: item.activityId
            }), item.id),
            b: "273ad0c0-1-" + i0,
            c: common_vendor.p({
              size: "lg",
              goodsFields: __props.data.fields,
              data: item,
              tagStyle: __props.data.badge,
              titleColor: (_a = __props.data.fields.name) == null ? void 0 : _a.color,
              subTitleColor: __props.data.fields.introduction.color,
              topRadius: __props.data.borderRadiusTop,
              bottomRadius: __props.data.borderRadiusBottom
            }),
            d: item.id
          };
        }),
        h: common_vendor.t(common_vendor.unref(btnBuy).type === "text" ? common_vendor.unref(btnBuy).text : ""),
        i: common_vendor.s(buyStyle.value),
        j: common_vendor.s({
          marginBottom: __props.data.space + "px"
        })
      } : {}, {
        k: common_vendor.unref(layoutType) === LayoutTypeEnum.TWO_COL && state.spuList.length
      }, common_vendor.unref(layoutType) === LayoutTypeEnum.TWO_COL && state.spuList.length ? {
        l: common_vendor.f(state.leftSpuList, (item, k0, i0) => {
          var _a;
          return {
            a: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/goods/point", {
              id: item.activityId
            }), item.id),
            b: common_vendor.o(($event) => calculateGoodsColumn($event, "left"), item.id),
            c: "273ad0c0-2-" + i0,
            d: common_vendor.p({
              size: "md",
              goodsFields: __props.data.fields,
              tagStyle: __props.data.badge,
              data: item,
              titleColor: (_a = __props.data.fields.name) == null ? void 0 : _a.color,
              subTitleColor: __props.data.fields.introduction.color,
              topRadius: __props.data.borderRadiusTop,
              bottomRadius: __props.data.borderRadiusBottom,
              titleWidth: 330 - common_vendor.unref(marginLeft) - common_vendor.unref(marginRight)
            }),
            e: item.id
          };
        }),
        m: common_vendor.t(common_vendor.unref(btnBuy).type === "text" ? common_vendor.unref(btnBuy).text : ""),
        n: common_vendor.s(buyStyle.value),
        o: common_vendor.s({
          paddingRight: __props.data.space + "rpx",
          marginBottom: __props.data.space + "px"
        }),
        p: common_vendor.f(state.rightSpuList, (item, k0, i0) => {
          var _a;
          return {
            a: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/goods/point", {
              id: item.activityId
            }), item.id),
            b: common_vendor.o(($event) => calculateGoodsColumn($event, "right"), item.id),
            c: "273ad0c0-3-" + i0,
            d: common_vendor.p({
              size: "md",
              goodsFields: __props.data.fields,
              tagStyle: __props.data.badge,
              data: item,
              titleColor: (_a = __props.data.fields.name) == null ? void 0 : _a.color,
              subTitleColor: __props.data.fields.introduction.color,
              topRadius: __props.data.borderRadiusTop,
              bottomRadius: __props.data.borderRadiusBottom,
              titleWidth: 330 - common_vendor.unref(marginLeft) - common_vendor.unref(marginRight)
            }),
            e: item.id
          };
        }),
        q: common_vendor.t(common_vendor.unref(btnBuy).type === "text" ? common_vendor.unref(btnBuy).text : ""),
        r: common_vendor.s(buyStyle.value),
        s: common_vendor.s({
          paddingLeft: __props.data.space + "rpx",
          marginBottom: __props.data.space + "px"
        })
      } : {});
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-273ad0c0"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/components/s-point-block/s-point-block.js.map
