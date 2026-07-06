"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../../sheep/index.js");
const sheep_hooks_useGoods = require("../../../sheep/hooks/useGoods.js");
const sheep_api_promotion_seckill = require("../../../sheep/api/promotion/seckill.js");
const sheep_helper_const = require("../../../sheep/helper/const.js");
if (!Array) {
  const _easycom_su_progress2 = common_vendor.resolveComponent("su-progress");
  const _easycom_s_goods_column2 = common_vendor.resolveComponent("s-goods-column");
  const _easycom_uni_load_more2 = common_vendor.resolveComponent("uni-load-more");
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  (_easycom_su_progress2 + _easycom_s_goods_column2 + _easycom_uni_load_more2 + _easycom_s_layout2)();
}
const _easycom_su_progress = () => "../../../sheep/ui/su-progress/su-progress.js";
const _easycom_s_goods_column = () => "../../../sheep/components/s-goods-column/s-goods-column.js";
const _easycom_uni_load_more = () => "../../../uni_modules/uni-load-more/components/uni-load-more/uni-load-more.js";
const _easycom_s_layout = () => "../../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  (_easycom_su_progress + _easycom_s_goods_column + _easycom_uni_load_more + _easycom_s_layout)();
}
const _sfc_main = {
  __name: "list",
  setup(__props) {
    common_vendor.useCssVars((_ctx) => ({
      "ac2cc216": common_vendor.unref(headerBg)
    }));
    const { safeAreaInsets, safeArea } = sheep_index.sheep.$platform.device;
    const statusBarHeight = sheep_index.sheep.$platform.device.statusBarHeight * 2;
    const pageHeight = (safeArea.height + safeAreaInsets.bottom) * 2 + statusBarHeight - sheep_index.sheep.$platform.navbar - 350;
    const headerBg = sheep_index.sheep.$url.css("/static/img/shop/goods/seckill-header.png");
    const goodsFields = {
      name: {
        show: true
      },
      introduction: {
        show: true
      },
      price: {
        show: true
      },
      marketPrice: {
        show: true
      }
    };
    const timeConfigList = common_vendor.ref([]);
    const getSeckillConfigList = async () => {
      const { data } = await sheep_api_promotion_seckill.SeckillApi.getSeckillConfigList();
      const now = common_vendor.dayjs();
      const today = now.format("YYYY-MM-DD");
      const select = common_vendor.ref([]);
      data.forEach((config, index) => {
        const startTime = common_vendor.dayjs(`${today} ${config.startTime}`);
        const endTime = common_vendor.dayjs(`${today} ${config.endTime}`);
        select.value[index] = config.id;
        if (now.isBefore(startTime)) {
          config.status = sheep_helper_const.TimeStatusEnum.WAIT_START;
        } else if (now.isAfter(endTime)) {
          config.status = sheep_helper_const.TimeStatusEnum.END;
        } else {
          config.status = sheep_helper_const.TimeStatusEnum.STARTED;
          activeTimeIndex.value = index;
        }
      });
      timeConfigList.value = data;
      handleChangeTimeConfig(activeTimeIndex.value, select.value[activeTimeIndex.value]);
      scrollToTimeConfig(activeTimeIndex.value);
    };
    const activeTimeElId = common_vendor.ref("");
    const scrollToTimeConfig = (index) => {
      common_vendor.nextTick$1(() => activeTimeElId.value = `timeItem${index}`);
    };
    const activeTimeIndex = common_vendor.ref(0);
    const activeTimeConfig = common_vendor.computed(() => timeConfigList.value[activeTimeIndex.value]);
    const handleChangeTimeConfig = (index, id) => {
      activeTimeIndex.value = index;
      activityPageParams.pageNum = 1;
      activityPageParams.configId = id;
      activityList.value = [];
      getActivityList();
    };
    const countDown = common_vendor.computed(() => {
      var _a;
      const endTime = (_a = activeTimeConfig.value) == null ? void 0 : _a.endTime;
      if (endTime) {
        return sheep_hooks_useGoods.useDurationTime(`${common_vendor.dayjs().format("YYYY-MM-DD")} ${endTime}`);
      }
    });
    const activityPageParams = common_vendor.reactive({
      configId: 0,
      // 时间段 ID
      pageNum: 1,
      // 页码
      pageSize: 5
      // 每页数量
    });
    const activityTotal = common_vendor.ref(0);
    const activityList = common_vendor.ref([]);
    const loadStatus = common_vendor.ref("");
    async function getActivityList() {
      loadStatus.value = "loading";
      const { data } = await sheep_api_promotion_seckill.SeckillApi.getSeckillActivityPage(activityPageParams);
      data.list.forEach((activity) => {
        activity.percent = parseInt(
          100 * (activity.totalStock - activity.stock) / activity.totalStock
        );
      });
      activityList.value = activityList.value.concat(...data.list);
      activityTotal.value = data.total;
      loadStatus.value = activityList.value.length < activityTotal.value ? "more" : "noMore";
    }
    function loadMore() {
      if (loadStatus.value !== "noMore") {
        activityPageParams.pageNum += 1;
        getActivityList();
      }
    }
    common_vendor.onReachBottom(() => loadMore());
    common_vendor.onLoad(async () => {
      await getSeckillConfigList();
    });
    return (_ctx, _cache) => {
      var _a, _b, _c, _d, _e, _f, _g, _h, _i;
      return common_vendor.e({
        a: common_vendor.s({
          marginTop: "-" + Number(statusBarHeight + 88) + "rpx"
        }),
        b: ((_b = (_a = activeTimeConfig.value) == null ? void 0 : _a.sliderPicUrls) == null ? void 0 : _b.length) > 0
      }, ((_d = (_c = activeTimeConfig.value) == null ? void 0 : _c.sliderPicUrls) == null ? void 0 : _d.length) > 0 ? {
        c: common_vendor.f(activeTimeConfig.value.sliderPicUrls, (picUrl, index, i0) => {
          return {
            a: common_vendor.unref(sheep_index.sheep).$url.cdn(picUrl),
            b: index
          };
        })
      } : {}, {
        d: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/priceTag.png"),
        e: common_vendor.f(timeConfigList.value, (config, index, i0) => {
          return {
            a: common_vendor.t(config.startTime),
            b: common_vendor.t(config == null ? void 0 : config.status),
            c: index,
            d: common_vendor.n({
              active: activeTimeIndex.value === index
            }),
            e: `timeItem${index}`,
            f: common_vendor.o(($event) => handleChangeTimeConfig(index, config.id), index)
          };
        }),
        f: activeTimeElId.value,
        g: ((_e = activeTimeConfig.value) == null ? void 0 : _e.status) === common_vendor.unref(sheep_helper_const.TimeStatusEnum).STARTED
      }, ((_f = activeTimeConfig.value) == null ? void 0 : _f.status) === common_vendor.unref(sheep_helper_const.TimeStatusEnum).STARTED ? {
        h: common_vendor.t(countDown.value.h),
        i: common_vendor.t(countDown.value.m),
        j: common_vendor.t(countDown.value.s)
      } : {
        k: common_vendor.t((_g = activeTimeConfig.value) == null ? void 0 : _g.status)
      }, {
        l: common_vendor.f(activityList.value, (activity, k0, i0) => {
          var _a2, _b2, _c2, _d2, _e2;
          return common_vendor.e({
            a: common_vendor.t(activity.stock),
            b: common_vendor.t(activity.unitName),
            c: "de8354c5-2-" + i0 + "," + ("de8354c5-1-" + i0),
            d: common_vendor.p({
              percentage: activity.percent,
              strokeWidth: "10",
              textInside: true,
              isAnimate: true
            })
          }, ((_a2 = activeTimeConfig.value) == null ? void 0 : _a2.status) === common_vendor.unref(sheep_helper_const.TimeStatusEnum).WAIT_START ? {
            e: common_vendor.n({
              disabled: ((_b2 = activeTimeConfig.value) == null ? void 0 : _b2.status) === common_vendor.unref(sheep_helper_const.TimeStatusEnum).END
            })
          } : ((_c2 = activeTimeConfig.value) == null ? void 0 : _c2.status) === common_vendor.unref(sheep_helper_const.TimeStatusEnum).STARTED ? {
            f: common_vendor.n({
              disabled: ((_d2 = activeTimeConfig.value) == null ? void 0 : _d2.status) === common_vendor.unref(sheep_helper_const.TimeStatusEnum).END
            }),
            g: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/goods/seckill", {
              id: activity.id
            }), activity.id)
          } : {
            h: common_vendor.n({
              disabled: ((_e2 = activeTimeConfig.value) == null ? void 0 : _e2.status) === common_vendor.unref(sheep_helper_const.TimeStatusEnum).END
            })
          }, {
            i: "de8354c5-1-" + i0 + ",de8354c5-0",
            j: common_vendor.p({
              size: "lg",
              data: {
                ...activity,
                price: activity.seckillPrice
              },
              goodsFields,
              seckillTag: true
            }),
            k: activity.id
          });
        }),
        m: ((_h = activeTimeConfig.value) == null ? void 0 : _h.status) === common_vendor.unref(sheep_helper_const.TimeStatusEnum).WAIT_START,
        n: ((_i = activeTimeConfig.value) == null ? void 0 : _i.status) === common_vendor.unref(sheep_helper_const.TimeStatusEnum).STARTED,
        o: activityTotal.value > 0
      }, activityTotal.value > 0 ? {
        p: common_vendor.o(loadMore, "5f"),
        q: common_vendor.p({
          status: loadStatus.value,
          ["content-text"]: {
            contentdown: "上拉加载更多"
          }
        })
      } : {}, {
        r: pageHeight + "rpx",
        s: common_vendor.s(_ctx.__cssVars()),
        t: common_vendor.p({
          bgStyle: {
            color: "rgb(245,28,19)"
          },
          navbar: "inner"
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-de8354c5"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/activity/seckill/list.js.map
