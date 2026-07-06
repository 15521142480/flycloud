"use strict";
const common_vendor = require("../../common/vendor.js");
function isArray(value) {
  if (typeof Array.isArray === "function") {
    return Array.isArray(value);
  } else {
    return Object.prototype.toString.call(value) === "[object Array]";
  }
}
function isObject(value) {
  return Object.prototype.toString.call(value) === "[object Object]";
}
function isEmpty(value) {
  if (value === "" || value === void 0 || value === null) {
    return true;
  }
  if (isArray(value)) {
    return value.length === 0;
  }
  if (isObject(value)) {
    return Object.keys(value).length === 0;
  }
  return false;
}
function cloneDeep(obj) {
  const d = isArray(obj) ? [...obj] : {};
  if (isObject(obj)) {
    for (const key in obj) {
      if (obj[key]) {
        if (obj[key] && typeof obj[key] === "object") {
          d[key] = cloneDeep(obj[key]);
        } else {
          d[key] = obj[key];
        }
      }
    }
  }
  return d;
}
const formatToFraction = (num) => {
  if (typeof num === "undefined")
    return 0;
  const parsedNumber = typeof num === "string" ? parseFloat(num) : num;
  return parseFloat((parsedNumber / 100).toFixed(2));
};
const floatToFixed2 = (num) => {
  let str = "0.00";
  if (typeof num === "undefined") {
    return str;
  }
  const f = formatToFraction(num);
  const decimalPart = f.toString().split(".")[1];
  const len = decimalPart ? decimalPart.length : 0;
  switch (len) {
    case 0:
      str = f.toString() + ".00";
      break;
    case 1:
      str = f.toString() + ".0";
      break;
    case 2:
      str = f.toString();
      break;
  }
  return str;
};
function formatDate(date, format = "YYYY-MM-DD HH:mm:ss") {
  if (!date) {
    return "";
  }
  if (format === void 0) {
    format = "YYYY-MM-DD HH:mm:ss";
  }
  return common_vendor.dayjs(date).format(format);
}
function handleTree(data, id = "id", parentId = "parentId", children = "children", rootId = 0) {
  const cloneData = JSON.parse(JSON.stringify(data));
  const treeData = cloneData.filter((father) => {
    let branchArr = cloneData.filter((child) => {
      return father[id] === child[parentId];
    });
    branchArr.length > 0 ? father.children = branchArr : "";
    return father[parentId] === rootId;
  });
  return treeData !== "" ? treeData : data;
}
function resetPagination(pagination) {
  pagination.list = [];
  pagination.total = 0;
  pagination.pageNum = 1;
}
const copyValueToTarget = (target, source) => {
  const newObj = Object.assign({}, target, source);
  Object.keys(newObj).forEach((key) => {
    if (Object.keys(target).indexOf(key) === -1) {
      delete newObj[key];
    }
  });
  Object.assign(target, newObj);
};
function jsonParse(str) {
  try {
    return JSON.parse(str);
  } catch (e) {
    common_vendor.index.__f__("warn", "at sheep/helper/utils.js:308", `str[${str}] 不是一个 JSON 字符串`);
    return str;
  }
}
function getWeekTimes() {
  const today = /* @__PURE__ */ new Date();
  const dayOfWeek = today.getDay();
  return [
    new Date(today.getFullYear(), today.getMonth(), today.getDate() - dayOfWeek, 0, 0, 0),
    new Date(today.getFullYear(), today.getMonth(), today.getDate() + (6 - dayOfWeek), 23, 59, 59)
  ];
}
function getMonthTimes() {
  const today = /* @__PURE__ */ new Date();
  const year = today.getFullYear();
  const month = today.getMonth();
  const startDate = new Date(year, month, 1, 0, 0, 0);
  const nextMonth = new Date(year, month + 1, 1);
  const endDate = new Date(nextMonth.getTime() - 1);
  return [startDate, endDate];
}
exports.cloneDeep = cloneDeep;
exports.copyValueToTarget = copyValueToTarget;
exports.floatToFixed2 = floatToFixed2;
exports.formatDate = formatDate;
exports.getMonthTimes = getMonthTimes;
exports.getWeekTimes = getWeekTimes;
exports.handleTree = handleTree;
exports.isEmpty = isEmpty;
exports.jsonParse = jsonParse;
exports.resetPagination = resetPagination;
//# sourceMappingURL=../../../.sourcemap/mp-weixin/sheep/helper/utils.js.map
