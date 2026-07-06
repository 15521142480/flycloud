"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_platform_share = require("../platform/share.js");
const sheep_store_cart = require("./cart.js");
const sheep_store_app = require("./app.js");
const sheep_hooks_useModal = require("../hooks/useModal.js");
const sheep_api_member_user = require("../api/member/user.js");
const sheep_api_pay_wallet = require("../api/pay/wallet.js");
const sheep_api_trade_order = require("../api/trade/order.js");
const sheep_api_promotion_coupon = require("../api/promotion/coupon.js");
const defaultUserInfo = {
  avatar: "",
  // 头像
  nickname: "",
  // 昵称
  gender: 0,
  // 性别
  mobile: "",
  // 手机号
  point: 0
  // 积分
};
const defaultUserWallet = {
  balance: 0
  // 余额
};
const defaultNumData = {
  unusedCouponCount: 0,
  orderCount: {
    allCount: 0,
    unpaidCount: 0,
    undeliveredCount: 0,
    deliveredCount: 0,
    uncommentedCount: 0,
    afterSaleCount: 0
  }
};
const user = common_vendor.defineStore("user", {
  state: () => ({
    userInfo: common_vendor.clone(defaultUserInfo),
    // 用户信息
    userWallet: common_vendor.clone(defaultUserWallet),
    // 用户钱包信息
    isLogin: !!common_vendor.index.getStorageSync("token"),
    // 登录状态
    numData: common_vendor.cloneDeep(defaultNumData),
    // 用户其他数据
    lastUpdateTime: 0
    // 上次更新时间
  }),
  actions: {
    // 获取用户信息
    async getInfo() {
      const { code, data } = await sheep_api_member_user.UserApi.getUserInfo();
      if (code !== 0) {
        return;
      }
      this.userInfo = data;
      return Promise.resolve(data);
    },
    // 获得用户钱包
    async getWallet() {
      const { code, data } = await sheep_api_pay_wallet.PayWalletApi.getPayWallet();
      if (code !== 0) {
        return;
      }
      this.userWallet = data;
    },
    // 获取订单、优惠券等其他资产信息
    getNumData() {
      sheep_api_trade_order.OrderApi.getOrderCount().then((res) => {
        if (res.code === 0) {
          this.numData.orderCount = res.data;
        }
      });
      sheep_api_promotion_coupon.CouponApi.getUnusedCouponCount().then((res) => {
        if (res.code === 0) {
          this.numData.unusedCouponCount = res.data;
        }
      });
    },
    // 设置 token
    setToken(token = "", refreshToken = "") {
      if (token === "") {
        this.isLogin = false;
        common_vendor.index.removeStorageSync("token");
        common_vendor.index.removeStorageSync("refresh-token");
      } else {
        this.isLogin = true;
        common_vendor.index.setStorageSync("token", token);
        common_vendor.index.setStorageSync("refresh-token", refreshToken);
        this.loginAfter();
      }
      return this.isLogin;
    },
    // 更新用户相关信息 (手动限流，5 秒之内不刷新)
    async updateUserData() {
      if (!this.isLogin) {
        this.resetUserData();
        return;
      }
      const nowTime = (/* @__PURE__ */ new Date()).getTime();
      if (this.lastUpdateTime + 5e3 > nowTime) {
        return;
      }
      this.lastUpdateTime = nowTime;
      await this.getInfo();
      this.getWallet();
      this.getNumData();
      return this.userInfo;
    },
    // 重置用户默认数据
    resetUserData() {
      this.setToken();
      this.userInfo = common_vendor.clone(defaultUserInfo);
      this.userWallet = common_vendor.clone(defaultUserWallet);
      this.numData = common_vendor.cloneDeep(defaultNumData);
      sheep_store_cart.cart().emptyList();
    },
    // 登录后，加载各种信息
    async loginAfter() {
      await this.updateUserData();
      sheep_store_cart.cart().getList();
      sheep_platform_share.$share.getShareInfo();
      if (sheep_store_app.app().platform.bind_mobile && !this.userInfo.mobile) {
        sheep_hooks_useModal.showAuthModal("changeMobile");
      }
      sheep_platform_share.$share.bindBrokerageUser();
    },
    // 登出系统
    async logout(showLogin = false) {
      this.resetUserData();
      if (showLogin) {
        sheep_hooks_useModal.showAuthModal();
      }
      return !this.isLogin;
    }
  },
  persist: {
    enabled: true,
    strategies: [
      {
        key: "user-store"
      }
    ]
  }
});
const __vite_glob_0_4 = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: user
}, Symbol.toStringTag, { value: "Module" }));
exports.__vite_glob_0_4 = __vite_glob_0_4;
exports.user = user;
//# sourceMappingURL=../../../.sourcemap/mp-weixin/sheep/store/user.js.map
