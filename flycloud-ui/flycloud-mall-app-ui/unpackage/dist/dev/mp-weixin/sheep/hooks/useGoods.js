"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_url_index = require("../url/index.js");
const sheep_helper_utils = require("../helper/utils.js");
function formatSales(type, num) {
  let prefix = type !== "exact" && num < 10 ? "销量" : "已售";
  return formatNum(prefix, type, num);
}
function formatExchange(type, num) {
  return formatNum("已兑换", type, num);
}
function formatStock(type, num) {
  return formatNum("库存", type, num);
}
function formatNum(prefix, type, num) {
  num = num || 0;
  if (type === "exact") {
    return prefix + num;
  }
  if (num < 10) {
    return `${prefix}≤10`;
  }
  const numStr = num.toString();
  const first = numStr[0];
  const other = "0".repeat(numStr.length - 1);
  return `${prefix}${first}${other}+`;
}
const VIDEO_SUFFIX_LIST = [".avi", ".mp4"];
function formatGoodsSwiper(urlList) {
  return (urlList == null ? void 0 : urlList.filter((url) => url).map((url, key) => {
    const isVideo = VIDEO_SUFFIX_LIST.some((suffix) => url.includes(suffix));
    const type = isVideo ? "video" : "image";
    const src = sheep_url_index.$url.cdn(url);
    return {
      type,
      src
    };
  })) || [];
}
function formatOrderColor(order) {
  if (order.status === 0) {
    return "info-color";
  }
  if (order.status === 10 || order.status === 20 || order.status === 30 && !order.commentStatus) {
    return "warning-color";
  }
  if (order.status === 30 && order.commentStatus) {
    return "success-color";
  }
  return "danger-color";
}
function formatOrderStatus(order) {
  if (order.status === 0) {
    return "待付款";
  }
  if (order.status === 10 && order.deliveryType === 1) {
    return "待发货";
  }
  if (order.status === 10 && order.deliveryType === 2) {
    return "待核销";
  }
  if (order.status === 20) {
    return "待收货";
  }
  if (order.status === 30 && !order.commentStatus) {
    return "待评价";
  }
  if (order.status === 30 && order.commentStatus) {
    return "已完成";
  }
  return "已关闭";
}
function formatOrderStatusDescription(order) {
  if (order.status === 0) {
    return `请在 ${sheep_helper_utils.formatDate(order.payExpireTime)} 前完成支付`;
  }
  if (order.status === 10) {
    return "商家未发货，请耐心等待";
  }
  if (order.status === 20) {
    return "商家已发货，请耐心等待";
  }
  if (order.status === 30 && !order.commentStatus) {
    return "已收货，快去评价一下吧";
  }
  if (order.status === 30 && order.commentStatus) {
    return "交易完成，感谢您的支持";
  }
  return "交易关闭";
}
function handleOrderButtons(order) {
  order.buttons = [];
  if (order.type === 3) {
    order.buttons.push("combination");
  }
  if (order.status === 20) {
    order.buttons.push("confirm");
  }
  if (order.logisticsId > 0) {
    order.buttons.push("express");
  }
  if (order.status === 0) {
    order.buttons.push("cancel");
    order.buttons.push("pay");
  }
  if (order.status === 30 && !order.commentStatus) {
    order.buttons.push("comment");
  }
  if (order.status === 40) {
    order.buttons.push("delete");
  }
}
function formatAfterSaleStatus(afterSale) {
  if (afterSale.status === 10) {
    return "申请售后";
  }
  if (afterSale.status === 20) {
    return "商品待退货";
  }
  if (afterSale.status === 30) {
    return "商家待收货";
  }
  if (afterSale.status === 40) {
    return "等待退款";
  }
  if (afterSale.status === 50) {
    return "退款成功";
  }
  if (afterSale.status === 61) {
    return "买家取消";
  }
  if (afterSale.status === 62) {
    return "商家拒绝";
  }
  if (afterSale.status === 63) {
    return "商家拒收货";
  }
  return "未知状态";
}
function formatAfterSaleStatusDescription(afterSale) {
  if (afterSale.status === 10) {
    return "退款申请待商家处理";
  }
  if (afterSale.status === 20) {
    return "请退货并填写物流信息";
  }
  if (afterSale.status === 30) {
    return "退货退款申请待商家处理";
  }
  if (afterSale.status === 40) {
    return "等待退款";
  }
  if (afterSale.status === 50) {
    return "退款成功";
  }
  if (afterSale.status === 61) {
    return "退款关闭";
  }
  if (afterSale.status === 62) {
    return `商家不同意退款申请，拒绝原因：${afterSale.auditReason}`;
  }
  if (afterSale.status === 63) {
    return `商家拒绝收货，不同意退款，拒绝原因：${afterSale.auditReason}`;
  }
  return "未知状态";
}
function handleAfterSaleButtons(afterSale) {
  afterSale.buttons = [];
  if ([10, 20, 30].includes(afterSale.status)) {
    afterSale.buttons.push("cancel");
  }
  if (afterSale.status === 20) {
    afterSale.buttons.push("delivery");
  }
}
function useDurationTime(toTime, fromTime = "") {
  toTime = getDayjsTime(toTime);
  if (fromTime === "") {
    fromTime = common_vendor.dayjs();
  }
  let duration = common_vendor.ref(toTime - fromTime);
  if (duration.value > 0) {
    setTimeout(() => {
      if (duration.value > 0) {
        duration.value -= 1e3;
      }
    }, 1e3);
  }
  let durationTime = common_vendor.dayjs.duration(duration.value);
  return {
    h: (durationTime.months() * 30 * 24 + durationTime.days() * 24 + durationTime.hours()).toString().padStart(2, "0"),
    m: durationTime.minutes().toString().padStart(2, "0"),
    s: durationTime.seconds().toString().padStart(2, "0"),
    ms: durationTime.$ms
  };
}
function getDayjsTime(time) {
  time = time.toString();
  if (time.indexOf("-") > 0) {
    return common_vendor.dayjs(time);
  }
  if (time.length > 10) {
    return common_vendor.dayjs(parseInt(time));
  }
  if (time.length === 10) {
    return common_vendor.dayjs.unix(parseInt(time));
  }
}
function fen2yuan(price) {
  return (price / 100).toFixed(2);
}
function fen2yuanSimple(price) {
  return fen2yuan(price).replace(/\.?0+$/, "");
}
function formatDiscountPercent(discountPercent) {
  return (discountPercent / 10).toFixed(1).replace(/\.?0+$/, "");
}
function convertProductPropertyList(skus) {
  let result = [];
  for (const sku of skus) {
    if (!sku.properties) {
      continue;
    }
    for (const property of sku.properties) {
      let resultProperty = result.find((item) => item.id === property.propertyId);
      if (!resultProperty) {
        resultProperty = {
          id: property.propertyId,
          name: property.propertyName,
          values: []
        };
        result.push(resultProperty);
      }
      let resultValue = resultProperty.values.find((item) => item.id === property.valueId);
      if (!resultValue) {
        resultProperty.values.push({
          id: property.valueId,
          name: property.valueName
        });
      }
    }
  }
  return result;
}
function appendSettlementProduct(spus, settlementInfos) {
  if (!settlementInfos || settlementInfos.length === 0) {
    return;
  }
  for (const spu of spus) {
    const settlementInfo = settlementInfos.find((info) => info.spuId === spu.id);
    if (!settlementInfo) {
      return;
    }
    const settlementSku = settlementInfo.skus.filter((sku) => sku.promotionPrice > 0).reduce((prev, curr) => prev.promotionPrice < curr.promotionPrice ? prev : curr, []);
    if (settlementSku) {
      spu.promotionType = settlementSku.promotionType;
      spu.promotionPrice = settlementSku.promotionPrice;
    }
    if (settlementInfo.rewardActivity) {
      spu.rewardActivity = settlementInfo.rewardActivity;
    }
  }
}
function getRewardActivityRuleGroupDescriptions(activity) {
  if (!activity || !activity.rules || activity.rules.length === 0) {
    return [];
  }
  const result = [
    { name: "满减", values: [] },
    { name: "赠品", values: [] },
    { name: "包邮", values: [] }
  ];
  activity.rules.forEach((rule) => {
    const conditionTypeStr = activity.conditionType === 10 ? `满 ${fen2yuanSimple(rule.limit)} 元` : `满 ${rule.limit} 件`;
    if (rule.limit) {
      result[0].values.push(`${conditionTypeStr} 减 ${fen2yuanSimple(rule.discountPrice)} 元`);
    }
    if (rule.point || rule.giveCouponTemplateCounts && rule.giveCouponTemplateCounts.length > 0) {
      let tips = [];
      if (rule.point) {
        tips.push(`送 ${rule.point} 积分`);
      }
      if (rule.giveCouponTemplateCounts && rule.giveCouponTemplateCounts.length > 0) {
        tips.push(`送 ${rule.giveCouponTemplateCounts.length} 张优惠券`);
      }
      result[1].values.push(`${conditionTypeStr} ${tips.join("、")}`);
    }
    if (rule.freeDelivery) {
      result[2].values.push(`${conditionTypeStr} 包邮`);
    }
  });
  result.forEach((item) => {
    if (item.values.length === 0) {
      result.splice(result.indexOf(item), 1);
    }
  });
  return result;
}
function getRewardActivityRuleItemDescriptions(activity) {
  if (!activity || !activity.rules || activity.rules.length === 0) {
    return [];
  }
  const result = [];
  activity.rules.forEach((rule) => {
    const conditionTypeStr = activity.conditionType === 10 ? `满${fen2yuanSimple(rule.limit)}元` : `满${rule.limit}件`;
    if (rule.limit) {
      result.push(`${conditionTypeStr}减${fen2yuanSimple(rule.discountPrice)}元`);
    }
    if (rule.point) {
      result.push(`${conditionTypeStr}送${rule.point}积分`);
    }
    if (rule.giveCouponTemplateCounts && rule.giveCouponTemplateCounts.length > 0) {
      result.push(`${conditionTypeStr}送${rule.giveCouponTemplateCounts.length}张优惠券`);
    }
    if (rule.freeDelivery) {
      result.push(`${conditionTypeStr}包邮`);
    }
  });
  return result;
}
function initDefaultSelect(propertyList, onSelectSku) {
  if (propertyList.length === 0) {
    return;
  }
  for (const property of propertyList) {
    const firstValue = (property.values || [])[0];
    if (firstValue && !firstValue.disabled) {
      onSelectSku(property.id, firstValue.id);
    }
  }
}
exports.appendSettlementProduct = appendSettlementProduct;
exports.convertProductPropertyList = convertProductPropertyList;
exports.fen2yuan = fen2yuan;
exports.fen2yuanSimple = fen2yuanSimple;
exports.formatAfterSaleStatus = formatAfterSaleStatus;
exports.formatAfterSaleStatusDescription = formatAfterSaleStatusDescription;
exports.formatDiscountPercent = formatDiscountPercent;
exports.formatExchange = formatExchange;
exports.formatGoodsSwiper = formatGoodsSwiper;
exports.formatOrderColor = formatOrderColor;
exports.formatOrderStatus = formatOrderStatus;
exports.formatOrderStatusDescription = formatOrderStatusDescription;
exports.formatSales = formatSales;
exports.formatStock = formatStock;
exports.getRewardActivityRuleGroupDescriptions = getRewardActivityRuleGroupDescriptions;
exports.getRewardActivityRuleItemDescriptions = getRewardActivityRuleItemDescriptions;
exports.handleAfterSaleButtons = handleAfterSaleButtons;
exports.handleOrderButtons = handleOrderButtons;
exports.initDefaultSelect = initDefaultSelect;
exports.useDurationTime = useDurationTime;
//# sourceMappingURL=../../../.sourcemap/mp-weixin/sheep/hooks/useGoods.js.map
