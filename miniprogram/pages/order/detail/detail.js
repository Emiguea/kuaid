const { get, put } = require('../../../utils/request');
const { orderStatusMap, formatTime } = require('../../../utils/util');
const auth = require('../../../utils/auth');

Page({
  data: {
    order: null,
    userInfo: null
  },

  onLoad(options) {
    this.orderId = options.id;
    this.setData({ userInfo: auth.getUserInfo() });
    this.loadDetail();
  },

  loadDetail() {
    get(`/orders/${this.orderId}`).then(res => {
      res.statusText = orderStatusMap[res.status];
      res.createdAtText = formatTime(res.createdAt);
      res.completedAtText = formatTime(res.completedAt);
      this.setData({ order: res });
    });
  },

  acceptOrder() {
    put(`/orders/${this.orderId}/accept`).then(() => {
      wx.showToast({ title: '接单成功', icon: 'success' });
      this.loadDetail();
    });
  },

  completeOrder() {
    put(`/orders/${this.orderId}/complete`).then(() => {
      wx.showToast({ title: '已完成', icon: 'success' });
      this.loadDetail();
    });
  },

  cancelOrder() {
    wx.showModal({
      title: '取消订单',
      content: '确定取消此订单吗？费用将退回余额。',
      success: (res) => {
        if (res.confirm) {
          put(`/orders/${this.orderId}/cancel`, { reason: '用户主动取消' }).then(() => {
            wx.showToast({ title: '已取消', icon: 'success' });
            this.loadDetail();
          });
        }
      }
    });
  }
});
