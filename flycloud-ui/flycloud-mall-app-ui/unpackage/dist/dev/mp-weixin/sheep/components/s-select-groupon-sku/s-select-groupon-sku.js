"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../index.js");
const sheep_hooks_useGoods = require("../../hooks/useGoods.js");
if (!Array) {
  const _easycom_su_number_box2 = common_vendor.resolveComponent("su-number-box");
  const _easycom_su_popup2 = common_vendor.resolveComponent("su-popup");
  (_easycom_su_number_box2 + _easycom_su_popup2)();
}
const _easycom_su_number_box = () => "../../ui/su-number-box/su-number-box.js";
const _easycom_su_popup = () => "../../ui/su-popup/su-popup.js";
if (!Math) {
  (_easycom_su_number_box + _easycom_su_popup)();
}
const _sfc_main = {
  __name: "s-select-groupon-sku",
  props: {
    show: {
      type: Boolean,
      default: false
    },
    goodsInfo: {
      type: Object,
      default() {
      }
    },
    grouponAction: {
      type: String,
      default: "create"
    },
    grouponNum: {
      type: [Number, String],
      default: 0
    }
  },
  emits: ["change", "addCart", "buy", "close", "ladder"],
  setup(__props, { emit: __emit }) {
    common_vendor.useCssVars((_ctx) => ({
      "478364b0": common_vendor.unref(headerBg)
    }));
    const headerBg = sheep_index.sheep.$url.css("/static/img/shop/goods/groupon-btn-long.png");
    const emits = __emit;
    const props = __props;
    const state = common_vendor.reactive({
      selectedSku: {},
      // 选中的 SKU
      currentPropertyArray: {},
      // 当前选中的属性，实际是个 Map。key 是 property 编号，value 是 value 编号
      grouponNum: props.grouponNum
    });
    const propertyList = sheep_hooks_useGoods.convertProductPropertyList(props.goodsInfo.skus);
    const skuList = common_vendor.computed(() => {
      let skuPrices = props.goodsInfo.skus;
      for (let price of skuPrices) {
        price.value_id_array = price.properties.map((item) => item.valueId);
      }
      return skuPrices;
    });
    common_vendor.watch(
      () => state.selectedSku,
      (newVal) => {
        emits("change", newVal);
      },
      {
        immediate: true,
        // 立即执行
        deep: true
        // 深度监听
      }
    );
    function onNumberChange(e) {
      if (e === 0)
        return;
      if (state.selectedSku.count === e)
        return;
      state.selectedSku.count = e;
    }
    function onBuy() {
      if (!state.selectedSku.id || state.selectedSku.id <= 0) {
        sheep_index.sheep.$helper.toast("请选择规格");
        return;
      }
      if (state.selectedSku.stock <= 0) {
        sheep_index.sheep.$helper.toast("库存不足");
        return;
      }
      emits("buy", state.selectedSku);
    }
    function changeDisabled(isChecked = false, propertyId = 0, valueId = 0) {
      let newSkus = [];
      if (isChecked) {
        for (let price of skuList.value) {
          if (price.stock <= 0) {
            continue;
          }
          if (price.value_id_array.indexOf(valueId) >= 0) {
            newSkus.push(price);
          }
        }
      } else {
        newSkus = getCanUseSkuList();
      }
      let noChooseValueIds = [];
      for (let price of newSkus) {
        noChooseValueIds = noChooseValueIds.concat(price.value_id_array);
      }
      noChooseValueIds = Array.from(new Set(noChooseValueIds));
      if (isChecked) {
        let index = noChooseValueIds.indexOf(valueId);
        noChooseValueIds.splice(index, 1);
      } else {
        Object.entries(state.currentPropertyArray).forEach(([propertyId2, currentPropertyId]) => {
          if (currentPropertyId.toString() !== "") {
            return;
          }
          let index = noChooseValueIds.indexOf(currentPropertyId);
          if (index >= 0) {
            noChooseValueIds.splice(index, 1);
          }
        });
      }
      let choosePropertyIds = [];
      if (!isChecked) {
        Object.entries(state.currentPropertyArray).forEach(([propertyId2, currentValueId]) => {
          if (currentValueId !== "") {
            choosePropertyIds.push(currentValueId);
          }
        });
      } else {
        choosePropertyIds = [propertyId];
      }
      for (let propertyIndex in propertyList) {
        if (choosePropertyIds.indexOf(propertyList[propertyIndex]["id"]) >= 0) {
          continue;
        }
        for (let valueIndex in propertyList[propertyIndex]["values"]) {
          propertyList[propertyIndex]["values"][valueIndex]["disabled"] = noChooseValueIds.indexOf(propertyList[propertyIndex]["values"][valueIndex]["id"]) < 0;
        }
      }
    }
    function getCanUseSkuList() {
      let newSkus = [];
      for (let sku of skuList.value) {
        if (sku.stock <= 0) {
          continue;
        }
        let isOk = true;
        Object.entries(state.currentPropertyArray).forEach(([propertyId, valueId]) => {
          if (valueId.toString() !== "" && sku.value_id_array.indexOf(valueId) < 0) {
            isOk = false;
          }
        });
        if (isOk) {
          newSkus.push(sku);
        }
      }
      return newSkus;
    }
    function onSelectSku(propertyId, valueId) {
      let isChecked = true;
      if (state.currentPropertyArray[propertyId] !== void 0 && state.currentPropertyArray[propertyId] === valueId) {
        isChecked = false;
        state.currentPropertyArray.splice(propertyId, 1, "");
      } else {
        state.currentPropertyArray[propertyId] = valueId;
      }
      let choosePropertyId = [];
      Object.entries(state.currentPropertyArray).forEach(([propertyId2, currentPropertyId]) => {
        if (currentPropertyId !== "") {
          choosePropertyId.push(currentPropertyId);
        }
      });
      let newSkuList = getCanUseSkuList();
      if (choosePropertyId.length === propertyList.length && newSkuList.length) {
        newSkuList[0].count = state.selectedSku.count || 1;
        state.selectedSku = newSkuList[0];
      } else {
        state.selectedSku = {};
      }
      changeDisabled(isChecked, propertyId, valueId);
    }
    changeDisabled(false);
    sheep_hooks_useGoods.initDefaultSelect(propertyList, onSelectSku);
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.unref(sheep_index.sheep).$url.cdn(state.selectedSku.picUrl || __props.goodsInfo.picUrl),
        b: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/goods/groupon-tag-white.png"),
        c: common_vendor.t(__props.goodsInfo.name),
        d: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(state.selectedSku.price || __props.goodsInfo.price || state.selectedSku.marketPrice)),
        e: common_vendor.t(state.selectedSku.stock || __props.goodsInfo.stock),
        f: common_vendor.f(common_vendor.unref(propertyList), (property, k0, i0) => {
          return {
            a: common_vendor.t(property.name),
            b: common_vendor.f(property.values, (value, k1, i1) => {
              return {
                a: common_vendor.t(value.name),
                b: common_vendor.n({
                  "checked-btn": state.currentPropertyArray[property.id] === value.id
                }),
                c: common_vendor.n({
                  "disabled-btn": value.disabled === true
                }),
                d: value.id,
                e: value.disabled === true,
                f: common_vendor.o(($event) => onSelectSku(property.id, value.id), value.id)
              };
            }),
            c: property.id
          };
        }),
        g: common_vendor.o(($event) => onNumberChange($event), "9b"),
        h: common_vendor.o(($event) => state.selectedSku.count = $event, "3d"),
        i: common_vendor.p({
          min: 1,
          max: state.selectedSku.stock,
          step: 1,
          activity: "groupon",
          modelValue: state.selectedSku.count
        }),
        j: common_vendor.t(__props.grouponNum + "人团"),
        k: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(state.selectedSku.price * state.selectedSku.count || __props.goodsInfo.price * state.selectedSku.count || state.selectedSku.marketPrice * state.selectedSku.count || __props.goodsInfo.price)),
        l: __props.grouponAction === "create"
      }, __props.grouponAction === "create" ? {} : __props.grouponAction === "join" ? {} : {}, {
        m: __props.grouponAction === "join",
        n: common_vendor.o(onBuy, "16"),
        o: common_vendor.o(($event) => emits("close"), "9b"),
        p: common_vendor.s(_ctx.__cssVars()),
        q: common_vendor.p({
          show: __props.show,
          round: "10"
        })
      });
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-f77ace9d"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/components/s-select-groupon-sku/s-select-groupon-sku.js.map
