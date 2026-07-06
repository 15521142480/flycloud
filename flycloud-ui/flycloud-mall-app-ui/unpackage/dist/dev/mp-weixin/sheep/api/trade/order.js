"use strict";
const sheep_request_index = require("../../request/index.js");
const sheep_helper_utils = require("../../helper/utils.js");
const OrderApi = {
  // 计算订单信息
  settlementOrder: (data) => {
    const data2 = {
      ...data
    };
    if (!(data.couponId > 0)) {
      delete data2.couponId;
    }
    if (!(data.addressId > 0)) {
      delete data2.addressId;
    }
    if (!(data.pickUpStoreId > 0)) {
      delete data2.pickUpStoreId;
    }
    if (sheep_helper_utils.isEmpty(data.receiverName)) {
      delete data2.receiverName;
    }
    if (sheep_helper_utils.isEmpty(data.receiverMobile)) {
      delete data2.receiverMobile;
    }
    if (!(data.combinationActivityId > 0)) {
      delete data2.combinationActivityId;
    }
    if (!(data.combinationHeadId > 0)) {
      delete data2.combinationHeadId;
    }
    if (!(data.seckillActivityId > 0)) {
      delete data2.seckillActivityId;
    }
    if (!(data.pointActivityId > 0)) {
      delete data2.pointActivityId;
    }
    if (!(data.deliveryType > 0)) {
      delete data2.deliveryType;
    }
    delete data2.items;
    for (let i = 0; i < data.items.length; i++) {
      data2[encodeURIComponent("items[" + i + "].skuId")] = data.items[i].skuId + "";
      data2[encodeURIComponent("items[" + i + "].count")] = data.items[i].count + "";
      if (data.items[i].cartId) {
        data2[encodeURIComponent("items[" + i + "].cartId")] = data.items[i].cartId + "";
      }
    }
    const queryString = Object.keys(data2).map((key) => key + "=" + data2[key]).join("&");
    return sheep_request_index.request({
      url: `/trade/order/settlement?${queryString}`,
      method: "GET",
      custom: {
        showError: true,
        showLoading: true
      }
    });
  },
  // 获得商品结算信息
  getSettlementProduct: (spuIds) => {
    return sheep_request_index.request({
      url: "/trade/order/settlement-product",
      method: "GET",
      params: { spuIds },
      custom: {
        showLoading: false,
        showError: false
      }
    });
  },
  // 创建订单
  createOrder: (data) => {
    return sheep_request_index.request({
      url: `/trade/order/create`,
      method: "POST",
      data
    });
  },
  // 获得订单详细：sync 是可选参数
  getOrderDetail: (id, sync) => {
    return sheep_request_index.request({
      url: `/trade/order/get-detail`,
      method: "GET",
      params: {
        id,
        sync
      },
      custom: {
        showLoading: false
      }
    });
  },
  // 订单列表
  getOrderPage: (params) => {
    return sheep_request_index.request({
      url: "/trade/order/page",
      method: "GET",
      params,
      custom: {
        showLoading: false
      }
    });
  },
  // 确认收货
  receiveOrder: (id) => {
    return sheep_request_index.request({
      url: `/trade/order/receive`,
      method: "PUT",
      params: {
        id
      }
    });
  },
  // 取消订单
  cancelOrder: (id) => {
    return sheep_request_index.request({
      url: `/trade/order/cancel`,
      method: "DELETE",
      params: {
        id
      }
    });
  },
  // 删除订单
  deleteOrder: (id) => {
    return sheep_request_index.request({
      url: `/trade/order/delete`,
      method: "DELETE",
      params: {
        id
      }
    });
  },
  // 获得交易订单的物流轨迹
  getOrderExpressTrackList: (id) => {
    return sheep_request_index.request({
      url: `/trade/order/get-express-track-list`,
      method: "GET",
      params: {
        id
      }
    });
  },
  // 获得交易订单数量
  getOrderCount: () => {
    return sheep_request_index.request({
      url: "/trade/order/get-count",
      method: "GET",
      custom: {
        showLoading: false,
        auth: true
      }
    });
  },
  // 创建单个评论
  createOrderItemComment: (data) => {
    return sheep_request_index.request({
      url: `/trade/order/item/create-comment`,
      method: "POST",
      data
    });
  }
};
const __vite_glob_0_36 = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: OrderApi
}, Symbol.toStringTag, { value: "Module" }));
exports.OrderApi = OrderApi;
exports.__vite_glob_0_36 = __vite_glob_0_36;
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/api/trade/order.js.map
