"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../index.js");
if (!Array) {
  const _easycom_su_swiper2 = common_vendor.resolveComponent("su-swiper");
  _easycom_su_swiper2();
}
const _easycom_su_swiper = () => "../../ui/su-swiper/su-swiper.js";
if (!Math) {
  _easycom_su_swiper();
}
const _sfc_main = {
  __name: "s-image-banner",
  props: {
    data: {
      type: Object,
      default: () => ({})
    },
    styles: {
      type: Object,
      default: () => ({})
    }
  },
  setup(__props) {
    const props = __props;
    function px2rpx(px) {
      let scale = common_vendor.index.upx2px(100) / 100;
      return px / scale;
    }
    const imgList = common_vendor.computed(
      () => props.data.items.map((item) => {
        const src = item.type === "img" ? item.imgUrl : item.videoUrl;
        return {
          ...item,
          type: item.type === "img" ? "image" : "video",
          src: sheep_index.sheep.$url.cdn(src),
          poster: sheep_index.sheep.$url.cdn(item.imgUrl)
        };
      })
    );
    return (_ctx, _cache) => {
      return {
        a: common_vendor.p({
          list: imgList.value,
          dotStyle: __props.data.indicator === "dot" ? "long" : "tag",
          imageMode: "scaleToFill",
          dotCur: "bg-mask-40",
          seizeHeight: 300,
          autoplay: __props.data.autoplay,
          interval: __props.data.interval * 1e3,
          mode: __props.data.type,
          height: px2rpx(__props.data.height)
        })
      };
    };
  }
};
wx.createComponent(_sfc_main);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/components/s-image-banner/s-image-banner.js.map
