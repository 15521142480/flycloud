"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_index = require("../../sheep/index.js");
const sheep_api_product_spu = require("../../sheep/api/product/spu.js");
const sheep_hooks_useGoods = require("../../sheep/hooks/useGoods.js");
const sheep_helper_utils = require("../../sheep/helper/utils.js");
if (!Array) {
  const _easycom_s_empty2 = common_vendor.resolveComponent("s-empty");
  const _easycom_su_number_box2 = common_vendor.resolveComponent("su-number-box");
  const _easycom_s_goods_item2 = common_vendor.resolveComponent("s-goods-item");
  const _easycom_su_fixed2 = common_vendor.resolveComponent("su-fixed");
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  (_easycom_s_empty2 + _easycom_su_number_box2 + _easycom_s_goods_item2 + _easycom_su_fixed2 + _easycom_s_layout2)();
}
const _easycom_s_empty = () => "../../sheep/components/s-empty/s-empty.js";
const _easycom_su_number_box = () => "../../sheep/ui/su-number-box/su-number-box.js";
const _easycom_s_goods_item = () => "../../sheep/components/s-goods-item/s-goods-item.js";
const _easycom_su_fixed = () => "../../sheep/ui/su-fixed/su-fixed.js";
const _easycom_s_layout = () => "../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  (_easycom_s_empty + _easycom_su_number_box + _easycom_s_goods_item + _easycom_su_fixed + _easycom_s_layout)();
}
const _sfc_main = {
  __name: "cart",
  setup(__props) {
    common_vendor.useCssVars((_ctx) => ({
      "6cd98758": common_vendor.unref(sys_navBar)
    }));
    common_vendor.index.hideTabBar({
      fail: () => {
      }
    });
    const sys_navBar = sheep_index.sheep.$platform.navbar;
    const cart = sheep_index.sheep.$store("cart");
    const state = common_vendor.reactive({
      editMode: common_vendor.computed(() => cart.editMode),
      list: common_vendor.computed(() => cart.list),
      selectedList: [],
      selectedIds: common_vendor.computed(() => cart.selectedIds),
      isAllSelected: common_vendor.computed(() => cart.isAllSelected),
      totalPriceSelected: common_vendor.computed(() => cart.totalPriceSelected)
    });
    function onSelectSingle(id) {
      cart.selectSingle(id);
    }
    function onChangeEditMode(flag) {
      cart.onChangeEditMode(flag);
    }
    function onSelectAll() {
      cart.selectAll(!state.isAllSelected);
    }
    async function onConfirm() {
      const items = [];
      state.selectedList = state.list.filter((item) => state.selectedIds.includes(item.id));
      state.selectedList.map((item) => {
        items.push({
          skuId: item.sku.id,
          count: item.count,
          cartId: item.id,
          categoryId: item.spu.categoryId
        });
      });
      if (sheep_helper_utils.isEmpty(items)) {
        sheep_index.sheep.$helper.toast("请先选择商品");
        return;
      }
      await validateDeliveryType(state.selectedList.map((item) => item.spu).map((spu) => spu.id));
      sheep_index.sheep.$router.go("/pages/order/confirm", {
        data: JSON.stringify({
          items
        })
      });
    }
    async function validateDeliveryType(spuIds) {
      const { data: spuList } = await sheep_api_product_spu.SpuApi.getSpuListByIds(spuIds.join(","));
      if (sheep_helper_utils.isEmpty(spuList)) {
        sheep_index.sheep.$helper.toast("未找到商品信息");
        throw new Error("未找到商品信息");
      }
      const deliveryTypesList = spuList.map((item) => item.deliveryTypes);
      const hasConflict = checkDeliveryConflicts(deliveryTypesList);
      if (hasConflict) {
        sheep_index.sheep.$helper.toast("选中商品支持的配送方式冲突，不允许提交");
        throw new Error("选中商品支持的配送方式冲突，不允许提交");
      }
    }
    function checkDeliveryConflicts(deliveryTypesList) {
      for (let i = 0; i < deliveryTypesList.length - 1; i++) {
        const currentTypes = deliveryTypesList[i];
        for (let j = i + 1; j < deliveryTypesList.length; j++) {
          const nextTypes = deliveryTypesList[j];
          const hasNoIntersection = !currentTypes.some((type) => nextTypes.includes(type));
          if (hasNoIntersection) {
            return true;
          }
        }
      }
      return false;
    }
    function onNumberChange(e, cartItem) {
      if (e === 0) {
        cart.delete(cartItem.id);
        return;
      }
      if (cartItem.goods_num === e)
        return;
      cartItem.goods_num = e;
      cart.update({
        goods_id: cartItem.id,
        goods_num: e,
        goods_sku_price_id: cartItem.goods_sku_price_id
      });
    }
    async function onDelete() {
      cart.delete(state.selectedIds);
    }
    function getCartList() {
      cart.getList();
    }
    common_vendor.onShow(() => {
      getCartList();
    });
    return (_ctx, _cache) => {
      var _a;
      return common_vendor.e({
        a: state.list.length === 0
      }, state.list.length === 0 ? {
        b: common_vendor.p({
          icon: "/static/cart-empty.png",
          text: "购物车空空如也,快去逛逛吧~"
        })
      } : {}, {
        c: state.list.length
      }, state.list.length ? common_vendor.e({
        d: common_vendor.t(state.list.length),
        e: state.editMode
      }, state.editMode ? {
        f: common_vendor.o(($event) => onChangeEditMode(false), "cb")
      } : {
        g: common_vendor.o(($event) => onChangeEditMode(true), "70")
      }, {
        h: common_vendor.f(state.list, (item, k0, i0) => {
          var _a2, _b, _c, _d;
          return common_vendor.e({
            a: state.selectedIds.includes(item.id),
            b: common_vendor.o(($event) => onSelectSingle(item.id), item.id),
            c: common_vendor.o(($event) => onSelectSingle(item.id), item.id),
            d: ((_a2 = item.spu) == null ? void 0 : _a2.status) !== 1 && !state.editMode
          }, ((_b = item.spu) == null ? void 0 : _b.status) !== 1 && !state.editMode ? {} : ((_c = item.spu) == null ? void 0 : _c.stock) <= 0 && !state.editMode ? {} : {}, {
            e: ((_d = item.spu) == null ? void 0 : _d.stock) <= 0 && !state.editMode
          }, !state.editMode ? {
            f: common_vendor.o(($event) => onNumberChange($event, item), item.id),
            g: "c30a4c2b-3-" + i0 + "," + ("c30a4c2b-2-" + i0),
            h: common_vendor.o(($event) => item.count = $event, item.id),
            i: common_vendor.p({
              max: item.sku.stock,
              min: 0,
              step: 1,
              modelValue: item.count
            })
          } : {}, {
            j: "c30a4c2b-2-" + i0 + ",c30a4c2b-0",
            k: common_vendor.p({
              img: item.spu.picUrl || item.goods.image,
              price: item.sku.price,
              skuText: item.sku.properties.length > 1 ? item.sku.properties.reduce((items2, items) => items2.valueName + " " + items.valueName) : item.sku.properties[0].valueName,
              title: item.spu.name,
              titleWidth: 400,
              priceColor: "#FF3000"
            }),
            l: item.id
          });
        }),
        i: !state.editMode,
        j: state.list.length > 0
      }, state.list.length > 0 ? common_vendor.e({
        k: state.isAllSelected,
        l: common_vendor.o(onSelectAll, "79"),
        m: common_vendor.o(onSelectAll, "42"),
        n: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(state.totalPriceSelected)),
        o: state.editMode
      }, state.editMode ? {
        p: common_vendor.o(onDelete, "be")
      } : {
        q: common_vendor.t(((_a = state.selectedIds) == null ? void 0 : _a.length) ? `(${state.selectedIds.length})` : ""),
        r: common_vendor.o(onConfirm, "02")
      }, {
        s: common_vendor.p({
          isInset: false,
          val: 48,
          bottom: true,
          placeholder: true
        })
      }) : {}) : {}, {
        t: common_vendor.s(_ctx.__cssVars()),
        v: common_vendor.p({
          bgStyle: {
            color: "#fff"
          },
          tabbar: "/pages/index/cart",
          title: "购物车"
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-c30a4c2b"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/index/cart.js.map
