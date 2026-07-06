"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../index.js");
if (!Array) {
  const _easycom_uni_list_item2 = common_vendor.resolveComponent("uni-list-item");
  const _easycom_uni_list2 = common_vendor.resolveComponent("uni-list");
  (_easycom_uni_list_item2 + _easycom_uni_list2)();
}
const _easycom_uni_list_item = () => "../../../uni_modules/uni-list/components/uni-list-item/uni-list-item.js";
const _easycom_uni_list = () => "../../../uni_modules/uni-list/components/uni-list/uni-list.js";
if (!Math) {
  (_easycom_uni_list_item + _easycom_uni_list)();
}
const _sfc_main = {
  __name: "s-menu-list",
  props: {
    data: {
      type: Object,
      default: () => ({})
    }
  },
  setup(__props) {
    return (_ctx, _cache) => {
      return {
        a: common_vendor.f(__props.data.list, (item, index, i0) => {
          return common_vendor.e({
            a: item.iconUrl
          }, item.iconUrl ? {
            b: common_vendor.unref(sheep_index.sheep).$url.cdn(item.iconUrl)
          } : {}, {
            c: common_vendor.t(item.title),
            d: common_vendor.s({
              color: item.titleColor
            }),
            e: common_vendor.t(item.subtitle),
            f: common_vendor.s({
              color: item.subtitleColor
            }),
            g: index,
            h: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go(item.url), index),
            i: "ad0f3bde-1-" + i0 + ",ad0f3bde-0"
          });
        }),
        b: common_vendor.p({
          showArrow: true,
          clickable: true
        }),
        c: common_vendor.p({
          border: true
        })
      };
    };
  }
};
wx.createComponent(_sfc_main);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/components/s-menu-list/s-menu-list.js.map
