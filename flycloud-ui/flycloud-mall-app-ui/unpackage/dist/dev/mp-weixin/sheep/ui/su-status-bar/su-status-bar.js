"use strict";
const sheep_index = require("../../index.js");
const _sfc_main = {
  __name: "su-status-bar",
  setup(__props) {
    const statusBarHeight = sheep_index.sheep.$platform.device.statusBarHeight + "px";
    return (_ctx, _cache) => {
      return {
        a: statusBarHeight
      };
    };
  }
};
wx.createComponent(_sfc_main);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/ui/su-status-bar/su-status-bar.js.map
