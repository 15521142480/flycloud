import { baseUrl } from '@/sheep/config';

const AUTH_SERVER_PREFIX = import.meta.env.AUTH_SERVER_PREFIX;
const MALL_SERVER_PREFIX = import.meta.env.MALL_SERVER_PREFIX;
const SYSTEM_SERVER_PREFIX = import.meta.env.SYSTEM_SERVER_PREFIX;

function normalizePrefix(prefix) {
  return `/${String(prefix || '').replace(/^\/+|\/+$/g, '')}`;
}

function escapeRegExp(value) {
  return value.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
}

function replaceServerPrefix(targetPrefix) {
  if (!baseUrl) {
    return '';
  }
  const mallPrefix = normalizePrefix(MALL_SERVER_PREFIX);
  const serverPrefix = normalizePrefix(targetPrefix);
  return baseUrl
    .replace(new RegExp(`${escapeRegExp(mallPrefix)}/?$`), serverPrefix)
    .replace(/\/$/, '');
}


// 获取认证服务网关地址，供授权、验证码等认证服务接口复用
export function getAuthBaseUrl() {
  return replaceServerPrefix(AUTH_SERVER_PREFIX);
}


// 系统服务
export function getSystemBaseUrl() {
  return replaceServerPrefix(SYSTEM_SERVER_PREFIX);
}
