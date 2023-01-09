<template>
  <div>
    <v-card width="100%" height="135px" class="article-card" @click="toArticle">
      <van-row>
        <van-col>
          <div class="article-banner">
            <van-image
                :src="bannerSrc"
                width="130px"
                height="120px"
                fit="fill"
            ></van-image>
          </div>
        </van-col>
        <van-col class="article-info">
          <h2>标题：{{reducedTitle(item.articleTitle)}}</h2>
          <h5>发布于：{{getDateString(item.articleCreatedAt)}}</h5>
          <span>浏览量：{{item.articleViewCount}}</span>
          <div v-if="item.articleIsFree">
            <strong style="color: grey;"><span style="font-size: 16px">免费</span></strong>
          </div>
          <div v-else>
            <span style="font-size: 16px">￥{{item.articlePrice}}</span>
          </div>
        </van-col>
      </van-row>
    </v-card>
  </div>
</template>

<script>
export default {
  name: 'Article',
  props: ['item', 'getDateString'],
  data() {
    return {
      bannerSrc: '',
    }
  },
  methods: {
    reducedTitle(title) {
      if(title.length > 6){
        let reduced = title.substring(0, 5)
        return reduced + '...'
      }

      return title
    },
    toArticle() {
      this.$router.push('/article/' + this.item.articleId)
    }
  },
  mounted() {
    this.$request.get("/acquire/articleBanner?aid=" + this.item.articleId)
        .then(
            (res) => {
              let src;
              switch (res.stateEnum.state) {
                case 0: {
                  if (res.returnObject === null) {
                    src = this.$store.getters.defaultBannerSrc
                  } else {
                    src = res.returnObject
                  }

                  break
                }
                default: {
                  src = this.$store.getters.defaultBannerSrc
                }
              }

              this.bannerSrc = src
            }
        )
  }
};
</script>

<style scoped>
.article-card {
  margin-top: 10px;
}
.article-banner {
  position: relative;
  text-align: center;
  top: 8px;
  left: 5px;
  border-radius: 10px;
}
.article-info {
  position: relative;
  top: 8px;
  left: 15px;
}
</style>
