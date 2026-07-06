"use strict";
const sheep_store_index = require("../store/index.js");
const sheep_config_index = require("../config/index.js");
const sheep_helper_fileConfig = require("../helper/fileConfig.js");
const sheep_config_server = require("../config/server.js");
const cdn = (url = "", cdnurl = "") => {
  if (!url)
    return "";
  if (isAbsoluteUrl(url)) {
    return url;
  }
  if (cdnurl === "") {
    cdnurl = sheep_store_index.$store("app").info.cdnurl;
  }
  if (!cdnurl) {
    cdnurl = String(url).indexOf("/static/") === 0 ? getGatewayBaseUrl() : getFileResourceBaseUrl();
  }
  return joinUrl(cdnurl, url);
};
const $url = {
  // 添加cdn域名前缀
  cdn,
  // 对象存储自动剪裁缩略图
  thumb: (url = "", params) => {
    url = cdn(url);
    return append_thumbnail_params(url, params);
  },
  // 静态资源地址
  static: (url = "", staticurl = "") => {
    if (staticurl === "") {
      staticurl = sheep_config_index.staticUrl;
    }
    if (staticurl !== "local") {
      url = cdn(url, staticurl);
    }
    return url;
  },
  // css背景图片地址
  css: (url = "", staticurl = "") => {
    if (staticurl === "") {
      staticurl = sheep_config_index.staticUrl;
    }
    if (staticurl !== "local") {
      url = cdn(url, staticurl);
    }
    return `url(${url})`;
  }
};
function isAbsoluteUrl(url = "") {
  const lowerUrl = String(url).toLowerCase();
  return lowerUrl.indexOf("http://") === 0 || lowerUrl.indexOf("https://") === 0 || lowerUrl.indexOf("//") === 0 || lowerUrl.indexOf("data:") === 0 || lowerUrl.indexOf("blob:") === 0;
}
function joinUrl(prefix = "", path = "") {
  return `${String(prefix).replace(/\/+$/, "")}/${String(path).replace(/^\/+/, "")}`;
}
function getGatewayBaseUrl() {
  return sheep_config_server.getMallBaseUrl().replace(/\/flycloud-mall\/?$/, "");
}
function getFileResourceBaseUrl() {
  const cachedFileBaseUrl = sheep_helper_fileConfig.getFileBaseUrl();
  if (cachedFileBaseUrl) {
    return cachedFileBaseUrl;
  }
  return joinUrl(getGatewayBaseUrl(), "static");
}
function append_thumbnail_params(url, params) {
  const filesystem = sheep_store_index.$store("app").info.filesystem;
  if (filesystem === "public") {
    return url;
  }
  let width = params.width || "200";
  let height = params.height || "200";
  let mode = params.mode || "lfit";
  let quality = params.quality || 90;
  let gravity = params.gravity || "center";
  let suffix = "";
  let crop_str = "";
  let quality_str = "";
  let size = width + "x" + height;
  switch (filesystem) {
    case "aliyun":
      if (!gravity && gravity != "center") {
        mode = "mfit";
        crop_str = "/crop,g_" + gravityFormatter("aliyun", gravity) + ",w_" + width + ",h_" + height;
      }
      if (quality > 0 && quality < 100) {
        quality_str = "/quality,q_" + quality;
      }
      suffix = "x-oss-process=image/resize,m_" + mode + ",w_" + width + ",h_" + height;
      suffix += crop_str + quality_str;
      break;
    case "qcloud":
      let mode_str = "thumbnail";
      if (mode == "fill" || !gravity && gravity != "center") {
        mode_str = "crop";
        mode = "fill";
        crop_str = "/gravity/" + gravityFormatter("qcloud", gravity);
      }
      if (quality > 0 && quality < 100) {
        quality_str = "/rquality/" + quality;
      }
      switch (mode) {
        case "lfit":
          size = "" + size + ">";
          break;
        case "mfit":
          size = "!" + size + "r";
        case "fill":
          break;
        case "pad":
          size = size + "/pad/1";
          break;
        case "fixed":
          size = size + "!";
          break;
      }
      suffix = "imageMogr2/" + mode_str + "/" + size + crop_str + quality_str;
      break;
    case "qiniu":
      if (mode == "fill" || !gravity && gravity != "center") {
        mode = "mfit";
        crop_str = "/gravity/" + gravityFormatter("qiniu", gravity) + "/crop/" + size;
      }
      if (quality > 0 && quality < 100) {
        quality_str = "/quality/" + quality;
      }
      switch (mode) {
        case "lfit":
        case "pad":
          size = size + ">";
          break;
        case "mfit":
          size = "!" + size + "r";
          break;
        case "fill":
          break;
        case "fixed":
          size = size + "!";
          break;
      }
      suffix = "imageMogr2/thumbnail/" + size + crop_str + quality_str;
      break;
  }
  return url + "?" + suffix;
}
function gravityFormatter(type, gravity) {
  let gravityFormatMap = {
    aliyun: {
      north_west: "nw",
      // 左上
      north: "north",
      // 中上
      north_east: "ne",
      // 右上
      west: "west",
      // 左中
      center: "center",
      // 中部
      east: "east",
      // 右中
      south_west: "sw",
      // 左下
      south: "south",
      // 中下
      south_east: "se"
      // 右下
    },
    qcloud: {
      northwest: "nw",
      // 左上
      north: "north",
      // 中上
      northeast: "ne",
      // 右上
      west: "west",
      // 左中
      center: "center",
      // 中部
      east: "east",
      // 右中
      southwest: "sw",
      // 左下
      south: "south",
      // 中下
      southeast: "se"
      // 右下
    },
    qiniu: {
      NorthWest: "nw",
      // 左上
      North: "north",
      // 中上
      NorthEast: "ne",
      // 右上
      West: "west",
      // 左中
      Center: "center",
      // 中部
      East: "east",
      // 右中
      SouthWest: "sw",
      // 左下
      South: "south",
      // 中下
      SouthEast: "se"
      // 右下
    }
  };
  return gravityFormatMap[type][gravity];
}
exports.$url = $url;
//# sourceMappingURL=../../../.sourcemap/mp-weixin/sheep/url/index.js.map
