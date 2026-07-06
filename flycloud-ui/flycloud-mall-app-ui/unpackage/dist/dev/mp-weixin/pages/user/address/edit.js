"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../../sheep/index.js");
const sheep_validate_form = require("../../../sheep/validate/form.js");
const sheep_api_system_area = require("../../../sheep/api/system/area.js");
const sheep_api_member_address = require("../../../sheep/api/member/address.js");
if (!Array) {
  const _easycom_uni_easyinput2 = common_vendor.resolveComponent("uni-easyinput");
  const _easycom_uni_forms_item2 = common_vendor.resolveComponent("uni-forms-item");
  const _easycom_uni_icons2 = common_vendor.resolveComponent("uni-icons");
  const _easycom_su_switch2 = common_vendor.resolveComponent("su-switch");
  const _easycom_uni_forms2 = common_vendor.resolveComponent("uni-forms");
  const _easycom_su_fixed2 = common_vendor.resolveComponent("su-fixed");
  const _easycom_su_region_picker2 = common_vendor.resolveComponent("su-region-picker");
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  (_easycom_uni_easyinput2 + _easycom_uni_forms_item2 + _easycom_uni_icons2 + _easycom_su_switch2 + _easycom_uni_forms2 + _easycom_su_fixed2 + _easycom_su_region_picker2 + _easycom_s_layout2)();
}
const _easycom_uni_easyinput = () => "../../../uni_modules/uni-easyinput/components/uni-easyinput/uni-easyinput.js";
const _easycom_uni_forms_item = () => "../../../uni_modules/uni-forms/components/uni-forms-item/uni-forms-item.js";
const _easycom_uni_icons = () => "../../../uni_modules/uni-icons/components/uni-icons/uni-icons.js";
const _easycom_su_switch = () => "../../../sheep/ui/su-switch/su-switch.js";
const _easycom_uni_forms = () => "../../../uni_modules/uni-forms/components/uni-forms/uni-forms.js";
const _easycom_su_fixed = () => "../../../sheep/ui/su-fixed/su-fixed.js";
const _easycom_su_region_picker = () => "../../../sheep/ui/su-region-picker/su-region-picker.js";
const _easycom_s_layout = () => "../../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  (_easycom_uni_easyinput + _easycom_uni_forms_item + _easycom_uni_icons + _easycom_su_switch + _easycom_uni_forms + _easycom_su_fixed + _easycom_su_region_picker + _easycom_s_layout)();
}
const _sfc_main = {
  __name: "edit",
  setup(__props) {
    const addressFormRef = common_vendor.ref(null);
    const state = common_vendor.reactive({
      showRegion: false,
      model: {
        name: "",
        mobile: "",
        detailAddress: "",
        defaultStatus: false,
        areaName: ""
      },
      rules: {}
    });
    const rules = {
      name: {
        rules: [
          {
            required: true,
            errorMessage: "请输入收货人姓名"
          }
        ]
      },
      mobile: sheep_validate_form.mobile,
      detailAddress: {
        rules: [
          {
            required: true,
            errorMessage: "请输入详细地址"
          }
        ]
      },
      areaName: {
        rules: [
          {
            required: true,
            errorMessage: "请选择您的位置"
          }
        ]
      }
    };
    const onRegionConfirm = (e) => {
      state.model.areaName = `${e.province_name} ${e.city_name} ${e.district_name}`;
      state.model.areaId = e.district_id;
      state.showRegion = false;
    };
    const getAreaData = () => {
      if (common_vendor.isEmpty(common_vendor.index.getStorageSync("areaData"))) {
        sheep_api_system_area.AreaApi.getAreaTree().then((res) => {
          if (res.code === 0) {
            common_vendor.index.setStorageSync("areaData", res.data);
          }
        });
      }
    };
    const onSave = async () => {
      const validate = await common_vendor.unref(addressFormRef).validate().catch((error) => {
        common_vendor.index.__f__("log", "at pages/user/address/edit.vue:169", "error: ", error);
      });
      if (!validate) {
        return;
      }
      const formData = {
        ...state.model
      };
      const { code } = state.model.id > 0 ? await sheep_api_member_address.AddressApi.updateAddress(formData) : await sheep_api_member_address.AddressApi.createAddress(formData);
      if (code === 0) {
        sheep_index.sheep.$router.back();
      }
    };
    const onDelete = () => {
      common_vendor.index.showModal({
        title: "提示",
        content: "确认删除此收货地址吗？",
        success: async function(res) {
          if (!res.confirm) {
            return;
          }
          const { code } = await sheep_api_member_address.AddressApi.deleteAddress(state.model.id);
          if (code === 0) {
            sheep_index.sheep.$router.back();
          }
        }
      });
    };
    common_vendor.onLoad(async (options) => {
      getAreaData();
      if (options.id) {
        let { code, data } = await sheep_api_member_address.AddressApi.getAddress(options.id);
        if (code !== 0) {
          return;
        }
        state.model = data;
      }
      if (options.data) {
        let data = JSON.parse(options.data);
        const areaData = common_vendor.index.getStorageSync("areaData");
        const findAreaByName = (areas, name) => areas.find((item) => item.name === name);
        let provinceObj = findAreaByName(areaData, data.province_name);
        let cityObj = provinceObj ? findAreaByName(provinceObj.children, data.city_name) : void 0;
        let districtObj = cityObj ? findAreaByName(cityObj.children, data.district_name) : void 0;
        let areaId = (districtObj || cityObj || provinceObj).id;
        state.model = {
          ...state.model,
          areaId,
          areaName: [data.province_name, data.city_name, data.district_name].filter(Boolean).join(" "),
          defaultStatus: false,
          detailAddress: data.address,
          mobile: data.mobile,
          name: data.consignee
        };
      }
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.o(($event) => state.model.name = $event, "08"),
        b: common_vendor.p({
          placeholder: "请填写收货人姓名",
          inputBorder: false,
          placeholderStyle: "color:#BBBBBB;font-size:30rpx;font-weight:400;line-height:normal",
          modelValue: state.model.name
        }),
        c: common_vendor.p({
          name: "name",
          label: "收货人"
        }),
        d: common_vendor.o(($event) => state.model.mobile = $event, "49"),
        e: common_vendor.p({
          type: "number",
          placeholder: "请输入手机号",
          inputBorder: false,
          placeholderStyle: "color:#BBBBBB;font-size:30rpx;font-weight:400;line-height:normal",
          modelValue: state.model.mobile
        }),
        f: common_vendor.p({
          name: "mobile",
          label: "手机号"
        }),
        g: common_vendor.p({
          type: "right"
        }),
        h: common_vendor.o(($event) => state.model.areaName = $event, "9c"),
        i: common_vendor.p({
          disabled: true,
          inputBorder: false,
          styles: {
            disableColor: "#fff",
            color: "#333"
          },
          placeholderStyle: "color:#BBBBBB;font-size:30rpx;font-weight:400;line-height:normal",
          placeholder: "请选择省市区",
          modelValue: state.model.areaName
        }),
        j: common_vendor.o(($event) => state.showRegion = true, "c4"),
        k: common_vendor.p({
          name: "areaName",
          label: "省市区"
        }),
        l: common_vendor.o(($event) => state.model.detailAddress = $event, "c4"),
        m: common_vendor.p({
          inputBorder: false,
          type: "textarea",
          placeholderStyle: "color:#BBBBBB;font-size:30rpx;font-weight:400;line-height:normal",
          placeholder: "请输入详细地址",
          clearable: true,
          modelValue: state.model.detailAddress
        }),
        n: common_vendor.p({
          name: "detailAddress",
          label: "详细地址",
          formItemStyle: {
            alignItems: "flex-start"
          },
          labelStyle: {
            lineHeight: "5em"
          }
        }),
        o: common_vendor.o(($event) => state.model.defaultStatus = $event, "a9"),
        p: common_vendor.p({
          modelValue: state.model.defaultStatus
        }),
        q: common_vendor.sr(addressFormRef, "e04c42e1-1,e04c42e1-0", {
          "k": "addressFormRef"
        }),
        r: common_vendor.o(($event) => state.model = $event, "2e"),
        s: common_vendor.p({
          rules,
          validateTrigger: "bind",
          labelWidth: "160",
          labelAlign: "left",
          border: true,
          labelStyle: {
            fontWeight: "bold"
          },
          modelValue: state.model
        }),
        t: common_vendor.o(onSave, "d9"),
        v: state.model.id
      }, state.model.id ? {
        w: common_vendor.o(onDelete, "33")
      } : {}, {
        x: common_vendor.p({
          bottom: true,
          opacity: false,
          bg: "",
          placeholder: true,
          noFixed: false,
          index: 10
        }),
        y: common_vendor.o(($event) => state.showRegion = false, "57"),
        z: common_vendor.o(onRegionConfirm, "05"),
        A: common_vendor.p({
          show: state.showRegion
        }),
        B: common_vendor.p({
          title: state.model.id ? "编辑地址" : "新增地址"
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-e04c42e1"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/user/address/edit.js.map
