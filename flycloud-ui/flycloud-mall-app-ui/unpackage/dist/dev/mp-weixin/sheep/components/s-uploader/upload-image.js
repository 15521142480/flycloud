"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../index.js");
const _sfc_main = {
  name: "uploadImage",
  emits: ["uploadFiles", "choose", "delFile"],
  props: {
    filesList: {
      type: [Array, String],
      default() {
        return [];
      }
    },
    disabled: {
      type: Boolean,
      default: false
    },
    disablePreview: {
      type: Boolean,
      default: false
    },
    limit: {
      type: [Number, String],
      default: 9
    },
    imageStyles: {
      type: Object,
      default() {
        return {
          width: "auto",
          height: "auto",
          border: {}
        };
      }
    },
    delIcon: {
      type: Boolean,
      default: true
    },
    readonly: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    list() {
      if (typeof this.filesList === "string") {
        if (this.filesList) {
          return [this.filesList];
        } else {
          return [];
        }
      }
      return this.filesList;
    },
    styles() {
      let styles = {
        width: "auto",
        height: "auto",
        border: {}
      };
      return Object.assign(styles, this.imageStyles);
    },
    boxStyle() {
      const { width = "auto", height = "auto" } = this.styles;
      let obj = {};
      if (height === "auto") {
        if (width !== "auto") {
          obj.height = this.value2px(width);
          obj["padding-top"] = 0;
        } else {
          obj.height = 0;
        }
      } else {
        obj.height = this.value2px(height);
        obj["padding-top"] = 0;
      }
      if (width === "auto") {
        if (height !== "auto") {
          obj.width = this.value2px(height);
        } else {
          obj.width = "33.3%";
        }
      } else {
        obj.width = this.value2px(width);
      }
      let classles = "";
      for (let i in obj) {
        classles += `${i}:${obj[i]};`;
      }
      return classles;
    },
    borderStyle() {
      let { border } = this.styles;
      let obj = {};
      const widthDefaultValue = 1;
      const radiusDefaultValue = 3;
      if (typeof border === "boolean") {
        obj.border = border ? "1px #eee solid" : "none";
      } else {
        let width = border && border.width || widthDefaultValue;
        width = this.value2px(width);
        let radius = border && border.radius || radiusDefaultValue;
        radius = this.value2px(radius);
        obj = {
          "border-width": width,
          "border-style": border && border.style || "solid",
          "border-color": border && border.color || "#eee",
          "border-radius": radius
        };
      }
      let classles = "";
      for (let i in obj) {
        classles += `${i}:${obj[i]};`;
      }
      return classles;
    }
  },
  methods: {
    getImageUrl(url) {
      if ("blob:http:" === url.substr(0, 10)) {
        return url;
      } else {
        return sheep_index.sheep.$url.cdn(url);
      }
    },
    uploadFiles(item, index) {
      this.$emit("uploadFiles", item);
    },
    choose() {
      this.$emit("choose");
    },
    delFile(index) {
      this.$emit("delFile", index);
    },
    previewImage(img, index) {
      let urls = [];
      if (Number(this.limit) === 1 && this.disablePreview && !this.disabled) {
        this.$emit("choose");
      }
      if (this.disablePreview)
        return;
      this.list.forEach((i) => {
        urls.push(this.getImageUrl(i));
      });
      common_vendor.index.previewImage({
        urls,
        current: index
      });
    },
    value2px(value) {
      if (typeof value === "number") {
        value += "px";
      } else {
        if (value.indexOf("%") === -1) {
          value = value.indexOf("px") !== -1 ? value : value + "px";
        }
      }
      return value;
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return common_vendor.e({
    a: common_vendor.f($options.list, (url, index, i0) => {
      return common_vendor.e({
        a: $options.getImageUrl(url),
        b: common_vendor.o(($event) => $options.previewImage(url, index), index)
      }, $props.delIcon && !$props.readonly ? {
        c: common_vendor.o(($event) => $options.delFile(index), index)
      } : {}, {
        d: index
      });
    }),
    b: $props.delIcon && !$props.readonly,
    c: common_vendor.s($options.borderStyle),
    d: common_vendor.s($options.boxStyle),
    e: $options.list.length < $props.limit && !$props.readonly
  }, $options.list.length < $props.limit && !$props.readonly ? {
    f: common_vendor.s($options.borderStyle),
    g: common_vendor.o((...args) => $options.choose && $options.choose(...args), "7a"),
    h: common_vendor.s($options.boxStyle)
  } : {});
}
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/components/s-uploader/upload-image.js.map
