"use strict";
const sheep_platform_provider_wechat_miniProgram = require("./miniProgram.js");
let wechat = {};
if (typeof sheep_platform_provider_wechat_miniProgram.service !== "undefined") {
  wechat = sheep_platform_provider_wechat_miniProgram.service;
}
const wechat$1 = wechat;
exports.wechat = wechat$1;
//# sourceMappingURL=../../../../../.sourcemap/mp-weixin/sheep/platform/provider/wechat/index.js.map
