"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../index.js");
const _sfc_main = {
  __name: "s-hotzone-block",
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
    return (_ctx, _cache) => {
      return {
        a: common_vendor.unref(sheep_index.sheep).$url.cdn(__props.data.imgUrl),
        b: common_vendor.f(__props.data.list, (item, index, i0) => {
          return {
            a: index,
            b: common_vendor.s({
              top: `${item.top}px`,
              left: `${item.left}px`,
              width: `${item.width}px`,
              height: `${item.height}px`
            }),
            c: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go(item.url), index)
          };
        })
      };
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-3db3354a"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/components/s-hotzone-block/s-hotzone-block.js.map
