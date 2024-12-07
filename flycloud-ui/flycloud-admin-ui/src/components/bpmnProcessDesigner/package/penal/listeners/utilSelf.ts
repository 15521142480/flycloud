// 初始化表单数据
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
  throw new Error('未知的监听器类型')
}

export const listenerType = {
  classListener: 'Java 类',
  expressionListener: '表达式',
  delegateExpressionListener: '代理表达式',
  scriptListener: '脚本'
}

export const eventType = {
  create: '创建',
  assignment: '指派',
  complete: '完成',
  delete: '删除',
  update: '更新',
  timeout: '超时'
}

export const fieldType = {
  string: '字符串',
  expression: '表达式'
}
