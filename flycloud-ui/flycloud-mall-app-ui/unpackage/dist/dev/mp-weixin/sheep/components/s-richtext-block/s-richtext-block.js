"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_api_promotion_article = require("../../api/promotion/article.js");
if (!Array) {
  const _easycom_mp_html2 = common_vendor.resolveComponent("mp-html");
  _easycom_mp_html2();
}
const _easycom_mp_html = () => "../../../uni_modules/mp-html/components/mp-html/mp-html.js";
if (!Math) {
  _easycom_mp_html();
}
const _sfc_main = {
  __name: "s-richtext-block",
  props: {
    data: {
      type: Object,
      default: {}
    },
    styles: {
      type: Object,
      default() {
      }
    }
  },
  setup(__props) {
    const props = __props;
    const state = common_vendor.reactive({
      content: ""
    });
    common_vendor.onMounted(async () => {
      const { data } = await sheep_api_promotion_article.ArticleApi.getArticle(props.data.id);
      state.content = data.content;
    });
    return (_ctx, _cache) => {
      return {
        a: common_vendor.p({
          content: state.content
        }),
        b: common_vendor.s({
          marginLeft: __props.styles.marginLeft + "px",
          marginRight: __props.styles.marginRight + "px",
          marginBottom: __props.styles.marginBottom + "px",
          marginTop: __props.styles.marginTop + "px",
          padding: __props.styles.padding + "px"
        })
      };
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-03bc3ff6"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/components/s-richtext-block/s-richtext-block.js.map
