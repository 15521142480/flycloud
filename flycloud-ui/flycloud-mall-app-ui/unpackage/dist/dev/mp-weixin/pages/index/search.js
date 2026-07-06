"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_index = require("../../sheep/index.js");
if (!Array) {
  const _easycom_uni_search_bar2 = common_vendor.resolveComponent("uni-search-bar");
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  (_easycom_uni_search_bar2 + _easycom_s_layout2)();
}
const _easycom_uni_search_bar = () => "../../uni_modules/uni-search-bar/components/uni-search-bar/uni-search-bar.js";
const _easycom_s_layout = () => "../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  (_easycom_uni_search_bar + _easycom_s_layout)();
}
const _sfc_main = {
  __name: "search",
  setup(__props) {
    const state = common_vendor.reactive({
      historyList: []
    });
    function onSearch(keyword) {
      if (!keyword) {
        return;
      }
      saveSearchHistory(keyword);
      sheep_index.sheep.$router.go("/pages/goods/list", { keyword });
    }
    function saveSearchHistory(keyword) {
      if (state.historyList.includes(keyword)) {
        state.historyList.splice(state.historyList.indexOf(keyword), 1);
      }
      state.historyList.unshift(keyword);
      if (state.historyList.length >= 10) {
        state.historyList.length = 10;
      }
      common_vendor.index.setStorageSync("searchHistory", state.historyList);
    }
    function onDelete() {
      common_vendor.index.showModal({
        title: "提示",
        content: "确认清除搜索历史吗？",
        success: function(res) {
          if (res.confirm) {
            state.historyTag = [];
            common_vendor.index.removeStorageSync("searchHistory");
          }
        }
      });
    }
    common_vendor.onLoad(() => {
      state.historyList = common_vendor.index.getStorageSync("searchHistory") || [];
    });
    return (_ctx, _cache) => {
      return {
        a: common_vendor.o(($event) => onSearch($event.value), "4c"),
        b: common_vendor.p({
          radius: "33",
          placeholder: "请输入关键字",
          cancelButton: "none",
          focus: true
        }),
        c: common_vendor.o(onDelete, "8d"),
        d: common_vendor.f(state.historyList, (item, index, i0) => {
          return {
            a: common_vendor.t(item),
            b: common_vendor.o(($event) => onSearch(item), index),
            c: index
          };
        }),
        e: common_vendor.p({
          bgStyle: {
            color: "#FFF"
          },
          title: "搜索"
        })
      };
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-5aac7367"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/index/search.js.map
