"use strict";
const common_vendor = require("../../../../common/vendor.js");
const sheep_index = require("../../../../sheep/index.js");
const sheep_helper_throttle = require("../../../../sheep/helper/throttle.js");
const sheep_hooks_useModal = require("../../../../sheep/hooks/useModal.js");
if (!Array) {
  const _easycom_su_status_bar2 = common_vendor.resolveComponent("su-status-bar");
  const _easycom_su_fixed2 = common_vendor.resolveComponent("su-fixed");
  (_easycom_su_status_bar2 + _easycom_su_fixed2)();
}
const _easycom_su_status_bar = () => "../../../../sheep/ui/su-status-bar/su-status-bar.js";
const _easycom_su_fixed = () => "../../../../sheep/ui/su-fixed/su-fixed.js";
if (!Math) {
  (_easycom_su_status_bar + _easycom_su_fixed)();
}
const _sfc_main = {
  __name: "detail-navbar",
  emits: ["clickLeft"],
  setup(__props, { emit: __emit }) {
    const sys_statusBar = sheep_index.sheep.$platform.device.statusBarHeight;
    const sys_navBar = sheep_index.sheep.$platform.navbar;
    const capsuleStyle = {
      width: sheep_index.sheep.$platform.capsule.width + "px",
      height: sheep_index.sheep.$platform.capsule.height + "px"
    };
    const state = common_vendor.reactive({
      tabOpacityVal: 0,
      curTab: "goods",
      tabList: [
        {
          label: "商品",
          value: "goods",
          to: "detail-swiper-selector"
        },
        {
          label: "评价",
          value: "comment",
          to: "detail-comment-selector"
        },
        {
          label: "详情",
          value: "detail",
          to: "detail-content-selector"
        }
      ]
    });
    const emits = __emit;
    const hasHistory = sheep_index.sheep.$router.hasHistory();
    function onClickLeft() {
      if (hasHistory) {
        sheep_index.sheep.$router.back();
      } else {
        sheep_index.sheep.$router.go("/pages/index/index");
      }
      emits("clickLeft");
    }
    function onClickRight() {
      sheep_hooks_useModal.showMenuTools();
    }
    let commentCard = {
      top: 0,
      bottom: 0
    };
    function getCommentCardNode() {
      return new Promise((res, rej) => {
        common_vendor.index.createSelectorQuery().select(".detail-comment-selector").boundingClientRect((data) => {
          if (data) {
            commentCard.top = data.top;
            commentCard.bottom = data.top + data.height;
            res(data);
          } else {
            res(null);
          }
        }).exec();
      });
    }
    function onTab(tab) {
      let scrollTop = 0;
      if (tab.value === "comment") {
        scrollTop = commentCard.top - sys_navBar + 1;
      } else if (tab.value === "detail") {
        scrollTop = commentCard.bottom - sys_navBar + 1;
      }
      common_vendor.index.pageScrollTo({
        scrollTop,
        duration: 200
      });
    }
    common_vendor.onPageScroll((e) => {
      state.tabOpacityVal = e.scrollTop > sheep_index.sheep.$platform.navbar ? 1 : e.scrollTop * 0.01;
      if (commentCard.top === 0) {
        sheep_helper_throttle.throttle(() => {
          getCommentCardNode();
        }, 50);
      }
      if (e.scrollTop < commentCard.top - sys_navBar) {
        state.curTab = "goods";
      } else if (e.scrollTop >= commentCard.top - sys_navBar && e.scrollTop <= commentCard.bottom - sys_navBar) {
        state.curTab = "comment";
      } else {
        state.curTab = "detail";
      }
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.unref(hasHistory)
      }, common_vendor.unref(hasHistory) ? {} : {}, {
        b: common_vendor.o(onClickLeft, "80"),
        c: common_vendor.o(onClickRight, "6a"),
        d: common_vendor.f(state.tabList, (item, k0, i0) => {
          return {
            a: common_vendor.t(item.label),
            b: common_vendor.n(state.curTab === item.value ? "cur-tab-title" : ""),
            c: state.curTab === item.value,
            d: item.value,
            e: common_vendor.o(($event) => onTab(item), item.value)
          };
        }),
        e: common_vendor.s({
          opacity: state.tabOpacityVal
        }),
        f: common_vendor.s(capsuleStyle),
        g: common_vendor.n({
          "ss-p-x-20": common_vendor.unref(sheep_index.sheep).$platform.provider !== "alipay"
        }),
        h: common_vendor.s({
          height: common_vendor.unref(sys_navBar) - common_vendor.unref(sys_statusBar) + "px"
        }),
        i: common_vendor.p({
          alway: true,
          bgStyles: {
            background: "#fff"
          },
          val: 0,
          noNav: true,
          opacity: true,
          placeholder: false
        })
      });
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-3e9ea950"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../../.sourcemap/mp-weixin/pages/goods/components/detail/detail-navbar.js.map
