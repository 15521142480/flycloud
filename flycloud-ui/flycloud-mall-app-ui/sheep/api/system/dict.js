import request from '@/sheep/request';
import { getSystemBaseUrl } from '@/sheep/config/server';

const DictApi = {
  // 根据字典类型查询字典数据信息
  getDictDataListByType: (type) => {
    return request({
      url: getSystemBaseUrl() + `/dictData/getList`,
      method: 'GET',
      params: {
        type,
      },
    });
  },
};

export default DictApi;
