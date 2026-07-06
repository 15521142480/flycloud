"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../index.js");
const _sfc_main = {
  __name: "su-fixed",
  props: {
    noNav: {
      type: Boolean,
      default: false
    },
    bottom: {
      type: Boolean,
      default: false
    },
    bg: {
      type: String,
      default: ""
    },
    bgStyles: {
      type: Object,
      default() {
      }
    },
    val: {
      type: Number,
      default: 0
    },
    width: {
      type: [String, Number],
      default: 0
    },
    alway: {
      type: Boolean,
      default: true
    },
    opacity: {
      type: Boolean,
      default: false
    },
    index: {
      type: [Number, String],
      default: 0
    },
    placeholder: {
      type: [Boolean],
      default: false
    },
    sticky: {
      type: [Boolean],
      default: false
    },
    noFixed: {
      type: Boolean,
      default: false
    },
    ui: {
      type: String,
      default: ""
    },
    clickTo: {
      type: Boolean,
      default: false
    },
    //是否需要安全区
    isInset: {
      type: Boolean,
      default: true
    }
  },
  setup(__props) {
    const { safeAreaInsets } = sheep_index.sheep.$platform.device;
    const vm = common_vendor.getCurrentInstance();
    const uuid = sheep_index.sheep.$helper.guid();
    const sys_navBar = sheep_index.sheep.$platform.navbar;
    const state = common_vendor.reactive({
      content: {},
      fixed: true,
      scrollTop: 0,
      opacityVal: 0
    });
    const insetHeight = common_vendor.computed(() => {
      if (state.fixed && props.bottom) {
        if (props.isInset) {
          return props.val + "px";
        } else {
          return props.val + safeAreaInsets.bottom + "px";
        }
      } else {
        return "auto";
      }
    });
    const props = __props;
    state.fixed = !common_vendor.unref(props.sticky);
    common_vendor.onPageScroll((e) => {
      let top = e.scrollTop;
      state.scrollTop = top;
      state.opacityVal = top > sheep_index.sheep.$platform.navbar ? 1 : top * 0.01;
    });
    common_vendor.onMounted(() => {
      common_vendor.nextTick$1(() => {
        computedQuery();
      });
    });
    const computedQuery = () => {
      common_vendor.index.createSelectorQuery().in(vm).select(`#fixed-${uuid}`).boundingClientRect((data) => {
        if (data != null) {
          state.content = data;
          if (common_vendor.unref(props.sticky)) {
            setFixed(state.scrollTop);
          }
        }
      }).exec();
    };
    const setFixed = (value) => {
      if (common_vendor.unref(props.bottom)) {
        state.fixed = value >= state.content.bottom - sheep_index.sheep.$platform.device.windowHeight + state.content.height + common_vendor.unref(props.val);
      } else {
        state.fixed = value >= state.content.top - (common_vendor.unref(props.noNav) ? common_vendor.unref(props.val) : common_vendor.unref(props.val) + sheep_index.sheep.$platform.navbar);
      }
    };
    const toTop = () => {
      if (props.hasToTop) {
        common_vendor.index.pageScrollTo({
          scrollTop: state.content.top,
          duration: 100
        });
      }
    };
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.unref(safeAreaInsets).bottom && __props.bottom && __props.isInset
      }, common_vendor.unref(safeAreaInsets).bottom && __props.bottom && __props.isInset ? {
        b: common_vendor.s({
          height: common_vendor.unref(safeAreaInsets).bottom + "px"
        })
      } : {}, {
        c: common_vendor.o(toTop, "bb"),
        d: common_vendor.s({
          zIndex: __props.index + common_vendor.unref(sheep_index.sheep).$zIndex.navbar
        }),
        e: __props.bottom
      }, __props.bottom ? {
        f: common_vendor.n(__props.bg)
      } : {}, {
        g: common_vendor.n(__props.ui),
        h: common_vendor.n(__props.bg),
        i: common_vendor.s({
          zIndex: __props.index + common_vendor.unref(sheep_index.sheep).$zIndex.navbar - 1
        }),
        j: common_vendor.s(__props.bgStyles),
        k: common_vendor.s(__props.opacity ? {
          opacity: state.opacityVal
        } : ""),
        l: `fixed-${common_vendor.unref(uuid)}`,
        m: common_vendor.n({
          fixed: state.fixed
        }),
        n: common_vendor.s({
          left: __props.sticky ? "auto" : "0px",
          top: state.fixed && !__props.bottom ? (__props.noNav ? __props.val : __props.val + common_vendor.unref(sys_navBar)) + "px" : "auto",
          bottom: insetHeight.value,
          zIndex: __props.index + common_vendor.unref(sheep_index.sheep).$zIndex.navbar
        }),
        o: common_vendor.s(!__props.alway ? {
          opacity: state.opacityVal
        } : ""),
        p: __props.sticky ? state.fixed : __props.placeholder && state.fixed
      }, (__props.sticky ? state.fixed : __props.placeholder && state.fixed) ? {
        q: common_vendor.s({
          height: state.content.height + "px",
          width: __props.width + "px"
        })
      } : {});
    };
  }
};
wx.createComponent(_sfc_main);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/ui/su-fixed/su-fixed.js.map
