import request from '@/config/axios'
const BPM_BASE_URL = import.meta.env.VITE_BPM_SERVER


export const updateBpmSimpleModel = async (data) => {
  return await request.post({
    url: `/${BPM_BASE_URL}/model/simple/update`,
    data: data
  })
}

export const getBpmSimpleModel = async (id) => {
  return await request.get({
    url: `/${BPM_BASE_URL}/model/simple/get/` + id
  })
}
