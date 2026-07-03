import request from '@/sheep/request';
import { getAuthBaseUrl } from '@/sheep/config/server';
import { apiPath } from '@/sheep/config';

const CAPTCHA_IMAGE_WIDTH = Number(import.meta.env.CAPTCHA_IMAGE_WIDTH);
const CAPTCHA_IMAGE_HEIGHT = Number(import.meta.env.CAPTCHA_IMAGE_HEIGHT);

const CaptchaApi = {
  imageWidth: CAPTCHA_IMAGE_WIDTH,
  imageHeight: CAPTCHA_IMAGE_HEIGHT,

  // 获取图文点选验证码题目。
  getImageTextClickCaptcha: () => {
    return request({
      // baseURL: getAuthBaseUrl(),
      // url: '/auth/getImageTextClickCaptcha',
      url: getAuthBaseUrl() + '/auth/getImageTextClickCaptcha',
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
      url: getAuthBaseUrl() + '/auth/checkGetImageTextClickCaptcha',
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
