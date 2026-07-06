"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../index.js");
if (!Array) {
  const _easycom_su_video2 = common_vendor.resolveComponent("su-video");
  _easycom_su_video2();
}
const _easycom_su_video = () => "../su-video/su-video.js";
if (!Math) {
  _easycom_su_video();
}
const _sfc_main = {
  __name: "su-swiper",
  props: {
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
      default: 3e3
    },
    duration: {
      type: Number,
      default: 500
    },
    mode: {
      type: String,
      default: "default"
    },
    imageMode: {
      type: String,
      default: "scaleToFill"
    },
    list: {
      type: Array,
      default() {
        return [];
      }
    },
    dotStyle: {
      type: String,
      default: "long"
      //default long tag
    },
    dotCur: {
      type: String,
      default: "ss-bg-opactity-block"
    },
    bg: {
      type: String,
      default: "bg-none"
    },
    height: {
      type: Number,
      default: 0
    },
    imgHeight: {
      type: Number,
      default: 0
    },
    imgTopRadius: {
      type: Number,
      default: 0
    },
    imgBottomRadius: {
      type: Number,
      default: 0
    },
    isPreview: {
      type: Boolean,
      default: false
    },
    seizeHeight: {
      type: Number,
      default: 200
    }
  },
  setup(__props) {
    const state = common_vendor.reactive({
      imgHeight: 0,
      cur: 0,
      moveX: 0,
      videoPlaySataus: false,
      heightList: []
    });
    const refs = common_vendor.reactive({
      videoRef: {}
    });
    const props = __props;
    const swiperChange = (e) => {
      if (e.detail.source !== "touch" && e.detail.source !== "autoplay")
        return;
      state.cur = e.detail.current;
      state.videoPlaySataus = false;
      if (props.list[state.cur].type === "video") {
        refs.videoRef[`video_${state.cur}`].pausePlay();
      }
    };
    const onSwiperItem = (item) => {
      if (item.type === "video") {
        state.videoPlaySataus = true;
      } else {
        sheep_index.sheep.$router.go(item.url);
        onPreview();
      }
    };
    const onPreview = () => {
      if (!props.isPreview)
        return;
      let previewImage = common_vendor.clone(props.list);
      previewImage.forEach((item, index) => {
        if (item.type === "video") {
          previewImage.splice(index, 1);
        }
      });
      common_vendor.index.previewImage({
        urls: previewImage.length < 1 ? [props.src] : previewImage.reduce((pre, cur) => {
          pre.push(cur.src);
          return pre;
        }, []),
        current: state.cur
        // longPressActions: {
        //   itemList: ['发送给朋友', '保存图片', '收藏'],
        //   success: function (data) {
        //     uni.__f__('log','at sheep/ui/su-swiper/su-swiper.vue:216','选中了第' + (data.tapIndex + 1) + '个按钮,第' + (data.index + 1) + '张图片');
        //   },
        //   fail: function (err) {
        //     uni.__f__('log','at sheep/ui/su-swiper/su-swiper.vue:219',err.errMsg);
        //   },
        // },
      });
    };
    const transition = (e) => {
    };
    const animationfinish = (e) => {
      state.moveX = 0;
    };
    const videoTimeupdate = (e) => {
      props.list[state.cur].currentTime = e.detail.currentTime;
    };
    const customStyle = common_vendor.computed(() => {
      let height;
      if (props.height !== 0) {
        height = props.height;
      }
      if (props.height === 0) {
        if (state.imgHeight !== 0) {
          height = state.imgHeight;
        } else if (props.seizeHeight !== 0) {
          height = props.seizeHeight;
        }
      }
      return {
        height: height + "rpx"
      };
    });
    function onImgLoad(e) {
      if (props.height === 0) {
        let newHeight = e.detail.height / e.detail.width * 750;
        if (state.imgHeight < newHeight) {
          state.imgHeight = newHeight;
        }
      }
    }
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.f(props.list, (item, index, i0) => {
          return common_vendor.e({
            a: item.type === "image"
          }, item.type === "image" ? {
            b: props.imageMode,
            c: common_vendor.unref(sheep_index.sheep).$url.cdn(item.src),
            d: common_vendor.o(onImgLoad, index)
          } : {
            e: common_vendor.sr((el) => refs.videoRef[`video_${index}`] = el, "b37b3c30-0-" + i0, {
              "f": 1
            }),
            f: (el) => refs.videoRef[`video_${index}`] = el,
            g: common_vendor.o(videoTimeupdate, index),
            h: "b37b3c30-0-" + i0,
            i: common_vendor.p({
              poster: common_vendor.unref(sheep_index.sheep).$url.cdn(item.poster),
              src: common_vendor.unref(sheep_index.sheep).$url.cdn(item.src),
              index,
              moveX: state.moveX,
              initialTime: item.currentTime || 0,
              height: __props.seizeHeight
            })
          }, {
            j: index,
            k: state.cur == index ? 1 : "",
            l: common_vendor.o(($event) => onSwiperItem(item), index)
          });
        }),
        b: props.circular,
        c: state.cur,
        d: props.autoplay && !state.videoPlaySataus,
        e: props.interval,
        f: props.duration,
        g: common_vendor.o(transition, "95"),
        h: common_vendor.o(animationfinish, "c9"),
        i: common_vendor.s(customStyle.value),
        j: common_vendor.o(swiperChange, "e5"),
        k: !state.videoPlaySataus
      }, !state.videoPlaySataus ? common_vendor.e({
        l: props.dotStyle != "tag"
      }, props.dotStyle != "tag" ? {
        m: common_vendor.f(props.list, (item, index, i0) => {
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
        r: common_vendor.t(props.list.length),
        s: common_vendor.n(props.dotCur),
        t: common_vendor.n(props.dotStyle)
      } : {}) : {}, {
        v: common_vendor.n(props.mode),
        w: common_vendor.n(props.bg),
        x: common_vendor.n(props.ui)
      });
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-b37b3c30"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/ui/su-swiper/su-swiper.js.map
