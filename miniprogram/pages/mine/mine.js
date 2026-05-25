const { get, put } = require('../../utils/request');
const auth = require('../../utils/auth');

Page({
  data: {
    userInfo: null,
    balance: '0.00'
  },

  onShow() {
    if (!auth.checkLogin()) {
      wx.redirectTo({ url: '/pages/login/login' });
      return;
    }
    this.loadProfile();
  },

  loadProfile() {
    get('/user/profile').then(res => {
      this.setData({
        userInfo: res,
        balance: res.balance || '0.00'
      });
    });
  },

  goToBalance() {
    wx.navigateTo({ url: '/pages/balance/index/index' });
  },

  goToNotification() {
    wx.navigateTo({ url: '/pages/notification/notification' });
  },

  goToStationManage() {
    wx.navigateTo({ url: '/pages/station/list/list' });
  },

  applyForCourier() {
    wx.showModal({
      title: '申请成为快递员',
      content: '确认申请成为快递员？申请后可以进行快递入库和接单操作。',
      success: (res) => {
        if (res.confirm) {
          put('/user/role').then(() => {
            wx.showToast({ title: '申请成功', icon: 'success' });
            this.loadProfile();
          });
        }
      }
    });
  },

  handleLogout() {
    wx.showModal({
      title: '确认退出',
      content: '确定退出登录吗？',
      success: (res) => {
        if (res.confirm) {
          auth.logout();
          wx.redirectTo({ url: '/pages/login/login' });
        }
      }
    });
  }
});
