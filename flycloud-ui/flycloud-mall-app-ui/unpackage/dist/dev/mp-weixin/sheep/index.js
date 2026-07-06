"use strict";
const sheep_api_index = require("./api/index.js");
const sheep_url_index = require("./url/index.js");
const sheep_router_index = require("./router/index.js");
const sheep_platform_index = require("./platform/index.js");
const sheep_helper_index = require("./helper/index.js");
const sheep_config_zIndex = require("./config/zIndex.js");
const sheep_store_index = require("./store/index.js");
const common_vendor = require("../common/vendor.js");
common_vendor.dayjs.locale("zh-cn");
common_vendor.dayjs.extend(common_vendor.relativeTime);
common_vendor.dayjs.extend(common_vendor.duration);
const sheep = {
  $api: sheep_api_index.$api,
  $store: sheep_store_index.$store,
  $url: sheep_url_index.$url,
  $router: sheep_router_index.$router,
  $platform: sheep_platform_index._platform,
  $helper: sheep_helper_index.$helper,
  $zIndex: sheep_config_zIndex.zIndex
};
async function ShoproInit() {
  await sheep_store_index.$store("app").init();
  sheep_platform_index._platform.load();
}
const sheep$1 = sheep;
exports.ShoproInit = ShoproInit;
exports.sheep = sheep$1;
//# sourceMappingURL=../../.sourcemap/mp-weixin/sheep/index.js.map
