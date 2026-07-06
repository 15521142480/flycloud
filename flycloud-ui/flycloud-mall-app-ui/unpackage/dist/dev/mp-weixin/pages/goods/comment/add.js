"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../../sheep/index.js");
const sheep_api_trade_order = require("../../../sheep/api/trade/order.js");
if (!Array) {
  const _easycom_s_goods_item2 = common_vendor.resolveComponent("s-goods-item");
  const _easycom_uni_rate2 = common_vendor.resolveComponent("uni-rate");
  const _easycom_uni_easyinput2 = common_vendor.resolveComponent("uni-easyinput");
  const _easycom_s_uploader2 = common_vendor.resolveComponent("s-uploader");
  const _easycom_su_fixed2 = common_vendor.resolveComponent("su-fixed");
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  (_easycom_s_goods_item2 + _easycom_uni_rate2 + _easycom_uni_easyinput2 + _easycom_s_uploader2 + _easycom_su_fixed2 + _easycom_s_layout2)();
}
const _easycom_s_goods_item = () => "../../../sheep/components/s-goods-item/s-goods-item.js";
const _easycom_uni_rate = () => "../../../uni_modules/uni-rate/components/uni-rate/uni-rate.js";
const _easycom_uni_easyinput = () => "../../../uni_modules/uni-easyinput/components/uni-easyinput/uni-easyinput.js";
const _easycom_s_uploader = () => "../../../sheep/components/s-uploader/s-uploader.js";
const _easycom_su_fixed = () => "../../../sheep/ui/su-fixed/su-fixed.js";
const _easycom_s_layout = () => "../../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  (_easycom_s_goods_item + _easycom_uni_rate + _easycom_uni_easyinput + _easycom_s_uploader + _easycom_su_fixed + _easycom_s_layout)();
}
const _sfc_main = {
  __name: "add",
  setup(__props) {
    const state = common_vendor.reactive({
      orderInfo: {},
      commentList: [],
      id: null
    });
    function toggleAnonymous(commentIndex, event) {
      state.commentList[commentIndex].anonymous = event.detail.value[0] === "anonymousChecked";
    }
    async function onSubmit() {
      for (const comment of state.commentList) {
        await sheep_api_trade_order.OrderApi.createOrderItemComment(comment);
      }
      sheep_index.sheep.$router.back();
    }
    function uploadSuccess(payload, commentIndex) {
      state.commentList[commentIndex].picUrls = payload.tempFilePaths;
    }
    common_vendor.onLoad(async (options) => {
      if (!options.id) {
        sheep_index.sheep.$helper.toast(`缺少订单信息，请检查`);
        return;
      }
      state.id = options.id;
      const { code, data } = await sheep_api_trade_order.OrderApi.getOrderDetail(state.id);
      if (code !== 0) {
        sheep_index.sheep.$helper.toast("无待评价订单");
        return;
      }
      data.items.forEach((item) => {
        state.commentList.push({
          anonymous: false,
          orderItemId: item.id,
          descriptionScores: 5,
          benefitScores: 5,
          content: "",
          picUrls: []
        });
      });
      state.orderInfo = data;
    });
    return (_ctx, _cache) => {
      return {
        a: common_vendor.f(state.orderInfo.items, (item, index, i0) => {
          return {
            a: "c66153cb-1-" + i0 + ",c66153cb-0",
            b: common_vendor.p({
              img: item.picUrl,
              title: item.spuName,
              skuText: item.properties.map((property) => property.valueName).join(" "),
              price: item.payPrice,
              num: item.count
            }),
            c: "c66153cb-2-" + i0 + ",c66153cb-0",
            d: common_vendor.o(($event) => state.commentList[index].descriptionScores = $event, item.id),
            e: common_vendor.p({
              modelValue: state.commentList[index].descriptionScores
            }),
            f: "c66153cb-3-" + i0 + ",c66153cb-0",
            g: common_vendor.o(($event) => state.commentList[index].benefitScores = $event, item.id),
            h: common_vendor.p({
              modelValue: state.commentList[index].benefitScores
            }),
            i: "c66153cb-4-" + i0 + ",c66153cb-0",
            j: common_vendor.o(($event) => state.commentList[index].content = $event, item.id),
            k: common_vendor.p({
              inputBorder: false,
              type: "textarea",
              maxlength: "120",
              autoHeight: true,
              placeholder: "宝贝满足你的期待吗？说说你的使用心得，分享给想买的他们吧~",
              modelValue: state.commentList[index].content
            }),
            l: common_vendor.o((payload) => uploadSuccess(payload, index), item.id),
            m: "c66153cb-5-" + i0 + ",c66153cb-0",
            n: common_vendor.o(($event) => state.commentList[index].images = $event, item.id),
            o: common_vendor.p({
              fileMediatype: "image",
              limit: "9",
              mode: "grid",
              imageStyles: {
                width: "168rpx",
                height: "168rpx"
              },
              url: state.commentList[index].images
            }),
            p: common_vendor.o((event) => toggleAnonymous(index, event), item.id),
            q: item.id
          };
        }),
        b: common_vendor.o(onSubmit, "47"),
        c: common_vendor.p({
          bottom: true,
          placeholder: true
        }),
        d: common_vendor.p({
          title: "评价"
        })
      };
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-c66153cb"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/goods/comment/add.js.map
