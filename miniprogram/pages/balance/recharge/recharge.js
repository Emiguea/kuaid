const { post } = require('../../../utils/request');

Page({
  data: {
    amount: '',
    presetAmounts: ['10', '20', '50', '100']
  },

  selectAmount(e) {
    this.setData({ amount: e.currentTarget.dataset.amount });
  },

  onAmountInput(e) {
    this.setData({ amount: e.detail.value });
  },

  handleRecharge() {
    const amount = parseFloat(this.data.amount);
    if (!amount || amount <= 0) {
      wx.showToast({ title: '请输入有效金额', icon: 'none' });
      return;
    }

    wx.showLoading({ title: '充值中...' });
    post('/balance/recharge', { amount }).then(() => {
      wx.hideLoading();
      wx.showToast({ title: '充值成功', icon: 'success' });
      setTimeout(() => wx.navigateBack(), 1500);
    }).catch(() => {
      wx.hideLoading();
    });
  }
});
