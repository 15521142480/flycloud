import request from '@/sheep/request';
import { getSystemBaseUrl } from '@/sheep/config/server';
import { apiPath } from '@/sheep/config';

const PayChannelApi = {
  // 获得指定应用的开启的支付渠道编码列表
  getEnableChannelCodeList: (appId) => {
    return request({
      url: getSystemBaseUrl() + apiPath + '/pay/channel/get-enable-code-list',
      method: 'GET',
      params: { appId },
    });
  },
};

export default PayChannelApi;
