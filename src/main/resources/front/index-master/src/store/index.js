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

  changeUserName(context, newUserName) {
    if(state.loginState) {
      context.commit("CHANGE_USERNAME",newUserName)
    }
  },
  changeUserEmail(context, newUserEmail) {
    if(state.loginState) {
      context.commit("CHANGE_USER_EMAIL",newUserEmail)
    }
  },
  changeUserIntroduction(context, newUserIntroduction) {
    if(state.loginState) {
      context.commit("CHANGE_USER_INTRODUCTION",newUserIntroduction)
    }
  },
  updateUserActivationState(context) {
    if(state.loginState) {
      context.commit("UPDATE_USER_ACTIVATION_STATE")
    }
  },
  setNewPoint(context, newPoint) {
    if(state.loginState) {
      context.commit('ADD_VALUE_TO_POINT',newPoint)
    }
  },

  cacheAvatar(context,src) {
    if(state.loginState){
      context.commit('CACHE_AVATAR',src)
    }
  },

  intercept(context, target){
    context.commit('INTERCEPT',target)
  },
  resetIntercept(context){
    context.commit('RESET_INTERCEPT')
  },
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

    state.avatarCached = false
    state.avatarSrc = ''

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
        userState: sessionStorage.getItem('userState'),
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

  CHANGE_USERNAME(state, newUserName) {
    state.currentUser.userName = newUserName
    sessionStorage.setItem('userName',newUserName)
  },
  CHANGE_USER_EMAIL(state, newUserEmail) {
    state.currentUser.userEmail = newUserEmail
    sessionStorage.setItem('userEmail',newUserEmail)
  },
  CHANGE_USER_INTRODUCTION(state, newUserIntroduction) {
    state.currentUser.userIntroduction = newUserIntroduction
    sessionStorage.setItem('userIntroduction',newUserIntroduction)
  },
  UPDATE_USER_ACTIVATION_STATE(state) {
    state.currentUser.userHasActivated = true
    sessionStorage.setItem('userHasActivated',true.toString())

    if(state.currentUser.userState === "RESTRICTED") {
      state.currentUser.userState = "NORMAL"
      sessionStorage.setItem('userState',"NORMAL")
    }
  },
  ADD_VALUE_TO_POINT(state, newPoint) {
    state.currentUser.userPoint = newPoint
    sessionStorage.setItem('userPoint',newPoint)
  },

  CACHE_AVATAR(state,src) {
    state.avatarCached = true
    state.avatarSrc = src
  },

  INTERCEPT(state,target){
    state.interceptedTarget = target
  },
  RESET_INTERCEPT(state){
    state.interceptedTarget = undefined
  },
}

const state = {
    loginState: false,
    avatarCached: false,
    avatarSrc: '',
    defaultAvatar: require('../assets/img/icon-me-n@2x.png'),
    bannerArticleNotExist: require("../assets/article_not_exist.png"),
    defaultBanner: require("../assets/default_banner.jpeg"),
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
    },

    interceptedTarget: undefined,
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
  },
  defaultAvatar(state) {
    return state.defaultAvatar
  },
  bannerArticleNotExistSrc(state) {
    return state.bannerArticleNotExist
  },
  defaultBannerSrc(state) {
    return state.defaultBanner
  },
  intercepted(state){
    return state.interceptedTarget
  },
}

export default new Vuex.Store({
  actions,
  mutations,
  state,
  getters
});
