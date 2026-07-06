"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../index.js");
if (!Array) {
  const _easycom_uni_search_bar2 = common_vendor.resolveComponent("uni-search-bar");
  _easycom_uni_search_bar2();
}
const _easycom_uni_search_bar = () => "../../../uni_modules/uni-search-bar/components/uni-search-bar/uni-search-bar.js";
if (!Math) {
  _easycom_uni_search_bar();
}
const _sfc_main = {
  __name: "s-search-block",
  props: {
    data: {
      type: Object,
      default: () => ({})
    },
    // 输入框背景色
    elBackground: {
      type: String,
      default: ""
    },
    height: {
      type: Number,
      default: 36
    },
    // 图标颜色
    iconColor: {
      type: String,
      default: "#b0b3bf"
    },
    // 字体颜色
    fontColor: {
      type: String,
      default: "#b0b3bf"
    },
    // placeholder
    placeholder: {
      type: String,
      default: "这是一个搜索框"
    },
    // placeholder位置（left | center）
    placeholderPosition: {
      type: String,
      default: "left"
    },
    // 是否显示扫一扫图标
    showScan: {
      type: Boolean,
      default: false
    },
    radius: {
      type: Number,
      default: 10
    },
    width: {
      type: String,
      default: "100%"
    },
    navbar: {
      type: Boolean,
      default: true
    }
  },
  emits: ["click"],
  setup(__props, { emit: __emit }) {
    const state = common_vendor.reactive({
      searchVal: ""
    });
    const emits = __emit;
    const click = () => {
      emits("click");
    };
    function onSearch(e) {
      if (e.value) {
        sheep_index.sheep.$router.go("/pages/goods/list", { keyword: e.value });
        setTimeout(() => {
          state.searchVal = "";
        }, 100);
      }
    }
    function onScan() {
      common_vendor.index.scanCode({
        onlyFromCamera: false,
        sound: "default",
        scanType: ["qrCode", "barCode"],
        success: (res) => {
          showScanResult(res.result);
        },
        fail: (err) => {
          common_vendor.index.__f__("error", "at sheep/components/s-search-block/s-search-block.vue:178", err);
          common_vendor.index.showToast({
            title: err.errMsg === "scanCode:fail cancel" ? "操作已取消" : "扫码失败",
            icon: "error"
          });
        }
      });
    }
    function isValidUrl(str) {
      try {
        const url = str.trim();
        return (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("ftp://")) && // 简单长度验证
        url.length >= 10;
      } catch {
        return false;
      }
    }
    function showScanResult(text) {
      const isUrl = isValidUrl(text);
      common_vendor.index.showModal({
        title: "扫描结果",
        content: text,
        confirmText: isUrl ? "访问" : "复制",
        cancelText: "取消",
        success: (res) => {
          if (res.confirm) {
            if (isUrl) {
              handleUrl(text);
            } else {
              handleCopy(text);
            }
          }
        }
      });
    }
    function handleUrl(url) {
      common_vendor.index.navigateTo({
        url: `/pages/public/webview?url=${encodeURIComponent(url)}`
      });
    }
    function handleCopy(text) {
      common_vendor.index.setClipboardData({
        data: text,
        success: () => {
          common_vendor.index.showToast({
            title: "已复制到剪贴板",
            icon: "success"
          });
        },
        fail: () => {
          common_vendor.index.showToast({
            title: "复制失败，请重试",
            icon: "error"
          });
        }
      });
    }
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: __props.navbar
      }, __props.navbar ? common_vendor.e({
        b: __props.fontColor,
        c: common_vendor.t(__props.placeholder),
        d: __props.fontColor,
        e: __props.showScan
      }, __props.showScan ? {
        f: __props.fontColor,
        g: common_vendor.o(onScan, "fa")
      } : {}, {
        h: common_vendor.n(__props.placeholderPosition === "center" ? "ss-row-center" : "ss-row-left")
      }) : {}, {
        i: !__props.navbar
      }, !__props.navbar ? {
        j: common_vendor.o(onSearch, "b0"),
        k: common_vendor.o(($event) => state.searchVal = $event, "46"),
        l: common_vendor.p({
          radius: __props.data.borderRadius,
          placeholder: __props.data.placeholder,
          cancelButton: "none",
          clearButton: "none",
          modelValue: state.searchVal
        })
      } : {}, {
        m: common_vendor.f(__props.data.hotKeywords, (item, index, i0) => {
          return {
            a: common_vendor.t(item),
            b: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/goods/list", {
              keyword: item
            }), index),
            c: index
          };
        }),
        n: common_vendor.s({
          color: __props.data.textColor
        }),
        o: __props.data.hotKeywords && __props.data.hotKeywords.length && __props.navbar
      }, __props.data.hotKeywords && __props.data.hotKeywords.length && __props.navbar ? {
        p: common_vendor.f(__props.data.hotKeywords, (item, index, i0) => {
          return {
            a: common_vendor.t(item),
            b: index
          };
        }),
        q: common_vendor.s({
          color: __props.data.textColor,
          marginRight: "10rpx"
        })
      } : {}, {
        r: common_vendor.o(click, "65"),
        s: common_vendor.s({
          borderRadius: __props.radius + "px",
          background: __props.elBackground,
          height: __props.height + "px",
          width: __props.width
        }),
        t: common_vendor.n({
          "border-content": __props.navbar
        })
      });
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-1f3fe5bf"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/components/s-search-block/s-search-block.js.map
