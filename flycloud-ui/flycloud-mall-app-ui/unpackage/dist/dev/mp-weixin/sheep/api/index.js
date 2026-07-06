"use strict";
const sheep_api_auth_captcha = require("./auth/captcha.js");
const sheep_api_infra_file = require("./infra/file.js");
const sheep_api_infra_tenant = require("./infra/tenant.js");
const sheep_api_member_address = require("./member/address.js");
const sheep_api_member_auth = require("./member/auth.js");
const sheep_api_member_point = require("./member/point.js");
const sheep_api_member_signin = require("./member/signin.js");
const sheep_api_member_social = require("./member/social.js");
const sheep_api_member_user = require("./member/user.js");
const sheep_api_migration_app = require("./migration/app.js");
const sheep_api_migration_third = require("./migration/third.js");
const sheep_api_pay_channel = require("./pay/channel.js");
const sheep_api_pay_order = require("./pay/order.js");
const sheep_api_pay_transfer = require("./pay/transfer.js");
const sheep_api_pay_wallet = require("./pay/wallet.js");
const sheep_api_product_category = require("./product/category.js");
const sheep_api_product_comment = require("./product/comment.js");
const sheep_api_product_favorite = require("./product/favorite.js");
const sheep_api_product_history = require("./product/history.js");
const sheep_api_product_spu = require("./product/spu.js");
const sheep_api_promotion_activity = require("./promotion/activity.js");
const sheep_api_promotion_article = require("./promotion/article.js");
const sheep_api_promotion_combination = require("./promotion/combination.js");
const sheep_api_promotion_coupon = require("./promotion/coupon.js");
const sheep_api_promotion_diy = require("./promotion/diy.js");
const sheep_api_promotion_kefu = require("./promotion/kefu.js");
const sheep_api_promotion_point = require("./promotion/point.js");
const sheep_api_promotion_rewardActivity = require("./promotion/rewardActivity.js");
const sheep_api_promotion_seckill = require("./promotion/seckill.js");
const sheep_api_system_area = require("./system/area.js");
const sheep_api_system_dict = require("./system/dict.js");
const sheep_api_trade_afterSale = require("./trade/afterSale.js");
const sheep_api_trade_brokerage = require("./trade/brokerage.js");
const sheep_api_trade_cart = require("./trade/cart.js");
const sheep_api_trade_config = require("./trade/config.js");
const sheep_api_trade_delivery = require("./trade/delivery.js");
const sheep_api_trade_order = require("./trade/order.js");
const files = /* @__PURE__ */ Object.assign({ "./auth/captcha.js": sheep_api_auth_captcha.__vite_glob_0_0, "./infra/file.js": sheep_api_infra_file.__vite_glob_0_1, "./infra/tenant.js": sheep_api_infra_tenant.__vite_glob_0_2, "./member/address.js": sheep_api_member_address.__vite_glob_0_3, "./member/auth.js": sheep_api_member_auth.__vite_glob_0_4, "./member/point.js": sheep_api_member_point.__vite_glob_0_5, "./member/signin.js": sheep_api_member_signin.__vite_glob_0_6, "./member/social.js": sheep_api_member_social.__vite_glob_0_7, "./member/user.js": sheep_api_member_user.__vite_glob_0_8, "./migration/app.js": sheep_api_migration_app.__vite_glob_0_9, "./migration/third.js": sheep_api_migration_third.__vite_glob_0_10, "./pay/channel.js": sheep_api_pay_channel.__vite_glob_0_11, "./pay/order.js": sheep_api_pay_order.__vite_glob_0_12, "./pay/transfer.js": sheep_api_pay_transfer.__vite_glob_0_13, "./pay/wallet.js": sheep_api_pay_wallet.__vite_glob_0_14, "./product/category.js": sheep_api_product_category.__vite_glob_0_15, "./product/comment.js": sheep_api_product_comment.__vite_glob_0_16, "./product/favorite.js": sheep_api_product_favorite.__vite_glob_0_17, "./product/history.js": sheep_api_product_history.__vite_glob_0_18, "./product/spu.js": sheep_api_product_spu.__vite_glob_0_19, "./promotion/activity.js": sheep_api_promotion_activity.__vite_glob_0_20, "./promotion/article.js": sheep_api_promotion_article.__vite_glob_0_21, "./promotion/combination.js": sheep_api_promotion_combination.__vite_glob_0_22, "./promotion/coupon.js": sheep_api_promotion_coupon.__vite_glob_0_23, "./promotion/diy.js": sheep_api_promotion_diy.__vite_glob_0_24, "./promotion/kefu.js": sheep_api_promotion_kefu.__vite_glob_0_25, "./promotion/point.js": sheep_api_promotion_point.__vite_glob_0_26, "./promotion/rewardActivity.js": sheep_api_promotion_rewardActivity.__vite_glob_0_27, "./promotion/seckill.js": sheep_api_promotion_seckill.__vite_glob_0_28, "./system/area.js": sheep_api_system_area.__vite_glob_0_29, "./system/dict.js": sheep_api_system_dict.__vite_glob_0_30, "./trade/afterSale.js": sheep_api_trade_afterSale.__vite_glob_0_31, "./trade/brokerage.js": sheep_api_trade_brokerage.__vite_glob_0_32, "./trade/cart.js": sheep_api_trade_cart.__vite_glob_0_33, "./trade/config.js": sheep_api_trade_config.__vite_glob_0_34, "./trade/delivery.js": sheep_api_trade_delivery.__vite_glob_0_35, "./trade/order.js": sheep_api_trade_order.__vite_glob_0_36 });
let api = {};
Object.keys(files).forEach((key) => {
  api = {
    ...api,
    [key.replace(/(.*\/)*([^.]+).*/gi, "$2")]: files[key].default
  };
});
const $api = api;
exports.$api = $api;
//# sourceMappingURL=../../../.sourcemap/mp-weixin/sheep/api/index.js.map
