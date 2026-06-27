<template>
  <div class="node-handler-wrapper">
    <div class="node-handler">
      <el-popover
        trigger="hover"
        v-model:visible="popoverShow"
        placement="right-start"
        width="auto"
        v-if="!readonly"
      >
        <div class="handler-item-wrapper">
          <div class="handler-item" @click="addNode(NodeType.USER_TASK_NODE)">
            <div class="approve handler-item-icon">
              <span class="iconfont icon-approve icon-size"></span>
            </div>
            <div class="handler-item-text">{{
              t('auto.components.SimpleProcessDesignerV2.src.NodeHandler.k9b446de3')
            }}</div>
          </div>
          <div class="handler-item" @click="addNode(NodeType.COPY_TASK_NODE)">
            <div class="handler-item-icon copy">
              <span class="iconfont icon-size icon-copy"></span>
            </div>
            <div class="handler-item-text">{{
              t('auto.components.SimpleProcessDesignerV2.src.NodeHandler.k48a091c0')
            }}</div>
          </div>
          <div class="handler-item" @click="addNode(NodeType.CONDITION_BRANCH_NODE)">
            <div class="handler-item-icon condition">
              <span class="iconfont icon-size icon-exclusive"></span>
            </div>
            <div class="handler-item-text">{{
              t('auto.components.SimpleProcessDesignerV2.src.NodeHandler.k315a7ec1')
            }}</div>
          </div>
          <div class="handler-item" @click="addNode(NodeType.PARALLEL_BRANCH_NODE)">
            <div class="handler-item-icon parallel">
              <span class="iconfont icon-size icon-parallel"></span>
            </div>
            <div class="handler-item-text">{{
              t('auto.components.SimpleProcessDesignerV2.src.NodeHandler.k033713cb')
            }}</div>
          </div>
          <div class="handler-item" @click="addNode(NodeType.INCLUSIVE_BRANCH_NODE)">
            <div class="handler-item-icon inclusive">
              <span class="iconfont icon-size icon-inclusive"></span>
            </div>
            <div class="handler-item-text">{{
              t('auto.components.SimpleProcessDesignerV2.src.NodeHandler.k98cae0bd')
            }}</div>
          </div>
        </div>
        <template #reference>
          <div class="add-icon"><Icon icon="ep:plus" /></div>
        </template>
      </el-popover>
    </div>
  </div>
</template>

<script setup lang="ts">
import {
  ApproveMethodType,
  AssignEmptyHandlerType,
  AssignStartUserHandlerType,
  NODE_DEFAULT_NAME,
  NodeType,
  RejectHandlerType,
  SimpleFlowNode
} from './consts'
import { generateUUID } from '@/utils'
const { t } = useI18n()
defineOptions({
  name: 'NodeHandler'
})

const message = useMessage() // 消息弹窗

const popoverShow = ref(false)
const props = defineProps({
  childNode: {
    type: Object as () => SimpleFlowNode,
    default: null
  },
  currentNode: {
    type: Object as () => SimpleFlowNode,
    required: true
  }
})
const emits = defineEmits(['update:childNode'])

const readonly = inject<Boolean>('readonly') // 是否只读

const addNode = (type: number) => {
  // 校验：条件分支、包容分支后面，不允许直接添加并行分支
  if (
    type === NodeType.PARALLEL_BRANCH_NODE &&
    [NodeType.CONDITION_BRANCH_NODE, NodeType.INCLUSIVE_BRANCH_NODE].includes(
      props.currentNode?.type
    )
  ) {
    message.error(t('auto.components.SimpleProcessDesignerV2.src.NodeHandler.k45032df2'))
    return
  }

  popoverShow.value = false
  if (type === NodeType.USER_TASK_NODE) {
    const id = 'Activity_' + generateUUID()
    const data: SimpleFlowNode = {
      id: id,
      name: NODE_DEFAULT_NAME.get(NodeType.USER_TASK_NODE) as string,
      showText: '',
      type: NodeType.USER_TASK_NODE,
      approveMethod: ApproveMethodType.SEQUENTIAL_APPROVE,
      // 超时处理
      rejectHandler: {
        type: RejectHandlerType.FINISH_PROCESS
      },
      timeoutHandler: {
        enable: false
      },
      assignEmptyHandler: {
        type: AssignEmptyHandlerType.APPROVE
      },
      assignStartUserHandlerType: AssignStartUserHandlerType.START_USER_AUDIT,
      childNode: props.childNode
    }
    emits('update:childNode', data)
  }
  if (type === NodeType.COPY_TASK_NODE) {
    const data: SimpleFlowNode = {
      id: 'Activity_' + generateUUID(),
      name: NODE_DEFAULT_NAME.get(NodeType.COPY_TASK_NODE) as string,
      showText: '',
      type: NodeType.COPY_TASK_NODE,
      childNode: props.childNode
    }
    emits('update:childNode', data)
  }
  if (type === NodeType.CONDITION_BRANCH_NODE) {
    const data: SimpleFlowNode = {
      name: t('auto.components.SimpleProcessDesignerV2.src.NodeHandler.k315a7ec1'),
      type: NodeType.CONDITION_BRANCH_NODE,
      id: 'GateWay_' + generateUUID(),
      childNode: props.childNode,
      conditionNodes: [
        {
          id: 'Flow_' + generateUUID(),
          name: t('auto.components.SimpleProcessDesignerV2.src.NodeHandler.kd610f7ab'),
          showText: '',
          type: NodeType.CONDITION_NODE,
          childNode: undefined,
          conditionType: 1,
          defaultFlow: false
        },
        {
          id: 'Flow_' + generateUUID(),
          name: t('auto.components.SimpleProcessDesignerV2.src.NodeHandler.kc9cada3e'),
          showText: t('auto.components.SimpleProcessDesignerV2.src.NodeHandler.k0f9db0ca'),
          type: NodeType.CONDITION_NODE,
          childNode: undefined,
          conditionType: undefined,
          defaultFlow: true
        }
      ]
    }
    emits('update:childNode', data)
  }
  if (type === NodeType.PARALLEL_BRANCH_NODE) {
    const data: SimpleFlowNode = {
      name: t('auto.components.SimpleProcessDesignerV2.src.NodeHandler.k033713cb'),
      type: NodeType.PARALLEL_BRANCH_NODE,
      id: 'GateWay_' + generateUUID(),
      childNode: props.childNode,
      conditionNodes: [
        {
          id: 'Flow_' + generateUUID(),
          name: t('auto.components.SimpleProcessDesignerV2.src.NodeHandler.k40a78ead'),
          showText: t('auto.components.SimpleProcessDesignerV2.src.NodeHandler.k68614ac3'),
          type: NodeType.CONDITION_NODE,
          childNode: undefined
        },
        {
          id: 'Flow_' + generateUUID(),
          name: t('auto.components.SimpleProcessDesignerV2.src.NodeHandler.k309959f5'),
          showText: t('auto.components.SimpleProcessDesignerV2.src.NodeHandler.k68614ac3'),
          type: NodeType.CONDITION_NODE,
          childNode: undefined
        }
      ]
    }
    emits('update:childNode', data)
  }
  if (type === NodeType.INCLUSIVE_BRANCH_NODE) {
    const data: SimpleFlowNode = {
      name: t('auto.components.SimpleProcessDesignerV2.src.NodeHandler.k98cae0bd'),
      type: NodeType.INCLUSIVE_BRANCH_NODE,
      id: 'GateWay_' + generateUUID(),
      childNode: props.childNode,
      conditionNodes: [
        {
          id: 'Flow_' + generateUUID(),
          name: t('auto.components.SimpleProcessDesignerV2.src.NodeHandler.k5a0bdfe5'),
          showText: '',
          type: NodeType.CONDITION_NODE,
          childNode: undefined,
          defaultFlow: false
        },
        {
          id: 'Flow_' + generateUUID(),
          name: t('auto.components.SimpleProcessDesignerV2.src.NodeHandler.kc9cada3e'),
          showText: t('auto.components.SimpleProcessDesignerV2.src.NodeHandler.k0f9db0ca'),
          type: NodeType.CONDITION_NODE,
          childNode: undefined,
          defaultFlow: true
        }
      ]
    }
    emits('update:childNode', data)
  }
}
</script>

<style lang="scss" scoped></style>
