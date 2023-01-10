<template>
  <div>
    <v-card width="100%" height="150px" :ripple="false" class="purchase-card">
      <van-row>
        <van-col class="purchase-info">
          <h3>{{purchaseType}}</h3>
          <h4>创建于：{{getDateString(item.purchaseCreatedAt)}}</h4>
          <h4>完成于：{{purchaseFinished}}</h4>
          <span style="font-size: 14px">价格￥{{item.purchaseValue}}</span>
          <h4>当前状态:{{purchaseStateString}}</h4>
        </van-col>
      </van-row>
      <van-row>
        <van-col span="12">
        </van-col>
        <van-col span="12">
          <van-row style="justify-content: space-around">
            <van-button plain :color="'#b71515'" :disabled="item.purchaseState !== 'CREATED'" @click="cancel">
              取消订单
            </van-button>
            <van-button type="info" :disabled="item.purchaseState !== 'CREATED'" @click="purchase">
              进行支付
            </van-button>
          </van-row>
        </van-col>
      </van-row>
    </v-card>

  </div>
</template>

<script>

import { Dialog, Toast } from 'vant';

export default {
  name: 'PurchaseCard',
  props: ['item', 'getDateString'],
  data() {
    return {
      showPurchaseDialog: false,
      showCancelDialog: false
    }
  },
  computed: {
    loginState() {
      return this.$store.getters.loginState
    },
    user() {
      return this.$store.getters.user
    },
    purchaseType() {
      switch (this.item.targetType) {
        case "ARTICLE": return "购买文章-id:" + this.item.targetId
        case "QUESTION": return "向用户id" + this.item.targetId + "提问"
        case "LISTEN": return "旁听问题-id:" + this.item.targetId
        default: return "位置订单"
      }
    },
    purchaseFinished() {
      if(this.item.purchaseState === 'FINISHED') {
        return this.getDateString(this.item.purchaseFinishedAt)
      } else {
        return "尚未支付"
      }
    },
    purchaseStateString() {
      switch (this.item.purchaseState) {
        case "CREATED": return "待支付"
        case "CANCELLED": return "已取消"
        case "TIMEOUT": return "已超时"
        case "FINISHED": return "已完成"
        default: return "未知状态"
      }
    }
  },
  methods: {
    purchase() {
      if(this.loginState) {
        Dialog.confirm({
          title: '进行支付',
          message: '订单价格为' + this.item.purchaseValue + ',你当前的余额为' + this.user.userPoint + ",是否进行支付?",
        })
        .then(() => {
          this.$request.post('/purchase/finish?uid=' + this.user.userId + '&pid=' + this.item.purchaseId).then(
            (res) => {
              switch (res.stateEnum.state) {
                case 0: {
                  Toast.success("支付成功")
                  this.item.purchaseState = "FINISHED"
                  this.item.purchaseFinishedAt = new Date()
                  this.$store.dispatch('setNewPoint', res.returnObject)

                  switch (this.item.targetType) {
                    case "ARTICLE": {
                      this.$router.push('/article/' + this.item.targetId)
                    }
                  }

                  return
                }
                case -1: {
                  if(res.specificCode === 5) {
                    Toast.fail("订单已超时，无法支付")
                    this.item.purchaseState = "TIMEOUT"
                    return
                  } else if(res.specificCode === 4) {
                    Toast.fail("余额不足")

                    return
                  }

                  Toast.fail("购买失败")
                  return
                }
                default: {
                  Toast.fail("购买失败")
                  return
                }
              }
            }
          )
        })
        .catch(() => {
          //
        });
      }
    },
    cancel() {
      if(this.loginState) {
        Dialog.confirm({
          title: '取消订单',
          message: '你确定要取消订单吗？',
        })
            .then(() => {
              this.$request.post('/purchase/cancel?uid=' + this.user.userId + '&pid=' + this.item.purchaseId).then(
                  (res) => {
                    switch (res.stateEnum.state) {
                      case 0: {
                        Toast.success("取消成功")
                        this.item.purchaseState = "CANCELLED"

                        switch (this.item.targetType) {
                          case "ARTICLE": {
                            this.$router.push('/article/' + this.item.targetId)
                          }
                        }
                        return
                      }
                      case -1: {
                        if(res.specificCode === 4) {
                          Toast.fail("订单已超时，无法取消")
                          this.item.purchaseState = "TIMEOUT"
                          return
                        }

                        Toast.fail("取消失败")
                        return
                      }
                      default: {
                        Toast.fail("取消失败")
                        return
                      }
                    }
                  }
              )
            })
            .catch(() => {
              //
            });
      }
    },
  }
};
</script>

<style scoped>
.purchase-card {
  margin-top: 10px;
}
.purchase-info {
  position: relative;
  top: 8px;
  left: 15px;
}
</style>
