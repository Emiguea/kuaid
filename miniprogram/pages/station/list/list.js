const { get } = require('../../../utils/request');

Page({
  data: {
    stations: []
  },

  onShow() {
    get('/stations').then(res => {
      this.setData({ stations: res });
    });
  },

  goToManage(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({ url: `/pages/station/manage/manage?id=${id}` });
  }
});
