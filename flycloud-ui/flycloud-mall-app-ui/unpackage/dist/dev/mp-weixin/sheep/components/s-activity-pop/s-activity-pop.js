"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../index.js");
const sheep_hooks_useGoods = require("../../hooks/useGoods.js");
if (!Array) {
  const _easycom_su_popup2 = common_vendor.resolveComponent("su-popup");
  _easycom_su_popup2();
}
const _easycom_su_popup = () => "../../ui/su-popup/su-popup.js";
if (!Math) {
  _easycom_su_popup();
}
const _sfc_main = {
  __name: "s-activity-pop",
  props: {
    modelValue: {
      type: Object,
      default() {
      }
    },
    show: {
      type: Boolean,
      default: false
    }
  },
  emits: ["close"],
  setup(__props, { emit: __emit }) {
    const props = __props;
    const emits = __emit;
    const state = common_vendor.reactive({
      rewardActivity: common_vendor.computed(() => props.modelValue.rewardActivity),
      couponInfo: common_vendor.computed(() => props.modelValue.couponInfo)
    });
    const getBuy = (id) => {
      emits("get", id);
    };
    function onGoodsList(e) {
      sheep_index.sheep.$router.go("/pages/activity/index", {
        activityId: e.id
      });
    }
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: state.rewardActivity && state.rewardActivity.id > 0
      }, state.rewardActivity && state.rewardActivity.id > 0 ? {
        b: common_vendor.f(common_vendor.unref(sheep_hooks_useGoods.getRewardActivityRuleGroupDescriptions)(state.rewardActivity), (item, index, i0) => {
          return {
            a: common_vendor.t(item.name),
            b: common_vendor.t(item.values.join(";")),
            c: common_vendor.o(($event) => onGoodsList(state.rewardActivity), index),
            d: index
          };
        }),
        c: common_vendor.t(common_vendor.unref(sheep_index.sheep).$helper.timeFormat(state.rewardActivity.startTime, "yyyy.mm.dd")),
        d: common_vendor.t(common_vendor.unref(sheep_index.sheep).$helper.timeFormat(state.rewardActivity.endTime, "yyyy.mm.dd"))
      } : {}, {
        e: state.couponInfo.length
      }, state.couponInfo.length ? {
        f: common_vendor.f(state.couponInfo, (item, k0, i0) => {
          return common_vendor.e({
            a: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(item.discountPrice)),
            b: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(item.usePrice)),
            c: common_vendor.t(item.name),
            d: common_vendor.t(item.validityType == 1 ? common_vendor.unref(sheep_index.sheep).$helper.timeFormat(item.validStartTime, "yyyy-mm-dd") + "-" + common_vendor.unref(sheep_index.sheep).$helper.timeFormat(item.validEndTime, "yyyy-mm-dd") : "领取后" + item.fixedStartTerm + "-" + item.fixedEndTerm + "天可用"),
            e: item.canTake
          }, item.canTake ? {
            f: common_vendor.o(($event) => getBuy(item.id), item.id)
          } : {}, {
            g: item.id
          });
        })
      } : {}, {
        g: common_vendor.o(($event) => emits("close"), "fa"),
        h: common_vendor.p({
          show: __props.show,
          type: "bottom",
          round: "20",
          showClose: true
        })
      });
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-c5b2a531"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/components/s-activity-pop/s-activity-pop.js.map
