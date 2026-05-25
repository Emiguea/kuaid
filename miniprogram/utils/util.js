const formatTime = (date) => {
  if (!date) return '';
  if (typeof date === 'string') date = new Date(date);
  const y = date.getFullYear();
  const m = (date.getMonth() + 1).toString().padStart(2, '0');
  const d = date.getDate().toString().padStart(2, '0');
  const h = date.getHours().toString().padStart(2, '0');
  const min = date.getMinutes().toString().padStart(2, '0');
  return `${y}-${m}-${d} ${h}:${min}`;
};

const expressStatusMap = {
  0: '待取件',
  1: '已取件',
  2: '已过期',
  3: '已退回'
};

const orderStatusMap = {
  0: '待接单',
  1: '已接单',
  2: '配送中',
  3: '已完成',
  4: '已取消'
};

module.exports = { formatTime, expressStatusMap, orderStatusMap };
