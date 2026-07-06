"use strict";
const common_vendor = require("../../../../common/vendor.js");
const sheep_index = require("../../../index.js");
const sheep_hooks_useModal = require("../../../hooks/useModal.js");
const sheep_api_infra_file = require("../../../api/infra/file.js");
const sheep_api_member_user = require("../../../api/member/user.js");
if (!Array) {
  const _easycom_uni_forms_item2 = common_vendor.resolveComponent("uni-forms-item");
  const _easycom_uni_easyinput2 = common_vendor.resolveComponent("uni-easyinput");
  const _easycom_uni_forms2 = common_vendor.resolveComponent("uni-forms");
  (_easycom_uni_forms_item2 + _easycom_uni_easyinput2 + _easycom_uni_forms2)();
}
const _easycom_uni_forms_item = () => "../../../../uni_modules/uni-forms/components/uni-forms-item/uni-forms-item.js";
const _easycom_uni_easyinput = () => "../../../../uni_modules/uni-easyinput/components/uni-easyinput/uni-easyinput.js";
const _easycom_uni_forms = () => "../../../../uni_modules/uni-forms/components/uni-forms/uni-forms.js";
if (!Math) {
  (_easycom_uni_forms_item + _easycom_uni_easyinput + _easycom_uni_forms)();
}
const _sfc_main = {
  __name: "mp-authorization",
  props: {
    agreeStatus: {
      type: Boolean,
      default: false
    }
  },
  setup(__props) {
    const userInfo = common_vendor.computed(() => sheep_index.sheep.$store("user").userInfo);
    const accountLoginRef = common_vendor.ref(null);
    const state = common_vendor.reactive({
      model: {
        nickname: userInfo.value.nickname,
        avatar: userInfo.value.avatar
      },
      rules: {},
      disabledStyle: {
        color: "#999",
        disableColor: "#fff"
      }
    });
    function onChooseAvatar(e) {
      const tempUrl = e.detail.avatarUrl || "";
      uploadAvatar(tempUrl);
    }
    async function uploadAvatar(tempUrl) {
      if (!tempUrl) {
        return;
      }
      const uploadResult = sheep_api_infra_file.FileApi.normalizeUploadResult(await sheep_api_infra_file.FileApi.uploadFile(tempUrl));
      state.model.avatar = uploadResult.path;
    }
    async function onConfirm() {
      const { model } = state;
      const { nickname, avatar } = model;
      if (!nickname) {
        sheep_index.sheep.$helper.toast("请输入昵称");
        return;
      }
      if (!avatar) {
        sheep_index.sheep.$helper.toast("请选择头像");
        return;
      }
      const { code } = await sheep_api_member_user.UserApi.updateUser({
        avatar: state.model.avatar,
        nickname: state.model.nickname
      });
      if (code === 0) {
        sheep_index.sheep.$helper.toast("授权成功");
        await sheep_index.sheep.$store("user").getInfo();
        sheep_hooks_useModal.closeAuthModal();
      }
    }
    return (_ctx, _cache) => {
      return {
        a: common_vendor.unref(sheep_index.sheep).$url.cdn(state.model.avatar),
        b: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/user/info"), "a1"),
        c: common_vendor.o(onChooseAvatar, "bb"),
        d: common_vendor.p({
          name: "avatar",
          label: "头像"
        }),
        e: common_vendor.o(($event) => state.model.nickname = $event, "89"),
        f: common_vendor.p({
          type: "nickname",
          placeholder: "请输入昵称",
          inputBorder: false,
          modelValue: state.model.nickname
        }),
        g: common_vendor.p({
          name: "nickname",
          label: "昵称"
        }),
        h: common_vendor.o(onConfirm, "b8"),
        i: common_vendor.sr(accountLoginRef, "bf1cb4c0-0", {
          "k": "accountLoginRef"
        }),
        j: common_vendor.o(($event) => state.model = $event, "af"),
        k: common_vendor.p({
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
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-bf1cb4c0"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../../.sourcemap/mp-weixin/sheep/components/s-auth-modal/components/mp-authorization.js.map
