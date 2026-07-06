"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../index.js");
const sheep_hooks_useModal = require("../../hooks/useModal.js");
if (!Math) {
  (NavbarItem + Navbar)();
}
const Navbar = () => "./components/navbar.js";
const NavbarItem = () => "./components/navbar-item.js";
const _sfc_main = {
  __name: "s-custom-navbar",
  props: {
    data: {
      type: Object,
      default: () => ({})
    },
    showLeftButton: {
      type: Boolean,
      default: false
    }
  },
  setup(__props) {
    const props = __props;
    const hasHistory = sheep_index.sheep.$router.hasHistory();
    const sticky = common_vendor.computed(() => {
      if (props.data.styleType === "inner") {
        if (props.data.alwaysShow) {
          return false;
        }
      }
      if (props.data.styleType === "normal") {
        return false;
      }
    });
    const navList = common_vendor.computed(() => {
      return props.data.mpCells || [];
    });
    const windowWidth = sheep_index.sheep.$platform.device.windowWidth;
    const cell = common_vendor.computed(() => {
      if (common_vendor.unref(navList).length) {
        let cell2 = (windowWidth - 90) / 8;
        cell2 = (windowWidth - 80 - common_vendor.unref(sheep_index.sheep.$platform.capsule).width) / 6;
        return cell2;
      }
    });
    const parseImgStyle = (item) => {
      let obj = {
        width: item.width * cell.value + (item.width - 1) * 10 + "px",
        left: item.left * cell.value + (item.left + 1) * 10 + "px",
        "border-radius": item.borderRadius + "px"
      };
      return obj;
    };
    const isAlways = common_vendor.computed(
      () => props.data.styleType === "inner" ? Boolean(props.data.alwaysShow) : true
    );
    const isOpacity = common_vendor.computed(
      () => props.data.styleType === "normal" ? false : props.showLeftButton ? false : props.data.styleType === "inner"
    );
    const isPlaceholder = common_vendor.computed(() => props.data.styleType === "normal");
    const bgStyles = common_vendor.computed(() => {
      return {
        background: props.data.bgType === "img" && props.data.bgImg ? `url(${sheep_index.sheep.$url.cdn(props.data.bgImg)}) no-repeat top center / 100% 100%` : props.data.bgColor
      };
    });
    function onClickLeft() {
      if (hasHistory) {
        sheep_index.sheep.$router.back();
      } else {
        sheep_index.sheep.$router.go("/pages/index/index");
      }
    }
    function onClickRight() {
      sheep_hooks_useModal.showMenuTools();
    }
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: __props.showLeftButton
      }, __props.showLeftButton ? common_vendor.e({
        b: common_vendor.unref(hasHistory)
      }, common_vendor.unref(hasHistory) ? {} : {}, {
        c: common_vendor.o(onClickLeft, "ac"),
        d: common_vendor.o(onClickRight, "bb"),
        e: __props.data.styleType === "inner" ? 1 : ""
      }) : {}, {
        f: common_vendor.f(navList.value, (item, index, i0) => {
          return {
            a: "6417c6b5-1-" + i0 + ",6417c6b5-0",
            b: common_vendor.p({
              data: item,
              width: parseImgStyle(item).width
            }),
            c: index,
            d: common_vendor.s(parseImgStyle(item)),
            e: common_vendor.n({
              "ss-flex ss-col-center ss-row-center": item.type !== "search"
            })
          };
        }),
        g: common_vendor.p({
          alway: isAlways.value,
          back: false,
          bg: "",
          placeholder: isPlaceholder.value,
          bgStyles: bgStyles.value,
          opacity: isOpacity.value,
          sticky: sticky.value
        })
      });
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-6417c6b5"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/components/s-custom-navbar/s-custom-navbar.js.map
