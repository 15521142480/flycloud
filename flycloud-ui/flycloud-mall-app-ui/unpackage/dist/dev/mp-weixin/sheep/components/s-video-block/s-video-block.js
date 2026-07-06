"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../index.js");
const sheep_helper_index = require("../../helper/index.js");
if (!Array) {
  const _easycom_su_video2 = common_vendor.resolveComponent("su-video");
  _easycom_su_video2();
}
const _easycom_su_video = () => "../../ui/su-video/su-video.js";
if (!Math) {
  _easycom_su_video();
}
const _sfc_main = {
  __name: "s-video-block",
  props: {
    data: {
      type: Object,
      default() {
      }
    },
    styles: {
      type: Object,
      default() {
      }
    }
  },
  setup(__props) {
    return (_ctx, _cache) => {
      return {
        a: common_vendor.p({
          uid: common_vendor.unref(sheep_helper_index.guid)(),
          src: common_vendor.unref(sheep_index.sheep).$url.cdn(__props.data.videoUrl),
          poster: common_vendor.unref(sheep_index.sheep).$url.cdn(__props.data.posterUrl),
          height: __props.styles.height * 2,
          autoplay: __props.data.autoplay
        })
      };
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-33d06052"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/components/s-video-block/s-video-block.js.map
