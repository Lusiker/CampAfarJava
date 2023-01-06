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
  },
};
</script>

<style lang="less"></style>
