const MALL_BASE_URL = import.meta.env.VITE_MALL_SERVER

/** 数据对照 Response VO */
export interface DataComparisonRespVO<T> {
  value: T
  reference: T
}
