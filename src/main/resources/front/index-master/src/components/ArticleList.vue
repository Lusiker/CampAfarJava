<template>
  <v-container>
    <v-list>
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
      this.$request.get('/user/getArticles?uidFrom=' + currentUid + "&uidTo=" + this.$route.params.uid + "&page=" + this.currentPage)
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
      let result = t.getFullYear() + "年" + t.getMonth() +"月" + t.getDate() + "日  " + t.getHours() + ":"
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
    this.$request.get('/user/userArticleCount?uid=' + this.$route.params.uid).then(
        (res) => {
          switch (res.stateEnum.state) {
            case 0: {
              //获取第一页
              this.totalItems = res.returnObject
              this.$request.get('/user/getArticles?uidFrom=' + currentUid + "&uidTo=" + this.$route.params.uid + "&page=" + this.currentPage)
                  .then(
                      (res) => {
                        switch(res.stateEnum.state) {
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

</style>
