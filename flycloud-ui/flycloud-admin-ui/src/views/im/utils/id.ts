/** 把后端长整型 ID 按字符串语义归一，避免 JS number 精度丢失。 */
export function normalizeId(id?: string | number | null): string {
  return id == null || id === '' ? '0' : String(id)
}

/** 比较两个字符串 ID 的大小，适用于雪花 ID 等纯数字字符串。 */
export function compareId(left?: string | number | null, right?: string | number | null): number {
  const a = normalizeId(left).replace(/^0+/, '') || '0'
  const b = normalizeId(right).replace(/^0+/, '') || '0'
  if (a.length !== b.length) {
    return a.length - b.length
  }
  return a.localeCompare(b)
}

/** 取两个 ID 里较大的一个。 */
export function maxId(left?: string | number | null, right?: string | number | null): string {
  return compareId(left, right) >= 0 ? normalizeId(left) : normalizeId(right)
}

/** 判断 ID 是否大于 0。 */
export function isPositiveId(id?: string | number | null): boolean {
  return compareId(id, '0') > 0
}
