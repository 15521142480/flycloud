"use strict";
const common_vendor = require("../../../common/vendor.js");
const pages_chat_util_emoji = require("../util/emoji.js");
const sheep_index = require("../../../sheep/index.js");
if (!Array) {
  const _easycom_s_uploader2 = common_vendor.resolveComponent("s-uploader");
  const _easycom_su_popup2 = common_vendor.resolveComponent("su-popup");
  (_easycom_s_uploader2 + _easycom_su_popup2)();
}
const _easycom_s_uploader = () => "../../../sheep/components/s-uploader/s-uploader.js";
const _easycom_su_popup = () => "../../../sheep/ui/su-popup/su-popup.js";
if (!Math) {
  (_easycom_s_uploader + _easycom_su_popup)();
}
const _sfc_main = {
  __name: "toolsPopup",
  props: {
    // 工具模式
    toolsMode: {
      type: String,
      default: ""
    },
    // 控制工具菜单弹出
    showTools: {
      type: Boolean,
      default: () => false
    }
  },
  emits: ["onEmoji", "imageSelect", "onShowSelect", "close"],
  setup(__props, { emit: __emit }) {
    const emits = __emit;
    function handleClose() {
      emits("close");
    }
    function onEmoji(emoji) {
      emits("onEmoji", emoji);
    }
    function imageSelect(val) {
      emits("imageSelect", val);
    }
    function onShowSelect(mode) {
      emits("onShowSelect", mode);
    }
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: __props.toolsMode === "emoji"
      }, __props.toolsMode === "emoji" ? {
        b: common_vendor.f(common_vendor.unref(pages_chat_util_emoji.emojiPage), (emoji, k0, i0) => {
          return {
            a: common_vendor.f(emoji, (item, k1, i1) => {
              return {
                a: item,
                b: common_vendor.unref(sheep_index.sheep).$url.cdn(`/static/img/chat/emoji/${item.file}`),
                c: common_vendor.o(($event) => onEmoji(item), item)
              };
            }),
            b: emoji
          };
        })
      } : {
        c: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/chat/image.png"),
        d: common_vendor.o(($event) => imageSelect({
          type: "image",
          data: $event
        }), "42"),
        e: common_vendor.p({
          ["file-mediatype"]: "image",
          imageStyles: {
            width: 50,
            height: 50,
            border: false
          }
        }),
        f: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/chat/goods.png"),
        g: common_vendor.o(($event) => onShowSelect("goods"), "9a"),
        h: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/chat/order.png"),
        i: common_vendor.o(($event) => onShowSelect("order"), "df")
      }, {
        j: common_vendor.o(handleClose, "2b"),
        k: common_vendor.p({
          show: __props.showTools
        })
      });
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-72742241"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/chat/components/toolsPopup.js.map
