"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_config_index = require("../config/index.js");
const sheep_config_server = require("../config/server.js");
const sheep_store_index = require("../store/index.js");
const sheep_platform_index = require("../platform/index.js");
const sheep_hooks_useModal = require("../hooks/useModal.js");
const sheep_api_member_auth = require("../api/member/auth.js");
const sheep_helper_const = require("../helper/const.js");
const options = {
  // 显示操作成功消息 默认不显示
  showSuccess: false,
  // 成功提醒 默认使用后端返回值
  successMsg: "",
  // 显示失败消息 默认显示
  showError: true,
  // 失败提醒 默认使用后端返回信息
  errorMsg: "",
  // 显示请求时loading模态框 默认显示
  showLoading: true,
  // loading提醒文字
  loadingMsg: "加载中",
  // 需要授权才能请求 默认放开
  auth: false,
  // 是否传递 token
  isToken: true
};
let LoadingInstance = {
  target: null,
  count: 0
};
function closeLoading() {
  if (LoadingInstance.count > 0)
    LoadingInstance.count--;
  if (LoadingInstance.count === 0)
    common_vendor.index.hideLoading();
}
const http = new common_vendor.Request({
  // 默认接口为mall服务
  baseURL: sheep_config_server.getMallBaseUrl() + sheep_config_index.apiPath,
  timeout: 8e3,
  method: "GET",
  header: {
    Accept: "text/json",
    "Content-Type": "application/json;charset=UTF-8",
    platform: sheep_platform_index._platform.name
  },
  custom: options
});
http.interceptors.request.use(
  (config) => {
    if (config.custom.auth && !sheep_store_index.$store("user").isLogin) {
      sheep_hooks_useModal.showAuthModal();
      return Promise.reject();
    }
    if (config.custom.showLoading) {
      LoadingInstance.count++;
      LoadingInstance.count === 1 && common_vendor.index.showLoading({
        title: config.custom.loadingMsg,
        mask: true,
        fail: () => {
          common_vendor.index.hideLoading();
        }
      });
    }
    const token = config.custom.isToken ? getAccessToken() : void 0;
    if (token) {
      config.header["Authorization"] = buildAuthorization(token);
    }
    config.header["terminal"] = sheep_helper_const.getTerminal();
    config.header["Accept"] = "*/*";
    config.header["tenant-id"] = getTenantId();
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);
http.interceptors.response.use(
  (response) => {
    var _a, _b, _c;
    if (isTokenResponse(response.config.url) && ((_b = (_a = response.data) == null ? void 0 : _a.data) == null ? void 0 : _b.accessToken)) {
      sheep_store_index.$store("user").setToken(response.data.data.accessToken, response.data.data.refreshToken);
    }
    response.config.custom.showLoading && closeLoading();
    if (response.data.code !== 0) {
      if (response.data.code === 401 && ((_c = response.config.custom) == null ? void 0 : _c.isToken) !== false) {
        return refreshToken(response.config);
      }
      if ((response.data.code + "").includes("1011007")) {
        common_vendor.index.__f__("error", "at sheep/request/index.js:134", `分销用户绑定失败，原因：${response.data.msg}`);
      } else if (response.config.custom.showError) {
        common_vendor.index.showToast({
          title: response.data.msg || "服务器开小差啦,请稍后再试~",
          icon: "none",
          mask: true
        });
      }
    }
    if (response.config.custom.showSuccess && response.config.custom.successMsg !== "" && response.data.code === 0) {
      common_vendor.index.showToast({
        title: response.config.custom.successMsg,
        icon: "none"
      });
    }
    return Promise.resolve(response.data);
  },
  (error) => {
    var _a, _b, _c;
    const userStore = sheep_store_index.$store("user");
    const isLogin = userStore.isLogin;
    let errorMessage = "网络请求出错";
    if ((error == null ? void 0 : error.statusCode) === 401) {
      const errorConfig = error.config;
      ((_a = errorConfig == null ? void 0 : errorConfig.custom) == null ? void 0 : _a.showLoading) && closeLoading();
      if (!errorConfig) {
        return handleAuthorized(isLogin);
      }
      if (isRefreshTokenRequest(errorConfig)) {
        return handleAuthorized(isLogin);
      }
      if (((_b = errorConfig.custom) == null ? void 0 : _b.isToken) !== false) {
        return refreshToken(errorConfig);
      }
      return handleAuthorized(isLogin);
    }
    if (error !== void 0) {
      switch (error.statusCode) {
        case 400:
          errorMessage = "请求错误";
          break;
        case 401:
          errorMessage = isLogin ? "您的登陆已过期" : "请先登录";
          break;
        case 403:
          errorMessage = "拒绝访问";
          break;
        case 404:
          errorMessage = "请求出错";
          break;
        case 408:
          errorMessage = "请求超时";
          break;
        case 429:
          errorMessage = "请求频繁, 请稍后再访问";
          break;
        case 500:
          errorMessage = "服务器开小差啦,请稍后再试~";
          break;
        case 501:
          errorMessage = "服务未实现";
          break;
        case 502:
          errorMessage = "网络错误";
          break;
        case 503:
          errorMessage = "服务不可用";
          break;
        case 504:
          errorMessage = "网络超时";
          break;
        case 505:
          errorMessage = "HTTP 版本不受支持";
          break;
      }
      if (error.errMsg.includes("timeout"))
        errorMessage = "请求超时";
    }
    if (error && error.config) {
      if (error.config.custom.showError) {
        common_vendor.index.showToast({
          title: ((_c = error.data) == null ? void 0 : _c.msg) || errorMessage,
          icon: "none",
          mask: true
        });
      }
      error.config.custom.showLoading && closeLoading();
    }
    return false;
  }
);
let requestList = [];
let isRefreshToken = false;
const refreshToken = async (config) => {
  if (isRefreshTokenRequest(config)) {
    return Promise.reject("error");
  }
  if (!isRefreshToken) {
    const refreshToken2 = getRefreshToken();
    if (!refreshToken2) {
      return handleAuthorized();
    }
    isRefreshToken = true;
    try {
      const refreshTokenResult = await sheep_api_member_auth.AuthUtil.refreshToken(refreshToken2);
      if (refreshTokenResult.code !== 0) {
        throw new Error("刷新令牌失败");
      }
      config.header.Authorization = buildAuthorization(getAccessToken());
      requestList.forEach((cb) => {
        cb();
      });
      requestList = [];
      return request(config);
    } catch (e) {
      requestList.forEach((cb) => {
        cb();
      });
      return handleAuthorized();
    } finally {
      requestList = [];
      isRefreshToken = false;
    }
  } else {
    return new Promise((resolve) => {
      requestList.push(() => {
        config.header.Authorization = buildAuthorization(getAccessToken());
        resolve(request(config));
      });
    });
  }
};
const handleAuthorized = (wasLogin) => {
  const userStore = sheep_store_index.$store("user");
  const loginStatus = typeof wasLogin === "boolean" ? wasLogin : userStore.isLogin;
  userStore.logout(true);
  return Promise.reject({
    code: 401,
    msg: loginStatus ? "您的登陆已过期" : "请先登录"
  });
};
const getAccessToken = () => {
  return common_vendor.index.getStorageSync("token");
};
const getRefreshToken = () => {
  return common_vendor.index.getStorageSync("refresh-token");
};
const buildAuthorization = (token) => {
  if (!token) {
    return "";
  }
  return token.startsWith("Bearer ") ? token : `Bearer ${token}`;
};
function isTokenResponse(url = "") {
  return url.indexOf("/app/auth/") >= 0 || url.indexOf("/admin/auth/token") >= 0;
}
function isRefreshTokenRequest(config) {
  var _a, _b, _c, _d;
  return ((_a = config.custom) == null ? void 0 : _a.isRefreshToken) || ((_b = config.url) == null ? void 0 : _b.indexOf("/app/auth/refresh-token")) >= 0 || ((_c = config.url) == null ? void 0 : _c.indexOf("/admin/auth/token")) >= 0 && ((_d = config.data) == null ? void 0 : _d.grant_type) === "refresh_token";
}
const getTenantId = () => {
  return common_vendor.index.getStorageSync("tenant-id") || sheep_config_index.tenantId;
};
const request = (config) => {
  return http.middleware(config);
};
const request$1 = request;
exports.buildAuthorization = buildAuthorization;
exports.getAccessToken = getAccessToken;
exports.getRefreshToken = getRefreshToken;
exports.request = request$1;
//# sourceMappingURL=../../../.sourcemap/mp-weixin/sheep/request/index.js.map
