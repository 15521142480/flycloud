

export interface ClickCaptchaChallengeVo {
  captchaId: string
  imageBase64: string
  targetText: string
  expiresInSeconds: number
}

export interface ClickCaptchaVerifyDto {
  captchaId: string
  points: ClickCaptchaPointDto[]
}

export interface ClickCaptchaPointDto {
  x: number
  y: number
}

export interface TextCaptchaDataVo {
  imageTextClickCaptchaKey: string, // 图文点选验证码‌_key
  imageTextClickCaptchaValue: string, // 图文点选验证码‌_坐标信息
  imageTextClickCaptchaSuccessValue: string // 图文点选验证码‌_成功的值
}
