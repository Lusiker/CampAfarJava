<!--  -->
<template>
  <div class="container1">
    <div class="title-box">
      <img src="../assets/img/icon-back.png" alt="" @click="toBack" />
      <div class="text">我的订单</div>
    </div>
    <div class="body">
      <v-list>
        <div v-if="loaded">
          <PurchaseCard v-for="item in purchases" :item="item" :key="item.purchaseId" :get-date-string="getDateString" />
        </div>
      </v-list>
    </div>
  </div>
</template>

<script>
import { Toast } from 'vant';
import PurchaseCard from '@/components/PurchaseCard.vue';

export default {
  components: { PurchaseCard },
  data() {
    //这里存放数据
    return {
      loaded: false,
      purchases: [],
    };
  },
  methods: {
    toBack: function () {
      this.$router.push({
        path: "/member",
      });
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
    if(!this.$store.getters.loginState) {
      this.$store.dispatch('intercept','order')
      this.$router.push('/login-password')
    } else {
      this.loaded = false
      this.$request.get('/purchases?uid=' + this.$store.getters.user.userId).then(
          (res) => {
            if(res.stateEnum.state === 0) {
              this.purchases = res.returnObject
              this.loaded = true
            } else {
              Toast.fail("获取订单失败")
            }
          }
      )
    }
  }
};
</script>

<style lang="less" scoped>
div.container1 {
  box-sizing: border-box;
  padding-top: 50px;
  background: #f6f6f6;
  height: 100vh;
  overflow-y: scroll;
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
  div.body {
    div.order-item {
      width: 100%;
      height: auto;
      overflow-y: scroll;
      float: left;
      box-sizing: border-box;
      padding: 0.4rem;
      background-color: #fff;
      border-bottom: 1px solid rgba(0, 0, 0, 0.05);
      div.title {
        width: 100%;
        height: auto;
        float: left;
        display: flex;
        padding-bottom: 0.4rem;
        div.time {
          flex: 1;
          font-size: 0.2667rem;
          font-weight: 400;
          color: #999;
          line-height: 0.2667rem;
        }

        div.status {
          flex: 1;
          text-align: right;
          font-size: 0.3467rem;
          font-weight: 400;
          color: #999;
          line-height: 0.3467rem;
        }
      }

      div.bottom-box {
        width: 100%;
        height: auto;
        float: left;
        display: flex;
        div.left {
          flex: 1;
          div.name {
            width: 100%;
            height: auto;
            float: left;
            font-size: 0.4rem;
            font-weight: 400;
            color: #333;
            line-height: 0.4rem;
            padding-bottom: 0.4rem;
          }

          div.payment {
            width: 100%;
            height: auto;
            float: left;
            font-size: 0.2667rem;
            font-weight: 400;
            color: #999;
            line-height: 0.2667rem;
          }
        }

        div.right {
          width: 3.2rem;
          height: auto;
          text-align: right;
          font-size: 0.5333rem;
          font-weight: 400;
          color: #333;
          line-height: 0.5333rem;
          padding-top: 0.2667rem;
          span {
            font-size: 0.2667rem;
          }
        }
      }
    }
  }
}
</style>
