const { get, put } = require('../../utils/request');
const { formatTime } = require('../../utils/util');

Page({
  data: {
    notifications: [],
    page: 1,
    hasMore: true,
    loading: false
  },

  onShow() {
    this.setData({ notifications: [], page: 1, hasMore: true });
    this.loadData();
  },

  loadData() {
    if (this.data.loading || !this.data.hasMore) return;
    this.setData({ loading: true });

    get('/notifications', { page: this.data.page, size: 20 }).then(res => {
      const list = res.list.map(item => ({
        ...item,
        createdAtText: formatTime(item.createdAt)
      }));
      this.setData({
        notifications: this.data.notifications.concat(list),
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

  markRead(e) {
    const id = e.currentTarget.dataset.id;
    put(`/notifications/${id}/read`).then(() => {
      const notifications = this.data.notifications.map(item =>
        item.id === id ? { ...item, isRead: 1 } : item
      );
      this.setData({ notifications });
    });
  },

  markAllRead() {
    put('/notifications/read-all').then(() => {
      const notifications = this.data.notifications.map(item => ({ ...item, isRead: 1 }));
      this.setData({ notifications });
      wx.showToast({ title: '全部已读', icon: 'success' });
    });
  }
});
