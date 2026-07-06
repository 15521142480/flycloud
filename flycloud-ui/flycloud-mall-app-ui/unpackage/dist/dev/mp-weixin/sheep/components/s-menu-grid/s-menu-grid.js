"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../index.js");
if (!Array) {
  const _easycom_uni_grid_item2 = common_vendor.resolveComponent("uni-grid-item");
  const _easycom_uni_grid2 = common_vendor.resolveComponent("uni-grid");
  (_easycom_uni_grid_item2 + _easycom_uni_grid2)();
}
const _easycom_uni_grid_item = () => "../../../uni_modules/uni-grid/components/uni-grid-item/uni-grid-item.js";
const _easycom_uni_grid = () => "../../../uni_modules/uni-grid/components/uni-grid/uni-grid.js";
if (!Math) {
  (_easycom_uni_grid_item + _easycom_uni_grid)();
}
const _sfc_main = {
  __name: "s-menu-grid",
  props: {
    // 装修数据
    data: {
      type: Object,
      default: () => ({})
    },
    // 装修样式
    styles: {
      type: Object,
      default: () => ({})
    }
  },
  setup(__props) {
    const props = __props;
    const bgStyle = common_vendor.computed(() => {
      const {
        bgType,
        bgImg,
        bgColor
      } = props.styles;
      return {
        background: bgType === "img" ? `url(${bgImg}) no-repeat top center / 100% 100%` : bgColor
      };
    });
    return (_ctx, _cache) => {
      return {
        a: common_vendor.f(__props.data.list, (item, index, i0) => {
          return common_vendor.e({
            a: item.badge.show
          }, item.badge.show ? {
            b: common_vendor.t(item.badge.text),
            c: common_vendor.s({
              background: item.badge.bgColor,
              color: item.badge.textColor
            })
          } : {}, {
            d: common_vendor.unref(sheep_index.sheep).$url.cdn(item.iconUrl),
            e: common_vendor.t(item.title),
            f: common_vendor.s({
              color: item.titleColor
            }),
            g: common_vendor.t(item.subtitle),
            h: common_vendor.s({
              color: item.subtitleColor
            }),
            i: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go(item.url), index),
            j: index,
            k: "1aac7574-1-" + i0 + ",1aac7574-0"
          });
        }),
        b: common_vendor.p({
          showBorder: Boolean(__props.data.border),
          column: __props.data.column
        }),
        c: common_vendor.s(bgStyle.value),
        d: common_vendor.s({
          marginLeft: `${__props.data.space}px`
        })
      };
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-1aac7574"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/components/s-menu-grid/s-menu-grid.js.map
