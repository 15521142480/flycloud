"use strict";
const common_vendor = require("../../../common/vendor.js");
const _sfc_main = {
  name: "countDown",
  props: {
    justifyLeft: {
      type: String,
      default: ""
    },
    //距离开始提示文字
    tipText: {
      type: String,
      default: "倒计时"
    },
    dayText: {
      type: String,
      default: "天"
    },
    hourText: {
      type: String,
      default: "时"
    },
    minuteText: {
      type: String,
      default: "分"
    },
    secondText: {
      type: String,
      default: "秒"
    },
    datatime: {
      type: Number,
      default: 0
    },
    isDay: {
      type: Boolean,
      default: true
    },
    isCol: {
      type: Boolean,
      default: false
    },
    bgColor: {
      type: Object,
      default: null
    }
  },
  data: function() {
    return {
      day: "00",
      hour: "00",
      minute: "00",
      second: "00"
    };
  },
  created: function() {
    this.show_time();
  },
  mounted: function() {
  },
  methods: {
    show_time: function() {
      let that = this;
      function runTime() {
        let intDiff = that.datatime - Date.parse(/* @__PURE__ */ new Date()) / 1e3;
        let day = 0, hour = 0, minute = 0, second = 0;
        if (intDiff > 0) {
          if (that.isDay === true) {
            day = Math.floor(intDiff / (60 * 60 * 24));
          } else {
            day = 0;
          }
          hour = Math.floor(intDiff / (60 * 60)) - day * 24;
          minute = Math.floor(intDiff / 60) - day * 24 * 60 - hour * 60;
          second = Math.floor(intDiff) - day * 24 * 60 * 60 - hour * 60 * 60 - minute * 60;
          if (hour <= 9)
            hour = "0" + hour;
          if (minute <= 9)
            minute = "0" + minute;
          if (second <= 9)
            second = "0" + second;
          that.day = day;
          that.hour = hour;
          that.minute = minute;
          that.second = second;
        } else {
          that.day = "00";
          that.hour = "00";
          that.minute = "00";
          that.second = "00";
        }
      }
      runTime();
      setInterval(runTime, 1e3);
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return common_vendor.e({
    a: $props.tipText
  }, $props.tipText ? {
    b: common_vendor.t($props.tipText)
  } : {}, {
    c: $props.isDay === true
  }, $props.isDay === true ? {
    d: common_vendor.t(_ctx.day),
    e: common_vendor.t($props.bgColor.isDay ? "天" : ""),
    f: $props.bgColor.bgColor,
    g: $props.bgColor.Color
  } : {}, {
    h: $props.dayText
  }, $props.dayText ? {
    i: common_vendor.t($props.dayText),
    j: $props.bgColor.timeTxtwidth,
    k: $props.bgColor.bgColor
  } : {}, {
    l: common_vendor.t(_ctx.hour),
    m: common_vendor.n($props.isCol ? "timeCol" : ""),
    n: $props.bgColor.bgColor,
    o: $props.bgColor.Color,
    p: $props.bgColor.width,
    q: $props.hourText
  }, $props.hourText ? {
    r: common_vendor.t($props.hourText),
    s: common_vendor.n($props.isCol ? "whit" : ""),
    t: $props.bgColor.timeTxtwidth,
    v: $props.bgColor.bgColor
  } : {}, {
    w: common_vendor.t(_ctx.minute),
    x: common_vendor.n($props.isCol ? "timeCol" : ""),
    y: $props.bgColor.bgColor,
    z: $props.bgColor.Color,
    A: $props.bgColor.width,
    B: $props.minuteText
  }, $props.minuteText ? {
    C: common_vendor.t($props.minuteText),
    D: common_vendor.n($props.isCol ? "whit" : ""),
    E: $props.bgColor.timeTxtwidth,
    F: $props.bgColor.bgColor
  } : {}, {
    G: common_vendor.t(_ctx.second),
    H: common_vendor.n($props.isCol ? "timeCol" : ""),
    I: $props.bgColor.bgColor,
    J: $props.bgColor.Color,
    K: $props.bgColor.width,
    L: $props.secondText
  }, $props.secondText ? {
    M: common_vendor.t($props.secondText)
  } : {}, {
    N: common_vendor.s($props.justifyLeft)
  });
}
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-39f8fd44"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/components/s-count-down/s-count-down.js.map
