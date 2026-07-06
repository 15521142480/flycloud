"use strict";
const sheep_config_index = require("./index.js");
const SERVER_PREFIX = {
  mall: "/flycloud-mall",
  auth: "/flycloud-auth",
  system: "/flycloud-system"
};
function getServerBaseUrl(server = "mall") {
  const prefix = SERVER_PREFIX[server];
  if (!prefix) {
    throw new Error(`未知的服务类型：${server}`);
  }
  return `${sheep_config_index.baseUrl.replace(/\/$/, "")}${prefix}`;
}
function getMallBaseUrl() {
  return getServerBaseUrl("mall");
}
function getAuthBaseUrl() {
  return getServerBaseUrl("auth");
}
function getSystemBaseUrl() {
  return getServerBaseUrl("system");
}
exports.getAuthBaseUrl = getAuthBaseUrl;
exports.getMallBaseUrl = getMallBaseUrl;
exports.getSystemBaseUrl = getSystemBaseUrl;
//# sourceMappingURL=../../../.sourcemap/mp-weixin/sheep/config/server.js.map
