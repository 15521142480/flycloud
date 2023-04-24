import {
  SET_SINGER,
  SET_DISC, SET_PLAYLIST,
  SET_FAVORITE_LIST,
  SET_TOPLIST,
  SET_FULLSCREEN,
  SET_PLAYING_STATE,
  SET_CURRENTINDEX,
  SET_SEQUENCELIST,
  SET_MODE,
  SET_SEARCH_HISTORY,
  SET_PLAY_HISTORY
} from './mutation-type'

export default {

  set_isFtpModel (state, isFtpModel) {
    state.isFtpModel = isFtpModel
  }

}
