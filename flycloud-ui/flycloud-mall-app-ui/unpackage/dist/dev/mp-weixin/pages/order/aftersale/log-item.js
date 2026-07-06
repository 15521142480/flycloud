"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../../sheep/index.js");
const _sfc_main = {
  __name: "log-item",
  props: {
    item: {
      type: Object,
      // 当前日志
      default() {
      }
    },
    index: {
      type: Number,
      // item 在 data 的下标
      default: 0
    },
    data: {
      type: Object,
      // 日志列表
      default() {
      }
    }
  },
  setup(__props) {
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.n(__props.index === 0 ? "activity-color" : ""),
        b: __props.data.length - 1 !== __props.index
      }, __props.data.length - 1 !== __props.index ? {} : {}, {
        c: common_vendor.t(__props.item.content),
        d: common_vendor.t(common_vendor.unref(sheep_index.sheep).$helper.timeFormat(__props.item.createTime, "yyyy-mm-dd hh:MM:ss"))
      });
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-3a1a95e5"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/order/aftersale/log-item.js.map
