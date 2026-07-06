"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_config_index = require("../../../sheep/config/index.js");
const sheep_helper_utils = require("../../../sheep/helper/utils.js");
const sheep_request_index = require("../../../sheep/request/index.js");
function useWebSocket(opt) {
  const options = common_vendor.reactive({
    url: (sheep_config_index.baseUrl + sheep_config_index.websocketPath).replace("http", "ws") + "?token=" + sheep_request_index.getRefreshToken(),
    // ws 地址
    isReconnecting: false,
    // 正在重新连接
    reconnectInterval: 3e3,
    // 重连间隔，单位毫秒
    heartBeatInterval: 5e3,
    // 心跳间隔，单位毫秒
    pingTimeoutDuration: 1e3,
    // 超过这个时间，后端没有返回pong，则判定后端断线了。
    heartBeatTimer: null,
    // 心跳计时器
    destroy: false,
    // 是否销毁
    pingTimeout: null,
    // 心跳检测定时器
    reconnectTimeout: null,
    // 重连定时器ID的属性
    onConnected: () => {
    },
    // 连接成功时触发
    onClosed: () => {
    },
    // 连接关闭时触发
    onMessage: (data) => {
    }
    // 收到消息
  });
  const SocketTask = common_vendor.ref(null);
  const initEventListeners = () => {
    SocketTask.value.onOpen(() => {
      common_vendor.index.__f__("log", "at pages/chat/util/useWebSocket.js:31", "WebSocket 连接成功");
      options.onConnected();
      startHeartBeat();
    });
    SocketTask.value.onMessage((res) => {
      try {
        if (res.data === "pong") {
          resetPingTimeout();
        } else {
          options.onMessage(JSON.parse(res.data));
        }
      } catch (error) {
        common_vendor.index.__f__("error", "at pages/chat/util/useWebSocket.js:47", error);
      }
    });
    SocketTask.value.onClose((event) => {
      if (options.destroy) {
        options.onClosed();
      } else {
        stopHeartBeat();
        reconnect();
      }
    });
  };
  const sendMessage = (message) => {
    if (SocketTask.value && !options.destroy) {
      SocketTask.value.send({ data: message });
    }
  };
  const startHeartBeat = () => {
    options.heartBeatTimer = setInterval(() => {
      sendMessage("ping");
      options.pingTimeout = setTimeout(() => {
        reconnect();
      }, options.pingTimeoutDuration);
    }, options.heartBeatInterval);
  };
  const stopHeartBeat = () => {
    clearInterval(options.heartBeatTimer);
    resetPingTimeout();
  };
  const reconnect = () => {
    if (options.destroy || !SocketTask.value) {
      return;
    }
    options.isReconnecting = true;
    if (options.reconnectTimeout) {
      clearTimeout(options.reconnectTimeout);
    }
    options.reconnectTimeout = setTimeout(() => {
      if (!options.destroy) {
        options.isReconnecting = false;
        initSocket();
      }
    }, options.reconnectInterval);
  };
  const resetPingTimeout = () => {
    if (options.pingTimeout) {
      clearTimeout(options.pingTimeout);
      options.pingTimeout = null;
    }
  };
  const close = () => {
    options.destroy = true;
    stopHeartBeat();
    if (options.reconnectTimeout) {
      clearTimeout(options.reconnectTimeout);
    }
    if (SocketTask.value) {
      SocketTask.value.close();
      SocketTask.value = null;
    }
  };
  const initSocket = () => {
    options.destroy = false;
    sheep_helper_utils.copyValueToTarget(options, opt);
    SocketTask.value = common_vendor.index.connectSocket({
      url: options.url,
      complete: () => {
      },
      success: () => {
      }
    });
    initEventListeners();
  };
  initSocket();
  common_vendor.onBeforeUnmount(() => {
    close();
  });
  return { options };
}
exports.useWebSocket = useWebSocket;
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/chat/util/useWebSocket.js.map
