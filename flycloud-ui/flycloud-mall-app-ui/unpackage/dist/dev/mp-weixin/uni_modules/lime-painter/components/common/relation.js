"use strict";
const styles = (v = "") => v.split(";").filter((v2) => v2 && !/^[\n\s]+$/.test(v2)).map((v2) => {
  const key = v2.slice(0, v2.indexOf(":"));
  const value = v2.slice(v2.indexOf(":") + 1);
  return {
    [key.replace(/-([a-z])/g, function() {
      return arguments[1].toUpperCase();
    }).replace(/\s+/g, "")]: value.replace(/^\s+/, "").replace(/\s+$/, "") || ""
  };
});
function parent(parent2) {
  return {
    provide() {
      return {
        [parent2]: this
      };
    },
    data() {
      return {
        el: {
          id: null,
          css: {},
          views: []
        }
      };
    },
    watch: {
      css: {
        handler(v) {
          if (this.canvasId) {
            this.el.css = (typeof v == "object" ? v : v && Object.assign(...styles(v))) || {};
            this.canvasWidth = this.el.css && this.el.css.width || this.canvasWidth;
            this.canvasHeight = this.el.css && this.el.css.height || this.canvasHeight;
          }
        },
        immediate: true
      }
    }
  };
}
exports.parent = parent;
//# sourceMappingURL=../../../../../.sourcemap/mp-weixin/uni_modules/lime-painter/components/common/relation.js.map
