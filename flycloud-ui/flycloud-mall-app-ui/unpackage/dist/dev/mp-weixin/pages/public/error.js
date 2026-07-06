"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_index = require("../../sheep/index.js");
if (!Array) {
  const _easycom_s_empty2 = common_vendor.resolveComponent("s-empty");
  _easycom_s_empty2();
}
const _easycom_s_empty = () => "../../sheep/components/s-empty/s-empty.js";
if (!Math) {
  _easycom_s_empty();
}
const _sfc_main = {
  __name: "error",
  setup(__props) {
    const errCode = common_vendor.ref("");
    const errMsg = common_vendor.ref("");
    common_vendor.onLoad((options) => {
      errCode.value = options.errCode;
      errMsg.value = options.errMsg;
    });
    async function onReconnect() {
      common_vendor.index.reLaunch({
        url: "/pages/index/index"
      });
      await sheep_index.ShoproInit();
    }
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: errCode.value === "NetworkError"
      }, errCode.value === "NetworkError" ? {
        b: common_vendor.o(onReconnect, "a2"),
        c: common_vendor.p({
          icon: "/static/internet-empty.png",
          text: "网络连接失败",
          showAction: true,
          actionText: "重新连接",
          buttonColor: "#ff3000"
        })
      } : errCode.value === "TemplateError" ? {
        e: common_vendor.o(onReconnect, "66"),
        f: common_vendor.p({
          icon: "/static/internet-empty.png",
          text: "未找到模板,请前往后台启用对应模板",
          showAction: true,
          actionText: "重新加载",
          buttonColor: "#ff3000"
        })
      } : errCode.value !== "" ? {
        h: common_vendor.o(onReconnect, "a9"),
        i: common_vendor.p({
          icon: "/static/internet-empty.png",
          text: errMsg.value,
          showAction: true,
          actionText: "重新加载",
          buttonColor: "#ff3000"
        })
      } : {}, {
        d: errCode.value === "TemplateError",
        g: errCode.value !== ""
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-8e014bcf"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/public/error.js.map
