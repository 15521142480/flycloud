"use strict";
const sheep_store_app = require("./app.js");
const sheep_store_cart = require("./cart.js");
const sheep_store_modal = require("./modal.js");
const sheep_store_sys = require("./sys.js");
const sheep_store_user = require("./user.js");
const common_vendor = require("../../common/vendor.js");
const files = /* @__PURE__ */ Object.assign({ "./app.js": sheep_store_app.__vite_glob_0_0, "./cart.js": sheep_store_cart.__vite_glob_0_1, "./modal.js": sheep_store_modal.__vite_glob_0_2, "./sys.js": sheep_store_sys.__vite_glob_0_3, "./user.js": sheep_store_user.__vite_glob_0_4 });
const modules = {};
Object.keys(files).forEach((key) => {
  modules[key.replace(/(.*\/)*([^.]+).*/gi, "$2")] = files[key].default;
});
const setupPinia = (app) => {
  const pinia = common_vendor.createPinia();
  pinia.use(common_vendor.index$1);
  app.use(pinia);
};
const $store = (name) => {
  return modules[name]();
};
exports.$store = $store;
exports.setupPinia = setupPinia;
//# sourceMappingURL=../../../.sourcemap/mp-weixin/sheep/store/index.js.map
