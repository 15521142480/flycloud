"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_api_promotion_diy = require("../api/promotion/diy.js");
const sheep_api_infra_file = require("../api/infra/file.js");
require("../request/index.js");
const sheep_platform_index = require("../platform/index.js");
const sheep_router_index = require("../router/index.js");
const sheep_store_user = require("./user.js");
const sheep_store_sys = require("./sys.js");
const sheep_config_index = require("../config/index.js");
const sheep_helper_fileConfig = require("../helper/fileConfig.js");
const app = common_vendor.defineStore("app", {
  state: () => ({
    paramsForTabbar: {},
    // 为全局tabbar跳转传参用。原因是 tabbar 无法传参，只能通过全局状态传递
    info: {
      // 应用信息
      name: "",
      // 商城名称
      logo: "",
      // logo
      version: "",
      // 版本号
      copyright: "",
      // 版权信息 I
      copytime: "",
      // 版权信息 II
      cdnurl: "",
      // 云存储域名
      filesystem: ""
      // 云存储平台
    },
    platform: {
      share: {
        methods: [],
        // 支持的分享方式
        forwardInfo: {},
        // 默认转发信息
        posterInfo: {},
        // 海报信息
        linkAddress: ""
        // 复制链接地址
      },
      bind_mobile: 0
      // 登陆后绑定手机号提醒 (弱提醒，可手动关闭)
    },
    template: {
      // 店铺装修模板
      basic: {},
      // 基本信息
      home: {
        // 首页模板
        style: {},
        data: []
      },
      user: {
        // 个人中心模板
        style: {},
        data: []
      }
    },
    shareInfo: {},
    // 全局分享信息
    has_wechat_trade_managed: 0
    // 小程序发货信息管理  0 没有 || 1 有
  }),
  actions: {
    // 获取Shopro应用配置和模板
    async init(templateId = null) {
      const networkStatus = await sheep_platform_index._platform.checkNetwork();
      if (!networkStatus) {
        sheep_router_index.$router.error("NetworkError");
      }
      if (typeof sheep_config_index.baseUrl === "undefined") {
        sheep_router_index.$router.error("EnvError");
      }
      await adaptFileConfig();
      await adaptTemplate(this.template, templateId);
      {
        this.info = {
          name: "飞翔云商城",
          logo: "https://static.iocoder.cn/ruoyi-vue-pro-logo.png",
          version: "2026.04",
          copyright: "全部开源，个人与企业可 100% 免费使用",
          copytime: "Copyright© 2018-2025",
          cdnurl: "",
          // 云存储域名，空值时使用后端 /static 文件访问前缀
          filesystem: "public"
          // 云存储平台
        };
        this.platform = {
          share: {
            methods: ["forward", "poster", "link"],
            linkAddress: sheep_config_index.h5Url,
            posterInfo: {
              user_bg: "/static/img/shop/config/user-poster-bg.png",
              goods_bg: "/static/img/shop/config/goods-poster-bg.png",
              groupon_bg: "/static/img/shop/config/groupon-poster-bg.png"
            },
            forwardInfo: {
              title: "",
              image: "",
              desc: ""
            }
          },
          bind_mobile: 0
        };
        this.has_wechat_trade_managed = 0;
        const sysStore = sheep_store_sys.sys();
        sysStore.setTheme();
        const userStore = sheep_store_user.user();
        if (userStore.isLogin) {
          userStore.loginAfter();
        }
        return Promise.resolve(true);
      }
    },
    // 设置 paramsForTabbar
    setParamsForTabbar(params = {}) {
      this.paramsForTabbar = params;
    },
    clearParamsForTabbar() {
      this.paramsForTabbar = {};
    }
  },
  persist: {
    enabled: true,
    strategies: [
      {
        key: "app-store"
      }
    ]
  }
});
const adaptFileConfig = async () => {
  try {
    const { data } = await sheep_api_infra_file.FileApi.getFileConfig();
    sheep_helper_fileConfig.setFileConfig(data);
  } catch (error) {
    common_vendor.index.__f__("error", "at sheep/store/app.js:145", "adaptFileConfig 执行失败:", error);
  }
};
const adaptTemplate = async (appTemplate, templateId) => {
  var _a;
  const { data: diyTemplate } = templateId ? (
    // 查询指定模板，一般是预览时使用
    await sheep_api_promotion_diy.DiyApi.getDiyTemplate(templateId)
  ) : await sheep_api_promotion_diy.DiyApi.getUsedDiyTemplate();
  if (!diyTemplate) {
    sheep_router_index.$router.error("TemplateError");
    return;
  }
  const tabBar = (_a = diyTemplate == null ? void 0 : diyTemplate.property) == null ? void 0 : _a.tabBar;
  if (tabBar) {
    appTemplate.basic.tabbar = tabBar;
    if (tabBar == null ? void 0 : tabBar.theme) {
      appTemplate.basic.theme = tabBar == null ? void 0 : tabBar.theme;
    }
  }
  appTemplate.home = diyTemplate == null ? void 0 : diyTemplate.home;
  appTemplate.user = diyTemplate == null ? void 0 : diyTemplate.user;
};
const __vite_glob_0_0 = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: app
}, Symbol.toStringTag, { value: "Module" }));
exports.__vite_glob_0_0 = __vite_glob_0_0;
exports.app = app;
//# sourceMappingURL=../../../.sourcemap/mp-weixin/sheep/store/app.js.map
