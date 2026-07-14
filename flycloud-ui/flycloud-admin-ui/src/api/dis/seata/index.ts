import request from '@/config/axios'

const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER

/** Seata 测试数据。 */
export interface SeataTestDataVO {
  id: number
  databaseName: string
  testData: string
  createTime: Date | string
}

/** 两个数据源的 Seata 测试数据。 */
export interface SeataTestDataListVO {
  dataSourceOne: SeataTestDataVO[]
  dataSourceTwo: SeataTestDataVO[]
}

/** 执行 Seata 分布式事务测试。 */
export const seataTestApi = (
  isRollback: number,
  data: { dataSourceOneTestData: string; dataSourceTwoTestData: string }
) => {
  return request.post({ url: `/${SYS_BASE_URL}/test/seata/seataTest/${isRollback}`, data })
}

/** 查询两个数据源最新的十条 Seata 测试数据。 */
export const getSeataTestDataList = () => {
  return request.get<SeataTestDataListVO>({ url: `/${SYS_BASE_URL}/test/seata/testData/list` })
}
