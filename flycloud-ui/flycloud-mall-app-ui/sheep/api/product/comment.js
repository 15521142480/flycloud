import request from '@/sheep/request';

const CommentApi = {
  // 获得商品评价分页
  getCommentPage: (spuId, pageNum, pageSize, type) => {
    return request({
      url: '/product/comment/page',
      method: 'GET',
      params: {
        spuId,
        pageNum,
        pageSize,
        type,
      },
      custom: {
        showLoading: false,
        showError: false,
      },
    });
  },
};
export default CommentApi;
