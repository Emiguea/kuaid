const { get } = require('../../utils/request');
const auth = require('../../utils/auth');

Page({
  data: {
    userInfo: null,
    pendingCount: 0,
    unreadCount: 0,
    recentExpress: []
  },

  onShow() {
    if (!auth.checkLogin()) {
      wx.redirectTo({ url: '/pages/login/login' });
      return;
    }
    this.setData({ userInfo: auth.getUserInfo() });
    this.loadData();
  },

  loadData() {
    get('/notifications/unread-count').then(res => {
      this.setData({ unreadCount: res.count });
    });
  },

  goToExpress() {
    wx.switchTab({ url: '/pages/express/list/list' });
  },

  goToOrders() {
    wx.switchTab({ url: '/pages/order/list/list' });
  },

  goToNotification() {
    wx.navigateTo({ url: '/pages/notification/notification' });
  },

  goToBalance() {
    wx.navigateTo({ url: '/pages/balance/index/index' });
  }
});
