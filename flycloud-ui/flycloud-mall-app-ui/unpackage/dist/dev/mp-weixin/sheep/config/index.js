"use strict";
const common_vendor = require("../../common/vendor.js");
const id = "shopro";
const name = "shopro";
const displayName = "飞翔云商城";
const version$1 = "2026.04";
const description = "飞翔云商城，一套代码，同时发行到iOS、Android、H5、微信小程序多个平台，请使用手机扫码快速体验强大功能";
const scripts = {
  prettier: 'prettier --write  "{pages,sheep}/**/*.{js,json,tsx,css,less,scss,vue,html,md}"'
};
const repository = "https://github.com/sheepjs/shop.git";
const keywords = [
  "商城",
  "B2C",
  "商城模板"
];
const author = "";
const license = "MIT";
const bugs = {
  url: "https://github.com/sheepjs/shop/issues"
};
const homepage = "https://github.com/dcloudio/hello-uniapp#readme";
const dcloudext = {
  category: [
    "前端页面模板",
    "uni-app前端项目模板"
  ],
  sale: {
    regular: {
      price: "0.00"
    },
    sourcecode: {
      price: "0.00"
    }
  },
  contact: {
    qq: ""
  },
  declaration: {
    ads: "无",
    data: "无",
    permissions: "无"
  },
  npmurl: ""
};
const uni_modules = {
  dependencies: [],
  encrypt: [],
  platforms: {
    cloud: {
      tcb: "u",
      aliyun: "u"
    },
    client: {
      App: {
        "app-vue": "y",
        "app-nvue": "u"
      },
      "H5-mobile": {
        Safari: "y",
        "Android Browser": "y",
        "微信浏览器(Android)": "y",
        "QQ浏览器(Android)": "y"
      },
      "H5-pc": {
        Chrome: "y",
        IE: "y",
        Edge: "y",
        Firefox: "y",
        Safari: "y"
      },
      "小程序": {
        "微信": "y",
        "阿里": "u",
        "百度": "u",
        "字节跳动": "u",
        QQ: "u",
        "京东": "u"
      },
      "快应用": {
        "华为": "u",
        "联盟": "u"
      },
      Vue: {
        vue2: "u",
        vue3: "y"
      }
    }
  }
};
const dependencies = {
  dayjs: "^1.11.7",
  lodash: "^4.17.21",
  "lodash-es": "^4.17.21",
  "luch-request": "^3.0.8",
  pinia: "^2.0.33",
  "pinia-plugin-persist-uni": "^1.2.0",
  vue: "^3.5.11",
  "weixin-js-sdk": "^1.6.0"
};
const devDependencies = {
  prettier: "^2.8.7",
  vconsole: "^3.15.0"
};
const packageInfo = {
  id,
  name,
  displayName,
  version: version$1,
  description,
  scripts,
  repository,
  keywords,
  author,
  license,
  bugs,
  homepage,
  dcloudext,
  uni_modules,
  dependencies,
  devDependencies
};
const { version } = packageInfo;
exports.baseUrl = void 0;
{
  exports.baseUrl = "https://www.laixueshi.cn/app";
}
if (typeof exports.baseUrl === "undefined") {
  common_vendor.index.__f__("error", "at sheep/config/index.js:28", "请检查.env配置文件是否存在");
} else {
  common_vendor.index.__f__("log", "at sheep/config/index.js:30", `[飞翔云商城 ${version}]  https://doc.iocoder.cn`);
}
const apiPath = "/app";
const staticUrl = "https://www.laixueshi.cn/static";
const tenantId = "1";
const websocketPath = "/infra/ws";
const h5Url = "http://127.0.0.1:3000";
exports.apiPath = apiPath;
exports.h5Url = h5Url;
exports.staticUrl = staticUrl;
exports.tenantId = tenantId;
exports.websocketPath = websocketPath;
//# sourceMappingURL=../../../.sourcemap/mp-weixin/sheep/config/index.js.map
