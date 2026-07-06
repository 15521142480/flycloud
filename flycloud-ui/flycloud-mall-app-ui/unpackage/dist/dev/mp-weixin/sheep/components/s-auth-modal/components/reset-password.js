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
  __name: "reset-password",
  setup(__props) {
    const resetPasswordRef = common_vendor.ref(null);
    const isLogin = common_vendor.computed(() => sheep_index.sheep.$store("user").isLogin);
    const state = common_vendor.reactive({
      isMobileEnd: false,
      // 手机号输入完毕
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
        mobile: sheep_validate_form.mobile,
        password: sheep_validate_form.password
      }
    });
    const resetPasswordSubmit = async () => {
      const validate = await common_vendor.unref(resetPasswordRef).validate().catch((error) => {
        common_vendor.index.__f__("log", "at sheep/components/s-auth-modal/components/reset-password.vue:102", "error: ", error);
      });
      if (!validate) {
        return;
      }
      const { code } = await sheep_api_member_user.UserApi.resetUserPassword(state.model);
      if (code !== 0) {
        return;
      }
      sheep_hooks_useModal.showAuthModal("accountLogin");
    };
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.t(common_vendor.unref(sheep_hooks_useModal.getSmsTimer)("resetPassword")),
        b: state.isMobileEnd,
        c: state.isMobileEnd ? 1 : "",
        d: common_vendor.o(($event) => common_vendor.unref(sheep_hooks_useModal.getSmsCode)("resetPassword", state.model.mobile), "9f"),
        e: common_vendor.o(($event) => state.model.mobile = $event, "c8"),
        f: common_vendor.p({
          placeholder: "请输入手机号",
          type: "number",
          inputBorder: false,
          modelValue: state.model.mobile
        }),
        g: common_vendor.p({
          name: "mobile",
          label: "手机号"
        }),
        h: common_vendor.o(($event) => state.model.code = $event, "a3"),
        i: common_vendor.p({
          placeholder: "请输入验证码",
          type: "number",
          maxlength: "4",
          inputBorder: false,
          modelValue: state.model.code
        }),
        j: common_vendor.p({
          name: "code",
          label: "验证码"
        }),
        k: common_vendor.o(resetPasswordSubmit, "ff"),
        l: common_vendor.o(($event) => state.model.password = $event, "89"),
        m: common_vendor.p({
          type: "password",
          placeholder: "请输入密码",
          inputBorder: false,
          modelValue: state.model.password
        }),
        n: common_vendor.p({
          name: "password",
          label: "密码"
        }),
        o: common_vendor.sr(resetPasswordRef, "859b5179-0", {
          "k": "resetPasswordRef"
        }),
        p: common_vendor.o(($event) => state.model = $event, "5a"),
        q: common_vendor.p({
          rules: state.rules,
          validateTrigger: "bind",
          labelWidth: "140",
          labelAlign: "center",
          modelValue: state.model
        }),
        r: !isLogin.value
      }, !isLogin.value ? {
        s: common_vendor.o(($event) => common_vendor.unref(sheep_hooks_useModal.showAuthModal)("accountLogin"), "dc")
      } : {});
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-859b5179"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../../.sourcemap/mp-weixin/sheep/components/s-auth-modal/components/reset-password.js.map
