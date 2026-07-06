"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_helper_index = require("../../helper/index.js");
const _sfc_main = {
  name: "su-tabbar-item",
  props: {
    customStyle: {
      type: [Object, String],
      default: () => ({})
    },
    customClass: {
      type: String,
      default: ""
    },
    // 跳转的页面路径
    url: {
      type: String,
      default: ""
    },
    // 页面跳转的类型
    linkType: {
      type: String,
      default: "navigateTo"
    },
    // item标签的名称，作为与u-tabbar的value参数匹配的标识符
    name: {
      type: [String, Number, null],
      default: ""
    },
    // uView内置图标或者绝对路径的图片
    icon: {
      icon: String,
      default: ""
    },
    // 右上角的角标提示信息
    badge: {
      type: [String, Number, null],
      default: ""
    },
    // 是否显示圆点，将会覆盖badge参数
    dot: {
      type: Boolean,
      default: false
    },
    // 描述文本
    text: {
      type: String,
      default: ""
    },
    // 控制徽标的位置，对象或者字符串形式，可以设置top和right属性
    badgeStyle: {
      type: Object,
      default: () => {
      }
    },
    isCenter: {
      type: Boolean,
      default: false
    },
    centerImage: {
      type: String,
      default: ""
    }
  },
  data() {
    return {
      isActive: false,
      // 是否处于激活状态
      addStyle: sheep_helper_index.addStyle,
      parentData: {
        value: null,
        activeColor: "",
        // 选中标签的颜色
        inactiveColor: ""
        // 未选中标签的颜色
      },
      parent: {}
    };
  },
  created() {
    this.init();
  },
  methods: {
    getParentData(parentName = "") {
      if (!this.parent)
        this.parent = {};
      this.parent = sheep_helper_index.$parent.call(this, parentName);
      if (this.parent.children) {
        this.parent.children.indexOf(this) === -1 && this.parent.children.push(this);
      }
      if (this.parent && this.parentData) {
        Object.keys(this.parentData).map((key) => {
          this.parentData[key] = this.parent[key];
        });
      }
    },
    init() {
      this.updateParentData();
      if (!this.parent) {
        common_vendor.index.__f__("log", "at sheep/ui/su-tabbar-item/su-tabbar-item.vue:157", "u-tabbar-item必须搭配u-tabbar组件使用");
      }
      const index = this.parent.children.indexOf(this);
      this.isActive = (this.name.split("?")[0] || index) === this.parentData.value;
    },
    updateParentData() {
      this.getParentData("su-tabbar");
    },
    // 此方法将会被父组件u-tabbar调用
    updateFromParent() {
      this.init();
    },
    clickHandler() {
      this.$nextTick(() => {
        const index = this.parent.children.indexOf(this);
        const name = this.name || index;
        if (name !== this.parent.value) {
          this.parent.$emit("change", name);
        }
        this.$emit("click", name);
      });
    }
  }
};
if (!Array) {
  const _easycom_uni_badge2 = common_vendor.resolveComponent("uni-badge");
  _easycom_uni_badge2();
}
const _easycom_uni_badge = () => "../../../uni_modules/uni-badge/components/uni-badge/uni-badge.js";
if (!Math) {
  _easycom_uni_badge();
}
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return common_vendor.e({
    a: $props.isCenter
  }, $props.isCenter ? {
    b: $props.centerImage
  } : common_vendor.e({
    c: $props.icon
  }, $props.icon ? {
    d: $props.icon,
    e: $data.isActive ? $data.parentData.activeColor : $data.parentData.inactiveColor
  } : common_vendor.e({
    f: $data.isActive
  }, $data.isActive ? {} : {}), {
    g: common_vendor.p({
      absolute: "rightTop",
      size: "small",
      text: $props.badge || ($props.dot ? 1 : null),
      customStyle: $props.badgeStyle,
      isDot: $props.dot
    }),
    h: common_vendor.t($props.text),
    i: $data.isActive ? $data.parentData.activeColor : $data.parentData.inactiveColor
  }), {
    j: common_vendor.s($data.addStyle($props.customStyle)),
    k: common_vendor.o((...args) => $options.clickHandler && $options.clickHandler(...args), "12")
  });
}
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-a62e13d6"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/ui/su-tabbar-item/su-tabbar-item.js.map
