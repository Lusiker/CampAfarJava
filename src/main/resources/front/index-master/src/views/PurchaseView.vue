<template>
    <div class="container">
        <div class="nav-box">
            <img class="title-img" src="../assets/img/icon-back.png" alt="" @click="goToHome()" />
            <div class="title">商品详情</div>
        </div>

        <div class="blank">
           
        </div>

        <div class="blank"> </div>

        <div class="shopimg">
            <div class="order"><img src="../assets/img/钱.png" alt="" class="image"></div>
            <div class="txt">订单</div>
        </div>

        <div class="blankgrey"></div>

        <div class iteminfo>
            <div class="item">
                <div class="item-comp">
                    <div class="img">
                        <img :src="this.imgsrc" alt="" />
                    </div>
                    <div class="text">
                        <div class="text-title">{{"标题:"+ this.title }}</div>
                    <div class="text-info">
                        <div class="text-info-left">{{ "作者" + this.author }}</div>
                        <div class="text-info-right" v-if="this.charge > 0">
                            <span class="charge"> ￥ </span>{{ this.charge }}
                        </div>
                        <div class="free" v-else>
              <div style="color">免费</div>
            </div>
          </div>
        </div>
      </div>
      <div class="blankgrey"></div>
      <div class="init-time">
            {{ "创建时间：" + this.inittime}}
        </div>
        <hr>
        <div class="blank"> </div>
        <div class="blankgrey"></div>
        <div class="blankgrey"></div>
        
        <div class="cost">
            <img src="../assets/img/钱.png" alt="" class="costimg">
            {{ "订单金额:    ￥" + this.charge}}
        </div>
        <hr>
        <div class="cost">
            <img src="../assets/img/钱包.png" alt="" class="costimg">
            {{ "拥有金额:    ￥" + this.possess}}
        </div>
        <div class="box-body">
      <van-button @click="goToArticle()" class="btn2">支付</van-button>
    </div>
    </div>
    </div>
    </div>
</template>


<script>
import { Toast } from 'vant';
export default{

    data(){
        return{
            title:'我apex拉中之拉',
            inittime:'2023.01.07',
            charge:'12',
            author:'老流域',
            imgsrc: require('../assets/img/2.jpg'),
            possess:999,
        }
    },
    methods:{
        goToHome: function () {
            this.$router.push({
                path: "/",
            });
        },
        goToArticle: function () {
            // this.$router.push({
            //     path: "/",
            // });
        },
    }
}
</script>

<style lang = less scope>


.title-img {
    position: absolute;
    left: 0.2rem;
    width: 0.5067rem;
    height: 0.5067rem;
    top: 0.4267rem;
  }
  .title {
    width: 100%;
    height: auto;
    font-size: 0.6rem;
    font-weight: 500;
    text-align: center;
    color: #333;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  .blankgrey{
    height: 10px;
    background-color: #f3f6f9;
  }
  .btn2{
    color: #ffffff;
    background-color: #1989fa;
  }
  .blank {
    height: 45px;
    background-color: #ffffff;
  }
  .init-time {
    height: 40px;
    font-size: 18px;
    background-color: #ffffff;
  }
  .shopimg{
    display: flex;
    font-size: 20px;
    height: 45px;
  }
  .txt{
    vertical-align: middle;
    display: table-cell;
  }
  .box-body {
    float: right;
    background-color: #ffffff;
    padding: 0.6667rem 0.4rem 0.1333rem 0.4rem;
  }
  .order{
    height: 40px;
    width: 40px;
  }
  .image{
    height: 35px;
    width: 35px;
  }
  .cost{
    background-color: #ffffff;
    font-size: 20px;
  }
  .costimg{
    height: 40px;
    width: 40px;
    vertical-align: middle;
    background-color: #ffffff;
  }
  .item {
    background-color: #f3f6f9;
    margin-bottom: 0.4rem;
    
  .item-comp {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    .img {
      
      width: 3.2rem;
      height: 2.4rem;
      margin-right: 0.2667rem;
      img {
        width: 3.2rem;
        height: 2.4rem;
        border-radius: 0.1067rem;
      }
    }

    .text {
      flex: 1;
      padding-top: 0.1333rem;
      .text-title {
        width: 100%;
        height: 1.0667rem;
        font-size: 0.4rem;
        font-weight: 500;
        color: #171923;
        line-height: 0.5333rem;
        margin-bottom: 0.4rem;
      }

      .text-info {
        width: 100%;
        height: auto;
        display: flex;
        align-items: center;
        .text-info-left {
          flex: 1;
          text-align: left;
          font-size: 0.32rem;
          font-weight: 400;
          color: #999;
          line-height: 0.32rem;
        }

        .text-info-right {
          flex: 1;
          text-align: right;
          font-size: 0.48rem;
          line-height: 0.48rem;
          font-weight: 600;
          color: #ff5858;
          .charge {
            font-size: 0.3733rem;
            margin-right: -0.1333rem;
          }
        }
        .free {
          flex: 1;
          text-align: right;
          font-size: 0.3733rem;
          line-height: 0.48rem;
          font-weight: 600;
          color: #52c41a;
        }
      }
    }
  }
}
</style>