"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../index.js");
const _sfc_main = {
  __name: "s-block",
  props: {
    styles: {
      type: Object,
      default() {
      }
    }
  },
  setup(__props) {
    const props = __props;
    const elBackground = common_vendor.computed(() => {
      if (props.styles) {
        if (props.styles.bgType === "color") {
          return { background: props.styles.bgColor };
        }
        if (props.styles.bgType === "img") {
          return {
            background: `url(${sheep_index.sheep.$url.cdn(props.styles.bgImg)}) no-repeat top center / 100% auto`
          };
        }
      }
    });
    const elStyles = common_vendor.computed(() => {
      if (props.styles) {
        return {
          marginTop: `${props.styles.marginTop || 0}px`,
          marginBottom: `${props.styles.marginBottom || 0}px`,
          marginLeft: `${props.styles.marginLeft || 0}px`,
          marginRight: `${props.styles.marginRight || 0}px`,
          paddingTop: `${props.styles.paddingTop || 0}px`,
          paddingRight: `${props.styles.paddingRight || 0}px`,
          paddingBottom: `${props.styles.paddingBottom || 0}px`,
          paddingLeft: `${props.styles.paddingLeft || 0}px`,
          borderTopLeftRadius: `${props.styles.borderTopLeftRadius || 0}px`,
          borderTopRightRadius: `${props.styles.borderTopRightRadius || 0}px`,
          borderBottomRightRadius: `${props.styles.borderBottomRightRadius || 0}px`,
          borderBottomLeftRadius: `${props.styles.borderBottomLeftRadius || 0}px`,
          overflow: "hidden"
        };
      }
    });
    return (_ctx, _cache) => {
      return {
        a: common_vendor.s(elStyles.value),
        b: common_vendor.s(elBackground.value)
      };
    };
  }
};
wx.createComponent(_sfc_main);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/components/s-block/s-block.js.map
