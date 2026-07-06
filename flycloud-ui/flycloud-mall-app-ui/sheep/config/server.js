import { baseUrl } from '@/sheep/config';

/**
 * 后端服务，默认使用mall，即/flycloud-mall
 */
const SERVER_PREFIX = {
  mall: '/flycloud-mall',
  auth: '/flycloud-auth',
  system: '/flycloud-system',
};

/**
 * 获取指定服务网关地址
 */
export function getServerBaseUrl(server = 'mall') {
  const prefix = SERVER_PREFIX[server];

  if (!prefix) {
    throw new Error(`未知的服务类型：${server}`);
  }

  return `${baseUrl.replace(/\/$/, '')}${prefix}`;
}

/**
 * 商城服务
 */
export function getMallBaseUrl() {
  return getServerBaseUrl('mall');
}

/**
 * 认证服务
 */
export function getAuthBaseUrl() {
  return getServerBaseUrl('auth');
}

/**
 * 系统服务
 */
export function getSystemBaseUrl() {
  return getServerBaseUrl('system');
}
