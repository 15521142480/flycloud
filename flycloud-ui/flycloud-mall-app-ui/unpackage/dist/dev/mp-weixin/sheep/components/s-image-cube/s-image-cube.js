"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../index.js");
const _sfc_main = {
  __name: "s-image-cube",
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
    const windowWidth = sheep_index.sheep.$platform.device.windowWidth;
    const cell = common_vendor.computed(() => {
      return (windowWidth - ((props.styles.marginLeft || 0) + (props.styles.marginRight || 0) + (props.styles.padding || 0) * 2)) / 4;
    });
    const parseAdWrap = common_vendor.computed(() => {
      var _a, _b, _c, _d;
      let heightArr = props.data.list.reduce(
        (prev, cur) => prev.includes(cur.height + cur.top) ? prev : [...prev, cur.height + cur.top],
        []
      );
      let heightMax = Math.max(...heightArr);
      return {
        height: heightMax * cell.value + "px",
        width: windowWidth - (((_b = (_a = props.data) == null ? void 0 : _a.style) == null ? void 0 : _b.marginLeft) + ((_d = (_c = props.data) == null ? void 0 : _c.style) == null ? void 0 : _d.marginRight) + props.styles.padding * 2) * 2 + "px"
      };
    });
    const parseImgStyle = (item) => {
      let obj = {
        width: item.width * cell.value - props.data.space + "px",
        height: item.height * cell.value - props.data.space + "px",
        left: item.left * cell.value + "px",
        top: item.top * cell.value + "px",
        "border-top-left-radius": props.data.borderRadiusTop + "px",
        "border-top-right-radius": props.data.borderRadiusTop + "px",
        "border-bottom-left-radius": props.data.borderRadiusBottom + "px",
        "border-bottom-right-radius": props.data.borderRadiusBottom + "px"
      };
      return obj;
    };
    return (_ctx, _cache) => {
      return {
        a: common_vendor.f(__props.data.list, (item, index, i0) => {
          return {
            a: common_vendor.unref(sheep_index.sheep).$url.cdn(item.imgUrl),
            b: common_vendor.s(parseImgStyle(item)),
            c: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go(item.url), index),
            d: index
          };
        }),
        b: common_vendor.s({
          margin: __props.data.space + "px"
        }),
        c: common_vendor.s(parseAdWrap.value)
      };
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-c67133d8"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/components/s-image-cube/s-image-cube.js.map
