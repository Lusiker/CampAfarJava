<!--  -->
<template>
  <div class="">
    <div class="item" @click.stop="toDetail(item)">
      <div class="item-comp">
        <div class="img">
          <img :src="item.thumb" alt="" />
        </div>
        <div class="text">
          <div class="text-title">{{ item.title }}</div>
          <div class="text-info">
            <div class="text-info-left">{{ item.user_count + "人已订阅" }}</div>
            <div class="text-info-right" v-if="item.charge > 0">
              <span class="charge"> ￥ </span>
              {{ item.charge }}
            </div>
            <div class="free" v-else>
              <div style="color">免费</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';

export default {
  props: ["item"],
  //import引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    //这里存放数据
    return {};
  },
  //监听属性 类似于data概念
  computed: {},
  //监控data中的数据变化
  watch: {},
  //方法集合
  methods: {
    toDetail: function (item) {
      this.$router.push({
        path: "/vod/detail",
        query: {
          id: item.id,
        },
      });
    },
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {},
  //生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {},
  beforeCreate() {}, //生命周期 - 创建之前
  beforeMount() {}, //生命周期 - 挂载之前
  beforeUpdate() {}, //生命周期 - 更新之前
  updated() {}, //生命周期 - 更新之后
  beforeDestroy() {}, //生命周期 - 销毁之前
  destroyed() {}, //生命周期 - 销毁完成
  activated() {}, //如果页面有keep-alive缓存功能，这个函数会触发
};
</script>
<style lang="less" scoped>
.item {
  margin-bottom: 0.4rem;
  .item-comp {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    .img {
      width: 3.2rem;
      height: 2.4rem;
      margin-right: 0.2667rem;
      img {
        width: 3.2rem;
        height: 2.4rem;
        border-radius: 0.1067rem;
      }
    }

    .text {
      flex: 1;
      padding-top: 0.1333rem;
      .text-title {
        width: 100%;
        height: 1.0667rem;
        font-size: 0.4rem;
        font-weight: 500;
        color: #171923;
        line-height: 0.5333rem;
        margin-bottom: 0.4rem;
      }

      .text-info {
        width: 100%;
        height: auto;
        display: flex;
        align-items: center;
        .text-info-left {
          flex: 1;
          text-align: left;
          font-size: 0.32rem;
          font-weight: 400;
          color: #999;
          line-height: 0.32rem;
        }

        .text-info-right {
          flex: 1;
          text-align: right;
          font-size: 0.48rem;
          line-height: 0.48rem;
          font-weight: 600;
          color: #ff5858;
          .charge {
            font-size: 0.3733rem;
            margin-right: -0.1333rem;
          }
        }
        .free {
          flex: 1;
          text-align: right;
          font-size: 0.3733rem;
          line-height: 0.48rem;
          font-weight: 600;
          color: #52c41a;
        }
      }
    }
  }
}
</style>
