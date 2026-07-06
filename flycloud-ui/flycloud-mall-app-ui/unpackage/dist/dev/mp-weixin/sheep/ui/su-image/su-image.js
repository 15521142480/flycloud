"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../index.js");
const _sfc_main = {
  __name: "su-image",
  props: {
    src: {
      type: String,
      default: ""
    },
    errorSrc: {
      type: String,
      default: "/static/img/shop/empty_network.png"
    },
    mode: {
      type: String,
      default: "widthFix"
    },
    isPreview: {
      type: Boolean,
      default: false
    },
    previewList: {
      type: Array,
      default() {
        return [];
      }
    },
    current: {
      type: Number,
      default: -1
    },
    height: {
      type: Number,
      default: 0
    },
    width: {
      type: Number,
      default: 0
    },
    radius: {
      type: Number,
      default: 0
    }
  },
  emits: ["load", "error"],
  setup(__props, { emit: __emit }) {
    const state = common_vendor.reactive({
      isError: false,
      imgHeight: 600
    });
    const props = __props;
    const emits = __emit;
    const customStyle = common_vendor.computed(() => {
      return {
        height: (props.height || state.imgHeight) + "rpx",
        width: props.width ? props.width + "rpx" : "100%",
        borderRadius: props.radius ? props.radius + "rpx" : ""
      };
    });
    function onImgLoad(e) {
      if (props.height === 0) {
        state.imgHeight = e.detail.height / e.detail.width * 750;
      }
    }
    function onImgError(e) {
      state.isError = true;
      emits("error", e);
    }
    function onImgPreview() {
      if (!props.isPreview)
        return;
      common_vendor.index.previewImage({
        urls: props.previewList.length < 1 ? [props.src] : props.previewList,
        current: props.current,
        longPressActions: {
          itemList: ["发送给朋友", "保存图片", "收藏"],
          success: function(data) {
            common_vendor.index.__f__("log", "at sheep/ui/su-image/su-image.vue:113", "选中了第" + (data.tapIndex + 1) + "个按钮,第" + (data.index + 1) + "张图片");
          },
          fail: function(err) {
            common_vendor.index.__f__("log", "at sheep/ui/su-image/su-image.vue:116", err.errMsg);
          }
        }
      });
    }
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: !state.isError
      }, !state.isError ? {
        b: common_vendor.s(customStyle.value),
        c: __props.mode,
        d: common_vendor.unref(sheep_index.sheep).$url.cdn(__props.src),
        e: common_vendor.o(onImgPreview, "a3"),
        f: common_vendor.o(onImgLoad, "40"),
        g: common_vendor.o(onImgError, "94")
      } : {});
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-ed9e576c"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/ui/su-image/su-image.js.map
