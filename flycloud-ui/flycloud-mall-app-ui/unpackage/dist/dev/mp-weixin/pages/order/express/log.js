"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../../sheep/index.js");
const sheep_api_trade_order = require("../../../sheep/api/trade/order.js");
if (!Array) {
  const _easycom_uni_swiper_dot2 = common_vendor.resolveComponent("uni-swiper-dot");
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  (_easycom_uni_swiper_dot2 + _easycom_s_layout2)();
}
const _easycom_uni_swiper_dot = () => "../../../uni_modules/uni-swiper-dot/components/uni-swiper-dot/uni-swiper-dot.js";
const _easycom_s_layout = () => "../../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  (_easycom_uni_swiper_dot + HighlightNumber + _easycom_s_layout)();
}
const HighlightNumber = () => "../../components/HighlightNumberText.js";
const _sfc_main = {
  __name: "log",
  setup(__props) {
    const state = common_vendor.reactive({
      info: [],
      tracks: []
    });
    const goodsImages = common_vendor.computed(() => {
      let array = [];
      if (state.info.items) {
        state.info.items.forEach((item) => {
          array.push({
            image: item.picUrl
          });
        });
      }
      return array;
    });
    async function getExpressDetail(id) {
      const { data } = await sheep_api_trade_order.OrderApi.getOrderExpressTrackList(id);
      state.tracks = data;
    }
    async function getOrderDetail(id) {
      const { data } = await sheep_api_trade_order.OrderApi.getOrderDetail(id);
      state.info = data;
    }
    common_vendor.onLoad((options) => {
      getExpressDetail(options.id);
      getOrderDetail(options.id);
    });
    function handlePhoneClick(data) {
      handleClick(data);
    }
    function handleClick(data) {
      const phoneNumber = data.phoneNumber;
      if (!phoneNumber)
        return;
      const platform = common_vendor.index.getSystemInfoSync().platform.toLowerCase();
      if (platform === "devtools") {
        common_vendor.index.showToast({ title: "真机才可拨打电话", icon: "none" });
        handleCopy(phoneNumber);
        return;
      }
      if (platform === "wechat") {
        common_vendor.index.showToast({ title: "请手动拨打", icon: "none" });
        handleCopy(phoneNumber);
        return;
      }
      common_vendor.index.makePhoneCall({
        phoneNumber,
        success: () => {
          common_vendor.index.__f__("log", "at pages/order/express/log.vue:122", "拨打电话成功");
        },
        fail: (err) => {
          common_vendor.index.__f__("error", "at pages/order/express/log.vue:125", "拨打电话失败", err);
          common_vendor.index.showToast({ title: "拨号失败，请手动拨打", icon: "none" });
          handleCopy(phoneNumber);
        }
      });
    }
    function handleCopy(text) {
      common_vendor.index.setClipboardData({
        data: text,
        success: () => {
          common_vendor.index.showToast({ title: "已复制到剪贴板", icon: "success" });
        },
        fail: () => {
          common_vendor.index.showToast({ title: "复制失败", icon: "none" });
        }
      });
    }
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: goodsImages.value.length > 0
      }, goodsImages.value.length > 0 ? {
        b: common_vendor.f(goodsImages.value, (item, index, i0) => {
          return {
            a: common_vendor.unref(sheep_index.sheep).$url.static(item.image),
            b: index
          };
        }),
        c: common_vendor.p({
          info: goodsImages.value,
          current: state.current,
          mode: "round"
        }),
        d: common_vendor.t(state.info.logisticsNo),
        e: common_vendor.t(state.info.logisticsName)
      } : {}, {
        f: common_vendor.f(state.tracks, (item, index, i0) => {
          return common_vendor.e({
            a: state.tracks.length - 1 !== index
          }, state.tracks.length - 1 !== index ? {} : {}, {
            b: common_vendor.o(handlePhoneClick, item.title),
            c: "d9d25552-2-" + i0 + ",d9d25552-0",
            d: common_vendor.p({
              content: item.content
            }),
            e: common_vendor.t(common_vendor.unref(sheep_index.sheep).$helper.timeFormat(item.time, "yyyy-mm-dd hh:MM:ss")),
            f: item.title
          });
        }),
        g: common_vendor.p({
          title: "物流追踪"
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-d9d25552"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/order/express/log.js.map
