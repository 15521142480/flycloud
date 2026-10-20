const assert = require('node:assert/strict')
const fs = require('node:fs')
const path = require('node:path')
const test = require('node:test')
const vm = require('node:vm')
const ts = require('typescript')
const vue = require('vue')
const { parse } = require('vue/compiler-sfc')

/** 创建可手动完成的模拟请求，验证刷新和关闭弹窗时的异步顺序。 */
const deferred = () => {
  let resolve
  const promise = new Promise((done) => {
    resolve = done
  })
  return { promise, resolve }
}

/** 加载真实验证码组件脚本，仅替换图片尺寸和远程响应，不连接后端或识别真实验证码。 */
const createHarness = (options = {}) => {
  const filename = path.resolve(
    __dirname,
    '../src/views/Login/components/ImageTextClickCaptcha.vue'
  )
  const { descriptor } = parse(fs.readFileSync(filename, 'utf8'))
  const source =
    descriptor.scriptSetup.content +
    `
    globalThis.component = { openImageTextClickCaptchaProp, loadCaptcha, checkCaptcha,
      handleCaptchaClick, handleCaptchaClosed, captchaImageRef, captchaLoading,
      captchaDialogVisible, captchaChallenge, clickPoints };
  `
  const compiled = ts.transpileModule(source, {
    compilerOptions: {
      module: ts.ModuleKind.CommonJS,
      target: ts.ScriptTarget.ES2022,
      esModuleInterop: true
    }
  }).outputText
  const props = {
    textCaptchaData: {
      imageTextClickCaptchaKey: '',
      imageTextClickCaptchaValue: '',
      captchaCode: ''
    }
  }
  const calls = { load: 0, verify: [], success: [], errors: [] }
  const api = {
    getImageTextClickCaptchaApi: async () => {
      calls.load++
      if (options.load) return options.load(calls.load)
      return {
        captchaId: 'fixture-' + calls.load,
        targetText: '测试题',
        imageBase64: 'fixture-image'
      }
    },
    checkImageTextClickCaptchaApi: async (data) => {
      calls.verify.push(data)
      if (options.verify) return options.verify(data)
      return { captchaVerification: 'verified-fixture' }
    }
  }
  const dependencies = {
    '@/hooks/web/useI18n': { useI18n: () => ({ t: (key) => key }) },
    '@element-plus/icons-vue': { Refresh: {} },
    '@/api/login': api
  }
  const context = {
    ...vue,
    console,
    defineProps: () => props,
    defineExpose: () => {},
    defineEmits: () => (event, payload) => {
      if (event === 'update:textCaptchaData') props.textCaptchaData = payload
      if (event === 'success') calls.success.push(payload)
    },
    ElMessage: { error: (message) => calls.errors.push(message), success: () => {} }
  }
  vm.createContext(context)
  const factory = vm.runInContext(
    '(function(require, module, exports){' + compiled + '\n})',
    context
  )
  const module = { exports: {} }
  factory((name) => dependencies[name] || require(name), module, module.exports)
  context.component.captchaImageRef.value = {
    naturalWidth: 360,
    naturalHeight: 240,
    getBoundingClientRect: () => ({ left: 0, top: 0, width: 360, height: 240 })
  }
  return { ...context.component, calls, props }
}

/** 触发三个测试图片坐标，走实际点击收集与校验调用链。 */
const clickPoints = async (harness) => {
  await harness.handleCaptchaClick({ clientX: 60, clientY: 60 })
  await harness.handleCaptchaClick({ clientX: 150, clientY: 120 })
  await harness.handleCaptchaClick({ clientX: 270, clientY: 180 })
}

test('校验通过后发出同步好的登录凭证并关闭弹窗', async () => {
  const h = createHarness()
  await h.openImageTextClickCaptchaProp()
  await clickPoints(h)
  assert.equal(h.calls.verify.length, 1)
  assert.equal(h.calls.verify[0].captchaId, 'fixture-1')
  assert.equal(h.calls.success.length, 1)
  assert.equal(h.calls.success[0].captchaCode, 'verified-fixture')
  assert.equal(h.props.textCaptchaData.captchaCode, 'verified-fixture')
  assert.equal(h.captchaDialogVisible.value, false)
  assert.equal(h.captchaLoading.value, false)
})

test('题目过期或校验失败后自动换题，恢复可点击状态', async () => {
  const h = createHarness({
    verify: async () => {
      throw new Error('验证码已过期')
    }
  })
  await h.openImageTextClickCaptchaProp()
  await clickPoints(h)
  assert.equal(h.calls.load, 2)
  assert.equal(h.captchaChallenge.value.captchaId, 'fixture-2')
  assert.equal(h.clickPoints.value.length, 0)
  assert.equal(h.captchaLoading.value, false)
  assert.equal(h.calls.success.length, 0)
  assert.equal(h.calls.errors.length, 0)
})

test('刷新等待期间隐藏旧题目并忽略点击，新题目加载后使用新编号校验', async () => {
  const pending = deferred()
  const h = createHarness({
    load: (count) => (count === 1 ? { captchaId: 'old', targetText: '旧题目' } : pending.promise)
  })
  await h.openImageTextClickCaptchaProp()
  const refreshing = h.loadCaptcha()
  await h.handleCaptchaClick({ clientX: 60, clientY: 60 })
  assert.equal(h.captchaChallenge.value, undefined)
  assert.equal(h.clickPoints.value.length, 0)
  pending.resolve({ captchaId: 'new', targetText: '新题目' })
  await refreshing
  await clickPoints(h)
  assert.equal(h.calls.verify[0].captchaId, 'new')
})

test('连续刷新合并为一次加载请求', async () => {
  const pending = deferred()
  const h = createHarness({ load: () => pending.promise })
  const opening = h.openImageTextClickCaptchaProp()
  await h.loadCaptcha()
  assert.equal(h.calls.load, 1)
  pending.resolve({ captchaId: 'fixture', targetText: '测试题' })
  await opening
  assert.equal(h.captchaLoading.value, false)
})

test('校验进行期间不重复提交同一道题', async () => {
  const pending = deferred()
  const h = createHarness({ verify: () => pending.promise })
  await h.openImageTextClickCaptchaProp()
  const clicking = clickPoints(h)
  await new Promise(setImmediate)
  await h.checkCaptcha()
  assert.equal(h.calls.verify.length, 1)
  pending.resolve({ captchaVerification: 'verified-fixture' })
  await clicking
  assert.equal(h.calls.success.length, 1)
})

test('关闭弹窗后到达的校验成功响应不再触发自动登录', async () => {
  const pending = deferred()
  const h = createHarness({ verify: () => pending.promise })
  await h.openImageTextClickCaptchaProp()
  const clicking = clickPoints(h)
  await new Promise(setImmediate)
  h.captchaDialogVisible.value = false
  h.handleCaptchaClosed()
  pending.resolve({ captchaVerification: 'verified-fixture' })
  await clicking
  assert.equal(h.calls.success.length, 0)
  assert.equal(h.captchaLoading.value, false)
})

test('响应缺少登录凭证时不伪造校验成功，自动刷新题目', async () => {
  const h = createHarness({ verify: async () => ({}) })
  await h.openImageTextClickCaptchaProp()
  await clickPoints(h)
  assert.equal(h.calls.success.length, 0)
  assert.equal(h.calls.load, 2)
  assert.equal(h.calls.errors.length, 1)
})

test('题目加载失败保留失败状态，用户可刷新重试', async () => {
  const h = createHarness({
    load: async (count) => {
      if (count === 1) throw new Error('模拟加载失败')
      return { captchaId: 'recovered', targetText: '测试题' }
    }
  })
  await h.openImageTextClickCaptchaProp()
  assert.equal(h.captchaChallenge.value, undefined)
  assert.equal(h.captchaLoading.value, false)
  await h.loadCaptcha()
  assert.equal(h.captchaChallenge.value.captchaId, 'recovered')
})

test('关闭弹窗后到达的题目响应不再恢复旧题目', async () => {
  const pending = deferred()
  const h = createHarness({ load: () => pending.promise })
  const opening = h.openImageTextClickCaptchaProp()
  h.captchaDialogVisible.value = false
  h.handleCaptchaClosed()
  pending.resolve({ captchaId: 'cancelled', targetText: '测试题' })
  await opening
  assert.equal(h.captchaChallenge.value, undefined)
  assert.equal(h.props.textCaptchaData.imageTextClickCaptchaKey, '')
})
