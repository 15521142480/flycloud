"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../index.js");
if (!Array) {
  const _easycom_uni_tag2 = common_vendor.resolveComponent("uni-tag");
  _easycom_uni_tag2();
}
const _easycom_uni_tag = () => "../../../uni_modules/uni-tag/components/uni-tag/uni-tag.js";
if (!Math) {
  _easycom_uni_tag();
}
const _sfc_main = {
  __name: "s-address-item",
  props: {
    item: {
      type: Object,
      default() {
      }
    },
    hasBorderBottom: {
      type: Boolean,
      defult: true
    }
  },
  setup(__props) {
    const props = __props;
    const onEdit = () => {
      sheep_index.sheep.$router.go("/pages/user/address/edit", {
        id: props.item.id
      });
    };
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: !common_vendor.unref(common_vendor.isEmpty)(props.item)
      }, !common_vendor.unref(common_vendor.isEmpty)(props.item) ? common_vendor.e({
        b: props.item.defaultStatus
      }, props.item.defaultStatus ? {
        c: common_vendor.p({
          size: "small",
          ["custom-style"]: "background-color: var(--ui-BG-Main); border-color: var(--ui-BG-Main); color: #fff;",
          text: "默认"
        })
      } : {}, {
        d: common_vendor.t(props.item.areaName),
        e: common_vendor.t(props.item.detailAddress),
        f: common_vendor.t(props.item.name),
        g: common_vendor.t(props.item.mobile)
      }) : {}, {
        h: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/user/address/edit.png"),
        i: common_vendor.o(onEdit, "94"),
        j: common_vendor.n({
          "border-bottom": props.hasBorderBottom
        })
      });
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-bb7deb2b"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/components/s-address-item/s-address-item.js.map
