"use strict";
const common_vendor = require("../../../common/vendor.js");
const pages_chat_util_constants = require("../util/constants.js");
const pages_chat_util_emoji = require("../util/emoji.js");
const sheep_index = require("../../../sheep/index.js");
const sheep_helper_utils = require("../../../sheep/helper/utils.js");
if (!Array) {
  const _easycom_mp_html2 = common_vendor.resolveComponent("mp-html");
  const _easycom_su_image2 = common_vendor.resolveComponent("su-image");
  (_easycom_mp_html2 + _easycom_su_image2)();
}
const _easycom_mp_html = () => "../../../uni_modules/mp-html/components/mp-html/mp-html.js";
const _easycom_su_image = () => "../../../sheep/ui/su-image/su-image.js";
if (!Math) {
  (_easycom_mp_html + _easycom_su_image + GoodsItem + OrderItem)();
}
const GoodsItem = () => "./goods.js";
const OrderItem = () => "./order.js";
const _sfc_main = {
  __name: "messageListItem",
  props: {
    // 消息
    message: {
      type: Object,
      default: () => ({})
    },
    // 消息索引
    messageIndex: {
      type: Number,
      default: 0
    },
    // 消息列表
    messageList: {
      type: Array,
      default: () => []
    }
  },
  setup(__props) {
    const props = __props;
    const getMessageContent = common_vendor.computed(() => (item) => sheep_helper_utils.jsonParse(item.content));
    const userInfo = common_vendor.computed(() => sheep_index.sheep.$store("user").userInfo);
    const showTime = common_vendor.computed(() => (item, index) => {
      if (common_vendor.unref(props.messageList)[index + 1]) {
        let dateString = common_vendor.dayjs(common_vendor.unref(props.messageList)[index + 1].createTime).fromNow();
        return dateString !== common_vendor.dayjs(common_vendor.unref(item).createTime).fromNow();
      }
      return false;
    });
    const emojiMap = common_vendor.computed(() => {
      const map = /* @__PURE__ */ new Map();
      pages_chat_util_emoji.emojiList.forEach((emoji) => {
        map.set(emoji.name, emoji.file);
      });
      return map;
    });
    function replaceEmoji(data) {
      let newData = data;
      if (typeof newData !== "object") {
        let reg = /\[(.+?)]/g;
        let zhEmojiName = newData.match(reg);
        if (zhEmojiName) {
          zhEmojiName.forEach((item) => {
            const emojiFile = emojiMap.value.get(item) || "";
            if (emojiFile) {
              newData = newData.replace(
                item,
                `<img class="chat-img" style="width: 24px;height: 24px;margin: 0 3px;vertical-align: middle;" src="${sheep_index.sheep.$url.cdn(
                  "/static/img/chat/emoji/" + emojiFile
                )}"/>`
              );
            }
          });
        }
      }
      return newData;
    }
    const processedContent = common_vendor.computed(() => {
      if (props.message.contentType === pages_chat_util_constants.KeFuMessageContentTypeEnum.TEXT) {
        return replaceEmoji(getMessageContent.value(props.message).text || props.message.content);
      }
      return props.message.content;
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: __props.message.contentType === common_vendor.unref(pages_chat_util_constants.KeFuMessageContentTypeEnum).SYSTEM
      }, __props.message.contentType === common_vendor.unref(pages_chat_util_constants.KeFuMessageContentTypeEnum).SYSTEM ? {
        b: common_vendor.t(__props.message.content)
      } : {}, {
        c: __props.message.contentType !== common_vendor.unref(pages_chat_util_constants.KeFuMessageContentTypeEnum).SYSTEM && showTime.value(__props.message, __props.messageIndex)
      }, __props.message.contentType !== common_vendor.unref(pages_chat_util_constants.KeFuMessageContentTypeEnum).SYSTEM && showTime.value(__props.message, __props.messageIndex) ? {
        d: common_vendor.t(common_vendor.unref(sheep_helper_utils.formatDate)(__props.message.createTime))
      } : {}, {
        e: __props.message.contentType !== common_vendor.unref(pages_chat_util_constants.KeFuMessageContentTypeEnum).SYSTEM
      }, __props.message.contentType !== common_vendor.unref(pages_chat_util_constants.KeFuMessageContentTypeEnum).SYSTEM ? common_vendor.e({
        f: __props.message.senderType === common_vendor.unref(pages_chat_util_constants.UserTypeEnum).ADMIN,
        g: common_vendor.unref(sheep_index.sheep).$url.cdn(__props.message.senderAvatar) || common_vendor.unref(sheep_index.sheep).$url.static(""),
        h: __props.message.contentType === common_vendor.unref(pages_chat_util_constants.KeFuMessageContentTypeEnum).TEXT
      }, __props.message.contentType === common_vendor.unref(pages_chat_util_constants.KeFuMessageContentTypeEnum).TEXT ? {
        i: common_vendor.p({
          content: processedContent.value,
          domain: common_vendor.unref(sheep_index.sheep).$url.cdn(""),
          ["lazy-load"]: true
        }),
        j: __props.message.senderType === common_vendor.unref(pages_chat_util_constants.UserTypeEnum).ADMIN ? 1 : ""
      } : {}, {
        k: __props.message.contentType === common_vendor.unref(pages_chat_util_constants.KeFuMessageContentTypeEnum).IMAGE
      }, __props.message.contentType === common_vendor.unref(pages_chat_util_constants.KeFuMessageContentTypeEnum).IMAGE ? {
        l: common_vendor.p({
          isPreview: true,
          previewList: [common_vendor.unref(sheep_index.sheep).$url.cdn(getMessageContent.value(__props.message).picUrl || __props.message.content)],
          current: 0,
          src: common_vendor.unref(sheep_index.sheep).$url.cdn(getMessageContent.value(__props.message).picUrl || __props.message.content),
          height: 200,
          width: 200,
          mode: "aspectFill"
        }),
        m: __props.message.senderType === common_vendor.unref(pages_chat_util_constants.UserTypeEnum).ADMIN ? 1 : ""
      } : {}, {
        n: __props.message.contentType === common_vendor.unref(pages_chat_util_constants.KeFuMessageContentTypeEnum).PRODUCT
      }, __props.message.contentType === common_vendor.unref(pages_chat_util_constants.KeFuMessageContentTypeEnum).PRODUCT ? {
        o: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/goods/index", {
          id: getMessageContent.value(__props.message).spuId
        }), "ad"),
        p: common_vendor.p({
          goodsData: getMessageContent.value(__props.message)
        })
      } : {}, {
        q: __props.message.contentType === common_vendor.unref(pages_chat_util_constants.KeFuMessageContentTypeEnum).ORDER
      }, __props.message.contentType === common_vendor.unref(pages_chat_util_constants.KeFuMessageContentTypeEnum).ORDER ? {
        r: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/order/detail", {
          id: getMessageContent.value(__props.message).id
        }), "3c"),
        s: common_vendor.p({
          orderData: getMessageContent.value(__props.message)
        })
      } : {}, {
        t: __props.message.senderType === common_vendor.unref(pages_chat_util_constants.UserTypeEnum).MEMBER
      }, __props.message.senderType === common_vendor.unref(pages_chat_util_constants.UserTypeEnum).MEMBER ? {
        v: common_vendor.unref(sheep_index.sheep).$url.cdn(userInfo.value.avatar) || common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/chat/default.png")
      } : {}, {
        w: common_vendor.n(__props.message.senderType === common_vendor.unref(pages_chat_util_constants.UserTypeEnum).ADMIN ? `ss-row-left` : __props.message.senderType === common_vendor.unref(pages_chat_util_constants.UserTypeEnum).MEMBER ? `ss-row-right` : "")
      }) : {});
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-7b6b402a"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/chat/components/messageListItem.js.map
