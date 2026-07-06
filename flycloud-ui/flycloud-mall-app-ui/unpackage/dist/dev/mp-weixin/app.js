"use strict";
Object.defineProperty(exports, Symbol.toStringTag, { value: "Module" });
const common_vendor = require("./common/vendor.js");
const sheep_index = require("./sheep/index.js");
const sheep_store_index = require("./sheep/store/index.js");
if (!Math) {
  "./pages/index/index.js";
  "./pages/index/user.js";
  "./pages/index/category.js";
  "./pages/index/cart.js";
  "./pages/index/login.js";
  "./pages/index/search.js";
  "./pages/index/page.js";
  "./pages/goods/index.js";
  "./pages/goods/groupon.js";
  "./pages/goods/seckill.js";
  "./pages/goods/point.js";
  "./pages/goods/list.js";
  "./pages/goods/comment/add.js";
  "./pages/goods/comment/list.js";
  "./pages/order/detail.js";
  "./pages/order/confirm.js";
  "./pages/order/list.js";
  "./pages/order/aftersale/apply.js";
  "./pages/order/aftersale/return-delivery.js";
  "./pages/order/aftersale/list.js";
  "./pages/order/aftersale/detail.js";
  "./pages/order/aftersale/log.js";
  "./pages/order/express/log.js";
  "./pages/user/info.js";
  "./pages/user/goods-collect.js";
  "./pages/user/goods-log.js";
  "./pages/user/address/list.js";
  "./pages/user/address/edit.js";
  "./pages/user/goods_details_store/index.js";
  "./pages/user/wallet/money.js";
  "./pages/user/wallet/score.js";
  "./pages/commission/index.js";
  "./pages/commission/wallet.js";
  "./pages/commission/goods.js";
  "./pages/commission/order.js";
  "./pages/commission/team.js";
  "./pages/commission/promoter.js";
  "./pages/commission/commission-ranking.js";
  "./pages/commission/withdraw.js";
  "./pages/app/sign.js";
  "./pages/public/setting.js";
  "./pages/public/richtext.js";
  "./pages/public/faq.js";
  "./pages/public/error.js";
  "./pages/public/webview.js";
  "./pages/coupon/list.js";
  "./pages/coupon/detail.js";
  "./pages/chat/index.js";
  "./pages/pay/index.js";
  "./pages/pay/result.js";
  "./pages/pay/recharge.js";
  "./pages/pay/recharge-log.js";
  "./pages/activity/groupon/detail.js";
  "./pages/activity/groupon/order.js";
  "./pages/activity/index.js";
  "./pages/activity/groupon/list.js";
  "./pages/activity/seckill/list.js";
  "./pages/activity/point/list.js";
}
const _sfc_main = {
  __name: "App",
  setup(__props) {
    common_vendor.onLaunch(() => {
      common_vendor.index.hideTabBar({
        fail: () => {
        }
      });
      sheep_index.ShoproInit();
    });
    common_vendor.onShow(() => {
    });
    return () => {
    };
  }
};
function createApp() {
  const app = common_vendor.createSSRApp(_sfc_main);
  sheep_store_index.setupPinia(app);
  return {
    app
  };
}
createApp().app.mount("#app");
exports.createApp = createApp;
//# sourceMappingURL=../.sourcemap/mp-weixin/app.js.map
