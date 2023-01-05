// 对axios 进行配置
// 1. 配置基本url
// 2. 配置拦截
// 3. 其它处理
import axios from "axios";
import { Toast } from "vant";
let instance = axios.create({
  baseURL: "http://1.14.239.98",
  timeout: 10000,
});
// 请求拦截器
// axios 请求拦截器
instance.interceptors.request.use(
  function (config) {
    Toast.loading({
      message: "加载中...",
      forbidClick: true,
      duration: 0,
    });
    let token = localStorage.getItem("h5-token");
    if (token) {
      config.headers = {
        Authorization: `Bearer ${token}`,
      };
    }
    return config;
  },
  function (error) {
    Toast.clear();
    return Promise.reject(error);
  }
);
// 响应拦截器
instance.interceptors.response.use(
  function (response) {
    Toast.clear();
    // 才是后台真正返回的数据
    if (response.data.code != 0) {
      if (response.data.message == "错误") {
        // Toast.fail(response.data.message);
      } else {
        Toast.fail(response.data.message);
      }
    }
    return response.data;
  },
  function (error) {
    Toast.clear();
    return Promise.reject(error);
  }
);

export default instance;
