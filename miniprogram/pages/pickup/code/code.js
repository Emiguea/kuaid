const { get } = require('../../../utils/request');

Page({
  data: {
    express: null
  },

  onLoad(options) {
    if (options.id) {
      get(`/express/${options.id}`).then(res => {
        this.setData({ express: res });
      });
    }
  }
});
