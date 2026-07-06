"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_api_product_comment = require("../../../sheep/api/product/comment.js");
if (!Array) {
  const _easycom_su_tabs2 = common_vendor.resolveComponent("su-tabs");
  const _easycom_s_empty2 = common_vendor.resolveComponent("s-empty");
  const _easycom_uni_load_more2 = common_vendor.resolveComponent("uni-load-more");
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  (_easycom_su_tabs2 + _easycom_s_empty2 + _easycom_uni_load_more2 + _easycom_s_layout2)();
}
const _easycom_su_tabs = () => "../../../sheep/ui/su-tabs/su-tabs.js";
const _easycom_s_empty = () => "../../../sheep/components/s-empty/s-empty.js";
const _easycom_uni_load_more = () => "../../../uni_modules/uni-load-more/components/uni-load-more/uni-load-more.js";
const _easycom_s_layout = () => "../../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  (_easycom_su_tabs + commentItem + _easycom_s_empty + _easycom_uni_load_more + _easycom_s_layout)();
}
const commentItem = () => "../components/detail/comment-item.js";
const _sfc_main = {
  __name: "list",
  setup(__props) {
    const state = common_vendor.reactive({
      id: 0,
      // 商品 SPU 编号
      type: [
        { type: 0, name: "全部" },
        { type: 1, name: "好评" },
        { type: 2, name: "中评" },
        { type: 3, name: "差评" }
      ],
      currentTab: 0,
      // 选中的 TAB
      loadStatus: "",
      pagination: {
        list: [],
        total: 0,
        pageNum: 1,
        pageSize: 8
      }
    });
    function onTabsChange(e) {
      state.currentTab = e.index;
      state.pagination.pageNum = 1;
      state.pagination.list = [];
      state.pagination.total = 0;
      getList();
    }
    async function getList() {
      state.loadStatus = "loading";
      let res = await sheep_api_product_comment.CommentApi.getCommentPage(
        state.id,
        state.pagination.pageNum,
        state.pagination.pageSize,
        state.type[state.currentTab].type
      );
      if (res.code !== 0) {
        return;
      }
      state.pagination.list = common_vendor.concat(state.pagination.list, res.data.list);
      state.pagination.total = res.data.total;
      state.loadStatus = state.pagination.list.length < state.pagination.total ? "more" : "noMore";
    }
    function loadMore() {
      if (state.loadStatus === "noMore") {
        return;
      }
      state.pagination.pageNum++;
      getList();
    }
    common_vendor.onLoad((options) => {
      state.id = options.id;
      getList();
    });
    common_vendor.onReachBottom(() => {
      loadMore();
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.o(onTabsChange, "5d"),
        b: common_vendor.p({
          list: state.type,
          scrollable: false,
          current: state.currentTab
        }),
        c: common_vendor.f(state.pagination.list, (item, k0, i0) => {
          return {
            a: "0df9e865-2-" + i0 + ",0df9e865-0",
            b: common_vendor.p({
              item
            }),
            c: item
          };
        }),
        d: state.pagination.total === 0
      }, state.pagination.total === 0 ? {
        e: common_vendor.p({
          text: "暂无数据",
          icon: "/static/data-empty.png"
        })
      } : {}, {
        f: state.pagination.total > 0
      }, state.pagination.total > 0 ? {
        g: common_vendor.o(loadMore, "98"),
        h: common_vendor.p({
          ["icon-type"]: "auto",
          status: state.loadStatus,
          ["content-text"]: {
            contentdown: "上拉加载更多"
          }
        })
      } : {}, {
        i: common_vendor.p({
          title: "全部评论"
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-0df9e865"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/goods/comment/list.js.map
