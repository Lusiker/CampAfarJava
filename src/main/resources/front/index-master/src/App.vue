<template>
  <div id="app">
    <keep-alive :include="['HomeView']"></keep-alive>
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
