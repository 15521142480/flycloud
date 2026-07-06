"use strict";
const common_vendor = require("../../../../common/vendor.js");
const sheep_index = require("../../../../sheep/index.js");
if (!Array) {
  const _easycom_uni_rate2 = common_vendor.resolveComponent("uni-rate");
  const _easycom_su_image2 = common_vendor.resolveComponent("su-image");
  (_easycom_uni_rate2 + _easycom_su_image2)();
}
const _easycom_uni_rate = () => "../../../../uni_modules/uni-rate/components/uni-rate/uni-rate.js";
const _easycom_su_image = () => "../../../../sheep/ui/su-image/su-image.js";
if (!Math) {
  (_easycom_uni_rate + _easycom_su_image)();
}
const _sfc_main = {
  __name: "comment-item",
  props: {
    item: {
      type: Object,
      default() {
      }
    }
  },
  setup(__props) {
    return (_ctx, _cache) => {
      var _a, _b;
      return common_vendor.e({
        a: common_vendor.unref(sheep_index.sheep).$url.cdn(__props.item.userAvatar),
        b: common_vendor.t(__props.item.userNickname),
        c: common_vendor.o(($event) => __props.item.scores = $event, "87"),
        d: common_vendor.p({
          readonly: true,
          size: "18",
          modelValue: __props.item.scores
        }),
        e: common_vendor.t(__props.item.content),
        f: (_a = __props.item.picUrls) == null ? void 0 : _a.length
      }, ((_b = __props.item.picUrls) == null ? void 0 : _b.length) ? {
        g: common_vendor.f(__props.item.picUrls, (picUrl, index, i0) => {
          return {
            a: "5629fc5e-1-" + i0,
            b: common_vendor.p({
              isPreview: true,
              previewList: __props.item.picUrls.map((picUrl2) => common_vendor.unref(sheep_index.sheep).$url.cdn(picUrl2)),
              current: index,
              src: common_vendor.unref(sheep_index.sheep).$url.cdn(picUrl),
              height: 120,
              width: 120,
              mode: "aspectFill"
            }),
            c: picUrl
          };
        })
      } : {}, {
        h: __props.item.replyTime
      }, __props.item.replyTime ? {
        i: common_vendor.t(__props.item.replyContent)
      } : {});
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-5629fc5e"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../../.sourcemap/mp-weixin/pages/goods/components/detail/comment-item.js.map
