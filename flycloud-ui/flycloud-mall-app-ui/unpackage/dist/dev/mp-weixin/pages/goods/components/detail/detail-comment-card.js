"use strict";
const common_vendor = require("../../../../common/vendor.js");
const sheep_index = require("../../../../sheep/index.js");
const sheep_api_product_comment = require("../../../../sheep/api/product/comment.js");
if (!Array) {
  const _easycom_s_empty2 = common_vendor.resolveComponent("s-empty");
  _easycom_s_empty2();
}
const _easycom_s_empty = () => "../../../../sheep/components/s-empty/s-empty.js";
if (!Math) {
  (commentItem + _easycom_s_empty)();
}
const commentItem = () => "./comment-item.js";
const _sfc_main = {
  __name: "detail-comment-card",
  props: {
    goodsId: {
      type: [Number, String],
      default: 0
    }
  },
  setup(__props) {
    const props = __props;
    const state = common_vendor.reactive({
      commentList: [],
      // 评论列表，只展示最近的 3 条
      total: 0
      // 总评论数
    });
    async function getComment(id) {
      const { data } = await sheep_api_product_comment.CommentApi.getCommentPage(id, 1, 3, 0);
      state.commentList = data.list;
      state.total = data.total;
    }
    common_vendor.onBeforeMount(() => {
      getComment(props.goodsId);
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.t(state.total),
        b: state.commentList.length > 0
      }, state.commentList.length > 0 ? {
        c: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/goods/comment/list", {
          id: __props.goodsId
        }), "01")
      } : {}, {
        d: common_vendor.f(state.commentList, (item, k0, i0) => {
          return {
            a: "489ea6d9-0-" + i0,
            b: common_vendor.p({
              item
            }),
            c: item.id
          };
        }),
        e: state.commentList.length === 0
      }, state.commentList.length === 0 ? {
        f: common_vendor.p({
          paddingTop: "0",
          icon: "/static/comment-empty.png",
          text: "期待您的第一个评价"
        })
      } : {});
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-489ea6d9"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../../.sourcemap/mp-weixin/pages/goods/components/detail/detail-comment-card.js.map
