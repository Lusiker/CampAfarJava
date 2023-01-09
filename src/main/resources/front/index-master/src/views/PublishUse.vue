<template>
  <div class="container">
    <div class="title-box">
      <img src="../assets/img/icon-back.png" alt="" @click="goBack" />
      <div class="text">发布文章</div>
    </div>
    <div class="box-body">
      <h2>选择文章Banner</h2>
      <van-divider/>
      <van-uploader v-model="bannerList" :max-count="1"></van-uploader>
    </div>

    <div class="box-body">
      <h2>设置标题</h2>
      <van-divider/>
      <van-cell-group>
        <van-field v-model="title" label="文本" placeholder="请输入文章标题" />
      </van-cell-group>
      <van-notice-bar
          :text="'标题不能为空且长度不大于50(当前' + title.length + ')'"
          v-if="title.length !== 0"
      />
    </div>

    <div class="box-body">
      <h2>付费信息</h2>
      <van-divider/>
      <van-cell-group>
        <van-cell :title="'是否免费'">
          <van-switch v-model="isFree" /> {{isFree? '免费' : '付费'}}
        </van-cell>
        <van-cell title="文章价格">
          <van-field type="number" v-model="price" :disabled="isFree" placeholder="请输入文章价格" />
        </van-cell>
      </van-cell-group>
      <van-notice-bar
          :text="'价格应介于0.00到99999.99之间'"
          v-if="price.length !== 0 && !priceAvailable"
      />
    </div>

    <div class="box-body">
      <h2>编辑正文</h2>
      <div>{{ content }}</div>
      <van-divider/>
      <van-notice-bar
          :text="'图片数量不应超过10张(当前' + imageCount + ')'"
          v-if="imageCount !== 0"
      />
      <vue-editor
        v-model="content"
      />
    </div>

    <div class="box-body">
      <van-divider/>
      <van-button
          type="info"
          block
          :disabled="user.userState !=='NORMAL'"
          @click="submitArticle">提交</van-button>
    </div>
  </div>
</template>

<script>
import { VueEditor } from "vue2-editor";
import { Notify, Toast } from 'vant';

export default {
  components: { VueEditor, Toast },
  data() {
    return {
      bannerList: [],
      content: "",
      title: "",
      imageCount: 0,
      loading: false,
      isFree: false,
      price: "0.00"
    }
  },
  methods:{
    submitArticle() {
      if(this.loading) {
        Toast.fail("文章上传中")
        return
      }

      this.loading = true

      if(!this.priceAvailable) {
        Toast.fail("价格不合法")
        this.loading = false
        return
      }

      if(!this.isFree){
        if(Number(this.price) === 0.00){
          Toast.fail("付费内容价格不能为0")
          this.loading = false
          return
        }
      }

      if(this.title.length === 0 || this.title.length > 50) {
        Toast.fail("标题不合法")
        this.loading = false
        return
      }

      if(this.imageCount > 10) {
        Toast.fail("图片数量过多")
        this.loading = false
        return
      }

      if(this.loginState) {
        this.$request.post("/article/create?uid=" + this.user.userId + "&title=" + this.title + "&tags=0&isFree=" + this.isFree + "&price=" + this.price, {body:this.content}).then(
          (res) => {
            switch (res.stateEnum.state) {
              case 0: {
                Toast.success("文章信息发布成功")
                let newAid = res.returnObject
                let formData = new FormData();
                formData.append("file", this.bannerList[0].file);
                this.$request.post('/upload/articleBanner?uid=' + this.user.userId + "&aid=" + newAid, formData)
                  .then(
                      (res) => {
                        switch (res.stateEnum.state) {
                          case 0: {
                            this.loading = false
                            Toast.success("文章发布成功")
                            this.$router.push("/article/" + newAid)
                            return
                          }
                          case -1: {
                            this.loading = false

                            if(res.specificCode === 1) {
                              Toast.fail("请求参数有误！")
                              return
                            } else if(res.specificCode === 2) {
                              Toast.fail("用户不存在！")
                              return
                            } else if (res.specificCode === 3) {
                              Toast.fail("文章不存在！")
                              return
                            } else {
                              Toast.fail("未知错误！")
                              return
                            }
                          }
                          case -2: {
                            this.loading = false
                            Toast.fail("拒绝访问！")
                            return
                          }
                          case 1: {
                            this.loading = false
                            Toast.fail("服务器出现错误！")
                            break
                          }
                          default: {
                            this.loading = false
                            Toast.fail("文章Banner发布失败")
                            return
                          }
                        }
                      }
                  )
                break
              }
              case -1: {
                this.loading = false

                if(res.specificCode === 1) {
                  Toast.fail("请求参数有误！")
                  return
                } else if(res.specificCode === 2) {
                  Toast.fail("用户不存在！")
                  return
                } else if (res.specificCode === 3) {
                  Toast.fail("无法进行此操作！")
                  return
                } else {
                  Toast.fail("未知错误！")
                  return
                }
              }
              case 1: {
                this.loading = false
                Toast.fail("服务器出现错误！")
                break
              }
              default: {
                this.loading = false
                break
              }
            }
          },
          () => {

          }
        )
      }
    },
    goBack: function () {
        this.$router.push('/home');
      },
  },
  watch: {
    isFree(newValue) {
      if(newValue) {
        this.price = "0.00"
      }
    },
  },
  beforeMount() {
  },
  mounted() {
    if(!this.loginState) {
      this.$store.dispatch('intercept','publish')
      this.$router.push('/login-password')
    }

    if(this.$store.getters.user.userState === "RESTRICTED") {
      Notify({
        message: '您尚未验证邮箱，无法发表文章',
        duration: 3500,
      })
    }
  },
  computed: {
    loginState() {
      return this.$store.getters.loginState
    },
    user() {
      return this.$store.getters.user
    },
    priceAvailable() {
      let strings = this.price.split('.')
      if(strings.length > 1 && strings[1].length > 2) {
        return false
      }

      let n = Number(this.price)
      return !(n < 0 || n > 99999.99);
    }
  },
};
</script>

<style lang="less" scoped>
  .container {
    position: absolute;
    left: 0;
    top: 0;
    overflow-y: scroll;
    overflow-x: hidden;
    width: 100%;
    height: 100%;
    box-sizing: border-box;
    padding-top: 50px;
    background: #fff;
    div.title-box {
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
  }
  div.box-body {
    padding: 0.15rem 0.4rem 0 0.4rem;
  }
  .navheader {
    position: fixed;
    top: 0;
    width: 100%;
    height: 50px;
    background-color: #fff;
    z-index: 100;
    padding-left: 15px;
    padding-right: 15px;
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
    border-bottom: 1px solid #f1f2f6;
  }

  .navheader .back {
    position: absolute;
    left: 15px;
    width: 19px;
    height: 19px;
  }

  .navheader .title {
    width: 72%;
    height: auto;
    font-size: 17px;
    font-weight: 500;
    text-align: center;
    color: #333;
    line-height: 22px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  .box {
    width: 100%;
    height: auto;
    float: left;
    box-sizing: border-box;
  }
  .btn-confirm {
    width: 100%;
    height: 48px;
    background-color: rgba(#3ca7fa, 0.6);
    border-radius: 4px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 16px;
    font-weight: 500;
    color: #ffffff;
    cursor: pointer;
    &.active {
      background: #3ca7fa;
    }
  }
</style>
