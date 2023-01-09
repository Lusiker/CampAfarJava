<template>
  <v-container style="background-color: #f3f6f9">
    <v-list style="background-color: #f3f6f9">
      <div v-if="totalItems > 0">
        <van-pagination
            v-model="currentPage"
            :total-items="totalItems"
            :items-per-page="pageCapacity"
            @change="getPage"
        />
        <div v-if="!loading">
            <InfoCard
                v-for="elem in articles[currentPage]"
                :item="elem"
                :key="elem.articleId"
                :get-date-string="getDateString"
            />
          </div>
        <van-pagination v-model="currentPage" :total-items="totalItems" :items-per-page="pageCapacity" />
      </div>
      <div class="no-article" v-else>
        <span style="font-size: 15px"><strong>暂无文章...</strong></span>
      </div>
    </v-list>
  </v-container>
</template>

<script>
import { Toast } from 'vant';
import InfoCard from '@/components/InfoCard.vue';

export default {
  name: 'ArticleList',
  components: { InfoCard },
  props: ['uid'],
  data() {
    return {
      currentPage: 1,
      totalItems: 0,
      pageCapacity: 3,
      loading: true,
      articles: {},
    }
  },
  methods: {
    getPage() {
      this.loading = true
      let currentUid = this.loginState ? this.$store.getters.user.userId : 0
      let targetUid = this.$route.params.uid === undefined ? this.uid : this.$route.params.uid
      this.$request.get('/user/getArticles?uidFrom=' + currentUid + "&uidTo=" + targetUid + "&page=" + this.currentPage)
          .then(
              (res) => {
                switch (res.stateEnum.state) {
                  case 0: {
                    this.articles[this.currentPage] = res.returnObject.records
                    this.loading = false
                    break
                  }
                  default: {
                    Toast.fail("获取文章失败（页码：" + this.currentPage + "）")
                    break
                  }
                }
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
  mounted() {
    this.loading = true
    let currentUid = this.loginState ? this.$store.getters.user.userId : 0
    let targetUid = this.$route.params.uid === undefined ? this.uid : this.$route.params.uid
    this.$request.get('/user/userArticleCount?uid=' + targetUid).then(
        (res) => {
          switch (res.stateEnum.state) {
            case 0: {
              //获取第一页
              this.totalItems = res.returnObject
              if(this.totalItems !== 0) {
                this.$request.get('/user/getArticles?uidFrom=' + currentUid + "&uidTo=" + targetUid + "&page=" + this.currentPage)
                    .then(
                        (res) => {
                          switch (res.stateEnum.state) {
                            case 0: {
                              this.articles[1] = res.returnObject.records
                              this.loading = false

                              break
                            }
                            default: {
                              Toast.fail("获取文章失败（页码：1）")
                              break
                            }
                          }
                        }
                    )
              }
              break
            }
            default: {
              this.totalItems = 0
            }
          }
        }
    )
  }
};
</script>

<style scoped>
  .no-article {
    text-align: center;
    width: 100%;
    background-color: #f3f6f9;
  }
</style>
