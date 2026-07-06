"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../index.js");
const sheep_hooks_useModal = require("../../hooks/useModal.js");
if (!Array) {
  const _easycom_su_popup2 = common_vendor.resolveComponent("su-popup");
  _easycom_su_popup2();
}
const _easycom_su_popup = () => "../../ui/su-popup/su-popup.js";
if (!Math) {
  _easycom_su_popup();
}
const _sfc_main = {
  __name: "s-popup-image",
  props: {
    data: {
      type: Object,
      default() {
      }
    }
  },
  setup(__props) {
    const props = __props;
    const modalStore = JSON.parse(common_vendor.index.getStorageSync("modal-store") || "{}");
    common_vendor.index.__f__("log", "at sheep/components/s-popup-image/s-popup-image.vue:42", modalStore);
    const advHistory = modalStore.advHistory || [];
    const currentIndex = common_vendor.ref(0);
    const popupList = common_vendor.computed(() => {
      const list = props.data.list || [];
      const newList = [];
      if (list.length > 0) {
        list.forEach((adv) => {
          if (adv.showType === "once" && advHistory.includes(adv.imgUrl)) {
            adv.isShow = false;
          } else {
            adv.isShow = true;
            newList.push(adv);
          }
          sheep_hooks_useModal.saveAdvHistory(adv);
        });
      }
      return newList;
    });
    function onPopup(path) {
      sheep_index.sheep.$router.go(path);
    }
    function onClose(index) {
      currentIndex.value = index + 1;
      popupList.value[index].isShow = false;
    }
    return (_ctx, _cache) => {
      return {
        a: common_vendor.f(popupList.value, (item, index, i0) => {
          return common_vendor.e({
            a: index === currentIndex.value
          }, index === currentIndex.value ? {
            b: common_vendor.unref(sheep_index.sheep).$url.cdn(item.imgUrl),
            c: common_vendor.o(($event) => onPopup(item.url), index),
            d: common_vendor.o(($event) => onClose(index), index),
            e: "ef138843-0-" + i0,
            f: common_vendor.p({
              show: item.isShow,
              type: "center",
              backgroundColor: "none",
              round: "0",
              showClose: true,
              isMaskClick: false
            })
          } : {}, {
            g: index
          });
        })
      };
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-ef138843"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/components/s-popup-image/s-popup-image.js.map
