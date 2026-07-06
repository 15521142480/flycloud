"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../../sheep/index.js");
if (!Array) {
  const _easycom_su_popup2 = common_vendor.resolveComponent("su-popup");
  _easycom_su_popup2();
}
const _easycom_su_popup = () => "../../../sheep/ui/su-popup/su-popup.js";
if (!Math) {
  _easycom_su_popup();
}
const _sfc_main = {
  __name: "account-type-select",
  props: {
    modelValue: {
      type: Object,
      default() {
      }
    },
    show: {
      type: Boolean,
      default: false
    },
    methods: {
      // 开启的提现方式
      type: Array,
      default: []
    }
  },
  emits: ["update:modelValue", "change", "close"],
  setup(__props, { emit: __emit }) {
    const emits = __emit;
    const state = common_vendor.reactive({
      currentValue: ""
    });
    const typeList = [
      {
        icon: "/static/img/shop/pay/wallet.png",
        title: "钱包余额",
        value: "1"
      },
      {
        icon: "/static/img/shop/pay/bank.png",
        title: "银行卡转账",
        value: "2"
      },
      {
        icon: "/static/img/shop/pay/wechat.png",
        title: "微信收款码",
        // 微信手动转账
        value: "3"
      },
      {
        icon: "/static/img/shop/pay/alipay.png",
        title: "支付宝收款码",
        // 支付宝手动转账
        value: "4"
      },
      {
        icon: "/static/img/shop/pay/wechat_api.png",
        title: "微信零钱",
        // 微信 API 转账
        value: "5"
      },
      {
        icon: "/static/img/shop/pay/alipay_api.png",
        title: "支付宝余额",
        // 支付宝 API 转账
        value: "6"
      }
    ];
    function onChange(e) {
      state.currentValue = e.detail.value;
    }
    const onConfirm = async () => {
      if (state.currentValue === "") {
        sheep_index.sheep.$helper.toast("请选择提现方式");
        return;
      }
      emits("update:modelValue", {
        type: state.currentValue
      });
      emits("close");
    };
    const hideModal = () => {
      emits("close");
    };
    return (_ctx, _cache) => {
      return {
        a: common_vendor.f(typeList, (item, index, i0) => {
          return {
            a: common_vendor.unref(sheep_index.sheep).$url.static(item.icon),
            b: common_vendor.t(item.title),
            c: item.value,
            d: item.value === state.currentValue,
            e: !__props.methods.includes(parseInt(item.value)),
            f: index
          };
        }),
        b: common_vendor.o(onChange, "29"),
        c: common_vendor.o(onConfirm, "e8"),
        d: common_vendor.o(hideModal, "e7"),
        e: common_vendor.p({
          show: __props.show
        })
      };
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-a5da3655"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/commission/components/account-type-select.js.map
