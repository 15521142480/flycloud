/**
 * Shopro-request
 * @description api模块管理，loading配置，请求拦截，错误处理
 */

import Request from 'luch-request';
import { apiPath, baseUrl, tenantId } from '@/sheep/config';
import $store from '@/sheep/store';
import $platform from '@/sheep/platform';
import { showAuthModal } from '@/sheep/hooks/useModal';
import AuthUtil from '@/sheep/api/member/auth';
import { getTerminal } from '@/sheep/helper/const';

const options = {
  // 显示操作成功消息 默认不显示
  showSuccess: false,
  // 成功提醒 默认使用后端返回值
  successMsg: '',
  // 显示失败消息 默认显示
  showError: true,
  // 失败提醒 默认使用后端返回信息
  errorMsg: '',
  // 显示请求时loading模态框 默认显示
  showLoading: true,
  // loading提醒文字
  loadingMsg: '加载中',
  // 需要授权才能请求 默认放开
  auth: false,
  // 是否传递 token
  isToken: true,
};

// Loading全局实例
let LoadingInstance = {
  target: null,
  count: 0,
};

/**
 * 关闭loading
 */
function closeLoading() {
  if (LoadingInstance.count > 0) LoadingInstance.count--;
  if (LoadingInstance.count === 0) uni.hideLoading();
}

/**
 * @description 请求基础配置 可直接使用访问自定义请求
 */
const http = new Request({
  baseURL: baseUrl + apiPath,
  timeout: 8000,
  method: 'GET',
  header: {
    Accept: 'text/json',
    'Content-Type': 'application/json;charset=UTF-8',
    platform: $platform.name,
  },
  // #ifdef APP-PLUS
  sslVerify: false,
  // #endif
  // #ifdef H5
  // 跨域请求时是否携带凭证（cookies）仅H5支持（HBuilderX 2.6.15+）
  withCredentials: false,
  // #endif
  custom: options,
});

/**
 * @description 请求拦截器
 */
http.interceptors.request.use(
  (config) => {
    // 自定义处理【auth 授权】：必须登录的接口，则跳出 AuthModal 登录弹窗
    if (config.custom.auth && !$store('user').isLogin) {
      showAuthModal();
      return Promise.reject();
    }

    // 自定义处理【loading 加载中】：如果需要显示 loading，则显示 loading
    if (config.custom.showLoading) {
      LoadingInstance.count++;
      LoadingInstance.count === 1 &&
        uni.showLoading({
          title: config.custom.loadingMsg,
          mask: true,
          fail: () => {
            uni.hideLoading();
          },
        });
    }

    // 增加 token 令牌、terminal 终端、tenant 租户的请求头
    const token = config.custom.isToken ? getAccessToken() : undefined;
    if (token) {
      config.header['Authorization'] = buildAuthorization(token);
    }
    config.header['terminal'] = getTerminal();

    config.header['Accept'] = '*/*';
    config.header['tenant-id'] = getTenantId();
    return config;
  },
  (error) => {
    return Promise.reject(error);
  },
);

/**
 * @description 响应拦截器
 */
http.interceptors.response.use(
  (response) => {

    // 约定：登录或刷新令牌接口返回 accessToken 时，自动保存令牌。
    if (isTokenResponse(response.config.url) && response.data?.data?.accessToken) {
      $store('user').setToken(response.data.data.accessToken, response.data.data.refreshToken);
    }

    // 自定处理【loading 加载中】：如果需要显示 loading，则关闭 loading
    response.config.custom.showLoading && closeLoading();

    // 自定义处理【error 错误提示】：如果需要显示错误提示，则显示错误提示
    if (response.data.code !== 0) {
      // 特殊：如果 401 错误码，则跳转到登录页 or 刷新令牌
      if (response.data.code === 401 && response.config.custom?.isToken !== false) {
        return refreshToken(response.config);
      }
      // 特殊：处理分销用户绑定失败的提示
      if ((response.data.code + '').includes('1011007')) {
        console.error(`分销用户绑定失败，原因：${response.data.msg}`);
      } else if (response.config.custom.showError) {
        // 错误提示
        uni.showToast({
          title: response.data.msg || '服务器开小差啦,请稍后再试~',
          icon: 'none',
          mask: true,
        });
      }
    }

    // 自定义处理【showSuccess 成功提示】：如果需要显示成功提示，则显示成功提示
    if (
      response.config.custom.showSuccess &&
      response.config.custom.successMsg !== '' &&
      response.data.code === 0
    ) {
      uni.showToast({
        title: response.config.custom.successMsg,
        icon: 'none',
      });
    }

    // 返回结果：包括 code + data + msg
    return Promise.resolve(response.data);
  },
  (error) => {
    const userStore = $store('user');
    const isLogin = userStore.isLogin;
    let errorMessage = '网络请求出错';

    // 后端直接返回 HTTP 401 时，同样走刷新令牌或重新登录流程。
    if (error?.statusCode === 401) {
      const errorConfig = error.config;
      errorConfig?.custom?.showLoading && closeLoading();
      if (!errorConfig) {
        return handleAuthorized(isLogin);
      }
      if (isRefreshTokenRequest(errorConfig)) {
        return handleAuthorized(isLogin);
      }
      if (errorConfig.custom?.isToken !== false) {
        return refreshToken(errorConfig);
      }
      return handleAuthorized(isLogin);
    }

    if (error !== undefined) {
      switch (error.statusCode) {
        case 400:
          errorMessage = '请求错误';
          break;
        case 401:
          errorMessage = isLogin ? '您的登陆已过期' : '请先登录';
          // 正常情况下，后端不会返回 401 错误，所以这里不处理 handleAuthorized
          break;
        case 403:
          errorMessage = '拒绝访问';
          break;
        case 404:
          errorMessage = '请求出错';
          break;
        case 408:
          errorMessage = '请求超时';
          break;
        case 429:
          errorMessage = '请求频繁, 请稍后再访问';
          break;
        case 500:
          errorMessage = '服务器开小差啦,请稍后再试~';
          break;
        case 501:
          errorMessage = '服务未实现';
          break;
        case 502:
          errorMessage = '网络错误';
          break;
        case 503:
          errorMessage = '服务不可用';
          break;
        case 504:
          errorMessage = '网络超时';
          break;
        case 505:
          errorMessage = 'HTTP 版本不受支持';
          break;
      }
      if (error.errMsg.includes('timeout')) errorMessage = '请求超时';
      // #ifdef H5
      if (error.errMsg.includes('Network'))
        errorMessage = window.navigator.onLine ? '服务器异常' : '请检查您的网络连接';
      // #endif
    }

    if (error && error.config) {
      if (error.config.custom.showError) {
        uni.showToast({
          title: error.data?.msg || errorMessage,
          icon: 'none',
          mask: true,
        });
      }
      error.config.custom.showLoading && closeLoading();
    }

    return false;
  },
);

// Axios 无感知刷新令牌，参考 https://www.dashingdog.cn/article/11 与 https://segmentfault.com/a/1190000020210980 实现
let requestList = []; // 请求队列
let isRefreshToken = false; // 是否正在刷新中
const refreshToken = async (config) => {
  // 如果当前已经是刷新令牌请求，并且还是 401 错误，说明刷新令牌失败了，直接返回 Promise.reject(error)
  if (isRefreshTokenRequest(config)) {
    return Promise.reject('error');
  }

  // 如果未认证，并且未进行刷新令牌，说明可能是访问令牌过期了
  if (!isRefreshToken) {
    // 1. 如果获取不到刷新令牌，则只能执行登出操作
    const refreshToken = getRefreshToken();
    if (!refreshToken) {
      return handleAuthorized();
    }
    // 只有真正发起刷新时才标记刷新中，避免无刷新令牌时状态一直卡住，后续 401 不再弹登录框
    // https://github.com/yudaocode/yudao-mall-uniapp/issues/38
    isRefreshToken = true;
    // 2. 进行刷新访问令牌
    try {
      const refreshTokenResult = await AuthUtil.refreshToken(refreshToken);
      if (refreshTokenResult.code !== 0) {
        // 如果刷新不成功，直接抛出 e 触发 2.2 的逻辑
        // noinspection ExceptionCaughtLocallyJS
        throw new Error('刷新令牌失败');
      }
      // 2.1 刷新成功，则回放队列的请求 + 当前请求
      config.header.Authorization = buildAuthorization(getAccessToken());
      requestList.forEach((cb) => {
        cb();
      });
      requestList = [];
      return request(config);
    } catch (e) {
      // 为什么需要 catch 异常呢？刷新失败时，请求因为 Promise.reject 触发异常。
      // 2.2 刷新失败，只回放队列的请求
      requestList.forEach((cb) => {
        cb();
      });
      // 提示是否要登出。即不回放当前请求！不然会形成递归
      return handleAuthorized();
    } finally {
      requestList = [];
      isRefreshToken = false;
    }
  } else {
    // 添加到队列，等待刷新获取到新的令牌
    return new Promise((resolve) => {
      requestList.push(() => {
        config.header.Authorization = buildAuthorization(getAccessToken()); // 让每个请求携带自定义token 请根据实际情况自行修改
        resolve(request(config));
      });
    });
  }
};

/**
 * 处理 401 未登录的错误
 */
const handleAuthorized = (wasLogin) => {
  const userStore = $store('user');
  const loginStatus = typeof wasLogin === 'boolean' ? wasLogin : userStore.isLogin;
  userStore.logout(true);
  // 登录超时
  return Promise.reject({
    code: 401,
    msg: loginStatus ? '您的登陆已过期' : '请先登录',
  });
};

/** 获得访问令牌 */
export const getAccessToken = () => {
  return uni.getStorageSync('token');
};

/** 获得刷新令牌 */
export const getRefreshToken = () => {
  return uni.getStorageSync('refresh-token');
};

/** 构建授权请求头 */
export const buildAuthorization = (token) => {
  if (!token) {
    return '';
  }
  return token.startsWith('Bearer ') ? token : `Bearer ${token}`;
};

function isTokenResponse(url = '') {
  return url.indexOf('/member/auth/') >= 0 || url.indexOf('/oauth/token') >= 0;
}

function isRefreshTokenRequest(config) {
  return (
    config.custom?.isRefreshToken ||
    config.url?.indexOf('/member/auth/refresh-token') >= 0 ||
    (config.url?.indexOf('/oauth/token') >= 0 && config.data?.grant_type === 'refresh_token')
  );
}

/** 获得租户编号 */
export const getTenantId = () => {
  return uni.getStorageSync('tenant-id') || tenantId;
};

const request = (config) => {
  return http.middleware(config);
};

export default request;
