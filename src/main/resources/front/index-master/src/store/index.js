import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

const actions = {
  login(context,newUser){
    if(!state.loginState){
      context.commit('LOGIN',newUser)
    }
  },
  logout(context){
    if(state.loginState){
      context.commit('LOGOUT')
    }
  },
  tryRetrieveUserInfo(context){
    if(!state.loginState){
      context.commit('TRY_RETRIEVE_USER_INFO')
    }
  },

  cacheAvatar(context,src) {
    if(state.loginState){
      context.commit('CACHE_AVATAR',src)
    }
  }
}

const mutations = {
  LOGIN(state,newUser){
    state.loginState = true

    state.currentUser.userId = newUser.userId
    state.currentUser.userName = newUser.userName
    state.currentUser.userEmail = newUser.userEmail
    state.currentUser.userHasActivated = newUser.userHasActivated
    state.currentUser.userState = newUser.userState
    state.currentUser.userIntroduction = newUser.userIntroduction
    state.currentUser.userExp = newUser.userExp
    state.currentUser.userPoint = newUser.userPoint
    state.currentUser.userQuestionOpen = newUser.userQuestionOpen
    state.currentUser.userQuestionPrice = newUser.userQuestionPrice
    state.currentUser.lastLogin = new Date(newUser.lastLogin)
    state.currentUser.lastLoginString = newUser.lastLoginString
    state.userArticleCount = newUser.userArticleCount
    state.userFollowerCount = newUser.userFollowerCount
    state.userFollowingCount = newUser.userFollowingCount

    sessionStorage.setItem('loginState',true.toString())
    for(let key in state.currentUser){
      sessionStorage.setItem(key,state.currentUser[key].toString())
    }
  },

  LOGOUT(state) {
    state.loginState = false

    state.currentUser = {
      userId: 0,
      userName: "",
      userEmail: "",
      userHasActivated: false,
      userState: -1,
      userIntroduction: "",
      userArticleCount: 0,
      userFollowerCount: 0,
      userFollowingCount: 0,
      userExp: 0,
      userPoint: "0.00",
      userQuestionOpen: false,
      userQuestionPrice: "0.00",
      lastLogin: new Date(),
      lastLoginString: "",
    }

    sessionStorage.setItem('loginState',false.toString())
    for(let key in state.currentUser){
      sessionStorage.setItem(key,state.currentUser[key].toString())
    }
  },
  TRY_RETRIEVE_USER_INFO(state){
    if(sessionStorage.getItem('loginState') === "true"){
      state.loginState = true

      state.currentUser = {
        userId: Number(sessionStorage.getItem('userId')),
        userName: sessionStorage.getItem('userName'),
        userEmail: sessionStorage.getItem('userEmail'),
        userHasActivated: sessionStorage.getItem('userHasActivated') === "true",
        userState: Number(sessionStorage.getItem('userState')),
        userIntroduction: sessionStorage.getItem('userIntroduction'),
        userFollowerCount: Number(sessionStorage.getItem('userFollowerCount')),
        userFollowingCount: Number(sessionStorage.getItem('userFollowingCount')),
        userArticleCount: Number(sessionStorage.getItem('userArticleCount')),
        userExp: Number(sessionStorage.getItem('userExp')),
        userPoint: Number(sessionStorage.getItem('userPoint')),
        lastLogin: new Date(sessionStorage.getItem('lastLogin')),
        lastLoginString: sessionStorage.getItem('lastLoginString'),
        userQuestionPrice: sessionStorage.getItem('userQuestionPrice'),
        userQuestionOpen: Boolean(sessionStorage.getItem('userQuestionOpen')),
      }
    }
  },

  CACHE_AVATAR(state,src) {
    state.avatarCached = true
    state.avatarSrc = src
  }
}

const state = {
    loginState: false,
    avatarCached: false,
    avatarSrc: '',
    defaultAvatar: require('../assets/img/icon-me-n@2x.png'),
    currentUser: {
      userId: 0,
      userName: "",
      userEmail: "",
      userHasActivated: false,
      userState: -1,
      userIntroduction: "",
      userArticleCount: 0,
      userFollowerCount: 0,
      userFollowingCount: 0,
      userExp: 0,
      userPoint: "0.00",
      userQuestionOpen: false,
      userQuestionPrice: "0.00",
      lastLogin: new Date(),
      lastLoginString: "",
    }
}

const getters = {
  loginState(state){
    return state.loginState
  },
  user(state){
    return state.currentUser
  },
  avatarHasCached(state) {
    return state.avatarCached
  },
  avatar(state) {
    if(state.avatarCached) {
      return state.avatarSrc.length === 0 ? state.defaultAvatar : state.avatarSrc
    } else {
      return state.defaultAvatar
    }
  }
}

export default new Vuex.Store({
  actions,
  mutations,
  state,
  getters
});
