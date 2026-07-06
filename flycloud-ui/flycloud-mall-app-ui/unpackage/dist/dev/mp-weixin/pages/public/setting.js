"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_index = require("../../sheep/index.js");
const sheep_api_member_auth = require("../../sheep/api/member/auth.js");
if (!Array) {
  const _easycom_uni_list_item2 = common_vendor.resolveComponent("uni-list-item");
  const _easycom_uni_list2 = common_vendor.resolveComponent("uni-list");
  const _easycom_su_fixed2 = common_vendor.resolveComponent("su-fixed");
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  (_easycom_uni_list_item2 + _easycom_uni_list2 + _easycom_su_fixed2 + _easycom_s_layout2)();
}
const _easycom_uni_list_item = () => "../../uni_modules/uni-list/components/uni-list-item/uni-list-item.js";
const _easycom_uni_list = () => "../../uni_modules/uni-list/components/uni-list/uni-list.js";
const _easycom_su_fixed = () => "../../sheep/ui/su-fixed/su-fixed.js";
const _easycom_s_layout = () => "../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  (_easycom_uni_list_item + _easycom_uni_list + _easycom_su_fixed + _easycom_s_layout)();
}
const _sfc_main = {
  __name: "setting",
  setup(__props) {
    const appInfo = common_vendor.computed(() => sheep_index.sheep.$store("app").info);
    const isLogin = common_vendor.computed(() => sheep_index.sheep.$store("user").isLogin);
    const storageSize = common_vendor.index.getStorageInfoSync().currentSize + "Kb";
    common_vendor.reactive({
      showModal: false
    });
    function onCheckUpdate() {
      sheep_index.sheep.$platform.checkUpdate();
    }
    function onLogoff() {
      common_vendor.index.showModal({
        title: "提示",
        content: "确认注销账号？",
        success: async function(res) {
          if (!res.confirm) {
            return;
          }
          const { code } = await sheep_api_member_auth.AuthUtil.logout();
          if (code !== 0) {
            return;
          }
          sheep_index.sheep.$store("user").logout();
          sheep_index.sheep.$router.go("/pages/index/user");
        }
      });
    }
    function onLogout() {
      common_vendor.index.showModal({
        title: "提示",
        content: "确认退出账号？",
        success: async function(res) {
          if (!res.confirm) {
            return;
          }
          const { code } = await sheep_api_member_auth.AuthUtil.logout();
          if (code !== 0) {
            return;
          }
          sheep_index.sheep.$store("user").logout();
          sheep_index.sheep.$router.go("/pages/index/user");
        }
      });
    }
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.unref(sheep_index.sheep).$url.cdn(appInfo.value.logo),
        b: common_vendor.t(appInfo.value.name),
        c: common_vendor.o(onCheckUpdate, "a9"),
        d: common_vendor.p({
          title: "当前版本",
          rightText: appInfo.value.version,
          showArrow: true,
          clickable: true,
          border: false
        }),
        e: common_vendor.p({
          title: "本地缓存",
          rightText: storageSize,
          showArrow: true,
          border: false
        }),
        f: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/public/richtext", {
          title: "关于我们"
        }), "a1"),
        g: common_vendor.p({
          title: "关于我们",
          showArrow: true,
          clickable: true,
          border: false
        }),
        h: isLogin.value && common_vendor.unref(sheep_index.sheep).$platform.os === "ios" && common_vendor.unref(sheep_index.sheep).$platform.name === "App"
      }, isLogin.value && common_vendor.unref(sheep_index.sheep).$platform.os === "ios" && common_vendor.unref(sheep_index.sheep).$platform.name === "App" ? {
        i: common_vendor.o(onLogoff, "2f"),
        j: common_vendor.p({
          title: "注销账号",
          rightText: "",
          showArrow: true,
          clickable: true,
          border: false
        })
      } : {}, {
        k: common_vendor.p({
          border: false
        }),
        l: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/public/richtext", {
          title: "用户协议"
        }), "cf"),
        m: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/public/richtext", {
          title: "隐私协议"
        }), "50"),
        n: common_vendor.t(appInfo.value.copyright),
        o: common_vendor.t(appInfo.value.copytime),
        p: isLogin.value
      }, isLogin.value ? {
        q: common_vendor.o(onLogout, "c2")
      } : {}, {
        r: common_vendor.p({
          bottom: true,
          placeholder: true
        }),
        s: common_vendor.p({
          bgStyle: {
            color: "#fff"
          },
          title: "系统设置"
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-5fed78fb"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/public/setting.js.map
