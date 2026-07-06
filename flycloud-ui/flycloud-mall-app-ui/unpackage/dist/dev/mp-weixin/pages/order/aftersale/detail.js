"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../../sheep/index.js");
const sheep_hooks_useGoods = require("../../../sheep/hooks/useGoods.js");
const sheep_api_trade_afterSale = require("../../../sheep/api/trade/afterSale.js");
if (!Array) {
  const _easycom_s_goods_item2 = common_vendor.resolveComponent("s-goods-item");
  const _easycom_s_empty2 = common_vendor.resolveComponent("s-empty");
  const _easycom_su_fixed2 = common_vendor.resolveComponent("su-fixed");
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  (_easycom_s_goods_item2 + _easycom_s_empty2 + _easycom_su_fixed2 + _easycom_s_layout2)();
}
const _easycom_s_goods_item = () => "../../../sheep/components/s-goods-item/s-goods-item.js";
const _easycom_s_empty = () => "../../../sheep/components/s-empty/s-empty.js";
const _easycom_su_fixed = () => "../../../sheep/ui/su-fixed/su-fixed.js";
const _easycom_s_layout = () => "../../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  (_easycom_s_goods_item + _easycom_s_empty + _easycom_su_fixed + _easycom_s_layout)();
}
const _sfc_main = {
  __name: "detail",
  setup(__props) {
    common_vendor.useCssVars((_ctx) => ({
      "4745ac6a": common_vendor.unref(headerBg)
    }));
    const statusBarHeight = sheep_index.sheep.$platform.device.statusBarHeight * 2;
    const headerBg = sheep_index.sheep.$url.css("/static/img/shop/order/order_bg.png");
    const state = common_vendor.reactive({
      id: 0,
      // 售后编号
      info: {},
      // 收货信息
      loading: false,
      active: 0,
      // 在 list 的激活位置
      list: [
        {
          title: "提交申请"
        },
        {
          title: "处理中"
        },
        {
          title: "完成"
        }
      ]
      // 时间轴
    });
    function onApply(id) {
      common_vendor.index.showModal({
        title: "提示",
        content: "确定要取消此申请吗？",
        success: async function(res) {
          if (!res.confirm) {
            return;
          }
          const { code } = await sheep_api_trade_afterSale.AfterSaleApi.cancelAfterSale(id);
          if (code === 0) {
            await getDetail(id);
          }
        }
      });
    }
    const onCopy = () => {
      sheep_index.sheep.$helper.copyText(state.info.no);
    };
    async function getDetail(id) {
      state.loading = true;
      const { code, data } = await sheep_api_trade_afterSale.AfterSaleApi.getAfterSale(id);
      if (code !== 0) {
        state.info = null;
        return;
      }
      state.info = data;
      sheep_hooks_useGoods.handleAfterSaleButtons(state.info);
      if ([10].includes(state.info.status)) {
        state.active = 0;
      } else if ([20, 30].includes(state.info.status)) {
        state.active = 1;
      } else if ([40, 50].includes(state.info.status)) {
        state.active = 2;
      } else if ([61, 62, 63].includes(state.info.status)) {
        state.active = 2;
      }
    }
    common_vendor.onLoad((options) => {
      if (!options.id) {
        sheep_index.sheep.$helper.toast(`缺少订单信息，请检查`);
        return;
      }
      state.id = options.id;
      getDetail(options.id);
    });
    return (_ctx, _cache) => {
      var _a, _b, _c, _d;
      return common_vendor.e({
        a: !common_vendor.unref(common_vendor.isEmpty)(state.info) && state.loading
      }, !common_vendor.unref(common_vendor.isEmpty)(state.info) && state.loading ? {
        b: common_vendor.f(state.list, (item, index, i0) => {
          return common_vendor.e({
            a: state.list.length - 1 === index && [61, 62, 63].includes(state.info.status)
          }, state.list.length - 1 === index && [61, 62, 63].includes(state.info.status) ? {} : {
            b: common_vendor.n(state.active >= index ? "activity-color" : "info-color")
          }, {
            c: state.list.length - 1 !== index
          }, state.list.length - 1 !== index ? {
            d: common_vendor.n(state.active >= index ? "activity-bg" : "info-bg")
          } : {}, {
            e: common_vendor.t(item.title),
            f: common_vendor.n(state.active >= index ? "activity-color" : "info-color"),
            g: index
          });
        }),
        c: common_vendor.s({
          marginTop: "-" + Number(statusBarHeight + 88) + "rpx",
          paddingTop: Number(statusBarHeight + 88) + "rpx"
        }),
        d: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.formatAfterSaleStatusDescription)(state.info)),
        e: common_vendor.t(common_vendor.unref(sheep_index.sheep).$helper.timeFormat(state.info.updateTime, "yyyy-mm-dd hh:MM:ss")),
        f: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/order/aftersale/log", {
          id: state.id
        }), "c8"),
        g: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(state.info.refundPrice)),
        h: common_vendor.p({
          img: state.info.picUrl,
          title: state.info.spuName,
          titleWidth: 480,
          skuText: state.info.properties.map((property) => property.valueName).join(" "),
          num: state.info.count
        }),
        i: common_vendor.t(state.info.no),
        j: common_vendor.o(onCopy, "1c"),
        k: common_vendor.t(common_vendor.unref(sheep_index.sheep).$helper.timeFormat(state.info.createTime, "yyyy-mm-dd hh:MM:ss")),
        l: common_vendor.t(state.info.way === 10 ? "仅退款" : "退款退货"),
        m: common_vendor.t(state.info.applyReason),
        n: common_vendor.t(state.info.applyDescription)
      } : {}, {
        o: common_vendor.unref(common_vendor.isEmpty)(state.info) && state.loading
      }, common_vendor.unref(common_vendor.isEmpty)(state.info) && state.loading ? {
        p: common_vendor.p({
          icon: "/static/order-empty.png",
          text: "暂无该订单售后详情"
        })
      } : {}, {
        q: !common_vendor.unref(common_vendor.isEmpty)(state.info)
      }, !common_vendor.unref(common_vendor.isEmpty)(state.info) ? common_vendor.e({
        r: (_a = state.info.buttons) == null ? void 0 : _a.includes("cancel")
      }, ((_b = state.info.buttons) == null ? void 0 : _b.includes("cancel")) ? {
        s: common_vendor.o(($event) => onApply(state.info.id), "56")
      } : {}, {
        t: (_c = state.info.buttons) == null ? void 0 : _c.includes("delivery")
      }, ((_d = state.info.buttons) == null ? void 0 : _d.includes("delivery")) ? {
        v: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/order/aftersale/return-delivery", {
          id: state.info.id
        }), "ce")
      } : {}, {
        w: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/chat/index"), "25"),
        x: common_vendor.p({
          bottom: true,
          placeholder: true,
          bg: "bg-white"
        })
      }) : {}, {
        y: common_vendor.s(_ctx.__cssVars()),
        z: common_vendor.p({
          title: "售后详情",
          navbar: !common_vendor.unref(common_vendor.isEmpty)(state.info) && state.loading ? "inner" : "normal"
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-915d84ff"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/order/aftersale/detail.js.map
