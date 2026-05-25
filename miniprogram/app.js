App({
  globalData: {
    userInfo: null,
    token: null,
    baseUrl: 'http://localhost:8080/kuaid-express/api/v1'
  },

  onLaunch() {
    const token = wx.getStorageSync('accessToken');
    if (token) {
      this.globalData.token = token;
    }
  }
});
