"use strict";
const sheep_request_index = require("../../request/index.js");
const app = {
  //小程序直播
  mplive: {
    getRoomList: (ids) => sheep_request_index.request({
      url: "app/mplive/getRoomList",
      method: "GET",
      params: {
        ids: ids.join(",")
      }
    }),
    getMpLink: () => sheep_request_index.request({
      url: "app/mplive/getMpLink",
      method: "GET"
    })
  }
};
const __vite_glob_0_9 = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: app
}, Symbol.toStringTag, { value: "Module" }));
exports.__vite_glob_0_9 = __vite_glob_0_9;
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/api/migration/app.js.map
