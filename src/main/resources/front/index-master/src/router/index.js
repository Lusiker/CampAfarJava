import Vue from 'vue';
import VueRouter from "vue-router";
import HomeView from "../views/HomeView.vue";

Vue.use(VueRouter);
const routes = [
  {
    path: '/home',
    redirect: '/'
  },
  {
    path: "/",
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
    path: "/checkstand",
    name: "checkstand",
    component: () => import("../views/CheckstandView.vue"),
    meta: {
      title: "收银台",
      auth: true,
    },
  },
  {
    path: "/remit",
    name: "remit",
    component: () => import("../views/RemitView.vue"),
    meta: {
      title: "打款",
      auth: true,
    },
  },
  {
    path: "/message",
    name: "message",
    component: () => import("../views/MessageView.vue"),
    meta: {
      title: "我的消息",
      auth: true,
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
    path: "/user/:uid",
    name: "others",
    component: () => import("../views/OtherUserView.vue")
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
