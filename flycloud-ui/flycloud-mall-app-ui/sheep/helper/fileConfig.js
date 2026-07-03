const FILE_CONFIG_STORAGE_KEY = 'file-config';

/**
 * 缓存后端文件配置。
 */
export function setFileConfig(config = {}) {
  uni.setStorageSync(FILE_CONFIG_STORAGE_KEY, config || {});
}

/**
 * 读取后端文件配置。
 */
export function getFileConfig() {
  return uni.getStorageSync(FILE_CONFIG_STORAGE_KEY) || {};
}

/**
 * 获取缓存中的文件访问基础地址。
 */
export function getFileBaseUrl() {
  return String(getFileConfig()?.baseUrl || '').replace(/\/+$/, '');
}
