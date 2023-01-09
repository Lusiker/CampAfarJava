<template>
    <div class="container1">
      <div class="title-box">
        <div class="bottom-box">
          <div class="left active" @click="setAll">全部</div>
          <div class="right" @click="setFree">免费</div>
          <!-- 12 -->

          <!-- 12 -->
        </div>
      </div>

      <div style="margin-top: 60px;margin-bottom: 60px" v-if="num === 0">
        <van-pagination
            v-model="page"
            :page-count="totalPage"
            items-per-page="5"
            @change="getPage"
        />
        <div v-if="!loading">
          <v-list style="width: 100%;background-color: #f3f6f9">
            <InfoCard v-for="item in articles[page]" :item="item" :get-date-string="getDateString" :key="item.articleId"/>
          </v-list>
        </div>
      </div>
      <div style="margin-top: 60px;margin-bottom: 60px" v-else>
        <van-pagination
            v-model="page"
            :page-count="totalPage"
            items-per-page="5"
            @change="getPage"
        />
        <div v-if="!loading">
          <v-list style="width: 100%;background-color: #f3f6f9">
            <InfoCard v-for="item in articles[page]" :item="item" :get-date-string="getDateString" :key="item.articleId"/>
          </v-list>
        </div>
      </div>
      <kp-foot-nav type="knowledge"></kp-foot-nav>
    </div>
  </template>

  <script>
  import KPFootNav from "@/components/kp-foot-nav.vue";
  import { Toast } from 'vant';
  import InfoCard from '@/components/InfoCard.vue';

  export default {
    components: {
      InfoCard,
      "kp-foot-nav": KPFootNav,
    },
    data() {
      //这里存放数据
      return {
        filterState: 1,
        page: 1,
        totalPage: 1,
        num: 0,
        articles: {},
        loading: true,
      };
    },
    computed: {},
    watch: {},
    methods: {
      setAll: function () {
        this.num = 0;
        this.page = 1;
        let left = document.querySelector(".left");
        let right = document.querySelector(".right");
        left.className = "left active";
        right.className = "right";

        this.getArticles(false)
      },
      setFree: function () {
        this.num = 1;
        this.page = 1;
        let left = document.querySelector(".left");
        let right = document.querySelector(".right");
        left.className = "left";
        right.className = "right active";

        this.getArticles(true)
      },
      getArticles(free) {
        this.loading = true

        let url1 = '/articles/articleCount'
        if(free) {
          url1 = url1 + '?free=true'
        }

        this.$request.get(url1).then(
            (res) => {
              let count = res.returnObject
              this.totalPage = count / 5 + (count % 5 === 0 ? 0 : 1)

              let url2 = '/articles?page=' + this.page + "&orderBy=0"
              if (free) {
                url2 = url2 + "&free=true"
              }
              this.$request(url2)
                  .then(
                      (res) => {
                        switch (res.stateEnum.state) {
                          case 0: {
                            this.articles[this.page] = res.returnObject.records
                            this.loading = false

                            break
                          }
                          default: {
                            Toast.fail("获取文章信息失败")
                          }
                        }
                      }
                  )
            }
          )
      },
      getPage() {
        this.loading = true

        let url1 = '/articles/articleCount'
        if(this.num === 1) {
          url1 = url1 + '?free=true'
        }
        this.$request.get(url1).then(
            (res) => {
              let count = res.returnObject
              this.totalPage = count / 5 + (count % 5 === 0 ? 0 : 1)

              let url = '/articles?page=' + this.page
              if(this.num === 1) {
                url = url + '&free=true'
              }
              this.$request.get(url)
                  .then(
                      (res) => {
                        switch (res.stateEnum.state) {
                          case 0: {
                            this.articles[this.page] = res.returnObject.records
                            this.loading = false
                            break
                          }
                          default: {
                            Toast.fail("获取文章失败（页码：" + this.page + "）")
                            break
                          }
                        }
                      }
                  )
            }
        )
      },
      loadPageCount() {
        this.$request.get('/articles/articleCount').then(
            (res) => {
              let count = res.returnObject
              this.totalPage = count / 5 + (count % 5 === 0 ? 0 : 1)
            }
        )
      },
      getDateString(rawDate) {
        let t = new Date(rawDate)
        let result = t.getFullYear() + "年" + (t.getMonth() + 1) +"月" + t.getDate() + "日  " + t.getHours() + ":"
        if(t.getMinutes() < 10) {
          result += "0" + t.getMinutes() + ":"
        } else {
          result += t.getMinutes() + ":"
        }

        if(t.getSeconds() < 10) {
          result += "0" + t.getSeconds()
        } else {
          result += t.getSeconds()
        }

        return result
      }
    },
    beforeMount() {
      this.loadPageCount()
      this.getArticles()
    }
  };
</script>

<style lang="less" scoped>
  .article-card {
    width: 100%;
    height: auto;
  }

  div.container1 {
    position: relative;
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
      position: fixed;
      top: 0;
      z-index: 1;
      div.top-box {
        width: 100%;
        height: 1.3333rem;
        background: #fff;
        display: flex;
        flex-direction: row;
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
      margin-top: 200px;
      height: 100%;
      position: absolute;
    }
  }
  </style>
