export default {

  /**
   * 平台服务的请求基础路径
   */
  baseUrl: {
    dev: '/flycloud-system',
    pro: '/flycloud-system'
  },

  /**
   * 统一授权的请求基础路径
   */
  authBaseUrl: {
    dev: '/flycloud-auth',
    pro: '/flycloud-auth'
  },

  /**
   * 文件服务的请求基础路径
   */
  fileBaseUrl: {
    dev: '/flycloud-file-admin',
    pro: '/flycloud-file-admin'
  },

  // token在Cookie中存储的天数，默认1天
  cookieExpires: 1,

  // 是否使用国际化，默认为false
  //              如果不使用，则需要在路由中给需要在菜单中展示的路由设置meta: {title: 'xxx'}
  //              用来在菜单中显示文字
  useI18n: false,

  // 默认打开的首页的路由name值，默认为home
  // homeName: 'home',

  // 需要加载的插件
  plugin: {
  }
}
