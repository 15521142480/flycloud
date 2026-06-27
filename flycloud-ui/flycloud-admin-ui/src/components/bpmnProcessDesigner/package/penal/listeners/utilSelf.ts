import { useI18n } from '@/hooks/web/useI18n'
const { t } = useI18n()

export function initListenerForm(listener) {
  let self = {
    ...listener
  }
  if (listener.script) {
    self = {
      ...listener,
      ...listener.script,
      scriptType: listener.script.resource ? 'externalScript' : 'inlineScript'
    }
  }
  if (listener.event === 'timeout' && listener.eventDefinitions) {
    if (listener.eventDefinitions.length) {
      let k = ''
      for (const key in listener.eventDefinitions[0]) {
        console.log(listener.eventDefinitions, key)
        if (key.indexOf('time') !== -1) {
          k = key
          self.eventDefinitionType = key.replace('time', '').toLowerCase()
        }
      }
      console.log(k)
      self.eventTimeDefinitions = listener.eventDefinitions[0][k].body
    }
  }
  return self
}

export function initListenerType(listener) {
  let listenerType
  if (listener.class) listenerType = 'classListener'
  if (listener.expression) listenerType = 'expressionListener'
  if (listener.delegateExpression) listenerType = 'delegateExpressionListener'
  if (listener.script) listenerType = 'scriptListener'
  return {
    ...JSON.parse(JSON.stringify(listener)),
    ...(listener.script ?? {}),
    listenerType: listenerType
  }
}

/** 将 ProcessListenerDO 转换成 initListenerForm 想同的 Form 对象 */
export function initListenerForm2(processListener) {
  if (processListener.valueType === 'class') {
    return {
      listenerType: 'classListener',
      class: processListener.value,
      event: processListener.event,
      fields: []
    }
  } else if (processListener.valueType === 'expression') {
    return {
      listenerType: 'expressionListener',
      expression: processListener.value,
      event: processListener.event,
      fields: []
    }
  } else if (processListener.valueType === 'delegateExpression') {
    return {
      listenerType: 'delegateExpressionListener',
      delegateExpression: processListener.value,
      event: processListener.event,
      fields: []
    }
  }
  throw new Error(
    t('auto.components.bpmnProcessDesigner.package.penal.listeners.utilSelf.k846ff3ab')
  )
}

export const listenerType = {
  classListener: t(
    'auto.components.bpmnProcessDesigner.package.penal.listeners.utilSelf.k385d4569'
  ),
  expressionListener: t(
    'auto.components.bpmnProcessDesigner.package.penal.listeners.utilSelf.k513c1c63'
  ),
  delegateExpressionListener: t(
    'auto.components.bpmnProcessDesigner.package.penal.listeners.utilSelf.kf5ed3fb9'
  ),
  scriptListener: t(
    'auto.components.bpmnProcessDesigner.package.penal.listeners.utilSelf.k7fbccbeb'
  )
}

export const eventType = {
  create: t('auto.components.bpmnProcessDesigner.package.penal.listeners.utilSelf.kfcbd0932'),
  assignment: t('auto.components.bpmnProcessDesigner.package.penal.listeners.utilSelf.k5c9aebfa'),
  complete: t('auto.components.bpmnProcessDesigner.package.penal.listeners.utilSelf.k33246f6a'),
  delete: t('common.delete'),
  update: t('auto.components.bpmnProcessDesigner.package.penal.listeners.utilSelf.kd9db02d0'),
  timeout: t('auto.components.bpmnProcessDesigner.package.penal.listeners.utilSelf.kff06c243')
}

export const fieldType = {
  string: t('auto.components.bpmnProcessDesigner.package.penal.listeners.utilSelf.k4dc9621a'),
  expression: t('auto.components.bpmnProcessDesigner.package.penal.listeners.utilSelf.k513c1c63')
}
