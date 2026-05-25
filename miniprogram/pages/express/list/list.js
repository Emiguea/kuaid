const { get } = require('../../../utils/request');
const { expressStatusMap } = require('../../../utils/util');
const auth = require('../../../utils/auth');

Page({
  data: {
    expressList: [],
    page: 1,
    hasMore: true,
    loading: false,
    userInfo: null
  },

  onShow() {
    const userInfo = auth.getUserInfo();
    this.setData({ userInfo, expressList: [], page: 1, hasMore: true });
    this.loadData();
  },

  loadData() {
    if (this.data.loading || !this.data.hasMore) return;
    this.setData({ loading: true });

    get('/express/my', { page: this.data.page, size: 10 }).then(res => {
      const list = res.list.map(item => ({
        ...item,
        statusText: expressStatusMap[item.status]
      }));
      this.setData({
        expressList: this.data.expressList.concat(list),
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
    wx.navigateTo({ url: `/pages/express/detail/detail?id=${id}` });
  },

  goToRegister() {
    wx.navigateTo({ url: '/pages/express/register/register' });
  }
});
