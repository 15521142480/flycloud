"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_api_trade_cart = require("../api/trade/cart.js");
const cart = common_vendor.defineStore("cart", {
  state: () => ({
    list: [],
    // 购物车列表（invalidList + validList）
    selectedIds: [],
    // 已选列表
    isAllSelected: false,
    // 是否全选
    totalPriceSelected: 0,
    // 选中项总金额
    newList: [],
    // 除去已下架的购物车列表（validList）
    editMode: false
    // 是否是编辑模式
  }),
  actions: {
    // 获取购物车列表
    async getList() {
      const { data, code } = await sheep_api_trade_cart.CartApi.getCartList();
      if (code === 0) {
        this.list = [...data.validList, ...data.invalidList];
        this.newList = data.validList;
        this.selectedIds = [];
        this.isAllSelected = true;
        this.totalPriceSelected = 0;
        (this.editMode ? this.list : this.newList).forEach((item) => {
          var _a;
          if (item.selected) {
            this.selectedIds.push(item.id);
            this.totalPriceSelected += item.count * ((_a = item.sku) == null ? void 0 : _a.price);
          } else {
            this.isAllSelected = false;
          }
        });
      }
    },
    onChangeEditMode(flag) {
      this.editMode = flag;
      this.selectedIds = [];
      this.getList();
    },
    // 添加购物车
    async add(goodsInfo) {
      const { code } = await sheep_api_trade_cart.CartApi.addCart({
        skuId: goodsInfo.id,
        count: goodsInfo.goods_num
      });
      if (code === 0) {
        await this.getList();
      }
    },
    // 更新购物车
    async update(goodsInfo) {
      const { code } = await sheep_api_trade_cart.CartApi.updateCartCount({
        id: goodsInfo.goods_id,
        count: goodsInfo.goods_num
      });
      if (code === 0) {
        await this.getList();
      }
    },
    // 移除购物车
    async delete(ids) {
      let idsTemp = "";
      if (Array.isArray(ids)) {
        idsTemp = ids.join(",");
      } else {
        idsTemp = ids;
      }
      const { code } = await sheep_api_trade_cart.CartApi.deleteCart(idsTemp);
      if (code === 0) {
        await this.getList();
      }
    },
    // 单选购物车商品
    async selectSingle(goodsId) {
      const { code } = await sheep_api_trade_cart.CartApi.updateCartSelected({
        ids: [goodsId],
        selected: !this.selectedIds.includes(goodsId)
        // 取反
      });
      if (code === 0) {
        await this.getList();
      }
    },
    // 全选购物车商品
    async selectAll(flag) {
      const { code } = await sheep_api_trade_cart.CartApi.updateCartSelected({
        ids: this.list.map((item) => item.id),
        selected: flag
      });
      if (code === 0) {
        await this.getList();
      }
    },
    // 清空购物车。注意，仅用于用户退出时，重置数据
    emptyList() {
      this.list = [];
      this.selectedIds = [];
      this.isAllSelected = true;
      this.totalPriceSelected = 0;
    }
  },
  persist: {
    enabled: true,
    strategies: [
      {
        key: "cart-store"
      }
    ]
  }
});
const __vite_glob_0_1 = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: cart
}, Symbol.toStringTag, { value: "Module" }));
exports.__vite_glob_0_1 = __vite_glob_0_1;
exports.cart = cart;
//# sourceMappingURL=../../../.sourcemap/mp-weixin/sheep/store/cart.js.map
