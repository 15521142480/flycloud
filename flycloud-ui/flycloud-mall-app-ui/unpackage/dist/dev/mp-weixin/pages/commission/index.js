"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_index = require("../../sheep/index.js");
const sheep_helper_const = require("../../sheep/helper/const.js");
if (!Array) {
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  _easycom_s_layout2();
}
const _easycom_s_layout = () => "../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  (commissionInfo + accountInfo + commissionMenu + commissionLog + commissionAuth + _easycom_s_layout)();
}
const commissionInfo = () => "./components/commission-info.js";
const accountInfo = () => "./components/account-info.js";
const commissionLog = () => "./components/commission-log.js";
const commissionMenu = () => "./components/commission-menu.js";
const commissionAuth = () => "./components/commission-auth.js";
const _sfc_main = {
  __name: "index",
  setup(__props) {
    const shareInfo = common_vendor.computed(() => {
      return sheep_index.sheep.$platform.share.getShareInfo(
        {
          params: {
            page: sheep_helper_const.SharePageEnum.HOME.value
            // 用户通邀请进入到首页
          }
        },
        {
          type: "user"
        }
      );
    });
    const bgStyle = {
      color: "#F7D598"
    };
    return (_ctx, _cache) => {
      return {
        a: common_vendor.p({
          navbar: "inner",
          title: "分销中心",
          bgStyle,
          onShareAppMessage: shareInfo.value
        })
      };
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-a5d04a00"]]);
_sfc_main.__runtimeHooks = 2;
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/commission/index.js.map
