"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../index.js");
const sheep_api_product_spu = require("../../api/product/spu.js");
if (!Array) {
  const _easycom_s_goods_column2 = common_vendor.resolveComponent("s-goods-column");
  _easycom_s_goods_column2();
}
const _easycom_s_goods_column = () => "../s-goods-column/s-goods-column.js";
if (!Math) {
  _easycom_s_goods_column();
}
const _sfc_main = {
  __name: "s-goods-shelves",
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
    const props = __props;
    const { layoutType, spuIds } = props.data;
    let { marginLeft, marginRight } = props.styles;
    const goodsList = common_vendor.ref([]);
    common_vendor.onMounted(async () => {
      if (spuIds.length > 0) {
        let { data } = await sheep_api_product_spu.SpuApi.getSpuListByIds(spuIds.join(","));
        goodsList.value = data;
      }
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.unref(layoutType) === "twoCol"
      }, common_vendor.unref(layoutType) === "twoCol" ? {
        b: common_vendor.f(goodsList.value, (item, k0, i0) => {
          var _a;
          return {
            a: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/goods/index", {
              id: item.id
            }), item.id),
            b: "9630804b-0-" + i0,
            c: common_vendor.p({
              size: "xs",
              goodsFields: __props.data.fields,
              tagStyle: __props.data.badge,
              data: item,
              titleColor: (_a = __props.data.fields.name) == null ? void 0 : _a.color,
              topRadius: __props.data.borderRadiusTop,
              bottomRadius: __props.data.borderRadiusBottom,
              titleWidth: (454 - common_vendor.unref(marginRight) * 2 - __props.data.space * 2 - common_vendor.unref(marginLeft) * 2) / 2
            }),
            d: item.id
          };
        }),
        c: common_vendor.s({
          padding: __props.data.space + "rpx"
        }),
        d: common_vendor.s({
          margin: "-" + __props.data.space + "rpx"
        })
      } : {}, {
        e: common_vendor.unref(layoutType) === "threeCol"
      }, common_vendor.unref(layoutType) === "threeCol" ? {
        f: common_vendor.f(goodsList.value, (item, k0, i0) => {
          var _a;
          return {
            a: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/goods/index", {
              id: item.id
            }), item.id),
            b: "9630804b-1-" + i0,
            c: common_vendor.p({
              size: "sm",
              goodsFields: __props.data.fields,
              tagStyle: __props.data.badge,
              data: item,
              titleColor: (_a = __props.data.fields.name) == null ? void 0 : _a.color,
              topRadius: __props.data.borderRadiusTop,
              bottomRadius: __props.data.borderRadiusBottom
            }),
            d: item.id
          };
        }),
        g: common_vendor.s({
          padding: __props.data.space + "rpx"
        }),
        h: common_vendor.s({
          margin: "-" + __props.data.space + "rpx"
        })
      } : {}, {
        i: common_vendor.unref(layoutType) === "horizSwiper"
      }, common_vendor.unref(layoutType) === "horizSwiper" ? {
        j: common_vendor.f(goodsList.value, (item, k0, i0) => {
          var _a;
          return {
            a: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/goods/index", {
              id: item.id
            }), item.id),
            b: "9630804b-2-" + i0,
            c: common_vendor.p({
              size: "sm",
              goodsFields: __props.data.fields,
              tagStyle: __props.data.badge,
              data: item,
              titleColor: (_a = __props.data.fields.name) == null ? void 0 : _a.color,
              titleWidth: (750 - common_vendor.unref(marginRight) * 2 - __props.data.space * 4 - common_vendor.unref(marginLeft) * 2) / 3
            }),
            d: item.id
          };
        }),
        k: common_vendor.s({
          marginRight: __props.data.space * 2 + "rpx"
        })
      } : {});
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-9630804b"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/components/s-goods-shelves/s-goods-shelves.js.map
