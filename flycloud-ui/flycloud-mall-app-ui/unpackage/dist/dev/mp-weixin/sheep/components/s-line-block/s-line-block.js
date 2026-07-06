"use strict";
const common_vendor = require("../../../common/vendor.js");
if (!Array) {
  const _easycom_su_subline2 = common_vendor.resolveComponent("su-subline");
  _easycom_su_subline2();
}
const _easycom_su_subline = () => "../../ui/su-subline/su-subline.js";
if (!Math) {
  _easycom_su_subline();
}
const _sfc_main = {
  __name: "s-line-block",
  props: {
    data: {
      type: Object,
      default: {}
    }
  },
  setup(__props) {
    return (_ctx, _cache) => {
      return {
        a: common_vendor.p({
          ...__props.data
        })
      };
    };
  }
};
wx.createComponent(_sfc_main);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/components/s-line-block/s-line-block.js.map
