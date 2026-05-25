const { get, post, put } = require('../../../utils/request');

Page({
  data: {
    isEdit: false,
    stationId: null,
    name: '',
    address: '',
    contactPhone: ''
  },

  onLoad(options) {
    if (options.id) {
      this.setData({ isEdit: true, stationId: options.id });
      this.loadStation(options.id);
    }
  },

  loadStation(id) {
    get(`/stations/${id}`).then(res => {
      this.setData({
        name: res.name,
        address: res.address,
        contactPhone: res.contactPhone || ''
      });
    });
  },

  onInput(e) {
    this.setData({ [e.currentTarget.dataset.field]: e.detail.value });
  },

  handleSubmit() {
    if (!this.data.name || !this.data.address) {
      wx.showToast({ title: '请填写必要信息', icon: 'none' });
      return;
    }

    const data = {
      name: this.data.name,
      address: this.data.address,
      contactPhone: this.data.contactPhone
    };

    const request = this.data.isEdit
      ? put(`/stations/${this.data.stationId}`, data)
      : post('/stations', data);

    request.then(() => {
      wx.showToast({ title: this.data.isEdit ? '更新成功' : '创建成功', icon: 'success' });
      setTimeout(() => wx.navigateBack(), 1500);
    });
  }
});
