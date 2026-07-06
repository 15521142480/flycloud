"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../index.js");
const sheep_hooks_useModal = require("../../hooks/useModal.js");
const _sfc_main = {
  __name: "s-user-card",
  props: {
    // 装修数据
    data: {
      type: Object,
      default: () => ({})
    },
    // 装修样式
    styles: {
      type: Object,
      default: () => ({})
    },
    // 头像
    avatar: {
      type: String,
      default: ""
    },
    nickname: {
      type: String,
      default: "请先登录"
    },
    vip: {
      type: [String, Number],
      default: "1"
    },
    collectNum: {
      type: [String, Number],
      default: "1"
    },
    likeNum: {
      type: [String, Number],
      default: "1"
    }
  },
  setup(__props) {
    const userInfo = common_vendor.computed(() => sheep_index.sheep.$store("user").userInfo);
    const isLogin = common_vendor.computed(() => sheep_index.sheep.$store("user").isLogin);
    const props = __props;
    const bgStyle = common_vendor.computed(() => {
      const { bgType, bgImg, bgColor } = props.styles;
      return {
        background: bgType === "img" ? `url(${bgImg}) no-repeat top center / 100% 100%` : bgColor
      };
    });
    function onBind() {
      sheep_hooks_useModal.showAuthModal("changeMobile");
    }
    return (_ctx, _cache) => {
      var _a;
      return common_vendor.e({
        a: isLogin.value && userInfo.value.avatar ? common_vendor.unref(sheep_index.sheep).$url.cdn(userInfo.value.avatar) : common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/default_avatar.png"),
        b: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/user/info"), "24"),
        c: common_vendor.t(((_a = userInfo.value) == null ? void 0 : _a.nickname) || __props.nickname),
        d: common_vendor.o((...args) => common_vendor.unref(sheep_hooks_useModal.showShareModal) && common_vendor.unref(sheep_hooks_useModal.showShareModal)(...args), "3d"),
        e: isLogin.value && !userInfo.value.mobile
      }, isLogin.value && !userInfo.value.mobile ? {
        f: common_vendor.o(onBind, "e4")
      } : {}, {
        g: common_vendor.s(bgStyle.value),
        h: common_vendor.s({
          marginLeft: `${__props.data.space}px`
        })
      });
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-e8581682"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/components/s-user-card/s-user-card.js.map
