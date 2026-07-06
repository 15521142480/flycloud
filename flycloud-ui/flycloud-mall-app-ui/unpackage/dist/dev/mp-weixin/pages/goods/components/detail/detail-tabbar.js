"use strict";
const common_vendor = require("../../../../common/vendor.js");
const sheep_index = require("../../../../sheep/index.js");
const sheep_hooks_useModal = require("../../../../sheep/hooks/useModal.js");
const sheep_api_product_favorite = require("../../../../sheep/api/product/favorite.js");
if (!Array) {
  const _easycom_su_fixed2 = common_vendor.resolveComponent("su-fixed");
  _easycom_su_fixed2();
}
const _easycom_su_fixed = () => "../../../../sheep/ui/su-fixed/su-fixed.js";
if (!Math) {
  _easycom_su_fixed();
}
const _sfc_main = {
  __name: "detail-tabbar",
  props: {
    modelValue: {
      type: Object,
      default() {
      }
    },
    bg: {
      type: String,
      default: "bg-white"
    },
    bgStyles: {
      type: Object,
      default() {
      }
    },
    ui: {
      type: String,
      default: ""
    },
    noFixed: {
      type: Boolean,
      default: false
    },
    topRadius: {
      type: Number,
      default: 0
    },
    collectIcon: {
      type: Boolean,
      default: true
    },
    serviceIcon: {
      type: Boolean,
      default: true
    },
    shareIcon: {
      type: Boolean,
      default: true
    }
  },
  setup(__props) {
    common_vendor.reactive({});
    const props = __props;
    async function onFavorite() {
      if (props.modelValue.favorite) {
        const { code } = await sheep_api_product_favorite.FavoriteApi.deleteFavorite(props.modelValue.id);
        if (code !== 0) {
          return;
        }
        sheep_index.sheep.$helper.toast("取消收藏");
        props.modelValue.favorite = false;
      } else {
        const { code } = await sheep_api_product_favorite.FavoriteApi.createFavorite(props.modelValue.id);
        if (code !== 0) {
          return;
        }
        sheep_index.sheep.$helper.toast("收藏成功");
        props.modelValue.favorite = true;
      }
    }
    const onChat = () => {
      sheep_index.sheep.$router.go("/pages/chat/index", {
        id: props.modelValue.id
      });
    };
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: __props.collectIcon
      }, __props.collectIcon ? common_vendor.e({
        b: __props.modelValue.favorite
      }, __props.modelValue.favorite ? {
        c: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/goods/collect_1.gif")
      } : {
        d: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/goods/collect_0.png")
      }, {
        e: common_vendor.o(onFavorite, "3d")
      }) : {}, {
        f: __props.serviceIcon
      }, __props.serviceIcon ? {
        g: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/goods/message.png"),
        h: common_vendor.o(onChat, "60")
      } : {}, {
        i: __props.shareIcon
      }, __props.shareIcon ? {
        j: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/goods/share.png"),
        k: common_vendor.o((...args) => common_vendor.unref(sheep_hooks_useModal.showShareModal) && common_vendor.unref(sheep_hooks_useModal.showShareModal)(...args), "42")
      } : {}, {
        l: common_vendor.p({
          bottom: true,
          placeholder: true,
          bg: "bg-white"
        })
      });
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-4c75f2f1"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../../.sourcemap/mp-weixin/pages/goods/components/detail/detail-tabbar.js.map
