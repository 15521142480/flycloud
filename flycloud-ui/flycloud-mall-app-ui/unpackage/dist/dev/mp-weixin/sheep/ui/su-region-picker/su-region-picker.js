"use strict";
const common_vendor = require("../../../common/vendor.js");
if (!Array) {
  const _easycom_su_toolbar2 = common_vendor.resolveComponent("su-toolbar");
  const _easycom_su_popup2 = common_vendor.resolveComponent("su-popup");
  (_easycom_su_toolbar2 + _easycom_su_popup2)();
}
const _easycom_su_toolbar = () => "../su-toolbar/su-toolbar.js";
const _easycom_su_popup = () => "../su-popup/su-popup.js";
if (!Math) {
  (_easycom_su_toolbar + _easycom_su_popup)();
}
const _sfc_main = {
  __name: "su-region-picker",
  props: {
    show: {
      type: Boolean,
      default: false
    },
    // "取消"按钮的颜色
    cancelColor: {
      type: String,
      default: "#6666"
    },
    // "确定"按钮的颜色
    confirmColor: {
      type: String,
      default: "var(--ui-BG-Main)"
    },
    // 取消按钮的文字
    cancelText: {
      type: String,
      default: "取消"
    },
    // 确认按钮的文字
    confirmText: {
      type: String,
      default: "确认"
    }
  },
  emits: ["confirm", "cancel", "change"],
  setup(__props, { emit: __emit }) {
    const areaData = common_vendor.index.getStorageSync("areaData");
    const getSizeByNameLength = (name) => {
      let length = name.length;
      if (length <= 7)
        return "";
      if (length < 9) {
        return "font-size:28rpx";
      } else {
        return "font-size: 24rpx";
      }
    };
    const state = common_vendor.reactive({
      currentIndex: [0, 0, 0],
      moving: false
      // 列是否还在滑动中，微信小程序如果在滑动中就点确定，结果可能不准确
    });
    const emits = __emit;
    const provinceList = areaData;
    const cityList = common_vendor.computed(() => {
      return areaData[state.currentIndex[0]].children;
    });
    const districtList = common_vendor.computed(() => {
      var _a;
      return (_a = cityList.value[state.currentIndex[1]]) == null ? void 0 : _a.children;
    });
    const pickstart = () => {
      state.moving = true;
    };
    const pickend = () => {
      state.moving = false;
    };
    const onCancel = () => {
      emits("cancel");
    };
    const change = (e) => {
      if (state.currentIndex[0] === e.detail.value[0] && state.currentIndex[1] === e.detail.value[1]) {
        state.currentIndex[2] = e.detail.value[2];
        return;
      } else {
        if (state.currentIndex[0] !== e.detail.value[0]) {
          e.detail.value[1] = 0;
        }
        e.detail.value[2] = 0;
        state.currentIndex = e.detail.value;
      }
      emits("change", state.currentIndex);
    };
    const onConfirm = (event = null) => {
      if (state.moving)
        return;
      let index = state.currentIndex;
      let province = provinceList[index[0]];
      let city = cityList.value[index[1]];
      let district = districtList.value[index[2]];
      let result = {
        province_name: province.name,
        province_id: province.id,
        city_name: city.name,
        city_id: city.id,
        district_name: district.name,
        district_id: district.id
      };
      if (event)
        emits(event, result);
    };
    return (_ctx, _cache) => {
      return {
        a: common_vendor.o(onCancel, "e5"),
        b: common_vendor.o(($event) => onConfirm("confirm"), "84"),
        c: common_vendor.p({
          cancelColor: __props.cancelColor,
          confirmColor: __props.confirmColor,
          cancelText: __props.cancelText,
          confirmText: __props.confirmText,
          title: "选择区域"
        }),
        d: common_vendor.f(common_vendor.unref(provinceList), (province, k0, i0) => {
          return {
            a: common_vendor.t(province.name),
            b: common_vendor.s(getSizeByNameLength(province.name)),
            c: province.id
          };
        }),
        e: common_vendor.f(cityList.value, (city, k0, i0) => {
          return {
            a: common_vendor.t(city.name),
            b: common_vendor.s(getSizeByNameLength(city.name)),
            c: city.id
          };
        }),
        f: common_vendor.f(districtList.value, (district, k0, i0) => {
          return {
            a: common_vendor.t(district.name),
            b: common_vendor.s(getSizeByNameLength(district.name)),
            c: district.id
          };
        }),
        g: state.currentIndex,
        h: common_vendor.o(change, "f2"),
        i: common_vendor.o(pickstart, "a6"),
        j: common_vendor.o(pickend, "d0"),
        k: common_vendor.o(onCancel, "77"),
        l: common_vendor.p({
          show: __props.show,
          round: "20"
        })
      };
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-124df58f"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/ui/su-region-picker/su-region-picker.js.map
