"use strict";
const common_vendor = require("../../../../common/vendor.js");
if (!Math) {
  detailCell();
}
const detailCell = () => "./detail-cell.js";
const _sfc_main = {
  __name: "detail-cell-sku",
  props: {
    modelValue: {
      type: Array,
      default() {
        return [];
      }
    },
    sku: {
      type: Object
    }
  },
  setup(__props) {
    const props = __props;
    const value = common_vendor.computed(() => {
      var _a;
      if (!((_a = props.sku) == null ? void 0 : _a.id)) {
        return "请选择商品规格";
      }
      let str = "";
      props.sku.properties.forEach((property) => {
        str += property.propertyName + ":" + property.valueName + " ";
      });
      return str;
    });
    return (_ctx, _cache) => {
      return {
        a: common_vendor.p({
          label: "选择",
          value: value.value
        })
      };
    };
  }
};
wx.createComponent(_sfc_main);
//# sourceMappingURL=../../../../../.sourcemap/mp-weixin/pages/goods/components/detail/detail-cell-sku.js.map
