"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../index.js");
const _sfc_main = {
  __name: "s-title-block",
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
    const state = common_vendor.reactive({
      typeMap: {
        left: "ss-row-left",
        center: "ss-row-center"
      }
    });
    const props = __props;
    const elStyles = {
      background: `url(${sheep_index.sheep.$url.cdn(props.data.bgImgUrl)}) no-repeat top center / 100% auto`,
      fontSize: `${props.data.titleSize}px`,
      fontWeight: `${props.data.titleWeight}`,
      // add by fly：shopro 是在 props.styles.height，我们是在 props.data.height
      height: `${props.data.height || 40}px`
    };
    const titleStyles = {
      color: props.data.titleColor,
      fontSize: `${props.data.titleSize}px`,
      textAlign: props.data.textAlign,
      // add by fly：shopro 是在 props.data.skew，我们是在 props.data.marginLeft
      marginLeft: `${props.data.marginLeft || 0}px`
    };
    const descStyles = {
      color: props.data.descriptionColor,
      textAlign: props.data.textAlign,
      fontSize: `${props.data.descriptionSize}px`,
      fontWeight: `${props.data.descriptionWeight}px`,
      marginLeft: `${props.data.marginLeft || 0}px`
    };
    return (_ctx, _cache) => {
      var _a, _b;
      return common_vendor.e({
        a: __props.data.title
      }, __props.data.title ? {
        b: common_vendor.t(__props.data.title),
        c: common_vendor.s(titleStyles)
      } : {}, {
        d: __props.data.description
      }, __props.data.description ? {
        e: common_vendor.t(__props.data.description),
        f: common_vendor.s(descStyles)
      } : {}, {
        g: (_a = __props.data.more) == null ? void 0 : _a.show
      }, ((_b = __props.data.more) == null ? void 0 : _b.show) ? common_vendor.e({
        h: __props.data.more.type !== "icon"
      }, __props.data.more.type !== "icon" ? {
        i: common_vendor.t(__props.data.more.text)
      } : {}, {
        j: __props.data.more.type !== "text"
      }, __props.data.more.type !== "text" ? {} : {}, {
        k: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go(__props.data.more.url), "3b"),
        l: __props.data.descriptionColor
      }) : {}, {
        m: common_vendor.n(state.typeMap[__props.data.textAlign]),
        n: common_vendor.s(elStyles)
      });
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-2879f4b7"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/components/s-title-block/s-title-block.js.map
