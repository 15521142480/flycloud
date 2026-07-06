"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_index = require("../../sheep/index.js");
const sheep_hooks_useModal = require("../../sheep/hooks/useModal.js");
const sheep_api_member_user = require("../../sheep/api/member/user.js");
const sheep_components_sUploader_chooseAndUploadFile = require("../../sheep/components/s-uploader/choose-and-upload-file.js");
if (!Array) {
  const _easycom_su_image2 = common_vendor.resolveComponent("su-image");
  const _easycom_uni_easyinput2 = common_vendor.resolveComponent("uni-easyinput");
  const _easycom_uni_forms_item2 = common_vendor.resolveComponent("uni-forms-item");
  const _easycom_su_radio2 = common_vendor.resolveComponent("su-radio");
  const _easycom_uni_list_item2 = common_vendor.resolveComponent("uni-list-item");
  const _easycom_uni_list2 = common_vendor.resolveComponent("uni-list");
  const _easycom_uni_forms2 = common_vendor.resolveComponent("uni-forms");
  const _easycom_su_fixed2 = common_vendor.resolveComponent("su-fixed");
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  (_easycom_su_image2 + _easycom_uni_easyinput2 + _easycom_uni_forms_item2 + _easycom_su_radio2 + _easycom_uni_list_item2 + _easycom_uni_list2 + _easycom_uni_forms2 + _easycom_su_fixed2 + _easycom_s_layout2)();
}
const _easycom_su_image = () => "../../sheep/ui/su-image/su-image.js";
const _easycom_uni_easyinput = () => "../../uni_modules/uni-easyinput/components/uni-easyinput/uni-easyinput.js";
const _easycom_uni_forms_item = () => "../../uni_modules/uni-forms/components/uni-forms-item/uni-forms-item.js";
const _easycom_su_radio = () => "../../sheep/ui/su-radio/su-radio.js";
const _easycom_uni_list_item = () => "../../uni_modules/uni-list/components/uni-list-item/uni-list-item.js";
const _easycom_uni_list = () => "../../uni_modules/uni-list/components/uni-list/uni-list.js";
const _easycom_uni_forms = () => "../../uni_modules/uni-forms/components/uni-forms/uni-forms.js";
const _easycom_su_fixed = () => "../../sheep/ui/su-fixed/su-fixed.js";
const _easycom_s_layout = () => "../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  (_easycom_su_image + _easycom_uni_easyinput + _easycom_uni_forms_item + _easycom_su_radio + _easycom_uni_list_item + _easycom_uni_list + _easycom_uni_forms + _easycom_su_fixed + _easycom_s_layout)();
}
const placeholderStyle = "color:#BBBBBB;font-size:28rpx;line-height:normal";
const _sfc_main = {
  __name: "info",
  setup(__props) {
    const state = common_vendor.reactive({
      model: {},
      // 个人信息
      rules: {},
      thirdInfo: {}
      // 社交用户的信息
    });
    const sexRadioMap = [
      {
        name: "男",
        value: "1"
      },
      {
        name: "女",
        value: "2"
      }
    ];
    const userInfo = common_vendor.computed(() => sheep_index.sheep.$store("user").userInfo);
    function onChangeGender(e) {
      state.model.sex = e.detail.value;
    }
    const onChangeMobile = () => {
      sheep_hooks_useModal.showAuthModal("changeMobile");
    };
    async function onChooseAvatar(e) {
      debugger;
      const tempUrl = e.detail.avatarUrl || "";
      if (!tempUrl)
        return;
      const files = await sheep_components_sUploader_chooseAndUploadFile.uploadFilesFromPath(tempUrl);
      if (files.length > 0) {
        state.model.avatar = files[0].fileID || files[0].url;
      }
    }
    function onSetPassword() {
      sheep_hooks_useModal.showAuthModal("changePassword");
    }
    async function bindThirdOauth() {
      let result = await sheep_index.sheep.$platform.useProvider("wechat").bind();
      if (result) {
        await getUserInfo();
      }
    }
    function unBindThirdOauth() {
      common_vendor.index.showModal({
        title: "解绑提醒",
        content: "解绑后您将无法通过微信登录此账号",
        cancelText: "再想想",
        confirmText: "确定",
        success: async function(res) {
          if (!res.confirm) {
            return;
          }
          const result = await sheep_index.sheep.$platform.useProvider("wechat").unbind(state.thirdInfo.openid);
          if (result) {
            await getUserInfo();
          }
        }
      });
    }
    async function onSubmit() {
      const { code } = await sheep_api_member_user.UserApi.updateUser({
        avatar: state.model.avatar,
        nickname: state.model.nickname,
        sex: state.model.sex
      });
      if (code === 0) {
        await getUserInfo();
      }
    }
    const getUserInfo = async () => {
      const userInfo2 = await sheep_index.sheep.$store("user").getInfo();
      state.model = common_vendor.clone(userInfo2);
      if (sheep_index.sheep.$platform.name !== "H5") {
        const result = await sheep_index.sheep.$platform.useProvider("wechat").getInfo();
        state.thirdInfo = result || {};
      }
    };
    common_vendor.onBeforeMount(() => {
      getUserInfo();
    });
    return (_ctx, _cache) => {
      var _a, _b, _c, _d, _e;
      return common_vendor.e({
        a: common_vendor.p({
          isPreview: true,
          current: 0,
          src: common_vendor.unref(sheep_index.sheep).$url.cdn((_a = state.model) == null ? void 0 : _a.avatar) || common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/default_avatar.png"),
          height: 160,
          width: 160,
          radius: 80,
          mode: "scaleToFill"
        }),
        b: common_vendor.o(onChooseAvatar, "df"),
        c: common_vendor.o(($event) => state.model.nickname = $event, "c4"),
        d: common_vendor.p({
          type: "nickname",
          placeholder: "设置昵称",
          inputBorder: false,
          placeholderStyle,
          modelValue: state.model.nickname
        }),
        e: common_vendor.p({
          name: "nickname",
          label: "昵称"
        }),
        f: common_vendor.f(sexRadioMap, (item, k0, i0) => {
          var _a2;
          return {
            a: item.value,
            b: parseInt(item.value) === ((_a2 = state.model) == null ? void 0 : _a2.sex),
            c: common_vendor.t(item.name),
            d: item.value
          };
        }),
        g: common_vendor.o(onChangeGender, "e6"),
        h: common_vendor.p({
          name: "sex",
          label: "性别"
        }),
        i: (_b = userInfo.value.verification) == null ? void 0 : _b.mobile
      }, ((_c = userInfo.value.verification) == null ? void 0 : _c.mobile) ? {
        j: common_vendor.p({
          modelValue: true
        })
      } : {}, {
        k: common_vendor.o(($event) => userInfo.value.mobile = $event, "14"),
        l: common_vendor.p({
          placeholder: "请绑定手机号",
          inputBorder: false,
          disabled: true,
          styles: {
            disableColor: "#fff"
          },
          placeholderStyle,
          clearable: false,
          modelValue: userInfo.value.mobile
        }),
        m: common_vendor.o(onChangeMobile, "9e"),
        n: common_vendor.p({
          name: "mobile",
          label: "手机号"
        }),
        o: (_d = userInfo.value.verification) == null ? void 0 : _d.password
      }, ((_e = userInfo.value.verification) == null ? void 0 : _e.password) ? {
        p: common_vendor.p({
          modelValue: true
        })
      } : {}, {
        q: common_vendor.o(($event) => userInfo.value.password = $event, "01"),
        r: common_vendor.p({
          placeholder: "点击修改登录密码",
          inputBorder: false,
          styles: {
            disableColor: "#fff"
          },
          disabled: true,
          placeholderStyle: "color:#BBBBBB;font-size:28rpx;line-height:normal",
          clearable: false,
          modelValue: userInfo.value.password
        }),
        s: common_vendor.o(onSetPassword, "21"),
        t: common_vendor.p({
          name: "password",
          label: "登录密码"
        }),
        v: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/user/address/list"), "e7"),
        w: common_vendor.p({
          clickable: true,
          title: "地址管理",
          showArrow: true,
          border: false
        }),
        x: common_vendor.p({
          model: state.model,
          rules: state.rules,
          labelPosition: "left",
          border: true
        }),
        y: common_vendor.unref(sheep_index.sheep).$platform.name !== "H5"
      }, common_vendor.unref(sheep_index.sheep).$platform.name !== "H5" ? common_vendor.e({
        z: "WechatOfficialAccount" === common_vendor.unref(sheep_index.sheep).$platform.name
      }, "WechatOfficialAccount" === common_vendor.unref(sheep_index.sheep).$platform.name ? {
        A: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/platform/WechatOfficialAccount.png")
      } : {}, {
        B: "WechatMiniProgram" === common_vendor.unref(sheep_index.sheep).$platform.name
      }, "WechatMiniProgram" === common_vendor.unref(sheep_index.sheep).$platform.name ? {
        C: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/platform/WechatMiniProgram.png")
      } : {}, {
        D: "App" === common_vendor.unref(sheep_index.sheep).$platform.name
      }, "App" === common_vendor.unref(sheep_index.sheep).$platform.name ? {
        E: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/platform/wechat.png")
      } : {}, {
        F: state.thirdInfo
      }, state.thirdInfo ? {
        G: common_vendor.unref(sheep_index.sheep).$url.cdn(state.thirdInfo.avatar),
        H: common_vendor.t(state.thirdInfo.nickname)
      } : {}, {
        I: state.thirdInfo.openid
      }, state.thirdInfo.openid ? {
        J: common_vendor.o(unBindThirdOauth, "6a")
      } : {
        K: common_vendor.o(bindThirdOauth, "14")
      }) : {}, {
        L: common_vendor.o(onSubmit, "c4"),
        M: common_vendor.p({
          bottom: true,
          placeholder: true,
          bg: "none"
        }),
        N: common_vendor.p({
          title: "用户信息"
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-aab93774"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/user/info.js.map
