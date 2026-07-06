"use strict";
const sheep_request_index = require("../../request/index.js");
function getTenantByWebsite(website) {
  return sheep_request_index.request({
    url: "/system/tenant/get-by-website",
    method: "GET",
    params: { website },
    custom: {
      isToken: false
      // 避免登录情况下，跨租户访问被拦截
    }
  });
}
const __vite_glob_0_2 = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  getTenantByWebsite
}, Symbol.toStringTag, { value: "Module" }));
exports.__vite_glob_0_2 = __vite_glob_0_2;
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/api/infra/tenant.js.map
