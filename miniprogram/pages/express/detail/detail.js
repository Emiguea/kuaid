const { get, put } = require('../../../utils/request');
const { expressStatusMap, formatTime } = require('../../../utils/util');

Page({
  data: {
    express: null,
    pickupCode: ''
  },

  onLoad(options) {
    this.expressId = options.id;
    this.loadDetail();
  },

  loadDetail() {
    get(`/express/${this.expressId}`).then(res => {
      res.statusText = expressStatusMap[res.status];
      res.registeredAtText = formatTime(res.registeredAt);
      res.pickedAtText = formatTime(res.pickedAt);
      this.setData({ express: res });
    });
  },

  onPickupCodeInput(e) {
    this.setData({ pickupCode: e.detail.value });
  },

  confirmPickup() {
    if (!this.data.pickupCode) {
      wx.showToast({ title: '请输入取件码', icon: 'none' });
      return;
    }
    put(`/express/${this.expressId}/pickup`, { pickupCode: this.data.pickupCode }).then(() => {
      wx.showToast({ title: '取件成功', icon: 'success' });
      this.loadDetail();
    });
  }
});
