const app = getApp();

const login = () => {
  return new Promise((resolve, reject) => {
    wx.login({
      success(loginRes) {
        if (loginRes.code) {
          wx.request({
            url: app.globalData.baseUrl + '/auth/wx-login',
            method: 'POST',
            data: { code: loginRes.code },
            header: { 'Content-Type': 'application/json' },
            success(res) {
              if (res.data.code === 200) {
                const data = res.data.data;
                wx.setStorageSync('accessToken', data.accessToken);
                wx.setStorageSync('refreshToken', data.refreshToken);
                wx.setStorageSync('userInfo', {
                  userId: data.userId,
                  role: data.role,
                  nickname: data.nickname,
                  avatarUrl: data.avatarUrl
                });
                app.globalData.token = data.accessToken;
                app.globalData.userInfo = data;
                resolve(data);
              } else {
                wx.showToast({ title: res.data.message || '登录失败', icon: 'none' });
                reject(res.data);
              }
            },
            fail(err) {
              wx.showToast({ title: '网络异常', icon: 'none' });
              reject(err);
            }
          });
        } else {
          reject(new Error('wx.login failed'));
        }
      },
      fail(err) {
        reject(err);
      }
    });
  });
};

const checkLogin = () => {
  const token = wx.getStorageSync('accessToken');
  return !!token;
};

const getUserInfo = () => {
  return wx.getStorageSync('userInfo') || null;
};

const logout = () => {
  wx.removeStorageSync('accessToken');
  wx.removeStorageSync('refreshToken');
  wx.removeStorageSync('userInfo');
  app.globalData.token = null;
  app.globalData.userInfo = null;
};

module.exports = { login, checkLogin, getUserInfo, logout };
