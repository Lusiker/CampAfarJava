<!--  -->
<template>
  <div class="container">
    <div class="nav-box">
      <img src="../assets/img/icon-back.png" alt="" @click="goToDetail" />
      <div class="text">{{ mains.video.title }}</div>
    </div>
    <div class="video-box">
      <video
        :src="seeVideo.url"
        poster="http://1.14.239.98/storage/images/t69BQ0UPY3oByfUAKPGKiOkEZNmSfTjCgfqRmiCW.png"
        controls
      ></video>
    </div>
    <div class="body">
      <van-tabs v-model="active">
        <van-tab title="目录">
          <div class="bottom-box">
            <div class="item" v-for="item in mains.chapters" :key="item.id">
              <div class="title">
                {{ item.title }}
                <img src="../assets/img/icon-back.png" alt="" @click.stop="show" />
              </div>
              <div class="text">
                <div
                  class="item"
                  v-for="i in mains.videos[item.id]"
                  :key="i.chapter_id"
                  v-show="isshow"
                >
                  <div class="video-title">
                    <span class="free">试看</span>
                    <span class="text-class">{{ i.title }}</span>
                  </div>
                  <div class="video-img">
                    <img src="../assets/img/play.gif" alt="" />
                  </div>
                </div>
              </div>
            </div>
          </div>
        </van-tab>
        <van-tab title="评论">
          <div
            class="comments-box"
            v-if="commentsList.comments"
            v-for="item in commentsList.comments"
            :key="item.id"
          >
            <div class="comment-item">
              <div class="avatar">
                <img :src="users[1].avatar" alt="" />
              </div>
              <div class="content">
                <div class="name">{{ users[1].nick_name }}</div>
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
          <div class="bottom-tab">
            <div class="inp">
              <input type="text" placeholder="请输入评论内容" v-model="text" />
            </div>
            <div class="bottom-btn" @click="announce">发布</div>
          </div>
        </van-tab>
      </van-tabs>
    </div>
  </div>
</template>

<script>
import axios from "axios";
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
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
      mains: {
        video: {},
      },
      seeVideo: {},
      active: 0,
      commentsList: {},
      users: {},
      text: "",
      token: [],
      isshow: true,
    };
  },
  //监听属性 类似于data概念
  computed: {},
  //监控data中的数据变化
  watch: {},
  //方法集合
  methods: {
    goToDetail: function () {
      this.$router.push({
        path: "/vod/detail",
        query: {
          id: this.mains.course.id,
        },
      });
    },
    show: function () {
      let trensform = document.querySelector(".title img");
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
        .post(`/api/v2/video/${this.$route.query.id}/comment`, {
          content: this.text,
        })
        .then(() => {
          location.reload();
        });
      this.text = "";
    },
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  async created() {
    let res = await axios.get(`http://1.14.239.98/api/v2/video/${this.$route.query.id}`);
    this.mains = res.data.data;
    let videos = await this.$request.get(`/api/v2/video/${this.$route.query.id}/playinfo`);
    this.seeVideo = videos.data.urls[0];
    let comments = await this.$request.get(`/api/v2/video/${this.$route.query.id}/comments`);
    this.commentsList = comments.data;
    this.users = comments.data.users;
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
<style lang="less" scoped>
div.container {
  box-sizing: border-box;
  padding-top: 50px;
  height: 100vh;
  overflow-y: scroll;
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
    align-items: center;
    border-bottom: 1px solid #f1f2f6;
    img {
      position: absolute;
      left: 0.4rem;
      width: 0.5067rem;
      height: 0.5067rem;
    }

    div.text {
      width: 72%;
      height: auto;
      font-size: 0.4533rem;
      font-weight: 500;
      text-align: center;
      color: #333;
      line-height: 0.5867rem;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  }
  div.video-box {
    position: relative;
    font-size: 0;
    width: 100%;
    background-color: #000;
    height: auto;
    video {
      width: 100%;
      height: 6.6667rem;
    }
  }
  div.body {
    width: 100%;
    height: auto;
    float: left;
    box-sizing: border-box;
    padding-bottom: 53px;
    div.bottom-box {
      padding: 0.2667rem 0.4rem 0.4rem 0.4rem;
      div.item {
        width: 100%;
        height: auto;
        div.title {
          width: 100%;
          height: auto;
          display: flex;
          align-items: center;
          justify-content: space-between;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
          font-size: 0.4rem;
          color: #333;
          font-weight: 700;
          margin-bottom: 0.5333rem;
          margin-top: 0.2667rem;
          img {
            float: right;
            cursor: pointer;
            transform: rotate(270deg);
            width: 0.4rem;
            height: 0.4rem;
          }
        }

        div.text {
          display: block;
          width: 100%;
          height: auto;
          float: left;
          div.item {
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
            border-radius: 4px;
            div.video-title {
              flex: 1;
              white-space: nowrap;
              overflow: hidden;
              text-overflow: ellipsis;
              font-size: 0.3733rem;
              color: #333;

              span.free {
                padding: 0.0533rem 0.1067rem;
                background-color: #04c877;
                border-radius: 2px;
                color: #fff;
                font-size: 0.32rem;
                margin-right: 0.2667rem;
              }

              span.text-class {
                color: #3ca7fa;
              }
            }

            div.video-img {
              width: auto;
              height: auto;
              font-size: 0.32rem;
              margin-left: 0.2667rem;
              color: #999;
              img {
                margin-top: -0.3467rem;
                width: 0.64rem;
                height: 0.64rem;
              }
            }
          }
        }
      }
    }
    div.comments-box {
      width: 100%;
      height: auto;
      float: left;
      color: #333;
      box-sizing: border-box;
      padding: 0.5333rem 0.4rem 0.4rem 0.4rem;
      div.comment-item {
        width: 100%;
        height: auto;
        margin-top: 0;
        display: flex;
        div.avatar {
          width: 0.8rem;
          height: 0.8rem;
          margin-right: 0.2667rem;
          img {
            width: 32px;
            border-radius: 50%;
            height: 32px;
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

      div.bottom-btn {
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
}
</style>
