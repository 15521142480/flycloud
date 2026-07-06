"use strict";
const common_vendor = require("../../common/vendor.js");
const _sfc_main = {
  name: "HighlightNumber",
  props: {
    content: {
      type: String,
      required: true
    }
  },
  computed: {
    formattedContent() {
      const phoneRegex = /(1[3-9]\d{9})/g;
      const numberRegex = /(\d+)/g;
      let text = this.content;
      let result = [];
      let match;
      while ((match = phoneRegex.exec(text)) !== null) {
        if (match.index > 0) {
          const before = text.slice(0, match.index);
          result.push(...this.splitAndPush(before, false, false));
        }
        result.push({ text: match[0], isNumber: true, isPhone: true });
        text = text.slice(match.index + match[0].length);
      }
      while ((match = numberRegex.exec(text)) !== null) {
        if (match.index > 0) {
          const before = text.slice(0, match.index);
          result.push(...this.splitAndPush(before, false, false));
        }
        result.push({ text: match[0], isNumber: true });
        text = text.slice(match.index + match[0].length);
      }
      if (text.length > 0) {
        result.push(...this.splitAndPush(text, false, false));
      }
      return result;
    }
  },
  methods: {
    splitAndPush(str, isNumber = false, isPhone = false) {
      return str.split("").map((char) => ({ text: char, isNumber, isPhone }));
    },
    handleClick(part) {
      if (part.isPhone) {
        this.$emit("phone-click", { phoneNumber: part.text });
      } else if (part.isNumber) {
        this.$emit("number-click", { number: part.text });
      }
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: common_vendor.f($options.formattedContent, (part, index, i0) => {
      return {
        a: common_vendor.t(part.text),
        b: index,
        c: common_vendor.o(($event) => $options.handleClick(part), index),
        d: part.isNumber ? 1 : "",
        e: part.isPhone ? 1 : ""
      };
    })
  };
}
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-732c0235"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/components/HighlightNumberText.js.map
