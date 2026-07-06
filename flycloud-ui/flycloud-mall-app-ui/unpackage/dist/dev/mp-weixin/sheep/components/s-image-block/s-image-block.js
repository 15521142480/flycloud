"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../index.js");
if (!Array) {
  const _easycom_su_image2 = common_vendor.resolveComponent("su-image");
  _easycom_su_image2();
}
const _easycom_su_image = () => "../../ui/su-image/su-image.js";
if (!Math) {
  _easycom_su_image();
}
const _sfc_main = {
  __name: "s-image-block",
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
        a: common_vendor.p({
          src: common_vendor.unref(sheep_index.sheep).$url.cdn(__props.data.imgUrl),
          mode: "widthFix"
        }),
        b: common_vendor.o(($event) => {
          var _a;
          return common_vendor.unref(sheep_index.sheep).$router.go((_a = __props.data) == null ? void 0 : _a.url);
        }, "a3")
      };
    };
  }
};
wx.createComponent(_sfc_main);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/components/s-image-block/s-image-block.js.map
