"use strict";
const common_vendor = require("../../../../common/vendor.js");
const sheep_index = require("../../../../sheep/index.js");
const sheep_hooks_useGoods = require("../../../../sheep/hooks/useGoods.js");
const sheep_api_promotion_combination = require("../../../../sheep/api/promotion/combination.js");
const _sfc_main = {
  __name: "groupon-card-list",
  props: {
    modelValue: {
      type: Object,
      default() {
      }
    }
  },
  emits: ["join"],
  setup(__props, { emit: __emit }) {
    const props = __props;
    const state = common_vendor.reactive({
      list: []
    });
    const emits = __emit;
    function onJoinGroupon(record) {
      emits("join", record);
    }
    function endTime(time) {
      const durationTime = sheep_hooks_useGoods.useDurationTime(time);
      if (durationTime.ms <= 0) {
        return "该团已解散";
      }
      let timeText = "剩余 ";
      timeText += `${durationTime.h}时`;
      timeText += `${durationTime.m}分`;
      timeText += `${durationTime.s}秒`;
      return timeText;
    }
    common_vendor.onMounted(async () => {
      const { data } = await sheep_api_promotion_combination.CombinationApi.getHeadCombinationRecordList(props.modelValue.id, 0, 10);
      state.list = data;
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: state.list.length > 0
      }, state.list.length > 0 ? {
        b: common_vendor.t(state.list.length),
        c: common_vendor.f(state.list, (record, index, i0) => {
          return {
            a: common_vendor.unref(sheep_index.sheep).$url.cdn(record.avatar),
            b: common_vendor.t(record.nickname),
            c: common_vendor.t(record.userSize - record.userCount),
            d: common_vendor.t(endTime(record.expireTime)),
            e: common_vendor.o(($event) => onJoinGroupon(record), index),
            f: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/activity/groupon/detail", {
              id: record.id
            }), index),
            g: index
          };
        })
      } : {});
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-2bb0fe2d"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../../.sourcemap/mp-weixin/pages/goods/components/groupon/groupon-card-list.js.map
