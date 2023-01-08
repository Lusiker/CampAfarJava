<!--  -->
<template>
    <div class="container">
      <div class="title-box">
        <div class="top-box">
          <!-- <div class="back-img">
            <img src="../assets/img/icon-back.png" alt="" @click="goToback" />
          </div> -->
          <div class="title">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;知识列表</div>
          <div class="button" @click="show">
            <div class="button-text">
              <span>筛选</span>
              <img src="../assets/img/icon-filter.png" alt="" />
            </div>
            <div class="btn-box" v-show="isShow">
              <div class="item" @click="filtrate(0)">全部</div>
              <div class="item" v-for="item in choice" :key="item.id" @click="filtrate(item.id)">
                {{ item.name }}
              </div>
            </div>
            <div class="mask" v-show="isShow" @click.stop="hide"></div>
          </div>
        </div>

        <div class="bottom-box">
          <div class="left active" @click="goTofree">全部</div>
          <div class="right" @click="goTovod">免费</div>
          <!-- 12 -->

          <!-- 12 -->
        </div>

      </div>



      <div class="box-body" v-if="num === 0">

        <van-pagination
          v-model="page"
          :page-count="totalPage"
          items-per-page="5"
        />
      </div>
      <div class="box-body" v-else>

      </div>
      <kp-foot-nav type="knowledge"></kp-foot-nav>
      <div v-for="article in articles" :key="article.articleId">

      </div>
    </div>

  </template>

  <script>
  import KPFootNav from "@/components/kp-foot-nav.vue";
  import KPCourseItem from "@/components/KPCourseItem.vue";

  export default {
    //import引入的组件需要注入到对象中才能使用
    components: {
      KPCourseItem,
      "kp-foot-nav": KPFootNav,
    },
    data() {
      //这里存放数据
      return {
        page: 1,
        totalPage: 1,
        num: 0,
        articles: [{articleId:0},{articleId:1},{articleId:2}],
        frees: {},
        isShow: false,
        choice: [],
      };
    },
    computed: {},
    watch: {},
    methods: {
      show: function () {
        this.isShow = true;
      },
      hide: function () {
        this.isShow = false;
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
      loadPageCount() {
        this.$request.get('/articles/articleCount').then(
            (res) => {
              let count = res.returnObject
              this.totalPage = Math.ceil(count / 5)
            }
        )
      }
    },
    beforeMount() {
      this.loadPageCount()
    }
  };
</script>

<style lang="less">
  .article-card {
    width: 100%;
    height: auto;
  }

  div.container {
    position: absolute;
    left: 0;
    top: 0;
    overflow-y: scroll;
    overflow-x: hidden;
    background-color: #f3f6f9;
    width: 100%;
    height: 100%;
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
    div.grey {
      float: left;
      width: 100%;
      height: 0.2667rem;
      background: #f3f6f9;
    }
    div.box-body {
      margin-top: 100px;
      padding: 0.6667rem 0.4rem 0.1333rem 0.4rem;
    }
  }
  </style>
