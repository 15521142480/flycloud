import { baseUrl } from '@/sheep/config';

/**
 * 标准化网关服务前缀。
 */
function normalizePrefix(prefix) {
  return `/${String(prefix || '').replace(/^\/+|\/+$/g, '')}`;
}

/**
 * 转义正则表达式特殊字符。
 */
function escapeRegExp(value = '') {
  return value.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
}

const SERVER_PREFIX = {
  mall: normalizePrefix(import.meta.env.SHOPRO_MALL_SERVER_PREFIX),
  auth: normalizePrefix(import.meta.env.SHOPRO_AUTH_SERVER_PREFIX),
  system: normalizePrefix(import.meta.env.SHOPRO_SYSTEM_SERVER),
};

/**
 * 获取网关根地址；如果 baseUrl 已经带商城服务前缀，需要先去掉。
 */
export function getGatewayBaseUrl() {
  const mallPrefix = SERVER_PREFIX.mall;
  return baseUrl
    .replace(new RegExp(`${escapeRegExp(mallPrefix)}/?$`), '')
    .replace(/\/$/, '');
}

/**
 * 获取指定服务网关地址
 */
export function getServerBaseUrl(server = 'mall') {
  const prefix = SERVER_PREFIX[server];

  if (!prefix) {
    throw new Error(`未知的服务类型：${server}`);
  }

  return `${getGatewayBaseUrl()}${prefix}`;
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

/**
 * 获取系统服务 WebSocket 地址。
 */
export function getSystemWebSocketUrl(token) {
  const url = `${getSystemBaseUrl()}/ws`.replace(/^http/, 'ws');
  return token ? `${url}?token=${encodeURIComponent(token)}` : url;
}
