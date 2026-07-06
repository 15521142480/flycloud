"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../index.js");
const _sfc_main = {
  __name: "s-menu-button",
  props: {
    // 装修数据
    data: {
      type: Object,
      default: () => ({})
    },
    // 装修样式
    styles: {
      type: Object,
      default: () => ({})
    },
    circular: {
      type: Boolean,
      default: true
    },
    autoplay: {
      type: Boolean,
      default: false
    },
    interval: {
      type: Number,
      default: 5e3
    },
    duration: {
      type: Number,
      default: 500
    },
    ui: {
      type: String,
      default: ""
    },
    mode: {
      //default
      type: String,
      default: "default"
    },
    dotStyle: {
      type: String,
      default: "long"
      //default long tag
    },
    dotCur: {
      type: String,
      default: "ui-BG-Main"
    },
    height: {
      type: Number,
      default: 300
    },
    // 是否有边框
    hasBorder: {
      type: Boolean,
      default: true
    },
    // 边框颜色
    borderColor: {
      type: String,
      default: "red"
    },
    background: {
      type: String,
      default: "blue"
    },
    hoverClass: {
      type: String,
      default: "ss-hover-class"
      //'none'为没有hover效果
    },
    // 一排宫格数
    col: {
      type: [Number, String],
      default: 3
    },
    iconSize: {
      type: Number,
      default: 80
    },
    color: {
      type: String,
      default: "#000"
    }
  },
  setup(__props) {
    const state = common_vendor.reactive({
      cur: 0
    });
    const props = __props;
    const bgStyle = common_vendor.computed(() => {
      const {
        bgType,
        bgImg,
        bgColor
      } = props.styles;
      return {
        background: bgType === "img" ? `url(${bgImg}) no-repeat top center / 100% 100%` : bgColor
      };
    });
    const menuList = common_vendor.computed(() => splitData(props.data.list, props.data.row * props.data.column));
    const swiperHeight = common_vendor.computed(() => props.data.row * (props.data.layout === "iconText" ? 200 : 180));
    sheep_index.sheep.$platform.device.windowWidth;
    const swiperChange = (e) => {
      state.cur = e.detail.current;
    };
    const splitData = (oArr = [], length = 1) => {
      let arr = [];
      let minArr = [];
      oArr.forEach((c) => {
        if (minArr.length === length) {
          minArr = [];
        }
        if (minArr.length === 0) {
          arr.push(minArr);
        }
        minArr.push(c);
      });
      return arr;
    };
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.f(menuList.value, (arr, index, i0) => {
          return {
            a: common_vendor.f(arr, (item, index2, i1) => {
              return common_vendor.e({
                a: item.badge.show
              }, item.badge.show ? {
                b: common_vendor.t(item.badge.text),
                c: common_vendor.s({
                  background: item.badge.bgColor,
                  color: item.badge.textColor
                })
              } : {}, {
                d: item.iconUrl
              }, item.iconUrl ? {
                e: common_vendor.s({
                  width: props.iconSize + "rpx",
                  height: props.iconSize + "rpx"
                }),
                f: common_vendor.unref(sheep_index.sheep).$url.cdn(item.iconUrl)
              } : {}, __props.data.layout === "iconText" ? {
                g: common_vendor.t(item.title),
                h: common_vendor.s({
                  color: item.titleColor
                })
              } : {}, {
                i: index2,
                j: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go(item.url), index2)
              });
            }),
            b: index,
            c: state.cur == index ? 1 : ""
          };
        }),
        b: __props.data.layout === "iconText",
        c: common_vendor.s({
          width: `${100 * (1 / __props.data.column)}%`,
          height: "200rpx"
        }),
        d: props.circular,
        e: state.cur,
        f: props.autoplay,
        g: props.interval,
        h: props.duration,
        i: common_vendor.s({
          height: swiperHeight.value + "rpx"
        }),
        j: common_vendor.o(swiperChange, "cb"),
        k: menuList.value.length > 1
      }, menuList.value.length > 1 ? common_vendor.e({
        l: props.dotStyle != "tag"
      }, props.dotStyle != "tag" ? {
        m: common_vendor.f(menuList.value.length, (item, index, i0) => {
          return {
            a: index,
            b: common_vendor.n(state.cur == index ? "cur" : "")
          };
        }),
        n: common_vendor.n(props.dotCur),
        o: common_vendor.n(props.dotStyle)
      } : {}, {
        p: props.dotStyle == "tag"
      }, props.dotStyle == "tag" ? {
        q: common_vendor.t(state.cur + 1),
        r: common_vendor.t(menuList.value.length),
        s: common_vendor.n(props.dotCur),
        t: common_vendor.n(props.dotStyle)
      } : {}) : {}, {
        v: common_vendor.n(props.mode),
        w: common_vendor.n(props.ui),
        x: common_vendor.s(bgStyle.value),
        y: common_vendor.s({
          height: swiperHeight.value + (menuList.value.length > 1 ? 50 : 0) + "rpx"
        })
      });
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-75f856ac"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/components/s-menu-button/s-menu-button.js.map
