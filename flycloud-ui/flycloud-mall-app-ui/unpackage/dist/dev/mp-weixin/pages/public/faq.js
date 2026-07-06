"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_index = require("../../sheep/index.js");
if (!Array) {
  const _easycom_uni_collapse_item2 = common_vendor.resolveComponent("uni-collapse-item");
  const _easycom_uni_collapse2 = common_vendor.resolveComponent("uni-collapse");
  const _easycom_s_empty2 = common_vendor.resolveComponent("s-empty");
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  (_easycom_uni_collapse_item2 + _easycom_uni_collapse2 + _easycom_s_empty2 + _easycom_s_layout2)();
}
const _easycom_uni_collapse_item = () => "../../uni_modules/uni-collapse/components/uni-collapse-item/uni-collapse-item.js";
const _easycom_uni_collapse = () => "../../uni_modules/uni-collapse/components/uni-collapse/uni-collapse.js";
const _easycom_s_empty = () => "../../sheep/components/s-empty/s-empty.js";
const _easycom_s_layout = () => "../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  (_easycom_uni_collapse_item + _easycom_uni_collapse + _easycom_s_empty + _easycom_s_layout)();
}
const _sfc_main = {
  __name: "faq",
  setup(__props) {
    const state = common_vendor.reactive({
      list: [],
      loading: true
    });
    common_vendor.onLoad(() => {
      {
        sheep_index.sheep.$router.go("/pages/public/richtext", {
          title: "常见问题"
        });
        return;
      }
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.f(state.list, (item, index, i0) => {
          return {
            a: common_vendor.t(index + 1 < 10 ? "0" + (index + 1) : index + 1),
            b: common_vendor.t(item.title),
            c: common_vendor.t(item.content),
            d: item,
            e: "b3a79332-2-" + i0 + ",b3a79332-1"
          };
        }),
        b: state.list.length === 0 && !state.loading
      }, state.list.length === 0 && !state.loading ? {
        c: common_vendor.p({
          text: "暂无常见问题",
          icon: "/static/collect-empty.png"
        })
      } : {}, {
        d: common_vendor.p({
          bgStyle: {
            color: "#FFF"
          },
          title: "常见问题"
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-b3a79332"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/public/faq.js.map
