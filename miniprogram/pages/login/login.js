const auth = require('../../utils/auth');

Page({
  data: {},

  onLoad() {
    if (auth.checkLogin()) {
      wx.switchTab({ url: '/pages/index/index' });
    }
  },

  handleLogin() {
    wx.showLoading({ title: '登录中...' });
    auth.login().then(() => {
      wx.hideLoading();
      wx.switchTab({ url: '/pages/index/index' });
    }).catch(() => {
      wx.hideLoading();
    });
  }
});
