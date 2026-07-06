"use strict";
const common_vendor = require("../../../../common/vendor.js");
const sheep_index = require("../../../index.js");
const sheep_validate_form = require("../../../validate/form.js");
const sheep_hooks_useModal = require("../../../hooks/useModal.js");
const sheep_api_member_auth = require("../../../api/member/auth.js");
if (!Array) {
  const _easycom_uni_easyinput2 = common_vendor.resolveComponent("uni-easyinput");
  const _easycom_uni_forms_item2 = common_vendor.resolveComponent("uni-forms-item");
  const _easycom_uni_forms2 = common_vendor.resolveComponent("uni-forms");
  const _easycom_s_click_captcha2 = common_vendor.resolveComponent("s-click-captcha");
  (_easycom_uni_easyinput2 + _easycom_uni_forms_item2 + _easycom_uni_forms2 + _easycom_s_click_captcha2)();
}
const _easycom_uni_easyinput = () => "../../../../uni_modules/uni-easyinput/components/uni-easyinput/uni-easyinput.js";
const _easycom_uni_forms_item = () => "../../../../uni_modules/uni-forms/components/uni-forms-item/uni-forms-item.js";
const _easycom_uni_forms = () => "../../../../uni_modules/uni-forms/components/uni-forms/uni-forms.js";
const _easycom_s_click_captcha = () => "../../s-click-captcha/s-click-captcha.js";
if (!Math) {
  (_easycom_uni_easyinput + _easycom_uni_forms_item + _easycom_uni_forms + _easycom_s_click_captcha)();
}
const _sfc_main = {
  __name: "account-login",
  props: {
    agreeStatus: {
      type: [Boolean, null],
      default: null
    }
  },
  emits: ["onConfirm"],
  setup(__props, { emit: __emit }) {
    const accountLoginRef = common_vendor.ref(null);
    const emits = __emit;
    const props = __props;
    const state = common_vendor.reactive({
      showCaptcha: false,
      // 是否展示图文点选验证码
      model: {
        mobile: "15521142480",
        // 账号
        password: "abc123456"
        // 密码
      },
      rules: {
        mobile: {
          rules: [
            {
              required: true,
              errorMessage: "请输入账号"
            }
          ]
        },
        password: sheep_validate_form.password
      }
    });
    async function accountLoginSubmit() {
      const validate = await common_vendor.unref(accountLoginRef).validate().catch((error) => {
        common_vendor.index.__f__("log", "at sheep/components/s-auth-modal/components/account-login.vue:96", "error: ", error);
      });
      if (!validate)
        return;
      if (props.agreeStatus !== true) {
        emits("onConfirm", true);
        if (props.agreeStatus === false) {
          sheep_index.sheep.$helper.toast("您已拒绝协议，无法继续登录");
        } else {
          sheep_index.sheep.$helper.toast("请选择是否同意协议");
        }
        return;
      }
      state.showCaptcha = true;
    }
    async function handleCaptchaSuccess(captchaVerification) {
      const loginData = {
        ...state.model,
        captchaCode: captchaVerification
      };
      const { code } = await sheep_api_member_auth.AuthUtil.loginByAccount(loginData);
      if (code === 0) {
        sheep_hooks_useModal.closeAuthModal();
      }
    }
    return (_ctx, _cache) => {
      return {
        a: common_vendor.o(($event) => common_vendor.unref(sheep_hooks_useModal.showAuthModal)("smsLogin"), "31"),
        b: common_vendor.o(($event) => common_vendor.unref(sheep_hooks_useModal.showAuthModal)("resetPassword"), "da"),
        c: common_vendor.o(($event) => state.model.mobile = $event, "5e"),
        d: common_vendor.p({
          placeholder: "请输入账号",
          inputBorder: false,
          modelValue: state.model.mobile
        }),
        e: common_vendor.p({
          name: "mobile",
          label: "账号"
        }),
        f: common_vendor.o(accountLoginSubmit, "4b"),
        g: common_vendor.o(($event) => state.model.password = $event, "d1"),
        h: common_vendor.p({
          type: "password",
          placeholder: "请输入密码",
          inputBorder: false,
          modelValue: state.model.password
        }),
        i: common_vendor.p({
          name: "password",
          label: "密码"
        }),
        j: common_vendor.sr(accountLoginRef, "f21c903c-0", {
          "k": "accountLoginRef"
        }),
        k: common_vendor.o(($event) => state.model = $event, "d7"),
        l: common_vendor.p({
          rules: state.rules,
          validateTrigger: "bind",
          labelWidth: "140",
          labelAlign: "center",
          modelValue: state.model
        }),
        m: common_vendor.o(handleCaptchaSuccess, "b2"),
        n: common_vendor.o(($event) => state.showCaptcha = $event, "98"),
        o: common_vendor.p({
          show: state.showCaptcha
        })
      };
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-f21c903c"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../../.sourcemap/mp-weixin/sheep/components/s-auth-modal/components/account-login.js.map
