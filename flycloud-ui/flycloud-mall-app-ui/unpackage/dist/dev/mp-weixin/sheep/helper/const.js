"use strict";
const common_vendor = require("../../common/vendor.js");
const TerminalEnum = {
  UNKNOWN: 0,
  // 未知, 目的：在无法解析到 terminal 时，使用它
  WECHAT_MINI_PROGRAM: 10,
  //微信小程序
  WECHAT_WAP: 11,
  // 微信公众号
  H5: 20,
  // H5 网页
  APP: 31
  // 手机 App
};
const getTerminal = () => {
  const platformType = common_vendor.index.getAppBaseInfo().uniPlatform;
  switch (platformType) {
    case "app":
      return TerminalEnum.APP;
    case "web":
      return TerminalEnum.H5;
    case "mp-weixin":
      return TerminalEnum.WECHAT_MINI_PROGRAM;
    default:
      return TerminalEnum.UNKNOWN;
  }
};
const PromotionDiscountTypeEnum = {
  PRICE: {
    type: 1,
    name: "满减"
  },
  PERCENT: {
    type: 2,
    name: "折扣"
  }
};
const CouponTemplateValidityTypeEnum = {
  DATE: {
    type: 1,
    name: "固定日期可用"
  },
  TERM: {
    type: 2,
    name: "领取之后可用"
  }
};
const TimeStatusEnum = {
  WAIT_START: "即将开始",
  STARTED: "进行中",
  END: "已结束"
};
const WxaSubscribeTemplate = {
  TRADE_ORDER_DELIVERY: "订单发货通知",
  PROMOTION_COMBINATION_SUCCESS: "拼团结果通知",
  PAY_WALLET_RECHARGER_SUCCESS: "充值成功通知"
};
const PromotionActivityTypeEnum = {
  NORMAL: {
    type: 0,
    name: "普通"
  },
  SECKILL: {
    type: 1,
    name: "秒杀"
  },
  BARGAIN: {
    type: 2,
    name: "砍价"
  },
  COMBINATION: {
    type: 3,
    name: "拼团"
  },
  POINT: {
    type: 4,
    name: "积分商城"
  }
};
const DeliveryTypeEnum = {
  EXPRESS: { type: 1, name: "快递发货" },
  PICK_UP: { type: 2, name: "用户自提" }
};
const getTimeStatusEnum = (startTime, endTime) => {
  const now = common_vendor.dayjs();
  if (now.isBefore(startTime)) {
    return TimeStatusEnum.WAIT_START;
  } else if (now.isAfter(endTime)) {
    return TimeStatusEnum.END;
  } else {
    return TimeStatusEnum.STARTED;
  }
};
const SharePageEnum = {
  HOME: {
    name: "首页",
    page: "/pages/index/index",
    value: "1"
  },
  GOODS: {
    name: "普通商品页",
    page: "/pages/goods/index",
    value: "2"
  },
  GROUPON: {
    name: "拼团商品页",
    page: "/pages/goods/groupon",
    value: "3"
  },
  SECKILL: {
    name: "秒杀商品页",
    page: "/pages/goods/seckill",
    value: "4"
  },
  GROUPON_DETAIL: {
    name: "参与拼团页",
    page: "/pages/activity/groupon/detail",
    value: "5"
  },
  POINT: {
    name: "积分商品页",
    page: "/pages/goods/point",
    value: "6"
  }
};
exports.CouponTemplateValidityTypeEnum = CouponTemplateValidityTypeEnum;
exports.DeliveryTypeEnum = DeliveryTypeEnum;
exports.PromotionActivityTypeEnum = PromotionActivityTypeEnum;
exports.PromotionDiscountTypeEnum = PromotionDiscountTypeEnum;
exports.SharePageEnum = SharePageEnum;
exports.TimeStatusEnum = TimeStatusEnum;
exports.WxaSubscribeTemplate = WxaSubscribeTemplate;
exports.getTerminal = getTerminal;
exports.getTimeStatusEnum = getTimeStatusEnum;
//# sourceMappingURL=../../../.sourcemap/mp-weixin/sheep/helper/const.js.map
