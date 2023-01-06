import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import {
  Button,
  Card,
  Cell,
  CellGroup, Collapse, CollapseItem,
  Divider, Field,
  Grid,
  GridItem,
  Loading,
  NoticeBar, Switch,
  Tag, Uploader
} from 'vant';

/* 1. 引入项目初始化样式 */
/* 1.1 @ 脚手架项目别称，代表 src目录
   1.2 webpack 处理
*/
import "@/assets/styles/reset.css";
/* 2. 引入移动端适配库 lib-flexible */
import "lib-flexible";
// 3. 引入请求库，并且挂在Vue 原型上面，以后每个Vue实例中，可以直接引用请求实例
import instance from "@/api/instance";
Vue.config.productionTip = false;
Vue.prototype.$request = instance;
// 配置全局前置导航守卫

Vue.filter("timeago", function (time) {
  let data = new Date(time);
  let dateTimeStamp = data.getTime();
  let min = 1000 * 60;
  let hour = min * 60;
  let day = hour * 24;
  let week = day * 7;
  let month = day * 30;
  let year = month * 12;
  let now = new Date().getTime();
  let diffValue = now - dateTimeStamp;

  let result = "";
  if (diffValue < 0) {
    result = "" + "未来";
  }
  let minC = diffValue / min;
  let hourC = diffValue / hour;
  let dayC = diffValue / day;
  let weekC = diffValue / week;
  let monthC = diffValue / month;
  let yearC = diffValue / year;

  if (yearC >= 1) {
    result = "" + parseInt(yearC) + "年前";
  } else if (monthC >= 1 && monthC < 12) {
    result = "" + parseInt(monthC) + " 个月前";
  } else if (weekC >= 1 && weekC < 5 && dayC > 6 && monthC < 1) {
    result = "" + parseInt(weekC) + "周前";
  } else if (dayC >= 1 && dayC <= 6) {
    result = "" + parseInt(dayC) + "天前";
  } else if (hourC >= 1 && hourC <= 24) {
    result = "" + parseInt(hourC) + "小时前";
  } else if (minC >= 1 && minC <= 59) {
    result = "" + parseInt(minC) + "分钟前";
  } else if (diffValue >= 0 && diffValue <= min) {
    result = "刚刚";
  }
  return result;
});

Vue.use(NoticeBar)
  .use(Divider)
  .use(Tag)
  .use(Loading)
  .use(Button)
  .use(Grid)
  .use(GridItem)
  .use(Card)
  .use(Cell)
  .use(CellGroup)
  .use(Collapse)
  .use(CollapseItem)
  .use(Uploader)
  .use(Field)
  .use(Switch);

new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount("#app");
