"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../index.js");
if (!Array) {
  const _easycom_s_live_card2 = common_vendor.resolveComponent("s-live-card");
  _easycom_s_live_card2();
}
const _easycom_s_live_card = () => "../s-live-card/s-live-card.js";
if (!Math) {
  _easycom_s_live_card();
}
const _sfc_main = {
  __name: "s-live-block",
  props: {
    data: {
      type: Object,
      default() {
      }
    },
    styles: {
      type: Object,
      default() {
      }
    }
  },
  setup(__props) {
    const state = common_vendor.reactive({
      liveList: [],
      mpLink: ""
    });
    const props = __props;
    const { mode, goodsFields, mpliveIds } = props.data || {};
    props.styles || {};
    async function getLiveListByIds(ids) {
      const { data } = await sheep_index.sheep.$api.app.mplive.getRoomList(ids);
      return data;
    }
    function goRoom(id) {
      common_vendor.index.navigateTo({
        url: `plugin-private://wx2b03c6e691cd7370/pages/live-player-plugin?room_id=${id}`
      });
    }
    common_vendor.onMounted(async () => {
      state.liveList = await getLiveListByIds(mpliveIds);
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.unref(mode) === 2 && state.liveList.length
      }, common_vendor.unref(mode) === 2 && state.liveList.length ? {
        b: common_vendor.f(state.liveList, (item, k0, i0) => {
          var _a, _b;
          return {
            a: common_vendor.o(($event) => goRoom(item.roomid), item.id),
            b: "74052372-0-" + i0,
            c: common_vendor.p({
              size: "md",
              goodsFields: common_vendor.unref(goodsFields),
              data: item,
              titleColor: (_a = common_vendor.unref(goodsFields).name) == null ? void 0 : _a.color,
              subTitleColor: (_b = common_vendor.unref(goodsFields).anchor_name) == null ? void 0 : _b.color,
              topRadius: __props.data.borderRadiusTop,
              bottomRadius: __props.data.borderRadiusBottom
            }),
            d: item.id
          };
        }),
        c: common_vendor.s({
          padding: __props.data.space + "rpx"
        }),
        d: common_vendor.s({
          margin: "-" + __props.data.space + "rpx"
        })
      } : {}, {
        e: common_vendor.unref(mode) === 1 && state.liveList.length
      }, common_vendor.unref(mode) === 1 && state.liveList.length ? {
        f: common_vendor.f(state.liveList, (item, k0, i0) => {
          var _a;
          return {
            a: common_vendor.o(($event) => goRoom(item.roomid), item.id),
            b: "74052372-1-" + i0,
            c: common_vendor.p({
              size: "sl",
              goodsFields: common_vendor.unref(goodsFields),
              data: item,
              titleColor: (_a = common_vendor.unref(goodsFields).name) == null ? void 0 : _a.color,
              subTitleColor: common_vendor.unref(goodsFields).anchor_name.color,
              topRadius: __props.data.borderRadiusTop,
              bottomRadius: __props.data.borderRadiusBottom
            }),
            d: item.id
          };
        }),
        g: common_vendor.s({
          marginBottom: __props.data.space + "px"
        })
      } : {});
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-74052372"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/components/s-live-block/s-live-block.js.map
