// 对axios 进行配置
// 1. 配置基本url
// 2. 配置拦截
// 3. 其它处理
import axios from "axios";
import { Toast } from "vant";
let instance = axios.create({
  baseURL: "http://localhost:8080",
  timeout: 10000,
})
// 响应拦截器
instance.interceptors.response.use(
  function (response) {
    return response.data;
  }
);

export default instance;
