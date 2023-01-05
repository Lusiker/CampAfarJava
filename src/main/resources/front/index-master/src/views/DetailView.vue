<!--  -->
<template>
  <div class="container">
    <div class="nav-box">
      <img src="../assets/img/icon-back.png" alt="" @click="goToHome()" />
      <div class="title">知识详情</div>
    </div>
    <div class="main-img">
      <!-- <img :src="mains.thumb" alt="" /> -->
      <!-- <div class="main-title">{{ mains.title }}</div> -->
      <div>标题</div>
      <div class="main-title"><h1>{{ mains.title }}</h1></div>
    </div>
    <div class="stat">
      <div class="left-box">
        <div class="charge" v-if="mains.charge > 0">
          <span>￥</span>
          {{ mains.charge }}
        </div>
        <div class="free" v-else>免费</div>
      </div>
      <div class="right-box">
        <span>{{ mains.user_count }}人已订阅</span>
      </div>
    </div>
    <div class="border"></div>
    <div class="body">
      <van-tabs v-model:active="active" line-width="20px">
        <van-tab title="内容">
          <div class="cource-desc">
            <!-- <div class="desc" v-html="mains.render_desc"></div> -->
            <p>内容是老流域apex拉中之拉，天天不上号，实属不行!爱上的发生打发的说法是打发打发的说法当时发生的发生对方的发生地方</p>
          </div>
          <div class="bottom-tab" v-if="token">
            <div class="btn">
              <div class="icon" @click="CollectClick">
                <img src="../assets/img/collect.png" alt="" v-if="isCollect == false" />
                <img src="../assets/img/collect-active.png" alt="" v-else-if="isCollect == true" />
              </div>
              <div class="text">收藏</div>
            </div>
            <div class="study">
              <span @click="tosee()">开始学习 </span>
            </div>
          </div>
          <div class="bottom-tab" v-else @click="tologin">
            <div class="btn">
              <div class="icon" @click="CollectClick">
                <img src="../assets/img/collect.png" alt="" v-if="isCollect == false" />
                <img src="../assets/img/collect-active.png" alt="" v-else-if="isCollect == true" />
              </div>
              <div class="text">收藏</div>
            </div>
            <!-- <div class="left">
              <span>VIP会员免费看</span>
            </div> -->
            <div class="right">
              <span>订阅课程</span>
            </div>
          </div>
        </van-tab>
        <!-- <van-tab title="目录">
          <div class="chapter-box" v-for="item in data.chapters" :key="item.id">
            <div class="chapter-title">
              {{ item.title }}
              <img src="../assets/img/icon-back.png" alt="" @click.stop="show" />
            </div>
            <div class="chapter-box" v-for="i in data.videos[item.id]" :key="i.id" v-show="isshow">
              <div class="left">
                <span class="free">试看</span>
                <span class="text">{{ i.title }}</span>
              </div>
              <div class="right">
                <span class="time">0{{ i.duration / 60 }}:00</span>
              </div>
            </div>
            <div class="bottom-tab" v-if="token">
              <div class="btn">
                <div class="icon" @click="CollectClick">
                  <img src="../assets/img/collect.png" alt="" v-if="isCollect == false" />
                  <img
                    src="../assets/img/collect-active.png"
                    alt=""
                    v-else-if="isCollect == true"
                  />
                </div>
                <div class="text">收藏</div>
              </div>
              <div class="study">
                <span @click="tosee()">开始学习 </span>
              </div>
            </div>
            <div class="bottom-tab" v-else>
              <div class="btn" @click="tologin">
                <div class="icon" @click="CollectClick">
                  <img src="../assets/img/collect.png" alt="" v-if="isCollect == false" />
                  <img
                    src="../assets/img/collect-active.png"
                    alt=""
                    v-else-if="isCollect == true"
                  />
                </div>
                <div class="text">收藏</div>
              </div>
              <div class="left" @click="tologin">
                <span>VIP会员免费看</span>
              </div>
              <div class="right" @click="tologin">
                <span>订阅课程</span>
              </div>
            </div>
          </div>
        </van-tab> -->
        <van-tab title="评论">
          <div
            class="comments-box"
            v-if="commentsList.comments"
            v-for="item in commentsList.comments"
            :key="item.id * item.user_id"
          >
            <div class="comments-item">
              <div class="avtor">
                <img :src="users.avatar" alt="" />
              </div>
              <div class="content">
                <div class="name">{{ users.nick_name }}</div>
                <div class="time">{{ item.created_at | timeago }}</div>
                <div class="text">
                  <div>{{ item.render_content }}</div>
                </div>
              </div>
            </div>
          </div>
          <div class="comments-box" v-else>
            <img src="../assets/img/img-placeholder.png" alt="" class="else" />
          </div>
          <div class="comments-tab">
            <div class="inp">
              <input type="text" placeholder="请输入评论内容" v-model="text" />
            </div>
            <div class="text" @click="announce">发布</div>
          </div>
        </van-tab>
      </van-tabs>
    </div>
  </div>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import axios from "axios";
import Vue from "vue";
import { Tab, Tabs, Toast } from "vant";

Vue.use(Tab);
Vue.use(Tabs);
export default {
  //import引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    //这里存放数据
    return {
      mains: {},
      active: 0,
      data: {
        videos: {},
      },
      isshow: true,
      remark: {},
      commentsList: {},
      users: {},
      text: "",
      token: [],
      num: 0,
      id: "",
      ID: "",
      isCollect: false,
    };
  },
  //监听属性 类似于data概念
  computed: {},
  //监控data中的数据变化
  watch: {},
  //方法集合
  methods: {
    goToHome: function () {
      this.$router.push({
        path: "/",
      });
    },
    CollectClick: function () {
      this.$request
        .get(
          `
api/v2/course/${this.$route.query.id}/like`
        )
        .then(() => {
          location.reload();
        });
    },
    tosee: function () {
      this.$router.push({
        path: "/video",
        query: {
          id: this.id,
        },
      });
    },
    show: function () {
      let trensform = document.querySelector(".chapter-title img");
      this.token = localStorage.getItem("h5-token");
      if (this.isshow == true) {
        trensform.style.transform = "rotate(180deg)";
      } else {
        trensform.style.transform = "rotate(270deg)";
      }
      this.isshow = !this.isshow;
    },
    announce: function () {
      if (!this.text) {
        Toast.fail("评论不能为空");
        return;
      }
      this.$request
        .post(`/api/v2/course/${this.$route.query.id}/comment`, {
          content: this.text,
        })
        .then(() => {
          location.reload();
        });
      this.text = "";
    },
    tologin: function () {
      this.$router.push({
        path: "/login-password",
      });
    },
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  async created() {
    let res = await this.$request.get(`http://1.14.239.98/api/v2/course/${this.$route.query.id}`);
    this.mains = res.data.course;
    this.data = res.data;
    this.isCollect = res.data.isCollect;
    let comments = await this.$request.get(
      `http://1.14.239.98/api/v2/course/${this.$route.query.id}/comments`
    );
    this.commentsList = comments.data;
    this.users = comments.data.users[1];
    this.token = localStorage.getItem("h5-token");
    this.ID = res.data.chapters[0].id;
    this.id = res.data.videos[this.ID][0].id;
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
  display: block;
  overflow-y: scroll;
  height: 100vh;
  div.nav-box {
    position: fixed;
    top: 0;
    width: 100%;
    height: 1.3333rem;
    background-color: #fff;
    z-index: 100;
    padding-left: 0.4rem;
    padding-right: 0.4rem;
    display: flex;
    flex-direction: row;
    justify-content: center;
    border-bottom: 1px solid #f1f2f6;
    line-height: 1.3333rem;
    img {
      position: absolute;
      left: 0.4rem;
      width: 0.5067rem;
      height: 0.5067rem;
      top: 0.4267rem;
    }

    div.title {
      width: 72%;
      height: auto;
      font-size: 0.4533rem;
      font-weight: 500;
      text-align: center;
      color: #333;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  }
  div.main-img {
    margin-top: 50px;
    width: 100%;
    height: auto;
    img {
      width: 100%;
    }
    .main-title {
      box-sizing: border-box;
      padding: 0.4rem 0.4rem 0 0.4rem;
      font-size: 0.5rem;
      font-weight: 500;
      color: #171923;
      word-break: break-all;
      line-height: 0.4rem;
      background-color: #fff;
    }
  }
  div.stat {
    width: 100%;
    height: auto;
    display: flex;
    align-items: center;
    justify-content: space-between;
    box-sizing: border-box;
    padding: 0.5333rem 0.4rem 0.4rem 0.4rem;
    font-size: 0.5333rem;
    line-height: 0.8rem;
    background-color: #fff;
    div.left-box {
      color: #ff4d4f;
      font-weight: 500;
      div.charge {
        color: #ff4d4f;
        font-weight: 500;
        span {
          font-size: 0.3733rem;
        }
      }

      div.free {
        font-size: 0.48rem;
        color: #04c877 !important;
        margin-right: 0.4rem;
      }
    }

    div.right-box {
      color: #999;
      font-weight: 400;
      font-size: 0.3733rem;
      span {
        color: #999;
        font-weight: 400;
        font-size: 0.3733rem;
      }
    }
  }
  .border {
    width: 100%;
    height: 0.2667rem;
    float: left;
    background-color: #f6f6f6;
    box-sizing: border-box;
  }
  div.body {
    width: 100%;
    height: auto;
    margin-top: 0.4rem;
    div.cource-desc {
      width: 100%;
      height: auto;
      float: left;
      color: #333;
      line-height: 0.56rem;
      box-sizing: border-box;
      padding: 0.5333rem 0.4rem 0.4rem 0.4rem;
      p{
        text-indent: 2em;
        font-size: 20px;
      }
      div.desc {
        width: 100%;
        p {
          img {
            width: 9.2rem;
            height: 13.4133rem;
          }
        }
      }
    }
    div.chapter-box {
      padding: 0.2667rem 0.4rem 1.3333rem 0.4rem;
      div.chapter-title {
        width: 100%;
        height: auto;
        display: flex;
        align-items: center;
        justify-content: space-between;
        font-size: 0.4rem;
        color: #333;
        font-weight: 700;
        margin-bottom: 0.5333rem;
        margin-top: 0.2667rem;
        img {
          width: 0.4rem;
          height: 0.4rem;
          transform: rotate(270deg);
        }
      }

      div.chapter-box {
        width: 100%;
        height: auto;
        display: flex;
        align-items: center;
        justify-content: space-between;
        margin-bottom: 0.2667rem;
        line-height: 0.96rem;
        box-sizing: border-box;
        padding: 0 0.2667rem 0 0.2667rem;
        background: #f6f6f6;
        border-radius: 0.1067rem;
        div.left {
          flex: 1;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
          font-size: 0.3733rem;
          color: #333;
          span.free {
            padding: 0.0533rem 0.1067rem;
            background-color: #04c877;
            border-radius: 0.0533rem;
            color: #fff;
            font-size: 0.32rem;
            margin-right: 0.2667rem;
          }
        }

        div.right {
          width: auto;
          height: auto;
          font-size: 0.32rem;
          margin-left: 10px;
          color: #999;
        }
      }
    }
    .comments-box {
      width: 100%;
      height: 2.1333rem;
      color: #333;
      line-height: 0.56rem;
      box-sizing: border-box;
      padding: 0.5333rem 0.4rem 3.4rem 0.4rem;
      img.else {
        position: absolute;
        top: 3.6rem;
        left: 3.4667rem;
        width: 3.2rem;
        height: 3.2rem;
      }
      div.comments-item {
        width: 100%;
        height: auto;
        float: left;
        margin-top: 0;
        margin-bottom: 0.8rem;
        display: flex;
        div.avtor {
          width: 0.8rem;
          height: 0.8rem;
          margin-right: 0.2667rem;
          img {
            width: 0.8533rem;
            height: 0.8533rem;
            border-radius: 50%;
          }
        }

        div.content {
          flex: 1;
          position: relative;
          div.name {
            width: 100%;
            height: auto;
            float: left;
            font-size: 0.32rem;
            box-sizing: border-box;
            color: #999;
            margin-top: 0.1333rem;
            margin-bottom: 0.3467rem;
          }

          div.time {
            position: absolute;
            font-size: 0.2667rem;
            color: #999;
            right: 0.4rem;
            top: 0.16rem;
          }

          div.text {
            width: 100%;
            height: auto;
            float: left;
            font-size: 0.3733rem;
            line-height: 1.2;
            color: #333;
          }
        }
      }
    }
  }
  div.bottom-tab {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    z-index: 11;
    width: 100%;
    height: 1.4133rem;
    background-color: #fff;
    border-top: 1px solid rgba(0, 0, 0, 0.1);
    display: flex;
    box-sizing: border-box;
    padding-left: 0.4rem;
    padding-right: 0.4rem;
    div.btn {
      width: 0.6667rem;
      height: auto;
      margin-right: 0.4rem;
      div.icon {
        width: 100%;
        height: 0.6667rem;
        box-sizing: border-box;
        padding-top: 0.1333rem;
        text-align: center;
        margin-bottom: 0.0267rem;
        img {
          width: 0.64rem;
          height: 0.64rem;
        }
      }

      div.text {
        width: 100%;
        height: 0.6667rem;
        float: left;
        line-height: 0.6667rem;
        font-size: 0.2667rem;
        font-weight: 400;
        color: #666;
        text-align: center;
      }
    }
    div.study {
      flex: 1;
      display: block;
      text-align: center;
      margin: 0.1067rem 0;
      line-height: 1.0667rem;
      font-size: 0.4rem;
      font-weight: 500;
      color: #fff;
      display: flex;
      align-items: center;
      justify-content: center;
      border-radius: 0.08rem;
      background-color: #3ca7fa;
      color: #fff;
    }
    div.left {
      border-radius: 3px 0 0 3px;
      background-color: #e2a500;
      color: #fff;
      flex: 1;
      display: block;
      text-align: center;
      margin: 0.1067rem 0;
      line-height: 1.0667rem;
      font-size: 0.4rem;
      font-weight: 500;
      display: flex;
      align-items: center;
      justify-content: center;
    }

    div.right {
      border-radius: 0 4px 4px 0;
      background-color: #ff5068;
      color: #fff;
      flex: 1;
      display: block;
      text-align: center;
      margin: 0.1067rem 0;
      line-height: 1.0667rem;
      font-size: 0.4rem;
      font-weight: 500;
      display: flex;
      align-items: center;
      justify-content: center;
    }
  }
  div.comments-tab {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    z-index: 11;
    width: 100%;
    height: 1.4133rem;
    background-color: #fff;
    border-top: 1px solid rgba(0, 0, 0, 0.1);
    display: flex;
    box-sizing: border-box;
    padding-left: 0.4rem;
    padding-right: 0.4rem;
    div.inp {
      flex: 1;
      padding-top: 0.2667rem;
      input {
        width: 100%;
        height: 0.5867rem;
        font-size: 0.3733rem;
        font-weight: 400;
        color: #333;
        line-height: 0.5867rem;
        box-sizing: border-box;
        margin-top: 0.2133rem;
        border: none;
      }
    }

    div.text {
      width: auto;
      height: 0.88rem;
      font-size: 0.3733rem;
      font-weight: 400;
      color: #666;
      line-height: 0.88rem;
      padding-left: 0.4rem;
      padding-top: 0.2667rem;
    }
  }
}
</style>
