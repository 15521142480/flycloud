"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../index.js");
const _sfc_main = {
  __name: "s-live-card",
  props: {
    goodsFields: {
      type: [Array, Object],
      default() {
        return {};
      }
    },
    tagStyle: {
      type: Object,
      default: {}
    },
    data: {
      type: Object,
      default: {}
    },
    size: {
      type: String,
      default: "sl"
    },
    background: {
      type: String,
      default: ""
    },
    topRadius: {
      type: Number,
      default: 0
    },
    bottomRadius: {
      type: Number,
      default: 0
    },
    titleColor: {
      type: String,
      default: "#333"
    },
    subTitleColor: {
      type: String,
      default: "#999999"
    }
  },
  emits: ["click", "getHeight"],
  setup(__props, { emit: __emit }) {
    const props = __props;
    const elStyles = common_vendor.computed(() => {
      return {
        background: props.background,
        "border-top-left-radius": props.topRadius + "px",
        "border-top-right-radius": props.topRadius + "px",
        "border-bottom-left-radius": props.bottomRadius + "px",
        "border-bottom-right-radius": props.bottomRadius + "px"
      };
    });
    const state = common_vendor.reactive({
      liveStatus: {
        101: {
          img: sheep_index.sheep.$url.static("/static/img/shop/app/mplive/living.png"),
          title: "直播中"
        },
        102: {
          img: sheep_index.sheep.$url.static("/static/img/shop/app/mplive/start.png"),
          title: "未开始"
        },
        103: {
          img: sheep_index.sheep.$url.static("/static/img/shop/app/mplive/ended.png"),
          title: "已结束"
        }
      }
    });
    const emits = __emit;
    const onClick = () => {
      emits("click");
    };
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: __props.size === "md"
      }, __props.size === "md" ? {
        b: state.liveStatus[__props.data.status].img,
        c: common_vendor.t(state.liveStatus[__props.data.status].title),
        d: common_vendor.unref(sheep_index.sheep).$url.cdn(__props.data.feeds_img),
        e: common_vendor.t(__props.data.name),
        f: common_vendor.s({
          color: __props.titleColor
        }),
        g: common_vendor.t(__props.data.anchor_name),
        h: common_vendor.s({
          color: __props.subTitleColor
        }),
        i: common_vendor.s(elStyles.value),
        j: common_vendor.o(onClick, "3a")
      } : {}, {
        k: __props.size === "sl"
      }, __props.size === "sl" ? {
        l: state.liveStatus[__props.data.status].img,
        m: common_vendor.t(state.liveStatus[__props.data.status].title),
        n: common_vendor.unref(sheep_index.sheep).$url.cdn(__props.data.feeds_img),
        o: common_vendor.t(__props.data.name),
        p: common_vendor.s({
          color: __props.titleColor
        }),
        q: common_vendor.t(__props.data.anchor_name),
        r: common_vendor.s({
          color: __props.subTitleColor
        }),
        s: common_vendor.s(elStyles.value),
        t: common_vendor.o(onClick, "9b")
      } : {});
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-e059c327"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/components/s-live-card/s-live-card.js.map
