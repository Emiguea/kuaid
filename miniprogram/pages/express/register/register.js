const { post, get } = require('../../../utils/request');

Page({
  data: {
    trackingNo: '',
    company: '',
    recipientPhone: '',
    recipientName: '',
    shelfNo: '',
    remark: '',
    stationId: null,
    stations: [],
    stationIndex: 0
  },

  onLoad() {
    get('/stations').then(res => {
      this.setData({ stations: res });
      if (res.length > 0) {
        this.setData({ stationId: res[0].id });
      }
    });
  },

  onInput(e) {
    const field = e.currentTarget.dataset.field;
    this.setData({ [field]: e.detail.value });
  },

  onStationChange(e) {
    const index = e.detail.value;
    this.setData({
      stationIndex: index,
      stationId: this.data.stations[index].id
    });
  },

  handleSubmit() {
    if (!this.data.trackingNo || !this.data.recipientPhone || !this.data.stationId) {
      wx.showToast({ title: '请填写必要信息', icon: 'none' });
      return;
    }

    wx.showLoading({ title: '入库中...' });
    post('/express', {
      trackingNo: this.data.trackingNo,
      company: this.data.company,
      stationId: this.data.stationId,
      recipientPhone: this.data.recipientPhone,
      recipientName: this.data.recipientName,
      shelfNo: this.data.shelfNo,
      remark: this.data.remark
    }).then(res => {
      wx.hideLoading();
      wx.showModal({
        title: '入库成功',
        content: `取件码: ${res.pickupCode}`,
        showCancel: false,
        success() {
          wx.navigateBack();
        }
      });
    }).catch(() => {
      wx.hideLoading();
    });
  }
});
