import request from '@/sheep/request';
import { getSystemBaseUrl } from '@/sheep/config/server';
import { apiPath } from '@/sheep/config';

const PointApi = {
  // 获得用户积分记录分页
  getPointRecordPage: (params) => {
    if (params.addStatus === undefined) {
      delete params.addStatus
    }
    const queryString = Object.keys(params)
      .map((key) => encodeURIComponent(key) + '=' + params[key])
      .join('&');
    return request({
      url: getSystemBaseUrl() + apiPath + `/member/point/record/page?${queryString}`,
      method: 'GET',
    });
  }
};

export default PointApi;
