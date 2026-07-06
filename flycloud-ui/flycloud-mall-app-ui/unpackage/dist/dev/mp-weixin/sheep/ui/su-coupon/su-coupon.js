"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../index.js");
const _sfc_main = {
  __name: "su-coupon",
  props: {
    size: {
      type: String,
      default: "lg"
    },
    textColor: {
      type: String,
      default: "#FF6000"
    },
    background: {
      type: String,
      default: "#FFC19C"
    },
    btnBg: {
      type: String,
      default: "#fff"
    },
    btnTextColor: {
      type: String,
      default: "#FF6000"
    },
    state: {
      type: Number,
      default: 0
    },
    couponId: {
      type: Number,
      default: 0
    },
    title: {
      type: String,
      default: "这是优惠券"
    },
    value: {
      type: [Number, String],
      default: 50
    },
    sellBy: {
      type: String,
      default: "2019.11.25至2019.12.25"
    },
    surplus: {
      type: [Number, String],
      default: 0
    },
    type: {
      type: String,
      default: ""
    }
  },
  setup(__props) {
    common_vendor.useCssVars((_ctx) => ({
      "c1c89bcc": __props.textColor,
      "66b278d6": __props.btnTextColor,
      "babd14b6": __props.btnBg
    }));
    const state = common_vendor.reactive({
      stateMap: {
        0: "立即领取",
        1: "去使用"
      }
    });
    const props = __props;
    const cardStyle = common_vendor.computed(() => {
      return {
        background: props.background
      };
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: props.size === "xs"
      }, props.size === "xs" ? {
        b: common_vendor.t(__props.type === "reduce" ? __props.value : Number(__props.value)),
        c: common_vendor.t(__props.type === "reduce" ? "元" : "折"),
        d: common_vendor.t(props.title),
        e: common_vendor.t(state.stateMap[props.state]),
        f: common_vendor.s(cardStyle.value),
        g: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/coupon/detail", {
          id: __props.couponId
        }), "8d")
      } : {}, {
        h: props.size === "md"
      }, props.size === "md" ? common_vendor.e({
        i: common_vendor.t(__props.type === "reduce" ? __props.value : Number(__props.value)),
        j: common_vendor.t(__props.type === "reduce" ? "元" : "折"),
        k: common_vendor.t(props.title),
        l: props.surplus >= 0
      }, props.surplus >= 0 ? {
        m: common_vendor.t(props.surplus)
      } : props.surplus === -1 ? {} : {}, {
        n: props.surplus === -1,
        o: common_vendor.t(state.stateMap[props.state]),
        p: common_vendor.s(cardStyle.value),
        q: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/coupon/detail", {
          id: __props.couponId
        }), "f3")
      }) : {}, {
        r: props.size === "lg"
      }, props.size === "lg" ? common_vendor.e({
        s: common_vendor.t(__props.type === "reduce" ? __props.value : Number(__props.value)),
        t: common_vendor.t(__props.type === "reduce" ? "元" : "折"),
        v: common_vendor.t(props.title),
        w: common_vendor.t(props.sellBy),
        x: common_vendor.t(state.stateMap[props.state]),
        y: props.surplus
      }, props.surplus ? {
        z: common_vendor.t(props.surplus)
      } : {}, {
        A: common_vendor.s(cardStyle.value),
        B: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/coupon/detail", {
          id: __props.couponId
        }), "ec")
      }) : {}, {
        C: common_vendor.s(_ctx.__cssVars())
      });
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-34099896"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/ui/su-coupon/su-coupon.js.map
