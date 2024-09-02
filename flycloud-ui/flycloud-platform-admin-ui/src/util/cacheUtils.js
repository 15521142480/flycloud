// 缓存key
export const userDataKey = 'userData'
export const userTokenKey = 'userToken'
export const userPermissionKey = 'permissionList'
export const userMenuTreeKey = 'menuTreeList'

// 字段key
export const userIdKey = 'id'
export const userNameKey = 'account'

/**
 * 设置用户信息
 * @param userData
 */
export const setUserData = (userData) => {
  return localStorage.setItem(userDataKey, JSON.stringify(userData))
}

/**
 * 获取用户信息
 */
export const getUserData = () => {
  return JSON.parse(localStorage.getItem(userDataKey))
}
export const getUserId = () => {
  return JSON.parse(localStorage.getItem(userDataKey))[userIdKey]
}
export const getUserName = () => {
  return JSON.parse(localStorage.getItem(userDataKey))[userNameKey]
}

/**
 * 设置用户token
 * @param userToken
 */
export const setUserToken = (userToken) => {
  return localStorage.setItem(userTokenKey, userToken)
}

/**
 * 获取用户token
 */
export const getUserToken = () => {
  return localStorage.getItem(userTokenKey)
}

/**
 * 设置用户权限
 * @param permission
 */
export const setPermission = (permission) => {
  return localStorage.setItem(userPermissionKey, permission)
}

/**
 * 获取用户权限
 */
export const getPermission = () => {
  return localStorage.getItem(userPermissionKey)
}

/**
 * 设置用户菜单
 * @param userMenuTree
 */
export const setUserMenu = (userMenuTree) => {
  return localStorage.setItem(userMenuTreeKey, userMenuTree)
}

/**
 * 获取用户菜单
 */
export const getUserMenu = () => {
  return localStorage.getItem(userMenuTreeKey)
}

// =====================
/**
 * 移除用户相关缓存
 */
export const removeUserCache = () => {
  localStorage.removeItem(userDataKey)
  localStorage.removeItem(userTokenKey)
  localStorage.removeItem(userPermissionKey)
  localStorage.removeItem(userMenuTreeKey)
}
