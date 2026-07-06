"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_api_trade_delivery = require("../../../sheep/api/trade/delivery.js");
const sheep_index = require("../../../sheep/index.js");
if (!Array) {
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  _easycom_s_layout2();
}
const _easycom_s_layout = () => "../../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  _easycom_s_layout();
}
const LONGITUDE = "user_longitude";
const LATITUDE = "user_latitude";
const _sfc_main = {
  __name: "index",
  setup(__props) {
    const state = common_vendor.reactive({
      loaded: false,
      loading: false,
      storeList: [],
      system_store: {},
      locationShow: false,
      user_latitude: 0,
      user_longitude: 0
    });
    const call = (phone) => {
      common_vendor.index.makePhoneCall({
        phoneNumber: phone
      });
    };
    const selfLocation = () => {
      common_vendor.index.getLocation({
        type: "gcj02",
        success: (res) => {
          try {
            state.user_latitude = res.latitude;
            state.user_longitude = res.longitude;
            common_vendor.index.setStorageSync(LATITUDE, res.latitude);
            common_vendor.index.setStorageSync(LONGITUDE, res.longitude);
          } catch (e) {
            common_vendor.index.__f__("error", "at pages/user/goods_details_store/index.vue:102", e);
          }
          getList();
        },
        complete: () => {
          getList();
        }
      });
    };
    const showMaoLocation = (e) => {
      common_vendor.index.openLocation({
        latitude: Number(e.latitude),
        longitude: Number(e.longitude),
        name: e.name,
        address: `${e.areaName}-${e.detailAddress}`,
        success: function() {
          common_vendor.index.__f__("log", "at pages/user/goods_details_store/index.vue:132", "success");
        }
      });
    };
    const checked = (addressInfo) => {
      common_vendor.index.$emit("SELECT_PICK_UP_INFO", {
        addressInfo
      });
      sheep_index.sheep.$router.back();
    };
    const getList = async () => {
      if (state.loading || state.loaded) {
        return;
      }
      state.loading = true;
      const { data, code } = await sheep_api_trade_delivery.DeliveryApi.getDeliveryPickUpStoreList({
        latitude: state.user_latitude,
        longitude: state.user_longitude
      });
      if (code !== 0) {
        return;
      }
      state.loading = false;
      state.storeList = data;
    };
    common_vendor.onMounted(() => {
      if (state.user_latitude && state.user_longitude) {
        getList();
      } else {
        selfLocation();
        getList();
      }
    });
    common_vendor.onLoad(() => {
      try {
        state.user_latitude = common_vendor.index.getStorageSync(LATITUDE);
        state.user_longitude = common_vendor.index.getStorageSync(LONGITUDE);
      } catch (e) {
        common_vendor.index.__f__("error", "at pages/user/goods_details_store/index.vue:182", e);
      }
    });
    return (_ctx, _cache) => {
      return {
        a: common_vendor.f(state.storeList, (item, index, i0) => {
          return common_vendor.e({
            a: common_vendor.unref(sheep_index.sheep).$url.cdn(item.logo),
            b: common_vendor.t(item.name),
            c: common_vendor.t(item.areaName),
            d: common_vendor.t(", " + item.detailAddress),
            e: common_vendor.o(($event) => call(item.phone), index),
            f: item.distance
          }, item.distance ? {
            g: common_vendor.t(item.distance.toFixed(2))
          } : {}, {
            h: common_vendor.o(($event) => showMaoLocation(item), index),
            i: index,
            j: common_vendor.o(($event) => checked(item), index)
          });
        }),
        b: common_vendor.p({
          bgStyle: {
            color: "#FFF"
          },
          title: "选择自提门店"
        })
      };
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-619edc27"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/user/goods_details_store/index.js.map
