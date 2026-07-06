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
  (_easycom_uni_easyinput2 + _easycom_uni_forms_item2 + _easycom_uni_forms2)();
}
const _easycom_uni_easyinput = () => "../../../../uni_modules/uni-easyinput/components/uni-easyinput/uni-easyinput.js";
const _easycom_uni_forms_item = () => "../../../../uni_modules/uni-forms/components/uni-forms-item/uni-forms-item.js";
const _easycom_uni_forms = () => "../../../../uni_modules/uni-forms/components/uni-forms/uni-forms.js";
if (!Math) {
  (_easycom_uni_easyinput + _easycom_uni_forms_item + _easycom_uni_forms)();
}
const _sfc_main = {
  __name: "sms-login",
  props: {
    agreeStatus: {
      type: [Boolean, null],
      default: null
    }
  },
  emits: ["onConfirm"],
  setup(__props, { emit: __emit }) {
    const smsLoginRef = common_vendor.ref(null);
    const emits = __emit;
    const props = __props;
    const state = common_vendor.reactive({
      isMobileEnd: false,
      // 手机号输入完毕
      codeText: "获取验证码",
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
    function checkAgreementAndGetSmsCode() {
      if (props.agreeStatus !== true) {
        emits("onConfirm", true);
        if (props.agreeStatus === false) {
          sheep_index.sheep.$helper.toast("您已拒绝协议，无法发送验证码");
        } else {
          sheep_index.sheep.$helper.toast("请选择是否同意协议");
        }
        return;
      }
      sheep_hooks_useModal.getSmsCode("smsLogin", state.model.mobile);
    }
    async function smsLoginSubmit() {
      const validate = await common_vendor.unref(smsLoginRef).validate().catch((error) => {
        common_vendor.index.__f__("log", "at sheep/components/s-auth-modal/components/sms-login.vue:113", "error: ", error);
      });
      if (!validate) {
        return;
      }
      if (props.agreeStatus !== true) {
        emits("onConfirm", true);
        if (props.agreeStatus === false) {
          sheep_index.sheep.$helper.toast("您已拒绝协议，无法继续登录");
        } else {
          sheep_index.sheep.$helper.toast("请选择是否同意协议");
        }
        return;
      }
      const { code } = await sheep_api_member_auth.AuthUtil.smsLogin(state.model);
      if (code === 0) {
        sheep_hooks_useModal.closeAuthModal();
      }
    }
    return (_ctx, _cache) => {
      return {
        a: common_vendor.o(($event) => common_vendor.unref(sheep_hooks_useModal.showAuthModal)("accountLogin"), "bf"),
        b: common_vendor.t(common_vendor.unref(sheep_hooks_useModal.getSmsTimer)("smsLogin")),
        c: state.isMobileEnd || props.agreeStatus === false,
        d: state.isMobileEnd || props.agreeStatus === false ? 1 : "",
        e: common_vendor.o(checkAgreementAndGetSmsCode, "3e"),
        f: common_vendor.o(($event) => state.model.mobile = $event, "74"),
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
        i: common_vendor.o(smsLoginSubmit, "dd"),
        j: common_vendor.o(($event) => state.model.code = $event, "76"),
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
        m: common_vendor.sr(smsLoginRef, "7636e637-0", {
          "k": "smsLoginRef"
        }),
        n: common_vendor.o(($event) => state.model = $event, "23"),
        o: common_vendor.p({
          rules: state.rules,
          validateTrigger: "bind",
          labelWidth: "140",
          labelAlign: "center",
          modelValue: state.model
        })
      };
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-7636e637"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../../.sourcemap/mp-weixin/sheep/components/s-auth-modal/components/sms-login.js.map
