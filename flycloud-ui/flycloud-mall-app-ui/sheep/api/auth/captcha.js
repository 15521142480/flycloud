import request from '@/sheep/request';
import { baseUrl } from '@/sheep/config';

const AUTH_SERVER_PREFIX = import.meta.env.AUTH_SERVER_PREFIX;
const MALL_SERVER_PREFIX = import.meta.env.MALL_SERVER_PREFIX;
const CAPTCHA_IMAGE_WIDTH = Number(import.meta.env.CAPTCHA_IMAGE_WIDTH);
const CAPTCHA_IMAGE_HEIGHT = Number(import.meta.env.CAPTCHA_IMAGE_HEIGHT);

function normalizePrefix(prefix) {
  return `/${String(prefix || '').replace(/^\/+|\/+$/g, '')}`;
}

function escapeRegExp(value) {
  return value.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
}

// 获取认证服务网关地址，避免影响商城接口的基础路径。
function getAuthBaseUrl() {
  if (!baseUrl) {
    return '';
  }
  const mallPrefix = normalizePrefix(MALL_SERVER_PREFIX);
  const authPrefix = normalizePrefix(AUTH_SERVER_PREFIX);
  return baseUrl
    .replace(new RegExp(`${escapeRegExp(mallPrefix)}/?$`), authPrefix)
    .replace(/\/$/, '');
}

const CaptchaApi = {
  imageWidth: CAPTCHA_IMAGE_WIDTH,
  imageHeight: CAPTCHA_IMAGE_HEIGHT,

  // 获取图文点选验证码题目。
  getImageTextClickCaptcha: () => {
    return request({
      baseURL: getAuthBaseUrl(),
      url: '/auth/getImageTextClickCaptcha',
      method: 'POST',
      custom: {
        isToken: false,
        showLoading: false,
      },
    });
  },

  // 校验图文点选验证码坐标。
  checkImageTextClickCaptcha: (data) => {
    return request({
      baseURL: getAuthBaseUrl(),
      url: '/auth/checkGetImageTextClickCaptcha',
      method: 'POST',
      data,
      custom: {
        isToken: false,
        showLoading: false,
      },
    });
  },
};

export default CaptchaApi;
