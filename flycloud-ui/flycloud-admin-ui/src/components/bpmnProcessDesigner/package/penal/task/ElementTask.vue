<template>
  <div class="panel-tab__content">
    <el-form size="small" label-width="90px">
      <!-- add by 芋艿：由于「异步延续」暂时用不到，所以这里 display 为 none -->
      <el-form-item
        :label="t('auto.components.bpmnProcessDesigner.package.penal.task.ElementTask.k02bcd55a')"
        style="display: none"
      >
        <el-checkbox
          v-model="taskConfigForm.asyncBefore"
          :label="t('auto.components.bpmnProcessDesigner.package.penal.task.ElementTask.k027f21ce')"
          :value="t('extra.ka28cefd2')"
          @change="changeTaskAsync"
        />
        <el-checkbox
          v-model="taskConfigForm.asyncAfter"
          :label="t('auto.components.bpmnProcessDesigner.package.penal.task.ElementTask.k02e94833')"
          :value="t('extra.ke08ba045')"
          @change="changeTaskAsync"
        />
        <el-checkbox
          v-model="taskConfigForm.exclusive"
          v-if="taskConfigForm.asyncAfter || taskConfigForm.asyncBefore"
          :label="t('auto.components.bpmnProcessDesigner.package.penal.task.ElementTask.k7b37cc8d')"
          :value="t('extra.kcff6d49a')"
          @change="changeTaskAsync"
        />
      </el-form-item>
      <component :is="witchTaskComponent" v-bind="$props" />
    </el-form>
  </div>
</template>

<script lang="ts" setup>
import UserTask from './task-components/UserTask.vue'
import ScriptTask from './task-components/ScriptTask.vue'
import ReceiveTask from './task-components/ReceiveTask.vue'
const { t } = useI18n()
defineOptions({ name: 'ElementTaskConfig' })

const props = defineProps({
  id: String,
  type: String
})
const taskConfigForm = ref({
  asyncAfter: false,
  asyncBefore: false,
  exclusive: false
})
const witchTaskComponent = ref()
const installedComponent = ref({
  // 手工任务与普通任务一致，不需要其他配置
  // 接收消息任务，需要在全局下插入新的消息实例，并在该节点下的 messageRef 属性绑定该实例
  // 发送任务、服务任务、业务规则任务共用一个相同配置
  UserTask: 'UserTask', // 用户任务配置
  ScriptTask: 'ScriptTask', // 脚本任务配置
  ReceiveTask: 'ReceiveTask' // 消息接收任务
})
const bpmnElement = ref()

const bpmnInstances = () => (window as any).bpmnInstances
const changeTaskAsync = () => {
  if (!taskConfigForm.value.asyncBefore && !taskConfigForm.value.asyncAfter) {
    taskConfigForm.value.exclusive = false
  }
  bpmnInstances().modeling.updateProperties(bpmnInstances().bpmnElement, {
    ...taskConfigForm.value
  })
}

watch(
  () => props.id,
  () => {
    bpmnElement.value = bpmnInstances().bpmnElement
    taskConfigForm.value.asyncBefore = bpmnElement.value?.businessObject?.asyncBefore
    taskConfigForm.value.asyncAfter = bpmnElement.value?.businessObject?.asyncAfter
    taskConfigForm.value.exclusive = bpmnElement.value?.businessObject?.exclusive
  },
  { immediate: true }
)
watch(
  () => props.type,
  () => {
    // witchTaskComponent.value = installedComponent.value[props.type]
    if (props.type == installedComponent.value.UserTask) {
      witchTaskComponent.value = UserTask
    }
    if (props.type == installedComponent.value.ScriptTask) {
      witchTaskComponent.value = ScriptTask
    }
    if (props.type == installedComponent.value.ReceiveTask) {
      witchTaskComponent.value = ReceiveTask
    }
  },
  { immediate: true }
)
</script>
