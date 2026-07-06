"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_index = require("../../sheep/index.js");
const pages_chat_util_constants = require("./util/constants.js");
const sheep_api_infra_file = require("../../sheep/api/infra/file.js");
const sheep_api_promotion_kefu = require("../../sheep/api/promotion/kefu.js");
const pages_chat_util_useWebSocket = require("./util/useWebSocket.js");
const sheep_helper_utils = require("../../sheep/helper/utils.js");
if (!Array) {
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  _easycom_s_layout2();
}
const _easycom_s_layout = () => "../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  (MessageInput + MessageList + ToolsPopup + SelectPopup + _easycom_s_layout)();
}
const MessageList = () => "./components/messageList.js";
const ToolsPopup = () => "./components/toolsPopup.js";
const MessageInput = () => "./components/messageInput.js";
const SelectPopup = () => "./components/select-popup.js";
const _sfc_main = {
  __name: "index",
  setup(__props) {
    const sys_navBar = sheep_index.sheep.$platform.navbar;
    const chat = common_vendor.reactive({
      msg: "",
      scrollInto: "",
      showTools: false,
      toolsMode: "",
      showSelect: false,
      selectMode: ""
    });
    async function onSendMessage() {
      if (!chat.msg)
        return;
      try {
        const data = {
          contentType: pages_chat_util_constants.KeFuMessageContentTypeEnum.TEXT,
          content: JSON.stringify({ text: chat.msg })
        };
        await sheep_api_promotion_kefu.KeFuApi.sendKefuMessage(data);
        chat.msg = "";
      } finally {
        chat.showTools = false;
      }
    }
    const messageListRef = common_vendor.ref();
    function handleToolsClose() {
      chat.showTools = false;
      chat.toolsMode = "";
    }
    function onEmoji(item) {
      chat.msg += item.name;
    }
    function onTools(mode) {
      if (isReconnecting.value) {
        sheep_index.sheep.$helper.toast("您已掉线！请返回重试");
        return;
      }
      if (chat.showTools && chat.toolsMode === mode) {
        handleToolsClose();
        return;
      }
      if (chat.showTools && chat.toolsMode !== mode) {
        chat.showTools = false;
        chat.toolsMode = "";
      }
      setTimeout(() => {
        chat.toolsMode = mode;
        chat.showTools = true;
      }, 200);
    }
    function onShowSelect(mode) {
      chat.showTools = false;
      chat.showSelect = true;
      chat.selectMode = mode;
    }
    async function onSelect({ type, data }) {
      let msg;
      switch (type) {
        case "image":
          const res = await sheep_api_infra_file.FileApi.uploadFile(data.tempFiles[0].path);
          const uploadResult = sheep_api_infra_file.FileApi.normalizeUploadResult(res);
          msg = {
            contentType: pages_chat_util_constants.KeFuMessageContentTypeEnum.IMAGE,
            content: JSON.stringify({ picUrl: uploadResult.path })
          };
          break;
        case "goods":
          msg = {
            contentType: pages_chat_util_constants.KeFuMessageContentTypeEnum.PRODUCT,
            content: JSON.stringify(data)
          };
          break;
        case "order":
          msg = {
            contentType: pages_chat_util_constants.KeFuMessageContentTypeEnum.ORDER,
            content: JSON.stringify(data)
          };
          break;
      }
      if (msg) {
        await sheep_api_promotion_kefu.KeFuApi.sendKefuMessage(msg);
        await messageListRef.value.refreshMessageList();
        chat.showTools = false;
        chat.showSelect = false;
        chat.selectMode = "";
      }
    }
    const { options } = pages_chat_util_useWebSocket.useWebSocket({
      // 连接成功
      onConnected: async () => {
      },
      // 收到消息
      onMessage: async (data) => {
        const type = data.type;
        if (!type) {
          common_vendor.index.__f__("error", "at pages/chat/index.vue:178", "未知的消息类型：" + data);
          return;
        }
        if (type === pages_chat_util_constants.WebSocketMessageTypeConstants.KEFU_MESSAGE_TYPE) {
          await messageListRef.value.refreshMessageList(sheep_helper_utils.jsonParse(data.content));
          return;
        }
        if (type === pages_chat_util_constants.WebSocketMessageTypeConstants.KEFU_MESSAGE_ADMIN_READ) {
          common_vendor.index.__f__("log", "at pages/chat/index.vue:189", "管理员已读消息");
          sheep_index.sheep.$helper.toast("客服已读您的消息");
        }
      }
    });
    const isReconnecting = common_vendor.toRefs(options).isReconnecting;
    return (_ctx, _cache) => {
      return {
        a: common_vendor.unref(sys_navBar) + "px",
        b: common_vendor.o(onTools, "eb"),
        c: common_vendor.o(onSendMessage, "ee"),
        d: common_vendor.o(($event) => chat.msg = $event, "24"),
        e: common_vendor.p({
          ["auto-focus"]: false,
          ["show-char-count"]: true,
          ["max-length"]: 500,
          modelValue: chat.msg
        }),
        f: common_vendor.sr(messageListRef, "5a559478-1,5a559478-0", {
          "k": "messageListRef"
        }),
        g: common_vendor.o(onTools, "2a"),
        h: common_vendor.o(onSendMessage, "c9"),
        i: common_vendor.o(($event) => chat.msg = $event, "be"),
        j: common_vendor.p({
          ["auto-focus"]: false,
          ["show-char-count"]: true,
          ["max-length"]: 500,
          modelValue: chat.msg
        }),
        k: common_vendor.o(handleToolsClose, "0f"),
        l: common_vendor.o(onEmoji, "65"),
        m: common_vendor.o(onSelect, "20"),
        n: common_vendor.o(onShowSelect, "20"),
        o: common_vendor.p({
          ["show-tools"]: chat.showTools,
          ["tools-mode"]: chat.toolsMode
        }),
        p: common_vendor.o(onSelect, "60"),
        q: common_vendor.o(($event) => chat.showSelect = false, "c6"),
        r: common_vendor.p({
          mode: chat.selectMode,
          show: chat.showSelect
        }),
        s: common_vendor.p({
          title: !common_vendor.unref(isReconnecting) ? "连接客服成功" : "会话重连中",
          navbar: "inner"
        })
      };
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-5a559478"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/chat/index.js.map
