"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_index = require("../../sheep/index.js");
const sheep_platform_share = require("../../sheep/platform/share.js");
const sheep_hooks_useModal = require("../../sheep/hooks/useModal.js");
const sheep_api_product_spu = require("../../sheep/api/product/spu.js");
const sheep_api_trade_brokerage = require("../../sheep/api/trade/brokerage.js");
const sheep_hooks_useGoods = require("../../sheep/hooks/useGoods.js");
if (!Array) {
  const _easycom_s_goods_item2 = common_vendor.resolveComponent("s-goods-item");
  const _easycom_s_empty2 = common_vendor.resolveComponent("s-empty");
  const _easycom_uni_load_more2 = common_vendor.resolveComponent("uni-load-more");
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  (_easycom_s_goods_item2 + _easycom_s_empty2 + _easycom_uni_load_more2 + _easycom_s_layout2)();
}
const _easycom_s_goods_item = () => "../../sheep/components/s-goods-item/s-goods-item.js";
const _easycom_s_empty = () => "../../sheep/components/s-empty/s-empty.js";
const _easycom_uni_load_more = () => "../../uni_modules/uni-load-more/components/uni-load-more/uni-load-more.js";
const _easycom_s_layout = () => "../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  (_easycom_s_goods_item + _easycom_s_empty + _easycom_uni_load_more + _easycom_s_layout)();
}
const _sfc_main = {
  __name: "goods",
  setup(__props) {
    const state = common_vendor.reactive({
      pagination: {
        list: [],
        total: 0,
        pageNum: 1,
        pageSize: 8
      },
      loadStatus: "",
      shareInfo: {}
    });
    function onShareGoods(goodsInfo) {
      state.shareInfo = sheep_platform_share.$share.getShareInfo(
        {
          title: goodsInfo.name,
          image: sheep_index.sheep.$url.cdn(goodsInfo.picUrl),
          desc: goodsInfo.introduction,
          params: {
            page: "2",
            query: goodsInfo.id
          }
        },
        {
          type: "goods",
          // 商品海报
          title: goodsInfo.name,
          // 商品名称
          image: sheep_index.sheep.$url.cdn(goodsInfo.picUrl),
          // 商品主图
          price: sheep_hooks_useGoods.fen2yuan(goodsInfo.price),
          // 商品价格
          original_price: sheep_hooks_useGoods.fen2yuan(goodsInfo.marketPrice)
          // 商品原价
        }
      );
      sheep_hooks_useModal.showShareModal();
    }
    async function getGoodsList() {
      state.loadStatus = "loading";
      let { code, data } = await sheep_api_product_spu.SpuApi.getSpuPage({
        pageSize: state.pagination.pageSize,
        pageNum: state.pagination.pageNum
      });
      if (code !== 0) {
        state.loadStatus = "error";
        return;
      }
      await Promise.all(
        data.list.map(async (item) => {
          try {
            const res = await sheep_api_trade_brokerage.BrokerageApi.getProductBrokeragePrice(item.id);
            item.brokerageMinPrice = res.data.brokerageMinPrice;
            item.brokerageMaxPrice = res.data.brokerageMaxPrice;
          } catch (error) {
            common_vendor.index.__f__("error", "at pages/commission/goods.vue:121", `获取商品【${item.name}】的佣金时出错：`, error);
          }
        })
      );
      state.pagination.list = common_vendor.concat(state.pagination.list, data.list);
      state.pagination.total = data.total;
      state.loadStatus = state.pagination.list.length < state.pagination.total ? "more" : "noMore";
    }
    common_vendor.onLoad(() => {
      getGoodsList();
    });
    function loadMore() {
      if (state.loadStatus === "noMore") {
        return;
      }
      state.pagination.pageNum++;
      getGoodsList();
    }
    common_vendor.onReachBottom(() => {
      loadMore();
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.f(state.pagination.list, (item, k0, i0) => {
          return common_vendor.e({
            a: item.brokerageMinPrice === void 0
          }, item.brokerageMinPrice === void 0 ? {} : item.brokerageMinPrice === item.brokerageMaxPrice ? {
            c: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(item.brokerageMinPrice))
          } : {
            d: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(item.brokerageMinPrice)),
            e: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(item.brokerageMaxPrice))
          }, {
            b: item.brokerageMinPrice === item.brokerageMaxPrice,
            f: common_vendor.o(($event) => onShareGoods(item), item.id),
            g: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/goods/index", {
              id: item.id
            }), item.id),
            h: "1b0a4b5d-1-" + i0 + ",1b0a4b5d-0",
            i: common_vendor.p({
              size: "lg",
              img: item.picUrl,
              title: item.name,
              subTitle: item.introduction,
              price: item.price,
              originPrice: item.marketPrice,
              priceColor: "#333"
            }),
            j: item.id
          });
        }),
        b: state.pagination.total === 0
      }, state.pagination.total === 0 ? {
        c: common_vendor.p({
          icon: "/static/goods-empty.png",
          text: "暂无推广商品"
        })
      } : {}, {
        d: state.pagination.total > 0
      }, state.pagination.total > 0 ? {
        e: common_vendor.o(loadMore, "dc"),
        f: common_vendor.p({
          status: state.loadStatus,
          ["content-text"]: {
            contentdown: "上拉加载更多"
          }
        })
      } : {}, {
        g: common_vendor.p({
          title: "推广商品",
          onShareAppMessage: state.shareInfo
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-1b0a4b5d"]]);
_sfc_main.__runtimeHooks = 2;
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/commission/goods.js.map
