// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import Iview from 'iview'
import 'iview/dist/styles/iview.css'

import cookies from 'vue-cookies'

// 引入全局vuex状态管理
import store from './store'

// 导入api接口
import api from '../src/api'

Vue.use(Iview)

Vue.config.productionTip = false

Vue.prototype.$api = api
Vue.prototype.$cookies = cookies

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: {App},
  template: '<App/>'
})
