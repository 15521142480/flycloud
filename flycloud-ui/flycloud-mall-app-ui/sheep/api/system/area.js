import request from '@/sheep/request';
import { getSystemBaseUrl } from '@/sheep/config/server';

const AreaApi = {
  // 获得地区树
  getAreaTree: () => {
    return request({
      url: getSystemBaseUrl() + '/area/tree',
      method: 'GET',
    });
  },
};

export default AreaApi;
