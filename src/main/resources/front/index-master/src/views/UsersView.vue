<!--  -->
<template>
  <div class="container">
    <div class="title-box">
      <img src="../assets/img/icon-back.png" alt="" @click="toBack" />
      <div class="text">个人中心</div>
    </div>
    <div class="user-avatar">
      <div class="value">
        <img :src="avatarSrc" alt="" />
        <input type="file" accept="image/*" class="input-avatar" @change="changeAvatar($event)" />
      </div>
      <div class="text">点击更换头像</div>
    </div>
    <div class="form-box">
      <div class="tit">账户信息</div>
      <div class="group-item">
        <div class="name">昵称</div>
        <div class="value">
          <span>{{ user.userName }}</span
          ><img src="../assets/img/icon-prev.png" class="arrow" />
        </div>
      </div>
      <div class="group-item">
        <div class="name">更换邮箱</div>
        <div class="value">
          <span>{{ user.userEmail }}</span
          ><img src="../assets/img/icon-prev.png" class="arrow" />
        </div>
      </div>
      <div class="group-item">
        <div class="name">激活邮箱</div>
        <div class="value">
          <span>当前状态:{{ user.userHasActivated ? '已激活' : '未激活' }}</span
          ><img src="../assets/img/icon-prev.png" class="arrow" />
        </div>
      </div>
      <div class="group-item">
        <div class="name">修改密码</div>
        <div class="value">
          <span class="un">设置密码</span><img src="../assets/img/icon-prev.png" class="arrow" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';

export default {
  //import引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    //这里存放数据
    return {
      file: "",
    };
  },
  //监听属性 类似于data概念
  computed: {
    loginState() {
      return this.$store.getters.loginState
    },
    user() {
      return this.$store.getters.user
    },
    avatarSrc() {
      return this.$store.getters.avatar
    }
  },
  //监控data中的数据变化
  watch: {},
  //方法集合
  methods: {
    toBack: function () {
      this.$router.push({
        path: "/member",
      });
    },
    changeAvatar: function (event) {
      if(this.loginState) {
        this.file = event.target.files[0];
        let formData = new FormData();
        formData.append("file", this.file);
        this.$request.post("/upload/avatar?uid=" + this.user.userId, formData)
            .then(() => {
              location.reload();
            });
      }
    },
  },
  //生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {
    if(this.loginState) {
      if (!this.$store.getters.avatarHasCached) {
        this.$request.get('/acquire/avatar?uid=' + this.user.userId)
            .then(
                (res) => {
                  switch (res.stateEnum.state) {
                    case 0: {
                      this.$store.dispatch('cacheAvatar', res.returnObject)
                      this.avatarLoading = false

                      break
                    }
                    default: {
                      this.avatarLoading = false

                      return
                    }
                  }
                }
            )
      } else {
        this.avatarLoading = false
      }
    }
  },
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
  div.user-avatar {
    width: 100%;
    height: auto;
    float: left;
    box-sizing: border-box;
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-top: 0.8rem;
    margin-bottom: 1.3333rem;
    div.value {
      width: 1.7067rem;
      height: 1.7067rem;
      float: left;
      position: relative;
      img {
        width: 1.7067rem;
        height: 1.7067rem;
        border-radius: 50%;
      }

      input.input-avatar {
        position: absolute;
        top: 0;
        left: 0;
        opacity: 0;
        width: 1.7067rem;
        height: 1.7067rem;
        border-radius: 50%;
      }
    }

    div.text {
      width: auto;
      height: 0.3467rem;
      font-size: 0.3467rem;
      font-weight: 400;
      color: #666;
      line-height: 0.3467rem;
      margin-top: 0.4rem;
    }
  }
  div.form-box {
    width: 100%;
    height: auto;
    float: left;
    margin-bottom: 1.3333rem;
    background-color: #fff;
    div.tit {
      width: 100%;
      height: auto;
      float: left;
      box-sizing: border-box;
      padding-left: 0.5333rem;
      font-size: 0.4267rem;
      font-weight: 600;
      color: #333;
      line-height: 0.4267rem;
      margin-bottom: 0.2667rem;
    }

    div.group-item {
      width: 100%;
      height: 1.4933rem;
      float: left;
      display: flex;
      box-sizing: border-box;
      padding: 0.2133rem 0.5333rem;
      border-bottom: 1px solid #f3f6f9;
      div.name {
        width: 4rem;
        height: 1.0667rem;
        float: left;
        line-height: 1.0667rem;
        font-size: 0.4267rem;
        color: #333;
      }

      div.value {
        flex: 1;
        height: 1.0667rem;
        text-align: right;
        font-size: 0.3733rem;
        color: #666;
        display: flex;
        align-items: center;
        justify-content: flex-end;
        span {
          text-align: right;
          font-size: 0.3733rem;
          color: #666;
        }

        img.arrow {
          display: inline-block;
          width: 0.32rem;
          height: 0.32rem;
          margin-left: 0.4rem;
        }
      }
    }
  }
}
</style>
