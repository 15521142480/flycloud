<template>
  <el-drawer
    :append-to-body="true"
    v-model="settingVisible"
    :show-close="false"
    :size="550"
    :before-close="saveConfig"
  >
    <template #header>
      <div class="config-header">
        <input
          v-if="showInput"
          type="text"
          class="config-editable-input"
          @blur="blurEvent()"
          v-mountedFocus
          v-model="nodeName"
          :placeholder="nodeName"
        />
        <div v-else class="node-name">
          {{ nodeName }} <Icon class="ml-1" icon="ep:edit-pen" :size="16" @click="clickIcon()" />
        </div>
        <div class="divide-line"></div>
      </div>
    </template>
    <el-tabs type="border-card" v-model="activeTabName">
      <el-tab-pane :label="t('auto.views.system.oauth2.client.ClientForm.k560165a6')" name="user">
        <div> {{ t('extra.k70211f9a') }} </div>
      </el-tab-pane>
      <el-tab-pane :label="t('extra.ke765ec60')" name="fields" v-if="formType === 10">
        <div class="field-setting-pane">
          <div class="field-setting-desc">{{ t('extra.k7c462e44') }}</div>
          <div class="field-permit-title">
            <div class="setting-title-label first-title"> {{ t('extra.kb81e9b9c') }} </div>
            <div class="other-titles">
              <span class="setting-title-label">{{
                t('auto.views.iot.product.detail.ThinkModelFunctionForm.kffc1d065')
              }}</span>
              <span class="setting-title-label">{{ t('extra.ka32b3bf7') }}</span>
              <span class="setting-title-label">{{ t('system.menu.hidden') }}</span>
            </div>
          </div>
          <div
            class="field-setting-item"
            v-for="(item, index) in fieldsPermissionConfig"
            :key="index"
          >
            <div class="field-setting-item-label"> {{ item.title }} </div>
            <el-radio-group class="field-setting-item-group" v-model="item.permission">
              <div class="item-radio-wrap">
                <el-radio
                  :value="FieldPermissionType.READ"
                  size="large"
                  :label="FieldPermissionType.READ"
                  ><span></span
                ></el-radio>
              </div>
              <div class="item-radio-wrap">
                <el-radio
                  :value="FieldPermissionType.WRITE"
                  size="large"
                  :label="FieldPermissionType.WRITE"
                  ><span></span
                ></el-radio>
              </div>
              <div class="item-radio-wrap">
                <el-radio
                  :value="FieldPermissionType.NONE"
                  size="large"
                  :label="FieldPermissionType.NONE"
                  ><span></span
                ></el-radio>
              </div>
            </el-radio-group>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
    <template #footer>
      <el-divider />
      <div>
        <el-button type="primary" @click="saveConfig">{{ t('extra.k008b8fcb') }}</el-button>
        <el-button @click="closeDrawer">{{
          t('auto.components.AppLinkInput.AppLinkSelectDialog.kd54aeadc')
        }}</el-button>
      </div>
    </template>
  </el-drawer>
</template>
<script setup lang="ts">
import { SimpleFlowNode, NodeType, FieldPermissionType, START_USER_BUTTON_SETTING } from '../consts'
import { useWatchNode, useDrawer, useNodeName, useFormFieldsPermission } from '../node'
const { t } = useI18n()
defineOptions({
  name: 'StartUserNodeConfig'
})
const props = defineProps({
  flowNode: {
    type: Object as () => SimpleFlowNode,
    required: true
  }
})
// 抽屉配置
const { settingVisible, closeDrawer, openDrawer } = useDrawer()
// 当前节点
const currentNode = useWatchNode(props)
// 节点名称
const { nodeName, showInput, clickIcon, blurEvent } = useNodeName(NodeType.COPY_TASK_NODE)
// 激活的 Tab 标签页
const activeTabName = ref('user')
// 表单字段权限配置
const { formType, fieldsPermissionConfig, getNodeConfigFormFields } = useFormFieldsPermission(
  FieldPermissionType.WRITE
)

// 保存配置
const saveConfig = async () => {
  activeTabName.value = 'user'
  currentNode.value.name = nodeName.value!
  // TODO 暂时写死。后续可以显示谁有权限可以发起
  currentNode.value.showText = t(
    'auto.components.SimpleProcessDesignerV2.src.nodes_config.StartUserNodeConfig.kc7424765'
  )
  // 设置表单权限
  currentNode.value.fieldsPermission = fieldsPermissionConfig.value
  // 设置发起人的按钮权限
  currentNode.value.buttonsSetting = START_USER_BUTTON_SETTING
  settingVisible.value = false
  return true
}
// 显示发起人节点配置， 由父组件传过来
const showStartUserNodeConfig = (node: SimpleFlowNode) => {
  nodeName.value = node.name
  // 表单字段权限
  getNodeConfigFormFields(node.fieldsPermission)
}

defineExpose({ openDrawer, showStartUserNodeConfig }) // 暴露方法给父组件
</script>

<style lang="scss" scoped></style>
