
import Vue from 'vue'
import Vuex from 'vuex'


// player
import mutations from './mutations'
import state from './state'
import getters from './getters'
import * as actions from './actions'
import createLogger from 'vuex/dist/logger'

Vue.use(Vuex)

/*
  全部vuex配置
  mutation: 必须是同步的
  actions-可异步(api-module)
*/
const debug = process.env.NODE_ENV !== 'production'
export default new Vuex.Store({
  state: state,
  getters: getters,
  mutations: mutations,
  actions: actions,
  modules: {

  },
  strict: debug,
  plugins: debug ? [createLogger()] : []
})
