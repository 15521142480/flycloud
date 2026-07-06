"use strict";
const common_vendor = require("../../../../common/vendor.js");
const sheep_index = require("../../../index.js");
const sheep_validate_form = require("../../../validate/form.js");
const sheep_hooks_useModal = require("../../../hooks/useModal.js");
const sheep_api_member_user = require("../../../api/member/user.js");
if (!Array) {
  const _easycom_uni_easyinput2 = common_vendor.resolveComponent("uni-easyinput");
  const _easycom_uni_forms_item2 = common_vendor.resolveComponent("uni-forms-item");
  const _easycom_uni_forms2 = common_vendor.resolveComponent("uni-forms");
  (_easycom_uni_easyinput2 + _easycom_uni_forms_item2 + _easycom_uni_forms2)();
}
const _easycom_uni_easyinput = () => "../../../../uni_modules/uni-easyinput/components/uni-easyinput/uni-easyinput.js";
const _easycom_uni_forms_item = () => "../../../../uni_modules/uni-forms/components/uni-forms-item/uni-forms-item.js";
const _easycom_uni_forms = () => "../../../../uni_modules/uni-forms/components/uni-forms/uni-forms.js";
if (!Math) {
  (_easycom_uni_easyinput + _easycom_uni_forms_item + _easycom_uni_forms)();
}
const _sfc_main = {
  __name: "change-mobile",
  setup(__props) {
    const changeMobileRef = common_vendor.ref(null);
    const userInfo = common_vendor.computed(() => sheep_index.sheep.$store("user").userInfo);
    const state = common_vendor.reactive({
      isMobileEnd: false,
      // 手机号输入完毕
      model: {
        mobile: "",
        // 手机号
        code: ""
        // 验证码
      },
      rules: {
        code: sheep_validate_form.code,
        mobile: sheep_validate_form.mobile
      }
    });
    async function changeMobileSubmit() {
      const validate = await common_vendor.unref(changeMobileRef).validate().catch((error) => {
        common_vendor.index.__f__("log", "at sheep/components/s-auth-modal/components/change-mobile.vue:98", "error: ", error);
      });
      if (!validate) {
        return;
      }
      const { code } = await sheep_api_member_user.UserApi.updateUserMobile(state.model);
      if (code !== 0) {
        return;
      }
      sheep_index.sheep.$store("user").getInfo();
      sheep_hooks_useModal.closeAuthModal();
    }
    async function getPhoneNumber(e) {
      if (e.detail.errMsg !== "getPhoneNumber:ok") {
        return;
      }
      const result = await sheep_index.sheep.$platform.useProvider().bindUserPhoneNumber(e.detail);
      if (result) {
        sheep_index.sheep.$store("user").getInfo();
        sheep_hooks_useModal.closeAuthModal();
      }
    }
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.t(userInfo.value.mobile ? "更换手机号" : "绑定手机号"),
        b: common_vendor.t(common_vendor.unref(sheep_hooks_useModal.getSmsTimer)("changeMobile")),
        c: state.isMobileEnd,
        d: state.isMobileEnd ? 1 : "",
        e: common_vendor.o(($event) => common_vendor.unref(sheep_hooks_useModal.getSmsCode)("changeMobile", state.model.mobile), "5c"),
        f: common_vendor.o(($event) => state.model.mobile = $event, "5d"),
        g: common_vendor.p({
          placeholder: "请输入手机号",
          inputBorder: false,
          type: "number",
          modelValue: state.model.mobile
        }),
        h: common_vendor.p({
          name: "mobile",
          label: "手机号"
        }),
        i: common_vendor.o(changeMobileSubmit, "c4"),
        j: common_vendor.o(($event) => state.model.code = $event, "85"),
        k: common_vendor.p({
          placeholder: "请输入验证码",
          inputBorder: false,
          type: "number",
          maxlength: "4",
          modelValue: state.model.code
        }),
        l: common_vendor.p({
          name: "code",
          label: "验证码"
        }),
        m: common_vendor.sr(changeMobileRef, "146b49fd-0", {
          "k": "changeMobileRef"
        }),
        n: common_vendor.o(($event) => state.model = $event, "81"),
        o: common_vendor.p({
          rules: state.rules,
          validateTrigger: "bind",
          labelWidth: "140",
          labelAlign: "center",
          modelValue: state.model
        }),
        p: "WechatMiniProgram" === common_vendor.unref(sheep_index.sheep).$platform.name
      }, "WechatMiniProgram" === common_vendor.unref(sheep_index.sheep).$platform.name ? {
        q: common_vendor.o(getPhoneNumber, "a9")
      } : {});
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-146b49fd"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../../.sourcemap/mp-weixin/sheep/components/s-auth-modal/components/change-mobile.js.map
