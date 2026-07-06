"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_api_promotion_article = require("../../sheep/api/promotion/article.js");
if (!Array) {
  const _easycom_mp_html2 = common_vendor.resolveComponent("mp-html");
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  (_easycom_mp_html2 + _easycom_s_layout2)();
}
const _easycom_mp_html = () => "../../uni_modules/mp-html/components/mp-html/mp-html.js";
const _easycom_s_layout = () => "../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  (_easycom_mp_html + _easycom_s_layout)();
}
const _sfc_main = {
  __name: "richtext",
  setup(__props) {
    const state = common_vendor.reactive({
      title: "",
      content: ""
    });
    async function getRichTextContent(id, title) {
      const { code, data } = await sheep_api_promotion_article.ArticleApi.getArticle(id, title);
      if (code !== 0) {
        return;
      }
      state.content = data.content;
      if (state.title !== data.title) {
        state.title = data.title;
        common_vendor.index.setNavigationBarTitle({
          title: state.title
        });
      }
    }
    common_vendor.onLoad((options) => {
      if (options.title) {
        state.title = options.title;
        common_vendor.index.setNavigationBarTitle({
          title: state.title
        });
      }
      getRichTextContent(options.id, options.title);
    });
    return (_ctx, _cache) => {
      return {
        a: common_vendor.p({
          content: state.content
        }),
        b: common_vendor.p({
          bgStyle: {
            color: "#FFF"
          },
          title: state.title
        })
      };
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-bc551482"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/public/richtext.js.map
