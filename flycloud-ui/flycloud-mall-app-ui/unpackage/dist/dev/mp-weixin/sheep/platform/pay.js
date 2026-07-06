"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_index = require("../index.js");
require("../helper/index.js");
const sheep_api_pay_order = require("../api/pay/order.js");
class SheepPay {
  constructor(payment, orderType, id) {
    this.payment = payment;
    this.id = id;
    this.orderType = orderType;
    this.payAction();
  }
  payAction() {
    const payAction = {
      WechatOfficialAccount: {
        wechat: () => {
          this.wechatOfficialAccountPay();
        },
        alipay: () => {
          this.redirectPay();
        },
        wallet: () => {
          this.walletPay();
        },
        mock: () => {
          this.mockPay();
        }
      },
      WechatMiniProgram: {
        wechat: () => {
          this.wechatMiniProgramPay();
        },
        alipay: () => {
          this.copyPayLink();
        },
        wallet: () => {
          this.walletPay();
        },
        mock: () => {
          this.mockPay();
        }
      },
      App: {
        wechat: () => {
          this.wechatAppPay();
        },
        alipay: () => {
          this.alipay();
        },
        wallet: () => {
          this.walletPay();
        },
        mock: () => {
          this.mockPay();
        }
      },
      H5: {
        wechat: () => {
          this.wechatWapPay();
        },
        alipay: () => {
          this.redirectPay();
        },
        wallet: () => {
          this.walletPay();
        },
        mock: () => {
          this.mockPay();
        }
      }
    };
    return payAction[sheep_index.sheep.$platform.name][this.payment]();
  }
  // 预支付
  prepay(channel) {
    return new Promise(async (resolve, reject) => {
      let data = {
        id: this.id,
        channelCode: channel,
        channelExtras: {}
      };
      if (["wx_pub", "wx_lite"].includes(channel)) {
        const openid = await sheep_index.sheep.$platform.useProvider("wechat").getOpenid(true);
        if (!openid) {
          this.bindWeixin();
          return;
        }
        data.channelExtras.openid = openid;
      }
      sheep_api_pay_order.PayOrderApi.submitOrder(data).then((res) => {
        res.code === 0 && resolve(res);
        if (res.code !== 0 && res.msg.indexOf("无效的openid") >= 0) {
          if (res.msg.indexOf("无效的openid") >= 0 || // 获取的 openid 不正确时，或者随便输入了个 openid
          res.msg.indexOf("下单账号与支付账号不一致") >= 0) {
            this.bindWeixin();
          }
        }
      });
    });
  }
  // 微信小程序支付
  async wechatMiniProgramPay() {
    let { code, data } = await this.prepay("wx_lite");
    if (code !== 0) {
      return;
    }
    const payConfig = JSON.parse(data.displayContent);
    common_vendor.index.requestPayment({
      provider: "wxpay",
      timeStamp: payConfig.timeStamp,
      nonceStr: payConfig.nonceStr,
      package: payConfig.packageValue,
      signType: payConfig.signType,
      paySign: payConfig.paySign,
      success: (res) => {
        this.payResult("success");
      },
      fail: (err) => {
        if (err.errMsg === "requestPayment:fail cancel") {
          sheep_index.sheep.$helper.toast("支付已手动取消");
        } else {
          this.payResult("fail");
        }
      }
    });
  }
  // 余额支付
  async walletPay() {
    const { code } = await this.prepay("wallet");
    code === 0 && this.payResult("success");
  }
  // 模拟支付
  async mockPay() {
    const { code } = await this.prepay("mock");
    code === 0 && this.payResult("success");
  }
  // 支付宝复制链接支付（通过支付宝 wap 支付实现）
  async copyPayLink() {
    let { code, data } = await this.prepay("alipay_wap");
    if (code !== 0) {
      return;
    }
    common_vendor.index.showModal({
      title: "支付宝支付",
      content: "复制链接到外部浏览器",
      confirmText: "复制链接",
      success: (res) => {
        if (res.confirm) {
          sheep_index.sheep.$helper.copyText(data.displayContent);
        }
      }
    });
  }
  // 支付宝支付（App）
  async alipay() {
    let that = this;
    const { code, data } = await this.prepay("alipay_app");
    if (code !== 0) {
      return;
    }
    common_vendor.index.requestPayment({
      provider: "alipay",
      orderInfo: data.displayContent,
      // 直接使用返回的支付参数
      success: (res) => {
        that.payResult("success");
      },
      fail: (err) => {
        if (err.errMsg === "requestPayment:fail [paymentAlipay:62001]user cancel") {
          sheep_index.sheep.$helper.toast("支付已手动取消");
        } else {
          that.payResult("fail");
        }
      }
    });
  }
  // 微信支付（App）
  async wechatAppPay() {
    let that = this;
    let { code, data } = await this.prepay("wx_app");
    if (code !== 0) {
      sheep_index.sheep.$helper.toast("获取支付信息失败");
      return;
    }
    let payConfig = JSON.parse(data.displayContent);
    if (typeof payConfig.appId === "undefined") {
      payConfig.appId = payConfig.appid;
    }
    if (typeof payConfig.nonceStr === "undefined") {
      payConfig.nonceStr = payConfig.noncestr;
    }
    if (typeof payConfig.timeStamp === "undefined") {
      payConfig.timeStamp = payConfig.timestamp;
    }
    common_vendor.index.requestPayment({
      provider: "wxpay",
      timeStamp: payConfig.timeStamp,
      nonceStr: payConfig.nonceStr,
      package: payConfig.packageValue,
      signType: payConfig.signType,
      paySign: payConfig.paySign,
      success: (res) => {
        that.payResult("success");
      },
      fail: (err) => {
        if (err.errMsg === "requestPayment:fail cancel") {
          sheep_index.sheep.$helper.toast("支付已手动取消");
        } else {
          sheep_index.sheep.$helper.toast("支付失败：" + err.errMsg);
          that.payResult("fail");
        }
      }
    });
  }
  // 支付结果跳转,success:成功，fail:失败
  payResult(resultType) {
    goPayResult(this.id, this.orderType, resultType);
  }
  // 引导绑定微信
  bindWeixin() {
    goBindWeixin();
  }
}
function getPayMethods(channels) {
  const payMethods = [
    {
      icon: "/static/img/shop/pay/wechat.png",
      title: "微信支付",
      value: "wechat",
      disabled: true
    },
    {
      icon: "/static/img/shop/pay/alipay.png",
      title: "支付宝支付",
      value: "alipay",
      disabled: true
    },
    {
      icon: "/static/img/shop/pay/wallet.png",
      title: "余额支付",
      value: "wallet",
      disabled: true
    },
    {
      icon: "/static/img/shop/pay/apple.png",
      title: "Apple Pay",
      value: "apple",
      disabled: true
    },
    {
      icon: "/static/img/shop/pay/wallet.png",
      title: "模拟支付",
      value: "mock",
      disabled: true
    }
  ];
  const platform = sheep_index.sheep.$platform.name;
  const wechatMethod = payMethods[0];
  if (platform === "WechatOfficialAccount" && channels.includes("wx_pub") || platform === "WechatMiniProgram" && channels.includes("wx_lite") || platform === "App" && channels.includes("wx_app")) {
    wechatMethod.disabled = false;
  }
  const alipayMethod = payMethods[1];
  if (platform === "H5" && channels.includes("alipay_wap") || platform === "WechatOfficialAccount" && channels.includes("alipay_wap") || platform === "WechatMiniProgram" && channels.includes("alipay_wap") || platform === "App" && channels.includes("alipay_app")) {
    alipayMethod.disabled = false;
  }
  const walletMethod = payMethods[2];
  if (channels.includes("wallet")) {
    walletMethod.disabled = false;
  }
  const mockMethod = payMethods[4];
  if (channels.includes("mock")) {
    mockMethod.disabled = false;
  }
  return payMethods;
}
function goPayResult(id, orderType, resultType) {
  sheep_index.sheep.$router.redirect("/pages/pay/result", {
    id,
    orderType,
    payState: resultType
  });
}
function goBindWeixin() {
  common_vendor.index.showModal({
    title: "微信支付",
    content: "请先绑定微信再使用微信支付",
    success: function(res) {
      if (res.confirm) {
        sheep_index.sheep.$platform.useProvider("wechat").bind();
      }
    }
  });
}
function getWeixinPayChannelCode() {
  const platform = sheep_index.sheep.$platform.name;
  switch (platform) {
    case "WechatOfficialAccount":
      return "wx_pub";
    case "WechatMiniProgram":
      return "wx_lite";
    case "App":
      return "wx_app";
    case "H5":
      return "wx_wap";
    default:
      return "";
  }
}
exports.SheepPay = SheepPay;
exports.getPayMethods = getPayMethods;
exports.getWeixinPayChannelCode = getWeixinPayChannelCode;
exports.goBindWeixin = goBindWeixin;
exports.goPayResult = goPayResult;
//# sourceMappingURL=../../../.sourcemap/mp-weixin/sheep/platform/pay.js.map
