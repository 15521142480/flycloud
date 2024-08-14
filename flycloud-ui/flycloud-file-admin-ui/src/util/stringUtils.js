/**
 * 判断是null 返回true 否则返回false
 * @returns {boolean}
 */
export const isNull = (str) => {
  return str === null || str === '' || str === undefined
}

export const isNotNull = (str) => {
  return !(str === null || str === '' || str === undefined)
}
