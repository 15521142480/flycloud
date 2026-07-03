import $store from '@/sheep/store';
import { baseUrl, staticUrl } from '@/sheep/config';

const cdn = (url = '', cdnurl = '') => {
  if (!url) return '';
  if (isAbsoluteUrl(url)) {
    return url;
  }
  if (cdnurl === '') {
    cdnurl = $store('app').info.cdnurl;
  }
  if (!cdnurl) {
    cdnurl = String(url).indexOf('/static/') === 0 ? getGatewayBaseUrl() : getFileBaseUrl();
  }
  return joinUrl(cdnurl, url);
};
export default {
  // 添加cdn域名前缀
  cdn,
  // 对象存储自动剪裁缩略图
  thumb: (url = '', params) => {
    url = cdn(url);
    return append_thumbnail_params(url, params);
  },
  // 静态资源地址
  static: (url = '', staticurl = '') => {
    if (staticurl === '') {
      staticurl = staticUrl;
    }
    if (staticurl !== 'local') {
      url = cdn(url, staticurl);
    }
    return url;
  },
  // css背景图片地址
  css: (url = '', staticurl = '') => {
    if (staticurl === '') {
      staticurl = staticUrl;
    }
    if (staticurl !== 'local') {
      url = cdn(url, staticurl);
    }
    // #ifdef APP-PLUS
    if (staticurl === 'local') {
      url = plus.io.convertLocalFileSystemURL(url);
    }
    // #endif
    return `url(${url})`;
  },
};

/**
 * 判断地址是否已经是浏览器或小程序可直接访问的完整资源地址。
 */
function isAbsoluteUrl(url = '') {
  const lowerUrl = String(url).toLowerCase();
  return (
    lowerUrl.indexOf('http://') === 0 ||
    lowerUrl.indexOf('https://') === 0 ||
    lowerUrl.indexOf('//') === 0 ||
    lowerUrl.indexOf('data:') === 0 ||
    lowerUrl.indexOf('blob:') === 0
  );
}

/**
 * 拼接 URL 前缀和相对路径，统一处理首尾斜杠。
 */
function joinUrl(prefix = '', path = '') {
  return `${String(prefix).replace(/\/+$/, '')}/${String(path).replace(/^\/+/, '')}`;
}

/**
 * 转义正则表达式特殊字符，用于安全匹配网关服务前缀。
 */
function escapeRegExp(value = '') {
  return value.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
}

/**
 * 获取网关根地址。
 *
 * 移动端接口默认走商城服务前缀，例如 /flycloud-mall；
 * 静态文件访问是网关根路径下的 /static，所以需要去掉服务前缀。
 */
function getGatewayBaseUrl() {
  const normalizedBaseUrl = String(baseUrl || '').replace(/\/+$/, '');
  const mallPrefix = `/${String(import.meta.env.MALL_SERVER_PREFIX || '').replace(
    /^\/+|\/+$/g,
    '',
  )}`;
  if (!normalizedBaseUrl || mallPrefix === '/') {
    return normalizedBaseUrl;
  }
  return normalizedBaseUrl.replace(new RegExp(`${escapeRegExp(mallPrefix)}$`), '');
}

/**
 * 获取后端静态文件访问前缀。
 */
function getFileBaseUrl() {
  return joinUrl(getGatewayBaseUrl(), 'static');
}

/**
 * 追加对象存储自动裁剪/压缩参数
 *
 * @return string
 */
function append_thumbnail_params(url, params) {
  const filesystem = $store('app').info.filesystem;
  if (filesystem === 'public') {
    return url;
  }
  let width = params.width || '200'; // 宽度
  let height = params.height || '200'; // 高度
  let mode = params.mode || 'lfit'; // 缩放模式
  let quality = params.quality || 90; // 压缩质量
  let gravity = params.gravity || 'center'; // 剪裁质量
  let suffix = '';
  let crop_str = '';
  let quality_str = '';
  let size = width + 'x' + height;
  switch (filesystem) {
    case 'aliyun':
      // 裁剪
      if (!gravity && gravity != 'center') {
        // 指定了裁剪区域
        mode = 'mfit';
        crop_str =
          '/crop,g_' + gravityFormatter('aliyun', gravity) + ',w_' + width + ',h_' + height;
      }

      // 质量压缩
      if (quality > 0 && quality < 100) {
        quality_str = '/quality,q_' + quality;
      }

      // 缩放参数
      suffix = 'x-oss-process=image/resize,m_' + mode + ',w_' + width + ',h_' + height;

      // 拼接裁剪和质量压缩
      suffix += crop_str + quality_str;
      break;
    case 'qcloud':
      let mode_str = 'thumbnail';
      if (mode == 'fill' || (!gravity && gravity != 'center')) {
        // 指定了裁剪区域
        mode_str = 'crop';
        mode = 'fill';
        crop_str = '/gravity/' + gravityFormatter('qcloud', gravity);
      }

      // 质量压缩
      if (quality > 0 && quality < 100) {
        quality_str = '/rquality/' + quality;
      }

      switch (mode) {
        case 'lfit':
          size = '' + size + '>';
          break;
        case 'mfit':
          size = '!' + size + 'r';
        case 'fill':
          break;
        case 'pad':
          size = size + '/pad/1';
          break;
        case 'fixed':
          size = size + '!';
          break;
      }

      suffix = 'imageMogr2/' + mode_str + '/' + size + crop_str + quality_str;
      break;
    case 'qiniu':
      if (mode == 'fill' || (!gravity && gravity != 'center')) {
        // 指定了裁剪区域,全部转为 mfit
        mode = 'mfit';
        crop_str = '/gravity/' + gravityFormatter('qiniu', gravity) + '/crop/' + size;
      }
      // 质量压缩
      if (quality > 0 && quality < 100) {
        quality_str = '/quality/' + quality;
      }

      switch (mode) {
        case 'lfit':
        case 'pad': // 七牛不支持在缩放之后，尺寸不足时，填充背景色,所以这里和 lfit 模式一样
          size = size + '>';
          break;
        case 'mfit':
          size = '!' + size + 'r';
          break;
        case 'fill':
          // 会被转为 mfit
          break;
        case 'fixed':
          size = size + '!';
          break;
      }

      suffix = 'imageMogr2/thumbnail/' + size + crop_str + quality_str;
      break;
  }
  return url + '?' + suffix;
}

/**
 * 裁剪区域格式转换
 *
 * @param string $type aliyun|qcloud|qiniu
 * @param string $gravity 统一的裁剪区域字符
 *
 * @return string
 */
function gravityFormatter(type, gravity) {
  let gravityFormatMap = {
    aliyun: {
      north_west: 'nw', // 左上
      north: 'north', // 中上
      north_east: 'ne', // 右上
      west: 'west', // 左中
      center: 'center', // 中部
      east: 'east', // 右中
      south_west: 'sw', // 左下
      south: 'south', // 中下
      south_east: 'se', // 右下
    },
    qcloud: {
      northwest: 'nw', // 左上
      north: 'north', // 中上
      northeast: 'ne', // 右上
      west: 'west', // 左中
      center: 'center', // 中部
      east: 'east', // 右中
      southwest: 'sw', // 左下
      south: 'south', // 中下
      southeast: 'se', // 右下
    },
    qiniu: {
      NorthWest: 'nw', // 左上
      North: 'north', // 中上
      NorthEast: 'ne', // 右上
      West: 'west', // 左中
      Center: 'center', // 中部
      East: 'east', // 右中
      SouthWest: 'sw', // 左下
      South: 'south', // 中下
      SouthEast: 'se', // 右下
    },
  };

  return gravityFormatMap[type][gravity];
}
