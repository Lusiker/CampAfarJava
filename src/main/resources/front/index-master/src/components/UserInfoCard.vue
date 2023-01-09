<template>
  <v-card
      class="author-info"
      width="100%"
      height="auto"
      tile
      border-radius="0px"
      v-if="!this.articleNotExist"
  >
    <v-overlay
        absolute
        :value="loading"
    >
      <van-loading/>
      用户信息读取中
    </v-overlay>
    <div v-show="!loading">
      <div v-if="loadFailed">
        <v-btn @click="getUserCardInfo">重新加载</v-btn>
      </div>
      <div v-else>
        <div class="card-title">
          <van-row clas="info-row">
            <van-col span="4">
              <v-avatar size="50">
                <img
                    alt="user"
                    :src="author.userAvatar"
                >
              </v-avatar>
            </van-col>
            <van-col>
                <span class="user-name">
                  {{author.userName}}
                </span>
              <div class="user-email">
                {{author.userEmail}}
              </div>
            </van-col>
            <v-btn
                :color="'#da5a22'"
                class="profile-btn"
                @click="toUserProfile"
            >
                <span class="btn-info">
                  用户资料
                </span>
            </v-btn>
          </van-row>
        </div>
        <v-divider/>
        <v-card-text>
          <div class="user-name">
            {{author.userIntroduction}}
          </div>
        </v-card-text>
      </div>
    </div>
  </v-card>
</template>

<script>
import { Toast } from 'vant';

export default {
  name: 'UserInfoCard',
  props: ['uid', 'articleNotExist'],
  data() {
    return {
      loading: true,
      loadFailed: false,
      author: {}
    }
  },
  methods: {
    toUserProfile() {
      this.$router.push('/user/' + this.author.userId)
    },
    getUserCardInfo() {
      this.loading = true
      this.$request.get("/user/cardInfo?uid=" + this.uid).then(
          (res) => {
            this.userLoaded = true
            switch (res.stateEnum.state) {
              case 0: {
                this.author = res.returnObject
                this.loading = false

                break
              }
              default: {
                Toast.fail("读取信息失败")
                this.loadUserFailed = true
                this.loading = false
              }
            }
          }
      )
    }
  },
  mounted() {
    this.getUserCardInfo()
  }
};
</script>

<style scoped>
.author-info {
  margin-top: 10px;
}
.profile-btn {
  position:absolute;
  top: 10px;
  right:10px
}
.card-title {
  height: 60px;
  width: 100%;
  padding-left: 10px;
  padding-top: 5px;
}
.user-name {
  font-size: 15px;
  padding-left: 10px;
}
.user-email {
  font-size: 12px;
  padding-left: 10px;
  font-weight: lighter;
}
.btn-info {
  font-size: 10px;
  color: white;
}
</style>
