"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_api_promotion_diy = require("../../sheep/api/promotion/diy.js");
if (!Array) {
  const _easycom_s_block_item2 = common_vendor.resolveComponent("s-block-item");
  const _easycom_s_block2 = common_vendor.resolveComponent("s-block");
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  (_easycom_s_block_item2 + _easycom_s_block2 + _easycom_s_layout2)();
}
const _easycom_s_block_item = () => "../../sheep/components/s-block-item/s-block-item.js";
const _easycom_s_block = () => "../../sheep/components/s-block/s-block.js";
const _easycom_s_layout = () => "../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  (_easycom_s_block_item + _easycom_s_block + _easycom_s_layout)();
}
const _sfc_main = {
  __name: "page",
  setup(__props) {
    const state = common_vendor.reactive({
      name: "",
      components: [],
      navigationBar: {},
      page: {}
    });
    common_vendor.onLoad(async (options) => {
      var _a, _b, _c;
      let id = options.id;
      if (options.scene) {
        const sceneParams = decodeURIComponent(options.scene).split("=");
        id = sceneParams[1];
      }
      const { code, data } = await sheep_api_promotion_diy.DiyApi.getDiyPage(id);
      if (code === 0) {
        state.name = data.name;
        state.components = (_a = data.property) == null ? void 0 : _a.components;
        state.navigationBar = (_b = data.property) == null ? void 0 : _b.navigationBar;
        state.page = (_c = data.property) == null ? void 0 : _c.page;
      }
    });
    common_vendor.onPageScroll(() => {
    });
    return (_ctx, _cache) => {
      return {
        a: common_vendor.f(state.components, (item, index, i0) => {
          return {
            a: "0a201e50-2-" + i0 + "," + ("0a201e50-1-" + i0),
            b: common_vendor.p({
              type: item.id,
              data: item.property,
              styles: item.property.style
            }),
            c: index,
            d: "0a201e50-1-" + i0 + ",0a201e50-0",
            e: common_vendor.p({
              styles: item.property.style
            })
          };
        }),
        b: common_vendor.p({
          title: state.name,
          navbar: "custom",
          bgStyle: state.page,
          navbarStyle: state.navigationBar,
          onShareAppMessage: true,
          showLeftButton: true
        })
      };
    };
  }
};
_sfc_main.__runtimeHooks = 3;
wx.createPage(_sfc_main);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/index/page.js.map
