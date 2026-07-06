"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../../sheep/index.js");
const sheep_api_system_area = require("../../../sheep/api/system/area.js");
const sheep_api_member_address = require("../../../sheep/api/member/address.js");
if (!Array) {
  const _easycom_s_address_item2 = common_vendor.resolveComponent("s-address-item");
  const _easycom_su_fixed2 = common_vendor.resolveComponent("su-fixed");
  const _easycom_s_empty2 = common_vendor.resolveComponent("s-empty");
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  (_easycom_s_address_item2 + _easycom_su_fixed2 + _easycom_s_empty2 + _easycom_s_layout2)();
}
const _easycom_s_address_item = () => "../../../sheep/components/s-address-item/s-address-item.js";
const _easycom_su_fixed = () => "../../../sheep/ui/su-fixed/su-fixed.js";
const _easycom_s_empty = () => "../../../sheep/components/s-empty/s-empty.js";
const _easycom_s_layout = () => "../../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  (_easycom_s_address_item + _easycom_su_fixed + _easycom_s_empty + _easycom_s_layout)();
}
const _sfc_main = {
  __name: "list",
  setup(__props) {
    const state = common_vendor.reactive({
      list: [],
      // 地址列表
      loading: true,
      openType: ""
      // 页面打开类型
    });
    const onSelect = (addressInfo) => {
      if (state.openType !== "select") {
        return;
      }
      common_vendor.index.$emit("SELECT_ADDRESS", {
        addressInfo
      });
      sheep_index.sheep.$router.back();
    };
    function importWechatAddress() {
      let wechatAddress = {};
      common_vendor.index.chooseAddress({
        success: (res) => {
          wechatAddress = {
            consignee: res.userName,
            mobile: res.telNumber,
            province_name: res.provinceName,
            city_name: res.cityName,
            district_name: res.countyName,
            address: res.detailInfo,
            region: "",
            is_default: false
          };
          if (!common_vendor.isEmpty(wechatAddress)) {
            sheep_index.sheep.$router.go("/pages/user/address/edit", {
              data: JSON.stringify(wechatAddress)
            });
          }
        },
        fail: (err) => {
          common_vendor.index.__f__("log", "at pages/user/address/list.vue:89", "%cuni.chooseAddress,调用失败", "color:green;background:yellow");
        }
      });
    }
    common_vendor.onLoad((option) => {
      if (option.type) {
        state.openType = option.type;
      }
    });
    common_vendor.onShow(async () => {
      state.list = (await sheep_api_member_address.AddressApi.getAddressList()).data;
      state.loading = false;
    });
    common_vendor.onBeforeMount(() => {
      if (!!common_vendor.index.getStorageSync("areaData")) {
        return;
      }
      sheep_api_system_area.AreaApi.getAreaTree().then((res) => {
        if (res.code === 0) {
          common_vendor.index.setStorageSync("areaData", res.data);
        }
      });
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: state.list.length
      }, state.list.length ? {
        b: common_vendor.f(state.list, (item, k0, i0) => {
          return {
            a: item.id,
            b: common_vendor.o(($event) => onSelect(item), item.id),
            c: "5a5957ad-1-" + i0 + ",5a5957ad-0",
            d: common_vendor.p({
              hasBorderBottom: true,
              item
            })
          };
        })
      } : {}, {
        c: ["WechatMiniProgram", "WechatOfficialAccount"].includes(common_vendor.unref(sheep_index.sheep).$platform.name)
      }, ["WechatMiniProgram", "WechatOfficialAccount"].includes(common_vendor.unref(sheep_index.sheep).$platform.name) ? {
        d: common_vendor.o(importWechatAddress, "9f")
      } : {}, {
        e: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/user/address/edit"), "6a"),
        f: common_vendor.p({
          bottom: true,
          placeholder: true
        }),
        g: state.list.length === 0 && !state.loading
      }, state.list.length === 0 && !state.loading ? {
        h: common_vendor.p({
          text: "暂无收货地址",
          icon: "/static/data-empty.png"
        })
      } : {}, {
        i: common_vendor.p({
          bgStyle: {
            color: "#FFF"
          },
          title: "收货地址"
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-5a5957ad"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/user/address/list.js.map
