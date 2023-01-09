<!-- 底部的导航部分 -->
<template>
  <div class="footer">
    <div class="item" @click.stop="handleClick('home')">
      <img v-if="type === 'home'" src="@/assets/img/icon-home-h@2x.png" alt="" />
      <img v-else src="@/assets/img/icon-home-n@2x.png" alt="" />
      <span>首页</span>
    </div>

    <div class="item" @click.stop="handleClick('knowledge')">
      <img v-if="type === 'knowledge'" src="@/assets/img/icon-study-h@2x.png" alt="" />
      <img v-else src="@/assets/img/icon-study-n@2x.png" alt="" /><span>知识</span>
    </div>

    <div class="item" @click.stop="handleClick('member')">
      <img v-if="type === 'member'" src="@/assets/img/icon-me-h@2x.png" alt="" />
      <img v-else src="@/assets/img/icon-me-n@2x.png" alt="" /><span>我的</span>
    </div>
  </div>
</template>

<script>
export default {
  props: ["type"],
  components: {},
  data() {
    return {};
  },
  computed: {},
  watch: {},
  //方法集合
  methods: {
    handleClick: function (name) {
      this.$router.push({
        name,
      });
    },
  }
};
</script>
<style lang="less" scoped>
.footer {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  width: 100%;
  height: auto;
  background: #ffffff;
  display: flex;
  flex-direction: row;
  justify-content: space-around;
  z-index: 0;
  margin-top: 60px;
  .item {
    height: 53px;
    display: flex;
    flex-direction: column;
    cursor: pointer;
    align-items: center;
    img {
      width: 28px;
      height: 28px;
      margin-top: 3px;
      margin-bottom: 7px;
    }
    span {
      height: 11px;
      font-size: 11px;
      font-weight: 400;
      color: #333333;
      line-height: 11px;
      text-align: center;
    }
  }
}
</style>
