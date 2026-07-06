"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../index.js");
const sheep_hooks_useGoods = require("../../hooks/useGoods.js");
const sheep_helper_const = require("../../helper/const.js");
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
  __name: "s-select-seckill-sku",
  props: {
    modelValue: {
      type: Object,
      default() {
      }
    },
    show: {
      type: Boolean,
      default: false
    },
    // 单次限购数量
    singleLimitCount: {
      type: Number,
      default: 1
    }
  },
  emits: ["change", "addCart", "buy", "close"],
  setup(__props, { emit: __emit }) {
    const emits = __emit;
    const props = __props;
    const state = common_vendor.reactive({
      goodsInfo: common_vendor.computed(() => props.modelValue),
      selectedSku: {},
      currentPropertyArray: {}
    });
    const getShowPriceText = common_vendor.computed(() => {
      let priceText = `￥${sheep_hooks_useGoods.fen2yuan(state.goodsInfo.price)}`;
      if (!common_vendor.isEmpty(state.selectedSku)) {
        const sku = state.selectedSku;
        priceText = `${sku.point}${!sku.pointPrice ? "" : `+￥${sheep_hooks_useGoods.fen2yuan(sku.pointPrice)}`}`;
      }
      return priceText;
    });
    const propertyList = sheep_hooks_useGoods.convertProductPropertyList(state.goodsInfo.skus);
    const skuList = common_vendor.computed(() => {
      let skuPrices = state.goodsInfo.skus;
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
    const onBuy = () => {
      if (state.selectedSku.id) {
        if (state.selectedSku.stock <= 0) {
          sheep_index.sheep.$helper.toast("库存不足");
        } else {
          emits("buy", state.selectedSku);
        }
      } else {
        sheep_index.sheep.$helper.toast("请选择规格");
      }
    };
    function onBuyCountChange(buyCount) {
      if (buyCount > 0 && state.selectedSku.count !== buyCount) {
        state.selectedSku.count = buyCount;
      }
    }
    const changeDisabled = (isChecked = false, propertyId = 0, valueId = 0) => {
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
    };
    const getCanUseSkuList = () => {
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
    };
    const onSelectSku = (propertyId, valueId) => {
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
    };
    changeDisabled(false);
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.unref(sheep_index.sheep).$url.cdn(state.selectedSku.picUrl || state.goodsInfo.picUrl),
        b: common_vendor.t(state.goodsInfo.name),
        c: state.goodsInfo.activity_type === common_vendor.unref(sheep_helper_const.PromotionActivityTypeEnum).POINT.type
      }, state.goodsInfo.activity_type === common_vendor.unref(sheep_helper_const.PromotionActivityTypeEnum).POINT.type ? common_vendor.e({
        d: !common_vendor.unref(common_vendor.isEmpty)(state.selectedSku)
      }, !common_vendor.unref(common_vendor.isEmpty)(state.selectedSku) ? {
        e: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/goods/score1.svg")
      } : {}, {
        f: common_vendor.t(getShowPriceText.value)
      }) : {
        g: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(state.selectedSku.price || state.goodsInfo.price))
      }, {
        h: common_vendor.t(state.selectedSku.stock || state.goodsInfo.stock),
        i: common_vendor.f(common_vendor.unref(propertyList), (property, k0, i0) => {
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
        j: common_vendor.o(($event) => onBuyCountChange($event), "c8"),
        k: common_vendor.o(($event) => state.selectedSku.count = $event, "c2"),
        l: common_vendor.p({
          min: 1,
          max: common_vendor.unref(common_vendor.min)([__props.singleLimitCount, state.selectedSku.stock]),
          step: 1,
          activity: "seckill",
          modelValue: state.selectedSku.count
        }),
        m: common_vendor.o(onBuy, "eb"),
        n: common_vendor.o(($event) => emits("close"), "da"),
        o: common_vendor.p({
          show: __props.show,
          round: "10"
        })
      });
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-e3bc621c"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/components/s-select-seckill-sku/s-select-seckill-sku.js.map
