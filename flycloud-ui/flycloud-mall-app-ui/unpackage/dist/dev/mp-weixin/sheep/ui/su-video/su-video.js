"use strict";
const common_vendor = require("../../../common/vendor.js");
require("../../index.js");
const _sfc_main = {
  __name: "su-video",
  props: {
    moveX: {
      type: [Number],
      default: 0
    },
    // 下标索引
    uid: {
      type: [Number, String],
      default: 0
    },
    // 视频高度
    height: {
      type: Number,
      default: 300
    },
    // 视频宽度
    width: {
      type: Number,
      default: 750
    },
    // 指定视频初始播放位置，单位为秒（s）
    initialTime: {
      type: Number,
      default: 0
    },
    src: {
      type: String,
      default: ""
    },
    poster: {
      type: String,
      default: "https://img1.baidu.com/it/u=1601695551,235775011&fm=26&fmt=auto"
    }
  },
  emits: ["videoTimeupdate"],
  setup(__props, { expose: __expose, emit: __emit }) {
    const vm = common_vendor.getCurrentInstance();
    const state = common_vendor.reactive({
      enableProgressGesture: false,
      // 手势滑动
      showModal: false
      // 弹框
    });
    const props = __props;
    const emits = __emit;
    const timeupdate = (e) => {
      emits("videoTimeupdate", e);
    };
    const videoErrorCallback = (e) => {
      common_vendor.index.__f__("log", "at sheep/ui/su-video/su-video.vue:134", "视频错误信息:", e.target.errMsg);
    };
    const play = () => {
      common_vendor.index.__f__("log", "at sheep/ui/su-video/su-video.vue:138", "视频开始");
    };
    const pause = () => {
      common_vendor.index.__f__("log", "at sheep/ui/su-video/su-video.vue:142", "视频暂停");
    };
    const end = () => {
      common_vendor.index.__f__("log", "at sheep/ui/su-video/su-video.vue:146", "视频结束");
    };
    const pausePlay = () => {
      const video = common_vendor.index.createVideoContext(`sVideo${props.index}`, vm);
      video.pause();
    };
    __expose({
      pausePlay
    });
    return (_ctx, _cache) => {
      return {
        a: `sVideo${__props.uid}`,
        b: common_vendor.s({
          height: __props.height + "rpx"
        }),
        c: __props.src,
        d: state.enableProgressGesture,
        e: __props.initialTime,
        f: common_vendor.o(videoErrorCallback, "79"),
        g: common_vendor.o(timeupdate, "cb"),
        h: common_vendor.o(play, "3c"),
        i: common_vendor.o(pause, "ab"),
        j: common_vendor.o(end, "a3"),
        k: __props.poster
      };
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-f4b9589e"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/ui/su-video/su-video.js.map
