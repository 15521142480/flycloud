"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../index.js");
const sheep_hooks_useModal = require("../../hooks/useModal.js");
if (!Array) {
  const _easycom_su_popup2 = common_vendor.resolveComponent("su-popup");
  _easycom_su_popup2();
}
const _easycom_su_popup = () => "../../ui/su-popup/su-popup.js";
if (!Math) {
  (_easycom_su_popup + canvasPoster)();
}
const canvasPoster = () => "./canvas-poster/index.js";
const _sfc_main = {
  __name: "s-share-modal",
  props: {
    shareInfo: {
      type: Object,
      default() {
      }
    }
  },
  setup(__props) {
    const show = common_vendor.computed(() => sheep_index.sheep.$store("modal").share);
    const shareConfig = common_vendor.computed(() => sheep_index.sheep.$store("app").platform.share);
    const SharePosterRef = common_vendor.ref("");
    const props = __props;
    const state = common_vendor.reactive({
      showShareGuide: false,
      // H5 的指引
      showPosterModal: false
      // 海报弹窗
    });
    const onShareByPoster = () => {
      sheep_hooks_useModal.closeShareModal();
      if (!sheep_index.sheep.$store("user").isLogin) {
        sheep_hooks_useModal.showAuthModal();
        return;
      }
      common_vendor.unref(SharePosterRef).getPoster();
      state.showPosterModal = true;
    };
    const onShareByForward = () => {
      sheep_hooks_useModal.closeShareModal();
    };
    const onShareByCopyLink = () => {
      sheep_index.sheep.$helper.copyText(props.shareInfo.link);
      sheep_hooks_useModal.closeShareModal();
    };
    function onCloseGuide() {
      state.showShareGuide = false;
    }
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.o(onCloseGuide, "b4"),
        b: common_vendor.p({
          show: state.showShareGuide,
          showClose: false
        }),
        c: state.showShareGuide
      }, state.showShareGuide ? {
        d: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/share/share_guide.png")
      } : {}, {
        e: shareConfig.value.methods.includes("forward")
      }, shareConfig.value.methods.includes("forward") ? {
        f: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/share/share_wx.png"),
        g: common_vendor.o(onShareByForward, "6b")
      } : {}, {
        h: shareConfig.value.methods.includes("poster")
      }, shareConfig.value.methods.includes("poster") ? {
        i: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/share/share_poster.png"),
        j: common_vendor.o(onShareByPoster, "8b")
      } : {}, {
        k: shareConfig.value.methods.includes("link")
      }, shareConfig.value.methods.includes("link") ? {
        l: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/share/share_link.png"),
        m: common_vendor.o(onShareByCopyLink, "a8")
      } : {}, {
        n: common_vendor.o((...args) => common_vendor.unref(sheep_hooks_useModal.closeShareModal) && common_vendor.unref(sheep_hooks_useModal.closeShareModal)(...args), "b2"),
        o: common_vendor.o(common_vendor.unref(sheep_hooks_useModal.closeShareModal), "f2"),
        p: common_vendor.p({
          show: show.value,
          round: "10",
          showClose: false
        }),
        q: common_vendor.sr(SharePosterRef, "a2a58660-2", {
          "k": "SharePosterRef"
        }),
        r: common_vendor.o(($event) => state.showPosterModal = false, "e5"),
        s: common_vendor.p({
          show: state.showPosterModal,
          shareInfo: __props.shareInfo
        })
      });
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-a2a58660"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/components/s-share-modal/s-share-modal.js.map
