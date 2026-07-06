"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_api_promotion_kefu = require("../../../sheep/api/promotion/kefu.js");
const sheep_helper_utils = require("../../../sheep/helper/utils.js");
const sheep_index = require("../../../sheep/index.js");
if (!Array) {
  const _easycom_su_fixed2 = common_vendor.resolveComponent("su-fixed");
  _easycom_su_fixed2();
}
const _easycom_su_fixed = () => "../../../sheep/ui/su-fixed/su-fixed.js";
if (!Math) {
  (MessageListItem + _easycom_su_fixed)();
}
const MessageListItem = () => "./messageListItem.js";
const _sfc_main = {
  __name: "messageList",
  setup(__props, { expose: __expose }) {
    common_vendor.useCssVars((_ctx) => ({
      "5db622f3": chatScrollHeight.value
    }));
    const { safeAreaInsets } = sheep_index.sheep.$platform.device;
    const safeAreaInsetsBottom = safeAreaInsets.bottom + "px";
    const messageList = common_vendor.ref([]);
    const showTip = common_vendor.ref(false);
    const showNewMessageTip = common_vendor.ref(false);
    const refreshMessage = common_vendor.ref(false);
    const isLoading = common_vendor.ref(false);
    const hasMore = common_vendor.ref(true);
    const keyboardHeight = common_vendor.ref(0);
    const scroll = common_vendor.ref({
      top: 0,
      oldTop: 0
    });
    const queryParams = common_vendor.reactive({
      no: 1,
      limit: 20,
      createTime: void 0
    });
    const chatScrollHeight = common_vendor.computed(() => {
      const baseHeight = "calc(100vh - 150px - " + safeAreaInsetsBottom + ")";
      if (keyboardHeight.value > 0) {
        return `calc(${baseHeight} - ${keyboardHeight.value}px)`;
      }
      return baseHeight;
    });
    const getMessageList = async () => {
      isLoading.value = true;
      try {
        const { data } = await sheep_api_promotion_kefu.KeFuApi.getKefuMessageList(queryParams);
        if (sheep_helper_utils.isEmpty(data)) {
          hasMore.value = false;
          return;
        }
        if (queryParams.no > 1 && refreshMessage.value) {
          const newMessageList = [];
          for (const message of data) {
            if (messageList.value.some((val) => val.id === message.id)) {
              continue;
            }
            newMessageList.push(message);
          }
          messageList.value = [...newMessageList, ...messageList.value];
          refreshMessage.value = false;
          return;
        }
        if (queryParams.no > 1) {
          if (data.length < queryParams.limit) {
            hasMore.value = false;
          }
          const historyMessages = data.filter(
            (msg) => !messageList.value.some((existing) => existing.id === msg.id)
          );
          if (historyMessages.length > 0) {
            messageList.value = [...messageList.value, ...historyMessages];
          }
        } else {
          messageList.value = data;
          if (data.length < queryParams.limit) {
            hasMore.value = false;
          }
        }
        if (data.slice(-1).length > 0) {
          queryParams.createTime = sheep_helper_utils.formatDate(data.slice(-1)[0].createTime);
        }
      } finally {
        isLoading.value = false;
      }
    };
    const loadMoreHistory = async () => {
      if (isLoading.value || !hasMore.value)
        return;
      queryParams.no += 1;
      await getMessageList();
    };
    const refreshMessageList = async (message = void 0) => {
      if (typeof message !== "undefined") {
        messageList.value.unshift(message);
        showNewMessageTip.value = true;
      } else {
        queryParams.createTime = void 0;
        refreshMessage.value = true;
        await getMessageList();
      }
      if (queryParams.no > 1) {
        showTip.value = true;
      } else {
        scrollToTop();
      }
    };
    const scrollToTop = () => {
      scroll.value.top = scroll.value.oldTop;
      setTimeout(() => {
        scroll.value.top = 0;
      }, 200);
      showTip.value = false;
    };
    const setKeyboardHeight = (height) => {
      keyboardHeight.value = height;
      if (height > 0) {
        scrollToTop();
      }
    };
    __expose({ getMessageList, refreshMessageList });
    const onScroll = (e) => {
      const { scrollTop } = e.detail;
      scroll.value.oldTop = scrollTop;
      if (scrollTop > 100) {
        showTip.value = true;
      } else {
        showTip.value = false;
      }
    };
    const setupKeyboardListeners = () => {
      common_vendor.index.onKeyboardHeightChange((res) => {
        setKeyboardHeight(res.height);
      });
    };
    common_vendor.onMounted(() => {
      queryParams.no = 1;
      scroll.value = {
        top: 0,
        oldTop: 0
      };
      getMessageList();
      setupKeyboardListeners();
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: isLoading.value
      }, isLoading.value ? {} : {}, {
        b: common_vendor.f(messageList.value, (item, index, i0) => {
          return {
            a: "7be030ca-0-" + i0,
            b: common_vendor.p({
              message: item,
              ["message-index"]: index,
              ["message-list"]: messageList.value
            }),
            c: item.id
          };
        }),
        c: scroll.value.top,
        d: common_vendor.o(onScroll, "50"),
        e: common_vendor.o(loadMoreHistory, "a4"),
        f: common_vendor.s(_ctx.__cssVars()),
        g: showTip.value
      }, showTip.value ? {
        h: common_vendor.t(showNewMessageTip.value ? "有新消息" : "回到底部"),
        i: common_vendor.o(scrollToTop, "81")
      } : {}, {
        j: common_vendor.s(_ctx.__cssVars()),
        k: common_vendor.p({
          bottom: true
        })
      });
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-7be030ca"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/chat/components/messageList.js.map
