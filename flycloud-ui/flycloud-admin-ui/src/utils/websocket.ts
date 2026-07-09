/**
 * 构建系统服务 WebSocket 地址。
 */
export const buildSystemWebSocketUrl = (token?: string): string => {
  const baseUrl = import.meta.env.VITE_BASE_URL || `${window.location.protocol}//${window.location.host}`
  const systemServer = import.meta.env.VITE_SYSTEM_SERVER
  const wsUrl = `${baseUrl.replace(/\/$/, '')}/${systemServer.replace(/^\/+|\/+$/g, '')}/ws`.replace(
    /^http/,
    'ws'
  )
  return token ? `${wsUrl}?token=${encodeURIComponent(token)}` : wsUrl
}
