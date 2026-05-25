const { get } = require('../../../utils/request');
const { formatTime } = require('../../../utils/util');

Page({
  data: {
    balance: '0.00',
    records: [],
    page: 1,
    hasMore: true
  },

  onShow() {
    this.setData({ records: [], page: 1, hasMore: true });
    this.loadBalance();
    this.loadRecords();
  },

  loadBalance() {
    get('/balance').then(res => {
      this.setData({ balance: res.balance });
    });
  },

  loadRecords() {
    get('/balance/records', { page: this.data.page, size: 20 }).then(res => {
      const list = res.list.map(item => ({
        ...item,
        createdAtText: formatTime(item.createdAt),
        amountText: (item.amount >= 0 ? '+' : '') + item.amount
      }));
      this.setData({
        records: this.data.records.concat(list),
        hasMore: this.data.page < res.totalPages,
        page: this.data.page + 1
      });
    });
  },

  onReachBottom() {
    if (this.data.hasMore) this.loadRecords();
  },

  goToRecharge() {
    wx.navigateTo({ url: '/pages/balance/recharge/recharge' });
  }
});
