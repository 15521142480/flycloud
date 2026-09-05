import { defineStore } from 'pinia';
import $share from '@/sheep/platform/share';
import { clone, cloneDeep } from 'lodash-es';
import cart from './cart';
import app from './app';
import { showAuthModal } from '@/sheep/hooks/useModal';
import UserApi from '@/sheep/api/member/user';
import PayWalletApi from '@/sheep/api/pay/wallet';
import OrderApi from '@/sheep/api/trade/order';
import CouponApi from '@/sheep/api/promotion/coupon';

// 默认用户信息
const defaultUserInfo = {
  avatar: '', // 头像
  nickname: '', // 昵称
  gender: 0, // 性别
  mobile: '', // 手机号
  point: 0, // 积分
};

// 默认钱包信息
const defaultUserWallet = {
  balance: 0, // 余额
};

// 默认订单、优惠券等其他资产信息
const defaultNumData = {
  unusedCouponCount: 0,
  orderCount: {
    allCount: 0,
    unpaidCount: 0,
    undeliveredCount: 0,
    deliveredCount: 0,
    uncommentedCount: 0,
    afterSaleCount: 0,
  },
};

const user = defineStore('user', {
  state: () => ({
    userInfo: clone(defaultUserInfo), // 用户信息
    userWallet: clone(defaultUserWallet), // 用户钱包信息
    isLogin: !!uni.getStorageSync('token'), // 登录状态
    numData: cloneDeep(defaultNumData), // 用户其他数据
    lastUpdateTime: 0, // 上次更新时间
  }),

  actions: {
    // 根据令牌同步登录状态，避免持久化状态与实际令牌不一致
    syncLoginState() {
      const hasToken = !!uni.getStorageSync('token');
      this.isLogin = hasToken;
      this.lastUpdateTime = 0;
      if (!hasToken) {
        this.userInfo = clone(defaultUserInfo);
        this.userWallet = clone(defaultUserWallet);
        this.numData = cloneDeep(defaultNumData);
      }
      return hasToken;
    },

    // 获取用户信息
    async getInfo() {
      const { code, data } = await UserApi.getUserInfo();
      if (code !== 0) {
        return;
      }
      this.userInfo = data;
      return Promise.resolve(data);
    },

    // 获得用户钱包
    async getWallet() {
      if (!this.isLogin || !uni.getStorageSync('token')) {
        this.userWallet = clone(defaultUserWallet);
        return;
      }
      const { code, data } = await PayWalletApi.getPayWallet();
      if (code !== 0) {
        return;
      }
      this.userWallet = data;
    },

    // 获取订单、优惠券等其他资产信息
    async getNumData() {
      const [orderResult, couponResult] = await Promise.all([
        OrderApi.getOrderCount(),
        CouponApi.getUnusedCouponCount(),
      ]);
      if (orderResult.code === 0) {
        this.numData.orderCount = orderResult.data;
      }
      if (couponResult.code === 0) {
        this.numData.unusedCouponCount = couponResult.data;
      }
    },

    // 设置 token
    setToken(token = '', refreshToken = '') {
      if (token === '') {
        this.isLogin = false;
        uni.removeStorageSync('token');
        uni.removeStorageSync('refresh-token');
      } else {
        this.isLogin = true;
        uni.setStorageSync('token', token);
        uni.setStorageSync('refresh-token', refreshToken);
        this.loginAfter();
      }
      return this.isLogin;
    },

    // 更新用户相关信息 (手动限流，5 秒之内不刷新)
    async updateUserData() {
      if (!this.isLogin || !uni.getStorageSync('token')) {
        this.resetUserData();
        return false;
      }
      // 防抖，5 秒之内不刷新
      const nowTime = new Date().getTime();
      if (this.lastUpdateTime + 5000 > nowTime) {
        return this.userInfo;
      }
      this.lastUpdateTime = nowTime;

      try {
        // 先确认当前令牌仍能取得用户信息，再请求钱包和用户资产接口
        const userInfo = await this.getInfo();
        if (!userInfo) {
          return false;
        }
        await Promise.all([this.getWallet(), this.getNumData()]);
        return userInfo;
      } catch {
        return false;
      }
    },

    // 重置用户默认数据
    resetUserData() {
      // 清空 token
      this.setToken();
      // 清空用户相关的缓存
      this.userInfo = clone(defaultUserInfo);
      this.userWallet = clone(defaultUserWallet);
      this.numData = cloneDeep(defaultNumData);
      this.lastUpdateTime = 0;
      // 清空购物车的缓存
      cart().emptyList();
    },

    // 登录后，加载各种信息
    async loginAfter() {
      const updated = await this.updateUserData();
      if (!updated) {
        return false;
      }

      // 加载购物车
      cart().getList();
      // 登录后设置全局分享参数
      $share.getShareInfo();

      // 提醒绑定手机号
      if (app().platform.bind_mobile && !this.userInfo.mobile) {
        showAuthModal('changeMobile');
      }

      // 绑定推广员
      $share.bindBrokerageUser();
      return true;
    },

    // 登出系统
    async logout(showLogin = false) {
      this.resetUserData();
      if (showLogin) {
        showAuthModal();
      }
      return !this.isLogin;
    },
  },
  persist: {
    enabled: true,
    strategies: [
      {
        key: 'user-store',
        paths: ['userInfo', 'userWallet', 'numData'],
      },
    ],
  },
});

export default user;
