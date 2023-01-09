// es 中 import 语法，引入模块 Vue 核心库
import Vue from 'vue';
// vue-router 核心库
import VueRouter from "vue-router";
// 引入一个组件
import HomeView from "../views/HomeView.vue";
// vue 中一个插件
// Vue.use(VueRouter)
Vue.use(VueRouter);
const routes = [
  {
    path: '/',
    redirect: '/home'
  },
  {
    path: "/home",
    name: "home",
    component: HomeView,
    meta: {
      title: "主页",
    },
  },
  {
    path: "/login-password",
    name: "login-password",
    component: () => import("../views/LoginPasswordView.vue"),
    meta: {
      title: "密码登录",
    },
  },
  {
    path: "/article/:aid",
    name: "articleDetail",
    component: () => import("../views/DetailView.vue"),
    meta: {
      title: "文章详情",
    },
  },
  {
    path: "/knowledge",
    name: "knowledge",
    component: () => import("../views/KnowledgeView.vue"),
    meta: {
      title: "知识",
    },
  },
  {
    path: "/member",
    name: "member",
    component: () => import("../views/MemberView.vue"),
    meta: {
      title: "我的",
    },
  },
  {
    path: "/users",
    name: "users",
    component: () => import("../views/UsersView.vue"),
    meta: {
      title: "用户中心",
    },
  },
  {
    path: '/user/:uid',
    name: '用户资料',
    component: () => import("../views/OtherUserView.vue")
  },
  {
    path: "/trolley",
    name: "trolley",
    component: () => import("../views/TrolleyView.vue"),
    meta: {
      title: "购物车",
    },
  },
  {
    path: "/order",
    name: "order",
    component: () => import("../views/OrderView.vue"),
    meta: {
      title: "我的订单",
    },
  },
  {
    path: "/purchase",
    name: "purchase",
    component: () => import("../views/PurchaseView.vue"),
    meta: {
      title: "购买",
    },
  },
  {
    path: "/myarticle",
    name: "myarticle",
    component: () => import("../views/MyArticleView.vue"),
    meta: {
      title: "我的文章",
    },
  },
  {
    path: "/register",
    name: "register",
    component:  () => import("../views/RegisterView.vue"),
    meta: {
      title: "账户注册",
    },
  },
  {
    path: '/publish',
    name: 'publish',
    component: () => import("../views/PublishUse.vue"),
    meta: {
      title: "发布"
    }
  },
  {
    path: '*',
    redirect: '/'
  }
];

// 创建一个路由的实例
const router = new VueRouter({
  routes,
});
// 通过es6 export 语法将路由模块进行导出
export default router;
