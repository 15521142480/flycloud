"use strict";
const common_vendor = require("../../../../common/vendor.js");
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
  __name: "change-password",
  setup(__props) {
    const changePasswordRef = common_vendor.ref(null);
    const state = common_vendor.reactive({
      model: {
        mobile: "",
        // 手机号
        code: "",
        // 验证码
        password: ""
        // 密码
      },
      rules: {
        code: sheep_validate_form.code,
        password: sheep_validate_form.password
      }
    });
    async function changePasswordSubmit() {
      const validate = await common_vendor.unref(changePasswordRef).validate().catch((error) => {
        common_vendor.index.__f__("log", "at sheep/components/s-auth-modal/components/change-password.vue:89", "error: ", error);
      });
      if (!validate) {
        return;
      }
      const { code } = await sheep_api_member_user.UserApi.updateUserPassword(state.model);
      if (code !== 0) {
        return;
      }
      sheep_hooks_useModal.closeAuthModal();
    }
    return (_ctx, _cache) => {
      return {
        a: common_vendor.t(common_vendor.unref(sheep_hooks_useModal.getSmsTimer)("resetPassword")),
        b: state.isMobileEnd,
        c: state.isMobileEnd ? 1 : "",
        d: common_vendor.o(($event) => common_vendor.unref(sheep_hooks_useModal.getSmsCode)("changePassword"), "ef"),
        e: common_vendor.o(($event) => state.model.code = $event, "e9"),
        f: common_vendor.p({
          placeholder: "请输入验证码",
          type: "number",
          maxlength: "4",
          inputBorder: false,
          modelValue: state.model.code
        }),
        g: common_vendor.p({
          name: "code",
          label: "验证码"
        }),
        h: common_vendor.o(changePasswordSubmit, "64"),
        i: common_vendor.o(($event) => state.model.password = $event, "42"),
        j: common_vendor.p({
          type: "password",
          placeholder: "请输入密码",
          inputBorder: false,
          modelValue: state.model.password
        }),
        k: common_vendor.p({
          name: "reNewPassword",
          label: "密码"
        }),
        l: common_vendor.sr(changePasswordRef, "639aa0c2-0", {
          "k": "changePasswordRef"
        }),
        m: common_vendor.o(($event) => state.model = $event, "5a"),
        n: common_vendor.p({
          rules: state.rules,
          validateTrigger: "bind",
          labelWidth: "140",
          labelAlign: "center",
          modelValue: state.model
        }),
        o: common_vendor.o((...args) => common_vendor.unref(sheep_hooks_useModal.closeAuthModal) && common_vendor.unref(sheep_hooks_useModal.closeAuthModal)(...args), "d5")
      };
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-639aa0c2"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../../.sourcemap/mp-weixin/sheep/components/s-auth-modal/components/change-password.js.map
