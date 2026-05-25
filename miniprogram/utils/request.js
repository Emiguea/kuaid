const app = getApp();

const request = (options) => {
  return new Promise((resolve, reject) => {
    const token = wx.getStorageSync('accessToken');
    const header = {
      'Content-Type': 'application/json'
    };
    if (token) {
      header['Authorization'] = 'Bearer ' + token;
    }

    wx.request({
      url: app.globalData.baseUrl + options.url,
      method: options.method || 'GET',
      data: options.data || {},
      header: header,
      success(res) {
        if (res.statusCode === 401) {
          // Token expired, try refresh
          refreshToken().then(() => {
            // Retry request
            request(options).then(resolve).catch(reject);
          }).catch(() => {
            wx.removeStorageSync('accessToken');
            wx.removeStorageSync('refreshToken');
            wx.redirectTo({ url: '/pages/login/login' });
            reject(res.data);
          });
          return;
        }
        if (res.data.code === 200) {
          resolve(res.data.data);
        } else {
          wx.showToast({ title: res.data.message || '请求失败', icon: 'none' });
          reject(res.data);
        }
      },
      fail(err) {
        wx.showToast({ title: '网络异常', icon: 'none' });
        reject(err);
      }
    });
  });
};

const refreshToken = () => {
  return new Promise((resolve, reject) => {
    const refreshTokenStr = wx.getStorageSync('refreshToken');
    if (!refreshTokenStr) {
      reject();
      return;
    }

    wx.request({
      url: app.globalData.baseUrl + '/auth/refresh',
      method: 'POST',
      data: { refreshToken: refreshTokenStr },
      header: { 'Content-Type': 'application/json' },
      success(res) {
        if (res.data.code === 200) {
          wx.setStorageSync('accessToken', res.data.data.accessToken);
          app.globalData.token = res.data.data.accessToken;
          resolve();
        } else {
          reject();
        }
      },
      fail() {
        reject();
      }
    });
  });
};

const get = (url, data) => request({ url, method: 'GET', data });
const post = (url, data) => request({ url, method: 'POST', data });
const put = (url, data) => request({ url, method: 'PUT', data });

module.exports = { request, get, post, put };
