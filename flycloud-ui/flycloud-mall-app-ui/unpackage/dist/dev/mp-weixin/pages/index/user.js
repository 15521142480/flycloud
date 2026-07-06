"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_index = require("../../sheep/index.js");
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
  __name: "user",
  setup(__props) {
    common_vendor.index.hideTabBar({
      fail: () => {
      }
    });
    const template = common_vendor.computed(() => sheep_index.sheep.$store("app").template.user);
    common_vendor.onShow(() => {
      sheep_index.sheep.$store("user").updateUserData();
    });
    common_vendor.onPullDownRefresh(() => {
      sheep_index.sheep.$store("user").updateUserData();
      setTimeout(function() {
        common_vendor.index.stopPullDownRefresh();
      }, 800);
    });
    common_vendor.onPageScroll(() => {
    });
    return (_ctx, _cache) => {
      return {
        a: common_vendor.f(template.value.components, (item, index, i0) => {
          return {
            a: "905907d8-2-" + i0 + "," + ("905907d8-1-" + i0),
            b: common_vendor.p({
              type: item.id,
              data: item.property,
              styles: item.property.style
            }),
            c: index,
            d: "905907d8-1-" + i0 + ",905907d8-0",
            e: common_vendor.p({
              styles: item.property.style
            })
          };
        }),
        b: common_vendor.p({
          title: "我的",
          tabbar: "/pages/index/user",
          navbar: "custom",
          bgStyle: template.value.page,
          navbarStyle: template.value.navigationBar,
          onShareAppMessage: true
        })
      };
    };
  }
};
_sfc_main.__runtimeHooks = 3;
wx.createPage(_sfc_main);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/index/user.js.map
