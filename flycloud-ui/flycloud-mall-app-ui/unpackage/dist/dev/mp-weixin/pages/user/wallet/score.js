"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../../sheep/index.js");
const sheep_api_member_point = require("../../../sheep/api/member/point.js");
const sheep_helper_utils = require("../../../sheep/helper/utils.js");
if (!Array) {
  const _easycom_uni_datetime_picker2 = common_vendor.resolveComponent("uni-datetime-picker");
  const _easycom_su_tabs2 = common_vendor.resolveComponent("su-tabs");
  const _easycom_su_sticky2 = common_vendor.resolveComponent("su-sticky");
  const _easycom_s_empty2 = common_vendor.resolveComponent("s-empty");
  const _easycom_uni_load_more2 = common_vendor.resolveComponent("uni-load-more");
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  (_easycom_uni_datetime_picker2 + _easycom_su_tabs2 + _easycom_su_sticky2 + _easycom_s_empty2 + _easycom_uni_load_more2 + _easycom_s_layout2)();
}
const _easycom_uni_datetime_picker = () => "../../../uni_modules/uni-datetime-picker/components/uni-datetime-picker/uni-datetime-picker.js";
const _easycom_su_tabs = () => "../../../sheep/ui/su-tabs/su-tabs.js";
const _easycom_su_sticky = () => "../../../sheep/ui/su-sticky/su-sticky.js";
const _easycom_s_empty = () => "../../../sheep/components/s-empty/s-empty.js";
const _easycom_uni_load_more = () => "../../../uni_modules/uni-load-more/components/uni-load-more/uni-load-more.js";
const _easycom_s_layout = () => "../../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  (_easycom_uni_datetime_picker + _easycom_su_tabs + _easycom_su_sticky + _easycom_s_empty + _easycom_uni_load_more + _easycom_s_layout)();
}
const _sfc_main = {
  __name: "score",
  setup(__props) {
    const statusBarHeight = sheep_index.sheep.$platform.device.statusBarHeight * 2;
    const userInfo = common_vendor.computed(() => sheep_index.sheep.$store("user").userInfo);
    const sys_navBar = sheep_index.sheep.$platform.navbar;
    const state = common_vendor.reactive({
      currentTab: 0,
      pagination: {
        list: 0,
        total: 0,
        pageSize: 6,
        pageNum: 1
      },
      loadStatus: "",
      date: [],
      today: ""
    });
    const tabMaps = [
      {
        name: "全部",
        value: "all"
      },
      {
        name: "收入",
        value: "true"
      },
      {
        name: "支出",
        value: "false"
      }
    ];
    const dateFilterText = common_vendor.computed(() => {
      if (state.date[0] === state.date[1]) {
        return state.date[0];
      } else {
        return state.date.join("~");
      }
    });
    async function getLogList() {
      state.loadStatus = "loading";
      let { code, data } = await sheep_api_member_point.PointApi.getPointRecordPage({
        pageNum: state.pagination.pageNum,
        pageSize: state.pagination.pageSize,
        addStatus: state.currentTab > 0 ? tabMaps[state.currentTab].value : void 0,
        "createTime[0]": state.date[0] + " 00:00:00",
        "createTime[1]": state.date[1] + " 23:59:59"
      });
      if (code !== 0) {
        return;
      }
      state.pagination.list = common_vendor.concat(state.pagination.list, data.list);
      state.pagination.total = data.total;
      state.loadStatus = state.pagination.list.length < state.pagination.total ? "more" : "noMore";
    }
    common_vendor.onLoad(() => {
      state.today = common_vendor.dayjs().format("YYYY-MM-DD");
      state.date = [state.today, state.today];
      getLogList();
    });
    function onChange(e) {
      state.currentTab = e.index;
      sheep_helper_utils.resetPagination(state.pagination);
      getLogList();
    }
    function onChangeTime(e) {
      state.date[0] = e[0];
      state.date[1] = e[e.length - 1];
      sheep_helper_utils.resetPagination(state.pagination);
      getLogList();
    }
    function onLoadMore() {
      if (state.loadStatus === "noMore") {
        return;
      }
      state.pagination.pageNum++;
      getLogList();
    }
    common_vendor.onReachBottom(() => {
      onLoadMore();
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.t(userInfo.value.point || 0),
        b: common_vendor.s({
          marginTop: "-" + Number(statusBarHeight + 88) + "rpx",
          paddingTop: Number(statusBarHeight + 88) + "rpx"
        }),
        c: common_vendor.t(dateFilterText.value),
        d: common_vendor.o(onChangeTime, "2f"),
        e: common_vendor.o(($event) => state.date = $event, "78"),
        f: common_vendor.p({
          type: "daterange",
          end: state.today,
          modelValue: state.date
        }),
        g: common_vendor.o(onChange, "3b"),
        h: common_vendor.p({
          list: tabMaps,
          scrollable: false,
          current: state.currentTab
        }),
        i: common_vendor.p({
          customNavHeight: common_vendor.unref(sys_navBar)
        }),
        j: state.pagination.total > 0
      }, state.pagination.total > 0 ? {
        k: common_vendor.f(state.pagination.list, (item, k0, i0) => {
          return common_vendor.e({
            a: common_vendor.t(item.title),
            b: common_vendor.t(item.description ? " - " + item.description : ""),
            c: common_vendor.t(common_vendor.unref(sheep_index.sheep).$helper.timeFormat(item.createTime, "yyyy-mm-dd hh:MM:ss")),
            d: item.point > 0
          }, item.point > 0 ? {
            e: common_vendor.t(item.point)
          } : {
            f: common_vendor.t(item.point)
          }, {
            g: item.id
          });
        })
      } : {
        l: common_vendor.p({
          text: "暂无数据",
          icon: "/static/data-empty.png"
        })
      }, {
        m: state.pagination.total > 0
      }, state.pagination.total > 0 ? {
        n: common_vendor.o(onLoadMore, "81"),
        o: common_vendor.p({
          status: state.loadStatus,
          ["content-text"]: {
            contentdown: "上拉加载更多"
          }
        })
      } : {}, {
        p: common_vendor.p({
          title: "我的积分",
          navbar: "inner"
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-aa108703"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/user/wallet/score.js.map
