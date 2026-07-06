"use strict";
require("../../../../sheep/index.js");
const common_vendor = require("../../../../common/vendor.js");
let platform = "other";
const _sfc_main = {
  name: "UniFab",
  emits: ["fabClick", "trigger"],
  props: {
    pattern: {
      type: Array,
      default() {
        return [];
      }
    },
    horizontal: {
      type: String,
      default: "left"
    },
    vertical: {
      type: String,
      default: "bottom"
    },
    direction: {
      type: String,
      default: "horizontal"
    },
    content: {
      type: Array,
      default() {
        return [];
      }
    },
    show: {
      type: Boolean,
      default: false
    },
    popMenu: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      fabShow: false,
      isShow: false,
      isAndroidNvue: platform === "android",
      styles: [
        {
          // color: '#3c3e49',
          // selectedColor: '#007AFF',
          // backgroundColor: '#fff',
          // buttonColor: '#007AFF',
          // iconColor: '#fff'
        }
      ]
    };
  },
  computed: {
    contentWidth(e) {
      return (this.content.length + 1) * 130 + "rpx";
    },
    contentWidthMin() {
      return "100rpx";
    },
    // 动态计算宽度
    boxWidth() {
      return this.getPosition(3, "horizontal");
    },
    // 动态计算高度
    boxHeight() {
      return this.getPosition(3, "vertical");
    },
    // 计算左下位置
    leftBottom() {
      return this.getPosition(0, "left", "bottom");
    },
    // 计算右下位置
    rightBottom() {
      return this.getPosition(0, "right", "bottom");
    },
    // 计算左上位置
    leftTop() {
      return this.getPosition(0, "left", "top");
    },
    rightTop() {
      return this.getPosition(0, "right", "top");
    },
    flexDirectionStart() {
      return this.getPosition(1, "vertical", "top");
    },
    flexDirectionEnd() {
      return this.getPosition(1, "vertical", "bottom");
    },
    horizontalLeft() {
      return this.getPosition(2, "horizontal", "left");
    },
    horizontalRight() {
      return this.getPosition(2, "horizontal", "right");
    }
  },
  watch: {
    // pattern: {
    // 	handler(val, oldVal) {
    // 		this.styles = Object.assign({}, this.styles, val)
    // 	},
    // 	deep: true
    // }
  },
  created() {
    this.isShow = this.show;
    if (this.top === 0) {
      this.fabShow = true;
    }
  },
  methods: {
    _onClick() {
      this.$emit("fabClick");
      if (!this.popMenu) {
        return;
      }
      this.isShow = !this.isShow;
    },
    open() {
      this.isShow = true;
    },
    close() {
      this.isShow = false;
    },
    /**
     * 按钮点击事件
     */
    _onItemClick(index, item) {
      this.$emit("trigger", {
        index,
        item
      });
    },
    /**
     * 获取 位置信息
     */
    getPosition(types, paramA, paramB) {
      if (types === 0) {
        return this.horizontal === paramA && this.vertical === paramB;
      } else if (types === 1) {
        return this.direction === paramA && this.vertical === paramB;
      } else if (types === 2) {
        return this.direction === paramA && this.horizontal === paramB;
      } else {
        return this.isShow && this.direction === paramA ? this.contentWidth : this.contentWidthMin;
      }
    }
  }
};
if (!Array) {
  const _easycom_uni_icons2 = common_vendor.resolveComponent("uni-icons");
  _easycom_uni_icons2();
}
const _easycom_uni_icons = () => "../../../uni-icons/components/uni-icons/uni-icons.js";
if (!Math) {
  _easycom_uni_icons();
}
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return common_vendor.e({
    a: $props.popMenu && ($options.leftBottom || $options.rightBottom || $options.leftTop || $options.rightTop) && $props.content.length > 0
  }, $props.popMenu && ($options.leftBottom || $options.rightBottom || $options.leftTop || $options.rightTop) && $props.content.length > 0 ? common_vendor.e({
    b: $options.flexDirectionStart || $options.horizontalLeft
  }, $options.flexDirectionStart || $options.horizontalLeft ? {} : {}, {
    c: common_vendor.f($props.content, (item, index, i0) => {
      return {
        a: item.active ? item.selectedIconPath : item.iconPath,
        b: common_vendor.t(item.text),
        c: $props.pattern[index].color,
        d: index,
        e: common_vendor.o(($event) => $options._onItemClick(index, item), index)
      };
    }),
    d: $props.direction == "vertical" ? 1 : "",
    e: $data.isShow ? 1 : "",
    f: $props.direction == "horizontal" ? 1 : "",
    g: $options.flexDirectionEnd || $options.horizontalRight
  }, $options.flexDirectionEnd || $options.horizontalRight ? {} : {}, {
    h: $props.horizontal === "left" ? 1 : "",
    i: $props.horizontal === "right" ? 1 : "",
    j: $props.direction === "vertical" ? 1 : "",
    k: $options.flexDirectionStart ? 1 : "",
    l: $options.flexDirectionEnd ? 1 : "",
    m: !$data.isAndroidNvue ? 1 : "",
    n: $options.boxWidth,
    o: $options.boxHeight,
    p: $options.leftBottom ? 1 : "",
    q: $options.rightBottom ? 1 : "",
    r: $options.leftTop ? 1 : "",
    s: $options.rightTop ? 1 : ""
  }) : {}, {
    t: $data.isShow && $props.content.length > 0 ? 1 : "",
    v: common_vendor.p({
      type: "plusempty",
      color: "#fff",
      size: "20"
    }),
    w: $options.leftBottom ? 1 : "",
    x: $options.rightBottom ? 1 : "",
    y: $options.leftTop ? 1 : "",
    z: $options.rightTop ? 1 : "",
    A: !$data.isAndroidNvue ? 1 : "",
    B: common_vendor.o((...args) => $options._onClick && $options._onClick(...args), "41")
  });
}
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../../.sourcemap/mp-weixin/uni_modules/uni-fab/components/uni-fab/uni-fab.js.map
