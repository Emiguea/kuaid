const { get, put } = require('../../../utils/request');
const { orderStatusMap } = require('../../../utils/util');
const auth = require('../../../utils/auth');

Page({
  data: {
    orders: [],
    page: 1,
    hasMore: true,
    loading: false,
    userInfo: null,
    currentTab: 'all'
  },

  onShow() {
    const userInfo = auth.getUserInfo();
    this.setData({ userInfo, orders: [], page: 1, hasMore: true });
    this.loadData();
  },

  loadData() {
    if (this.data.loading || !this.data.hasMore) return;
    this.setData({ loading: true });

    const role = this.data.userInfo.role === 1 ? 'courier' : 'student';
    const params = { role, page: this.data.page, size: 10 };

    get('/orders', params).then(res => {
      const list = res.list.map(item => ({
        ...item,
        statusText: orderStatusMap[item.status]
      }));
      this.setData({
        orders: this.data.orders.concat(list),
        hasMore: this.data.page < res.totalPages,
        page: this.data.page + 1,
        loading: false
      });
    }).catch(() => {
      this.setData({ loading: false });
    });
  },

  onReachBottom() {
    this.loadData();
  },

  goToDetail(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({ url: `/pages/order/detail/detail?id=${id}` });
  }
});
