"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_index = require("../../sheep/index.js");
const sheep_platform_share = require("../../sheep/platform/share.js");
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
  __name: "index",
  setup(__props) {
    common_vendor.index.hideTabBar({
      fail: () => {
      }
    });
    const template = common_vendor.computed(() => {
      var _a;
      return (_a = sheep_index.sheep.$store("app").template) == null ? void 0 : _a.home;
    });
    common_vendor.onLoad((options) => {
      if (options.scene) {
        const sceneParams = decodeURIComponent(options.scene).split("=");
        common_vendor.index.__f__("log", "at pages/index/index.vue:61", "sceneParams=>", sceneParams);
        options[sceneParams[0]] = sceneParams[1];
      }
      if (options.templateId) {
        sheep_index.sheep.$store("app").init(options.templateId);
      }
      if (options.spm) {
        sheep_platform_share.$share.decryptSpm(options.spm);
      }
      if (options.page) {
        sheep_index.sheep.$router.go(decodeURIComponent(options.page));
      }
    });
    common_vendor.onShow(async () => {
    });
    common_vendor.onPullDownRefresh(() => {
      sheep_index.sheep.$store("app").init();
      setTimeout(function() {
        common_vendor.index.stopPullDownRefresh();
      }, 800);
    });
    common_vendor.onPageScroll(() => {
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: template.value
      }, template.value ? {
        b: common_vendor.f(template.value.components, (item, index, i0) => {
          return {
            a: "00ddcd59-2-" + i0 + "," + ("00ddcd59-1-" + i0),
            b: common_vendor.p({
              type: item.id,
              data: item.property,
              styles: item.property.style
            }),
            c: index,
            d: "00ddcd59-1-" + i0 + ",00ddcd59-0",
            e: common_vendor.p({
              styles: item.property.style
            })
          };
        }),
        c: common_vendor.p({
          title: "首页",
          navbar: "custom",
          tabbar: "/pages/index/index",
          bgStyle: template.value.page,
          navbarStyle: template.value.navigationBar,
          onShareAppMessage: true
        })
      } : {});
    };
  }
};
_sfc_main.__runtimeHooks = 3;
wx.createPage(_sfc_main);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/index/index.js.map
