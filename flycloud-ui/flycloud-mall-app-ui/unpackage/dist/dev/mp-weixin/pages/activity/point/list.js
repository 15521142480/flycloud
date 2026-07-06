"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../../sheep/index.js");
const sheep_api_promotion_point = require("../../../sheep/api/promotion/point.js");
if (!Array) {
  const _easycom_s_point_card2 = common_vendor.resolveComponent("s-point-card");
  const _easycom_s_empty2 = common_vendor.resolveComponent("s-empty");
  const _easycom_uni_load_more2 = common_vendor.resolveComponent("uni-load-more");
  (_easycom_s_point_card2 + _easycom_s_empty2 + _easycom_uni_load_more2)();
}
const _easycom_s_point_card = () => "../../../sheep/components/s-point-card/s-point-card.js";
const _easycom_s_empty = () => "../../../sheep/components/s-empty/s-empty.js";
const _easycom_uni_load_more = () => "../../../uni_modules/uni-load-more/components/uni-load-more/uni-load-more.js";
if (!Math) {
  (_easycom_s_point_card + _easycom_s_empty + _easycom_uni_load_more + SLayout)();
}
const SLayout = () => "../../../sheep/components/s-layout/s-layout.js";
const _sfc_main = {
  __name: "list",
  setup(__props) {
    const { safeAreaInsets, safeArea } = sheep_index.sheep.$platform.device;
    const statusBarHeight = sheep_index.sheep.$platform.device.statusBarHeight * 2;
    const pageHeight = (safeArea.height + safeAreaInsets.bottom) * 2 + statusBarHeight - sheep_index.sheep.$platform.navbar - 350;
    const sPointCardRef = common_vendor.ref();
    const activityPageParams = common_vendor.reactive({
      pageNum: 1,
      // 页码
      pageSize: 5
      // 每页数量
    });
    const activityTotal = common_vendor.ref(0);
    const activityCount = common_vendor.ref(0);
    const loadStatus = common_vendor.ref("");
    async function getActivityList() {
      loadStatus.value = "loading";
      const { data } = await sheep_api_promotion_point.PointApi.getPointActivityPage(activityPageParams);
      await sPointCardRef.value.concatActivity(data.list);
      activityCount.value = sPointCardRef.value.getActivityCount();
      activityTotal.value = data.total;
      loadStatus.value = activityCount.value < activityTotal.value ? "more" : "noMore";
    }
    function loadMore() {
      if (loadStatus.value !== "noMore") {
        activityPageParams.pageNum += 1;
        getActivityList();
      }
    }
    common_vendor.onReachBottom(() => {
      loadMore();
    });
    common_vendor.onLoad(() => {
      getActivityList();
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.sr(sPointCardRef, "7d55d0c1-1,7d55d0c1-0", {
          "k": "sPointCardRef"
        }),
        b: activityTotal.value === 0
      }, activityTotal.value === 0 ? {
        c: common_vendor.p({
          icon: "/static/goods-empty.png",
          text: "暂无积分商品"
        })
      } : {}, {
        d: activityTotal.value > 0
      }, activityTotal.value > 0 ? {
        e: common_vendor.o(loadMore, "c3"),
        f: common_vendor.p({
          status: loadStatus.value,
          ["content-text"]: {
            contentdown: "上拉加载更多"
          }
        })
      } : {}, {
        g: pageHeight + "rpx",
        h: common_vendor.p({
          title: "积分商城",
          navbar: "normal",
          leftWidth: 0,
          rightWidth: 0
        })
      });
    };
  }
};
wx.createPage(_sfc_main);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/activity/point/list.js.map
