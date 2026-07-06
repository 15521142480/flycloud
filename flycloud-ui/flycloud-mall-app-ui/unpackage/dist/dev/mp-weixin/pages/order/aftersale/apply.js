"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../../sheep/index.js");
const sheep_api_trade_order = require("../../../sheep/api/trade/order.js");
const sheep_api_trade_config = require("../../../sheep/api/trade/config.js");
const sheep_hooks_useGoods = require("../../../sheep/hooks/useGoods.js");
const sheep_api_trade_afterSale = require("../../../sheep/api/trade/afterSale.js");
if (!Array) {
  const _easycom_s_goods_item2 = common_vendor.resolveComponent("s-goods-item");
  const _easycom_uni_easyinput2 = common_vendor.resolveComponent("uni-easyinput");
  const _easycom_s_uploader2 = common_vendor.resolveComponent("s-uploader");
  const _easycom_uni_forms2 = common_vendor.resolveComponent("uni-forms");
  const _easycom_su_fixed2 = common_vendor.resolveComponent("su-fixed");
  const _easycom_su_popup2 = common_vendor.resolveComponent("su-popup");
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  (_easycom_s_goods_item2 + _easycom_uni_easyinput2 + _easycom_s_uploader2 + _easycom_uni_forms2 + _easycom_su_fixed2 + _easycom_su_popup2 + _easycom_s_layout2)();
}
const _easycom_s_goods_item = () => "../../../sheep/components/s-goods-item/s-goods-item.js";
const _easycom_uni_easyinput = () => "../../../uni_modules/uni-easyinput/components/uni-easyinput/uni-easyinput.js";
const _easycom_s_uploader = () => "../../../sheep/components/s-uploader/s-uploader.js";
const _easycom_uni_forms = () => "../../../uni_modules/uni-forms/components/uni-forms/uni-forms.js";
const _easycom_su_fixed = () => "../../../sheep/ui/su-fixed/su-fixed.js";
const _easycom_su_popup = () => "../../../sheep/ui/su-popup/su-popup.js";
const _easycom_s_layout = () => "../../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  (_easycom_s_goods_item + _easycom_uni_easyinput + _easycom_s_uploader + _easycom_uni_forms + _easycom_su_fixed + _easycom_su_popup + _easycom_s_layout)();
}
const _sfc_main = {
  __name: "apply",
  setup(__props) {
    const form = common_vendor.ref(null);
    const state = common_vendor.reactive({
      orderId: 0,
      // 订单编号
      itemId: 0,
      // 订单项编号
      order: {},
      // 订单
      item: {},
      // 订单项
      config: {},
      // 交易配置
      // 售后类型
      wayList: [
        {
          text: "仅退款",
          value: "10"
        },
        {
          text: "退款退货",
          value: "20"
        }
      ],
      reasonList: [],
      // 可选的申请原因数组
      showModal: false,
      // 是否显示申请原因弹窗
      currentValue: ""
      // 当前选择的售后原因
    });
    let formData = common_vendor.reactive({
      way: "",
      applyReason: "",
      applyDescription: "",
      applyPicUrls: []
    });
    const rules = common_vendor.reactive({});
    async function submit() {
      let data = {
        orderItemId: state.itemId,
        refundPrice: state.item.payPrice,
        ...formData
      };
      const { code } = await sheep_api_trade_afterSale.AfterSaleApi.createAfterSale(data);
      if (code === 0) {
        common_vendor.index.showToast({
          title: "申请成功"
        });
        sheep_index.sheep.$router.redirect("/pages/order/aftersale/list");
      }
    }
    function onRefundChange(e) {
      formData.way = e.detail.value;
      state.reasonList = formData.way === "10" ? state.config.afterSaleRefundReasons || [] : state.config.afterSaleReturnReasons || [];
      formData.applyReason = "";
      state.currentValue = "";
    }
    function onChange(e) {
      state.currentValue = e.detail.value;
    }
    function onReason() {
      formData.applyReason = state.currentValue;
      state.showModal = false;
    }
    common_vendor.onLoad(async (options) => {
      if (!options.orderId || !options.itemId) {
        sheep_index.sheep.$helper.toast(`缺少订单信息，请检查`);
        return;
      }
      state.orderId = options.orderId;
      state.itemId = parseInt(options.itemId);
      const { code, data } = await sheep_api_trade_order.OrderApi.getOrderDetail(state.orderId);
      if (code !== 0) {
        return;
      }
      state.order = data;
      state.item = data.items.find((item) => item.id === state.itemId) || {};
      if (state.order.status === 10) {
        state.wayList.splice(1, 1);
      }
      state.config = (await sheep_api_trade_config.TradeConfigApi.getTradeConfig()).data;
    });
    return (_ctx, _cache) => {
      var _a;
      return common_vendor.e({
        a: common_vendor.p({
          img: state.item.picUrl,
          title: state.item.spuName,
          skuText: (_a = state.item.properties) == null ? void 0 : _a.map((property) => property.valueName).join(" "),
          price: state.item.price,
          num: state.item.count
        }),
        b: common_vendor.f(state.wayList, (item, index, i0) => {
          return {
            a: common_vendor.unref(formData).type === item.value,
            b: item.value,
            c: common_vendor.t(item.text),
            d: index
          };
        }),
        c: common_vendor.o(onRefundChange, "4a"),
        d: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(state.item.payPrice)),
        e: common_vendor.o(($event) => state.showModal = true, "a0"),
        f: common_vendor.unref(formData).applyReason
      }, common_vendor.unref(formData).applyReason ? {
        g: common_vendor.t(common_vendor.unref(formData).applyReason)
      } : {}, {
        h: common_vendor.o(($event) => state.showModal = true, "68"),
        i: common_vendor.o(($event) => common_vendor.unref(formData).applyDescription = $event, "2a"),
        j: common_vendor.p({
          inputBorder: false,
          type: "textarea",
          maxlength: "120",
          autoHeight: true,
          placeholder: "客官~请描述您遇到的问题，建议上传照片",
          modelValue: common_vendor.unref(formData).applyDescription
        }),
        k: common_vendor.o(($event) => common_vendor.unref(formData).applyPicUrls = $event, "f3"),
        l: common_vendor.p({
          fileMediatype: "image",
          limit: "9",
          mode: "grid",
          imageStyles: {
            width: "168rpx",
            height: "168rpx"
          },
          url: common_vendor.unref(formData).applyPicUrls
        }),
        m: common_vendor.sr(form, "5be4d0ee-2,5be4d0ee-0", {
          "k": "form"
        }),
        n: common_vendor.o(($event) => common_vendor.isRef(formData) ? formData.value = $event : formData = $event, "ac"),
        o: common_vendor.p({
          rules,
          ["label-position"]: "top",
          modelValue: common_vendor.unref(formData)
        }),
        p: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/chat/index"), "b1"),
        q: common_vendor.o(submit, "d3"),
        r: common_vendor.p({
          bottom: true,
          placeholder: true
        }),
        s: common_vendor.f(state.reasonList, (item, k0, i0) => {
          return {
            a: common_vendor.t(item),
            b: item,
            c: item === state.currentValue,
            d: item
          };
        }),
        t: common_vendor.o(onChange, "a7"),
        v: common_vendor.o(onReason, "1e"),
        w: common_vendor.o(($event) => state.showModal = false, "66"),
        x: common_vendor.p({
          show: state.showModal,
          round: "10",
          showClose: true
        }),
        y: common_vendor.p({
          title: "申请售后"
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-5be4d0ee"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/order/aftersale/apply.js.map
