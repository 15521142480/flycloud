"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_index = require("../../sheep/index.js");
const sheep_api_member_signin = require("../../sheep/api/member/signin.js");
if (!Array) {
  const _easycom_s_empty2 = common_vendor.resolveComponent("s-empty");
  const _easycom_su_popup2 = common_vendor.resolveComponent("su-popup");
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  (_easycom_s_empty2 + _easycom_su_popup2 + _easycom_s_layout2)();
}
const _easycom_s_empty = () => "../../sheep/components/s-empty/s-empty.js";
const _easycom_su_popup = () => "../../sheep/ui/su-popup/su-popup.js";
const _easycom_s_layout = () => "../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  (_easycom_s_empty + _easycom_su_popup + _easycom_s_layout)();
}
const _sfc_main = {
  __name: "sign",
  setup(__props) {
    common_vendor.useCssVars((_ctx) => ({
      "19707c8c": common_vendor.unref(headerBg)
    }));
    const headerBg = sheep_index.sheep.$url.css("/static/img/shop/app/sign.png");
    const state = common_vendor.reactive({
      loading: true,
      signInfo: {},
      // 签到信息
      signConfigList: [],
      // 签到配置列表
      maxDay: 0,
      // 最大的签到天数
      showModel: false,
      // 签到弹框
      signResult: {}
      // 签到结果
    });
    async function onSign() {
      const { code, data } = await sheep_api_member_signin.SignInApi.createSignInRecord();
      if (code !== 0) {
        return;
      }
      state.showModel = true;
      state.signResult = data;
      await getSignInfo();
    }
    function onConfirm() {
      state.showModel = false;
    }
    async function getSignInfo() {
      const { code, data } = await sheep_api_member_signin.SignInApi.getSignInRecordSummary();
      if (code !== 0) {
        return;
      }
      state.signInfo = data;
      state.loading = false;
    }
    async function getSignConfigList() {
      const { code, data } = await sheep_api_member_signin.SignInApi.getSignInConfigList();
      if (code !== 0) {
        return;
      }
      state.signConfigList = data;
      if (data.length > 0) {
        state.maxDay = data[data.length - 1].day;
      }
    }
    common_vendor.onReady(() => {
      getSignInfo();
      getSignConfigList();
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: state.loading
      }, state.loading ? {
        b: common_vendor.p({
          icon: "/static/data-empty.png",
          text: "签到活动还未开始"
        })
      } : {}, {
        c: state.loading
      }, state.loading ? {} : !state.loading ? common_vendor.e({
        e: common_vendor.t(state.signInfo.continuousDay),
        f: common_vendor.f(state.signConfigList, (item, index, i0) => {
          return {
            a: common_vendor.t(item.day),
            b: common_vendor.n((index === state.signConfigList.length ? "reward" : "") + " " + (state.signInfo.continuousDay >= item.day ? "rewardTxt" : "")),
            c: common_vendor.n((index + 1 === state.signConfigList.length ? "reward" : "") + " " + (state.signInfo.continuousDay >= item.day ? "venusSelect" : "")),
            d: common_vendor.t(item.point),
            e: common_vendor.n(state.signInfo.continuousDay >= item.day ? "on" : ""),
            f: index
          };
        }),
        g: !state.signInfo.todaySignIn
      }, !state.signInfo.todaySignIn ? {
        h: common_vendor.o(onSign, "ae")
      } : {}, {
        i: common_vendor.t(state.signInfo.totalDay),
        j: common_vendor.t(state.maxDay)
      }) : {}, {
        d: !state.loading,
        k: state.signResult.point
      }, state.signResult.point ? {
        l: common_vendor.t(state.signResult.point)
      } : {}, {
        m: state.signResult.experience
      }, state.signResult.experience ? {
        n: common_vendor.t(state.signResult.experience)
      } : {}, {
        o: common_vendor.t(state.signResult.day),
        p: common_vendor.o(onConfirm, "2d"),
        q: common_vendor.p({
          show: state.showModel,
          type: "center",
          round: "10",
          isMaskClick: false
        }),
        r: common_vendor.s(_ctx.__cssVars()),
        s: common_vendor.p({
          title: "签到有礼"
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-964858d2"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/app/sign.js.map
