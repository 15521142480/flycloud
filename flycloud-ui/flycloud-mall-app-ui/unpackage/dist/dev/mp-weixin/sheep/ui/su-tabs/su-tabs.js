"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_helper_index = require("../../helper/index.js");
const _sfc_main = {
  name: "su-tabs",
  data() {
    return {
      addStyle: sheep_helper_index.addStyle,
      addUnit: sheep_helper_index.addUnit,
      firstTime: true,
      scrollLeft: 0,
      scrollViewWidth: 0,
      lineOffsetLeft: 0,
      tabsRect: {
        left: 0
      },
      innerCurrent: 0,
      moving: false
    };
  },
  props: {
    // 滑块的移动过渡时间，单位ms
    duration: {
      type: Number,
      default: 300
    },
    // tabs标签数组
    list: {
      type: Array,
      default: []
    },
    // 滑块颜色
    lineColor: {
      type: String,
      default: ""
    },
    // 菜单选择中时的样式
    activeStyle: {
      type: [String, Object],
      default() {
        return {
          color: "#303133"
        };
      }
    },
    // 菜单非选中时的样式
    inactiveStyle: {
      type: [String, Object],
      default() {
        return {
          color: "#606266"
        };
      }
    },
    // 滑块长度
    lineWidth: {
      type: [String, Number],
      default: 20
    },
    // 滑块高度
    lineHeight: {
      type: [String, Number],
      default: 3
    },
    // 滑块背景显示大小，当滑块背景设置为图片时使用
    lineBgSize: {
      type: String,
      default: "cover"
    },
    // 菜单item的样式
    itemStyle: {
      type: [String, Object],
      default() {
        return {
          height: "44px"
        };
      }
    },
    // 菜单是否可滚动
    scrollable: {
      type: Boolean,
      default: true
    },
    // 当前选中标签的索引
    current: {
      type: [Number, String],
      default: 0
    },
    // 默认读取的键名
    keyName: {
      type: String,
      default: "name"
    }
  },
  watch: {
    current: {
      immediate: true,
      handler(newValue, oldValue) {
        if (newValue !== this.innerCurrent) {
          this.innerCurrent = newValue;
          this.$nextTick(() => {
            this.resize();
          });
        }
      }
    },
    // list变化时，重新渲染list各项信息
    list() {
      this.$nextTick(() => {
        this.resize();
      });
    }
  },
  computed: {
    textStyle() {
      return (index) => {
        const style = {};
        const customeStyle = index === this.innerCurrent ? sheep_helper_index.addStyle(this.activeStyle) : sheep_helper_index.addStyle(this.inactiveStyle);
        if (this.list[index].disabled) {
          style.color = "#c8c9cc";
        }
        return sheep_helper_index.deepMerge(customeStyle, style);
      };
    }
  },
  async mounted() {
    this.init();
  },
  methods: {
    $uGetRect(selector, all) {
      return new Promise((resolve) => {
        common_vendor.index.createSelectorQuery().in(this)[all ? "selectAll" : "select"](selector).boundingClientRect((rect) => {
          if (all && Array.isArray(rect) && rect.length) {
            resolve(rect);
          }
          if (!all && rect) {
            resolve(rect);
          }
        }).exec();
      });
    },
    setLineLeft() {
      const tabItem = this.list[this.innerCurrent];
      if (!tabItem) {
        return;
      }
      let lineOffsetLeft = this.list.slice(0, this.innerCurrent).reduce((total, curr) => total + curr.rect.width, 0);
      const lineWidth = sheep_helper_index.getPx(this.lineWidth);
      this.lineOffsetLeft = lineOffsetLeft + (tabItem.rect.width - lineWidth) / 2;
      if (this.firstTime) {
        setTimeout(() => {
          this.firstTime = false;
        }, 10);
      }
    },
    // nvue下设置滑块的位置
    animation(x, duration = 0) {
    },
    // 点击某一个标签
    clickHandler(item, index) {
      this.$emit("click", {
        ...item,
        index
      });
      if (item.disabled)
        return;
      this.innerCurrent = index;
      this.resize();
      this.$emit("change", {
        ...item,
        index
      });
    },
    init() {
      sheep_helper_index.sleep().then(() => {
        this.resize();
      });
    },
    setScrollLeft() {
      const tabRect = this.list[this.innerCurrent];
      const offsetLeft = this.list.slice(0, this.innerCurrent).reduce((total, curr) => {
        return total + curr.rect.width;
      }, 0);
      const windowWidth = common_vendor.index.getWindowInfo().windowWidth;
      let scrollLeft = offsetLeft - (this.tabsRect.width - tabRect.rect.width) / 2 - (windowWidth - this.tabsRect.right) / 2 + this.tabsRect.left / 2;
      scrollLeft = Math.min(scrollLeft, this.scrollViewWidth - this.tabsRect.width);
      this.scrollLeft = Math.max(0, scrollLeft);
    },
    // 获取所有标签的尺寸
    resize() {
      if (this.list.length === 0) {
        return;
      }
      Promise.all([this.getTabsRect(), this.getAllItemRect()]).then(
        ([tabsRect, itemRect = []]) => {
          this.tabsRect = tabsRect;
          this.scrollViewWidth = 0;
          itemRect.map((item, index) => {
            this.scrollViewWidth += item.width;
            this.list[index].rect = item;
          });
          this.setLineLeft();
          this.setScrollLeft();
        }
      );
    },
    // 获取导航菜单的尺寸
    getTabsRect() {
      return new Promise((resolve) => {
        this.queryRect("u-tabs__wrapper__scroll-view").then((size) => resolve(size));
      });
    },
    // 获取所有标签的尺寸
    getAllItemRect() {
      return new Promise((resolve) => {
        const promiseAllArr = this.list.map(
          (item, index) => this.queryRect(`u-tabs__wrapper__nav__item-${index}`, true)
        );
        Promise.all(promiseAllArr).then((sizes) => resolve(sizes));
      });
    },
    // 获取各个标签的尺寸
    queryRect(el, item) {
      return new Promise((resolve) => {
        this.$uGetRect(`.${el}`).then((size) => {
          resolve(size);
        });
      });
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: common_vendor.f($props.list, (item, index, i0) => {
      return {
        a: common_vendor.t(item[$props.keyName]),
        b: common_vendor.n(item.disabled && "u-tabs__wrapper__nav__item__text--disabled"),
        c: common_vendor.s($options.textStyle(index)),
        d: index,
        e: common_vendor.o(($event) => $options.clickHandler(item, index), index),
        f: `u-tabs__wrapper__nav__item-${index}`,
        g: common_vendor.n(`u-tabs__wrapper__nav__item-${index}`),
        h: common_vendor.n(item.disabled && "u-tabs__wrapper__nav__item--disabled")
      };
    }),
    b: common_vendor.s($data.addStyle($props.itemStyle)),
    c: common_vendor.s({
      flex: $props.scrollable ? "" : 1
    }),
    d: common_vendor.s({
      width: $data.addUnit($props.lineWidth),
      transform: `translate(${$data.lineOffsetLeft}px)`,
      transitionDuration: `${$data.firstTime ? 0 : $props.duration}ms`,
      height: $data.addUnit($props.lineHeight),
      background: $props.lineColor ? $props.lineColor : "var(--ui-BG-Main)",
      backgroundSize: $props.lineBgSize
    }),
    e: $props.scrollable,
    f: $data.scrollLeft
  };
}
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-035e7744"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/ui/su-tabs/su-tabs.js.map
