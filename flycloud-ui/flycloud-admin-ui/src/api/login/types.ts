export type UserLoginVO = {
  username: string
  password: string
  grant_type: string
  scope: string
  // 字母数组验证
  code: string
  codeKey: string
  // 图文点选验证码‌
  captchaCode: string
  socialType?: string
  socialCode?: string
  socialState?: string
}

export type TokenType = {
  id: string // 编号
  accessToken: string // 访问令牌
  refreshToken: string // 刷新令牌
  userId: string // 用户编号
  userType: number //用户类型
  clientId: string //客户端编号
  expiresTime: number //过期时间
}

export type UserVO = {
  id: string
  username: string
  name: string
  deptId: string
  email: string
  mobile: string
  sex: number
  avatar: string
  loginIp: string
  loginDate: string
}

export type RegisterVO = {
  name: string
  username: string
  password: string
}
