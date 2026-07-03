import request from '@/sheep/request';
import { getSystemBaseUrl } from '@/sheep/config/server';
import { apiPath } from '@/sheep/config';

const PayTransferApi = {
  // 同步转账单
  syncTransfer: (id) => {
    return request({
      url: getSystemBaseUrl() + apiPath + '/pay/transfer/sync',
      method: 'GET',
      params: { id },
    });
  },
};

export default PayTransferApi;
