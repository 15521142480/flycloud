import request from '@/config/axios'

const GEN_BASE_URL = import.meta.env.VITE_GEN_SERVER


// 生成代码
export const handleGeneratorCode = (tables: string) => {
  return request.get({ url: `/${GEN_BASE_URL}/gen/generatorCode/` + tables })
}
