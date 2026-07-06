"use strict";
const common_vendor = require("../../../../common/vendor.js");
const sheep_index = require("../../../../sheep/index.js");
if (!Array) {
  const _easycom_su_fixed2 = common_vendor.resolveComponent("su-fixed");
  _easycom_su_fixed2();
}
const _easycom_su_fixed = () => "../../../../sheep/ui/su-fixed/su-fixed.js";
if (!Math) {
  _easycom_su_fixed();
}
const _sfc_main = {
  __name: "detail-activity-tip",
  props: {
    activityList: {
      type: Array,
      default() {
        return [];
      }
    }
  },
  setup(__props) {
    common_vendor.useCssVars((_ctx) => ({
      "b2b2d866": common_vendor.unref(seckillBg),
      "f3c02508": common_vendor.unref(grouponBg)
    }));
    const seckillBg = sheep_index.sheep.$url.css("/static/img/shop/goods/seckill-tip-bg.png");
    const grouponBg = sheep_index.sheep.$url.css("/static/img/shop/goods/groupon-tip-bg.png");
    const props = __props;
    function onActivity(activity) {
      const type = activity.type;
      const typePath = type === 1 ? "seckill" : type === 3 ? "groupon" : void 0;
      if (!typePath) {
        return;
      }
      sheep_index.sheep.$router.go(`/pages/goods/${typePath}`, {
        id: activity.id
      });
    }
    return (_ctx, _cache) => {
      return {
        a: common_vendor.f(props.activityList, (activity, k0, i0) => {
          return common_vendor.e({
            a: activity.type === 1
          }, activity.type === 1 ? {
            b: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/goods/seckill-icon.png")
          } : activity.type === 3 ? {
            d: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/goods/groupon-icon.png")
          } : {}, {
            c: activity.type === 3,
            e: common_vendor.t(activity.name),
            f: common_vendor.o(($event) => onActivity(activity), activity.id),
            g: common_vendor.n(activity.type === 1 ? "seckill-box" : "groupon-box"),
            h: activity.id
          });
        }),
        b: common_vendor.s(_ctx.__cssVars()),
        c: common_vendor.p({
          bottom: true,
          placeholder: true,
          val: 44
        })
      };
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-8e741228"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../../.sourcemap/mp-weixin/pages/goods/components/detail/detail-activity-tip.js.map
