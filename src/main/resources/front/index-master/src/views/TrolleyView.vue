<!--  -->
<template>
  <div class="container">
    <div class="title-box">
      <div class="top-box">
        <div class="back-img">
          <img src="../assets/img/icon-back.png" alt="" @click="toBack" />
        </div>
        <div class="title">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;购物车</div>
        <div class="button" @click="show">
          <div class="button-text">
            <span>筛选</span>
            <img src="../assets/img/icon-filter.png" alt="" />
          </div>
          <!--          <div class="btn-box" v-show="isShow">-->
          <!--            <div class="item" @click="filtrate(0)">全部</div>-->
          <!--            <div class="item" v-for="item in choice" :key="item.id" @click="filtrate(item.id)">-->
          <!--              {{ item.name }}-->
          <!--            </div>-->
          <!--          </div>-->
          <div class="mask" v-show="isShow" @click.stop="hide"></div>
        </div>
      </div>

      <div class="bottom-box">
        <div class="left active"          @click="goTofree">全部</div>
        <div class="right" @click="goTovod">免费</div>
        <!-- 12 -->

        <!-- 12 -->
      </div>
    </div>

    <div class="box-body" >
      <div class="" v-for="item in mains" :key="item.id">
        <div class="item" @click.stop="goToPurchase(item)">
          <div class="item-comp">
            <div class="img">
              <img src="../assets/img/1.jpg" alt="" />
            </div>
            <div class="text">
              <div class="text-title">{{ item.title }}</div>
              <div class="text-info">

                <div class="text-info-left">{{"作者:"+item.uid}}</div>
                <div class="text-info-right">{{"价格:"+item.price}}</div>

              </div>

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
// a.1 引入vant 轮播图相关组件
import Vue from "vue";
import { Swipe, SwipeItem, Lazyload } from "vant";
Vue.use(Swipe);
Vue.use(SwipeItem);
Vue.use(Lazyload);
import KPBlank from "@/components/KPBlank.vue";
import KPFootNav from "@/components/kp-foot-nav.vue";
export default {
  //import引入的组件需要注入到对象中才能使用
  components: {
    KPBlank,
    "kp-foot-nav": KPFootNav,
  },
  data() {
    //这里存放数据
    return {
      blocks: [],
      slides: [],
      navs: [],
      blank: {},
      h5GzhV1: [],
      mains: [
        {title:'前后端如何数据联调',uid:'1',price:'2'},
        {title:'vue生命周期的讲解',uid:'3',price:'4'},
      ],
      showQRcode: false,//不知道是什么show里用到
      isShow:false,


      tables: [
        {
          src: require("../assets/img/icon-home-h@2x.png"),
          text: "首页",
        },
        {
          src: require("../assets/img/马上提问1.png"),
          text: "知识",
        },
        {
          src: require("../assets/img/icon-study-n@2x.png"),
          text: "问答",
        },
        {
          src: require("../assets/img/icon-me-n@2x.png"),
          text: "我的",
        },
      ],
    };
  },
  //监听属性 类似于data概念
  computed: {},
  //监控data中的数据变化
  watch: {},
  //方法集合
  methods: {

    toBack: function () {
      this.$router.push({
        path: "/member",
      });
    },
    toDetail: function (item) {
      //this.$router.push({
      // path: "/vod/detail",
      // query: {
      //   id: item.id,
      // },
      //});
    },
    goToPurchase: function (item) {
      this.$router.push({
      path: "/purchase",
      query: {
        id: item.id,
      },
      });
    },
    getList() {
      this.$axios({
        method: 'get',
        url: '',
        headers: {
          // 'Content-Type': "application/json;charset=UTF-8",
        },
        params: {

        }
      })
          .then(res=>{                    //请求成功后执行函数
            if(res){
              //定义从后端获取的joblist
              console.log("获取成功")
            }else{
              console.log("获取失败")
            }
          })
          .catch(err=>{                   //请求错误后执行函
            console.log("请求错误")
          })
    },
    goToDetail: function (item) {
      // this.$router.push({
      //   path: "/vod/detail",
      //   query: {
      //     id: item.href.split("?")[1].split("=")[1],
      //   },
      // });
    },

    show: function () {
      this.showQRcode = true;
    },
    hide: function () {
      this.showQRcode = false;
    },
    goTofree: function () {
      this.num = 0;
      let left = document.querySelector(".left");
      let right = document.querySelector(".right");
      left.className = "left active";
      right.className = "right";
    },
    goTovod: function () {
      this.num = 1;
      let left = document.querySelector(".left");
      let right = document.querySelector(".right");
      left.className = "left";
      right.className = "right active";
    },
  },
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
<style lang="less">
div.container {
  position: absolute;
  left: 0;
  top: 0;
  overflow-y: scroll;
  overflow-x: hidden;
  width: 100%;
  height: 100%;
  background-color: #f3f6f9;
  div.title-box {
    width: 100%;
    height: auto;
    float: left;
    box-sizing: border-box;
    padding: 0 0.4rem;
    background-color: #fff;
    line-height: 0.8rem;
    position: sticky;
    top: 0;
    z-index: 999;
    display: flex;
    flex-direction: column;
    div.top-box {
      width: 100%;
      height: 1.3333rem;
      background: #fff;
      display: flex;
      flex-direction: row;
      justify-content: space-between;
      align-items: center;
      div.back-img {
        width: 0.64rem;
        height: 0.64rem;
        cursor: pointer;
        img {
          width: 0.64rem;
          height: 0.64rem;
        }
      }

      div.title {
        width: 72%;
        height: 0.48rem;
        font-size: 0.48rem;
        font-weight: 500;
        color: #171923;
        line-height: 0.48rem;
        text-align: center;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }

      div.button {
        width: auto;
        height: auto;
        position: relative;
        div.button-text {
          font-size: 0.3733rem;
          font-weight: 400;
          color: #333;
          cursor: pointer;
          display: flex;
          align-items: center;
          span {
            margin-right: 5px;
          }

          img {
            width: 0.64rem;
            height: 0.64rem;
            margin-left: 0.1333rem;
          }
        }
        div.btn-box {
          position: absolute;
          top: 0.8rem;
          right: 0;
          z-index: 10080;
          width: 4.4267rem;
          height: auto;
          box-sizing: border-box;
          padding: 0 0.4rem;
          border-radius: 5px;
          background-color: #fff;
          div.item {
            width: 100%;
            height: auto;
            float: left;
            font-size: 14px;
            color: #333;
            line-height: 40px;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
          }
          div.active {
            color: #3ca7fa;
          }
        }

        div.mask {
          position: fixed;
          top: 0;
          left: 0;
          right: 0;
          bottom: 0;
          z-index: 997;
          background-color: rgba(0, 0, 0, 0.3);
        }
      }
    }

    div.bottom-box {
      width: 100%;
      height: 1.3333rem;
      background: #fff;
      display: flex;
      align-items: center;
      //22

      //11
      div.left {
        // color: #3ca7fa;
        font-weight: bolder;
        font-size: 0.4267rem;
        margin-right: 15px;
      }
      div.right {
        width: auto;
        height: auto;
        float: left;
        font-size: 0.4267rem;
        color: #666;
        margin-right: 0.4267rem;
        font-weight: bolder;
      }
      div.active {
        color: #3ca7fa;
      }
    }
  }

  div.box-body {
    margin-top: 100px;
    padding: 0.6667rem 0.4rem 0.1333rem 0.4rem;
  }

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
            font-size: 0.32rem;
            font-weight: 400;
            color: #999;
            line-height: 0.32rem;
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
}
</style>
