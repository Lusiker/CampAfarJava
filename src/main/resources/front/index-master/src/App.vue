<template>
  <div id="app">
    <!-- <van-button type="primary">主要按钮</van-button>
    <van-button type="info">信息按钮</van-button>
    <van-button type="default">默认按钮</van-button>
    <van-button type="warning">警告按钮</van-button>
    <van-button type="danger">危险按钮</van-button> -->
    <router-view />
  </div>
</template>

<script>
export default {
  data: function () {
    return {};
  },
  created() {
    if(!this.$store.getters.loginState){
      if(sessionStorage.getItem('loginState')){
        this.$store.dispatch('tryRetrieveUserInfo')
      }
    }
    // if (this.$router.path !== "/") {
    //   this.$router.replace("");
    // }
    // 1. 通过 axios.create 方法创建一个实例
    // let instance = axios.create({
    //   baseURL: "http://106.13.204.114",
    //   timeout: 100000,
    //   // headers: {
    //   //   "meedu-platform": "H5",
    //   // },
    // });
    // // 通过拦截器添加请求头信息
    // instance.interceptors.request.use(
    //   function (config) {
    //     console.log("请求之前的配置项：", config);
    //     config.headers["meedu-platform"] = "H5";
    //     // 后面这里登录之后还会添加一个登录凭证 token
    //     return config;
    //   },
    //   function (error) {
    //     return Promise.reject(error);
    //   }
    // );
    // // 响应的拦截器
    // instance.interceptors.response.use(
    //   function (response) {
    //     console.log("响应拦截器响应的数据,", response);
    //     return response.data;
    //   },
    //   function (error) {
    //     return Promise.reject(error);
    //   }
    // );
    // instance
    //   .get("/api/v2/viewBlock/page/blocks", {
    //     // 固定有一个key params
    //     params: {
    //       platform: "h5",
    //       page_name: "h5-page-index",
    //     },
    //   })
    //   .then((res) => {
    //     console.log(res);
    //   })
    //   .catch((err) => console.log(err))
    //   .finally(() => {
    //     console.log("执行完成了");
    //   });
  },
};
</script>

<style lang="less"></style>
