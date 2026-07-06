"use strict";
const common_vendor = require("../../../../common/vendor.js");
const sheep_index = require("../../../index.js");
const sheep_components_sShareModal_canvasPoster_poster_index = require("./poster/index.js");
if (!Array) {
  const _easycom_l_painter2 = common_vendor.resolveComponent("l-painter");
  const _easycom_su_popup2 = common_vendor.resolveComponent("su-popup");
  (_easycom_l_painter2 + _easycom_su_popup2)();
}
const _easycom_l_painter = () => "../../../../uni_modules/lime-painter/components/l-painter/l-painter.js";
const _easycom_su_popup = () => "../../../ui/su-popup/su-popup.js";
if (!Math) {
  (_easycom_l_painter + _easycom_su_popup)();
}
const _sfc_main = {
  __name: "index",
  props: {
    show: {
      type: Boolean,
      default: false
    },
    shareInfo: {
      type: Object,
      default: () => {
      }
    }
  },
  emits: ["success", "close"],
  setup(__props, { expose: __expose, emit: __emit }) {
    const props = __props;
    const poster = common_vendor.reactive({
      css: {
        // 根节点若无尺寸，自动获取父级节点
        width: sheep_index.sheep.$platform.device.windowWidth * 0.9,
        height: 600
      },
      views: []
    });
    const emits = __emit;
    const onClosePoster = () => {
      emits("close");
    };
    const painterRef = common_vendor.ref();
    const painterImageUrl = common_vendor.ref();
    const renderPoster = async () => {
      await painterRef.value.render(common_vendor.unref(poster));
    };
    const setPainterImageUrl = (path) => {
      painterImageUrl.value = path;
    };
    const onSavePoster = () => {
      if (["WechatOfficialAccount", "H5"].includes(sheep_index.sheep.$platform.name)) {
        sheep_index.sheep.$helper.toast("请长按图片保存");
        return;
      }
      common_vendor.index.saveImageToPhotosAlbum({
        filePath: painterImageUrl.value,
        success: (res) => {
          onClosePoster();
          sheep_index.sheep.$helper.toast("保存成功");
        },
        fail: (err) => {
          sheep_index.sheep.$helper.toast("保存失败");
          common_vendor.index.__f__("log", "at sheep/components/s-share-modal/canvas-poster/index.vue:106", "图片保存失败:", err);
        }
      });
    };
    async function getPoster() {
      painterImageUrl.value = void 0;
      poster.views = await sheep_components_sShareModal_canvasPoster_poster_index.getPosterData({
        width: poster.css.width,
        shareInfo: props.shareInfo
      });
      await renderPoster();
    }
    __expose({
      getPoster
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: !!painterImageUrl.value
      }, !!painterImageUrl.value ? {
        b: painterImageUrl.value,
        c: poster.css.height + "px",
        d: poster.css.width + "px"
      } : {}, {
        e: !!painterImageUrl.value
      }, !!painterImageUrl.value ? {
        f: common_vendor.o(onClosePoster, "e1"),
        g: common_vendor.t(["wechatOfficialAccount", "H5"].includes(common_vendor.unref(sheep_index.sheep).$platform.name) ? "长按图片保存" : "保存图片"),
        h: common_vendor.o(onSavePoster, "9e")
      } : {}, {
        i: common_vendor.sr(painterRef, "199e8f1d-1,199e8f1d-0", {
          "k": "painterRef"
        }),
        j: common_vendor.o(setPainterImageUrl, "c6"),
        k: common_vendor.p({
          isCanvasToTempFilePath: true,
          pathType: "url",
          hidden: true
        }),
        l: common_vendor.o(onClosePoster, "9f"),
        m: common_vendor.p({
          show: __props.show,
          round: "10",
          type: "center"
        })
      });
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-199e8f1d"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../../.sourcemap/mp-weixin/sheep/components/s-share-modal/canvas-poster/index.js.map
