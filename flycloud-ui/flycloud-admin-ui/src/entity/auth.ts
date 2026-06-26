

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
