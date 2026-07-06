"use strict";
const common_vendor = require("../../common/vendor.js");
const _sfc_main = {
  __name: "webview",
  setup(__props) {
    const url = common_vendor.ref("");
    common_vendor.onLoad((options) => {
      url.value = decodeURIComponent(options.url);
    });
    return (_ctx, _cache) => {
      return {
        a: url.value
      };
    };
  }
};
wx.createPage(_sfc_main);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/public/webview.js.map
