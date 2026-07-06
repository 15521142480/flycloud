"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_index = require("../../sheep/index.js");
if (!Array) {
  const _easycom_l_painter2 = common_vendor.resolveComponent("l-painter");
  _easycom_l_painter2();
}
const _easycom_l_painter = () => "../../uni_modules/lime-painter/components/l-painter/l-painter.js";
if (!Math) {
  _easycom_l_painter();
}
const _sfc_main = {
  __name: "pickUpVerify",
  props: {
    orderInfo: {
      type: Object,
      default() {
      }
    },
    systemStore: {
      type: Object,
      default() {
      }
    }
  },
  setup(__props, { expose: __expose }) {
    const props = __props;
    const state = common_vendor.reactive({
      qrcodeSize: 145
    });
    const showMaoLocation = () => {
      if (!props.systemStore.latitude || !props.systemStore.longitude) {
        sheep_index.sheep.$helper.toast("缺少经纬度信息无法查看地图!");
        return;
      }
      common_vendor.index.openLocation({
        latitude: props.systemStore.latitude,
        longitude: props.systemStore.longitude,
        scale: 8,
        name: props.systemStore.name,
        address: props.systemStore.areaName + props.systemStore.detailAddress
      });
    };
    const painterRef = common_vendor.ref();
    const painterImageUrl = common_vendor.ref();
    const showPainter = common_vendor.ref(true);
    const renderPoster = async (poster) => {
      await painterRef.value.render(poster);
    };
    const setPainterImageUrl = (path) => {
      painterImageUrl.value = path;
      showPainter.value = false;
    };
    const markCode = (text) => {
      renderPoster({
        css: {
          width: `${state.qrcodeSize}px`,
          height: `${state.qrcodeSize}px`
        },
        views: [
          {
            type: "qrcode",
            text,
            css: {
              width: `${state.qrcodeSize}px`,
              height: `${state.qrcodeSize}px`
            }
          }
        ]
      });
    };
    __expose({
      markCode
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: __props.orderInfo.deliveryType === 2 && __props.orderInfo.payStatus
      }, __props.orderInfo.deliveryType === 2 && __props.orderInfo.payStatus ? common_vendor.e({
        b: !!painterImageUrl.value
      }, !!painterImageUrl.value ? {
        c: painterImageUrl.value,
        d: `${state.qrcodeSize}px`,
        e: `${state.qrcodeSize}px`
      } : {}, {
        f: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/writeOff.png"),
        g: common_vendor.t(__props.orderInfo.pickUpVerifyCode),
        h: common_vendor.t(__props.systemStore.openingTime),
        i: common_vendor.t(__props.systemStore.closingTime)
      }) : {}, {
        j: __props.orderInfo.deliveryType === 2
      }, __props.orderInfo.deliveryType === 2 ? {
        k: common_vendor.o(showMaoLocation, "54")
      } : {}, {
        l: showPainter.value
      }, showPainter.value ? {
        m: common_vendor.sr(painterRef, "86ad57c6-0", {
          "k": "painterRef"
        }),
        n: common_vendor.o(setPainterImageUrl, "35"),
        o: common_vendor.p({
          isCanvasToTempFilePath: true,
          pathType: "url",
          hidden: true
        })
      } : {});
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-86ad57c6"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/order/pickUpVerify.js.map
