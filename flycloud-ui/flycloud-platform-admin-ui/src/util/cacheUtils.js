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
  let userData = JSON.parse(localStorage.getItem(userDataKey))
  if (userData === undefined || userData === null) {
    userData = {}
  }
  return userData
}
export const getUserId = () => {
  return getUserData()[userIdKey]
}
export const getUserName = () => {
  return getUserData()[userNameKey]
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
  return localStorage.setItem(userPermissionKey, JSON.stringify(permission))
}

/**
 * 获取用户权限
 */
export const getPermission = () => {
  return JSON.parse(localStorage.getItem(userPermissionKey))
}

/**
 * 设置用户菜单
 * @param userMenuTree
 */
export const setUserMenu = (userMenuTree) => {
  return localStorage.setItem(userMenuTreeKey, JSON.stringify(userMenuTree))
}

/**
 * 获取用户菜单
 */
export const getUserMenu = () => {
  return JSON.parse(localStorage.getItem(userMenuTreeKey))
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
  localStorage.setItem('isFtpStatus', '0')
}

// =====================
/**
 * 是否有该按钮权限
 * @returns boolean
 */
export const hasPermission = (btnPermission) => {
  if (getPermission() === null || getPermission() === undefined) {
    return false
  }
  if (getPermission().indexOf(btnPermission) >= 0) {
    return true
  }
  return false
}
