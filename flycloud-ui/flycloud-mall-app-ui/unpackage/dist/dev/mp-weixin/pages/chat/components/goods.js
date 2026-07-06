"use strict";
const common_vendor = require("../../../common/vendor.js");
if (!Array) {
  const _easycom_s_goods_item2 = common_vendor.resolveComponent("s-goods-item");
  _easycom_s_goods_item2();
}
const _easycom_s_goods_item = () => "../../../sheep/components/s-goods-item/s-goods-item.js";
if (!Math) {
  _easycom_s_goods_item();
}
const _sfc_main = {
  __name: "goods",
  props: {
    goodsData: {
      type: Object,
      default: {}
    }
  },
  setup(__props) {
    return (_ctx, _cache) => {
      return {
        a: common_vendor.p({
          title: __props.goodsData.spuName,
          img: __props.goodsData.picUrl,
          price: __props.goodsData.price,
          skuText: __props.goodsData.introduction,
          priceColor: "#FF3000",
          titleWidth: 400
        })
      };
    };
  }
};
wx.createComponent(_sfc_main);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/chat/components/goods.js.map
