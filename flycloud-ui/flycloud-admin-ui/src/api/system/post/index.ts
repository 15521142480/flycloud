import request from '@/config/axios'

const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER

export interface PostVO {
  id?: number
  name: string
  code: string
  sort: number
  status: number
  remark: string
  createTime?: Date
}

// 查询岗位列表
export const getPostPage = async (params: PageParam) => {
  return await request.get({ url: `/${SYS_BASE_URL}/post/list`, params })
}

// 获取岗位精简信息列表
export const getSimplePostList = async (): Promise<PostVO[]> => {
  return await request.get({ url: `/${SYS_BASE_URL}/post/getAllList`})
}

// 查询岗位详情
export const getPost = async (id: number) => {
  return await request.get({ url:`/${SYS_BASE_URL}/post/get/` + id })
}

// 新增/修改岗位
export const saveOrUpdate = async (data: PostVO) => {
  return await request.post({ url: `/${SYS_BASE_URL}/post/saveOrUpdate`, data })
}

// 新增岗位
// export const createPost = async (data: PostVO) => {
//   return await request.post({ url: `/${SYS_BASE_URL}/post/create`, data })
// }
//
// // 修改岗位
// export const updatePost = async (data: PostVO) => {
//   return await request.put({ url: `/${SYS_BASE_URL}/post/update`, data })
// }

// 删除岗位
export const deletePost = async (id: number) => {
  return await request.delete({ url: `/${SYS_BASE_URL}/post/delete` + id })
}

// 导出岗位
export const exportPost = async (params) => {
  return await request.download({ url: `/${SYS_BASE_URL}/post/export`, params })
}
