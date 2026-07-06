"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../index.js");
const sheep_hooks_useGoods = require("../../hooks/useGoods.js");
const _sfc_main = {
  __name: "s-goods-item",
  props: {
    img: {
      type: String,
      default: "https://img1.baidu.com/it/u=1601695551,235775011&fm=26&fmt=auto"
    },
    title: {
      type: String,
      default: ""
    },
    titleWidth: {
      type: Number,
      default: 0
    },
    skuText: {
      type: [String, Array],
      default: ""
    },
    price: {
      type: [String, Number],
      default: ""
    },
    priceColor: {
      type: [String],
      default: ""
    },
    num: {
      type: [String, Number],
      default: 0
    },
    point: {
      type: [String, Number],
      default: ""
    },
    radius: {
      type: [String],
      default: ""
    },
    marginBottom: {
      type: [String],
      default: ""
    }
  },
  setup(__props) {
    const props = __props;
    const skuString = common_vendor.computed(() => {
      if (!props.skuText) {
        return "";
      }
      if (typeof props.skuText === "object") {
        return props.skuText.join(",");
      }
      return props.skuText;
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.unref(sheep_index.sheep).$url.cdn(__props.img),
        b: __props.title
      }, __props.title ? {
        c: common_vendor.t(__props.title)
      } : {}, {
        d: skuString.value
      }, skuString.value ? {
        e: common_vendor.t(skuString.value)
      } : {}, {
        f: __props.price && Number(__props.price) > 0
      }, __props.price && Number(__props.price) > 0 ? {
        g: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(__props.price)),
        h: common_vendor.s({
          color: __props.priceColor
        })
      } : {}, {
        i: __props.point && Number(__props.price) > 0
      }, __props.point && Number(__props.price) > 0 ? {} : {}, {
        j: __props.point
      }, __props.point ? {
        k: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/goods/score1.svg"),
        l: common_vendor.t(__props.point)
      } : {}, {
        m: __props.num
      }, __props.num ? {
        n: common_vendor.t(__props.num)
      } : {}, {
        o: common_vendor.s({
          width: __props.titleWidth ? __props.titleWidth + "rpx" : ""
        }),
        p: common_vendor.s({
          borderRadius: __props.radius + "rpx",
          marginBottom: __props.marginBottom + "rpx"
        })
      });
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-8a2df41e"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/components/s-goods-item/s-goods-item.js.map
