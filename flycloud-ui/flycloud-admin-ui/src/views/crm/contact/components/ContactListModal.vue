<template>
  <Dialog
    v-model="dialogVisible"
    :title="t('auto.views.crm.contact.components.ContactListModal.k15581973')"
  >
    <!-- 搜索工作栏 -->
    <ContentWrap>
      <el-form
        ref="queryFormRef"
        :inline="true"
        :model="queryParams"
        class="-mb-15px"
        label-width="90px"
      >
        <el-form-item
          :label="t('auto.views.crm.contact.components.ContactListModal.ke34804e8')"
          prop="name"
        >
          <el-input
            v-model="queryParams.name"
            class="!w-240px"
            clearable
            :placeholder="t('auto.views.crm.contact.components.ContactListModal.kc5ffefd5')"
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item>
          <el-button @click="handleQuery">
            <Icon class="mr-5px" icon="ep:search" />
            {{ t('extra.k8314a980') }}
          </el-button>
          <el-button @click="resetQuery">
            <Icon class="mr-5px" icon="ep:refresh" />
            {{ t('extra.k4e2cb309') }}
          </el-button>
          <el-button v-hasPermi="['crm:business:saveOrUpdate']" type="primary" @click="openForm()">
            <Icon class="mr-5px" icon="ep:plus" />
            {{ t('extra.k3e388810') }}
          </el-button>
        </el-form-item>
      </el-form>
    </ContentWrap>

    <!-- 列表 -->
    <ContentWrap class="mt-10px">
      <el-table
        ref="contactRef"
        v-loading="loading"
        :data="list"
        :show-overflow-tooltip="true"
        :stripe="true"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column
          align="center"
          fixed="left"
          :label="t('auto.views.crm.contact.components.ContactListModal.kbe4c2616')"
          prop="name"
        >
          <template #default="scope">
            <el-link :underline="false" type="primary" @click="openDetail(scope.row.id)">
              {{ scope.row.name }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column align="center" :label="t('system.user.phone')" prop="mobile" />
        <el-table-column
          align="center"
          :label="t('auto.views.crm.contact.ContactForm.ka2a92e50')"
          prop="post"
        />
        <el-table-column
          align="center"
          :label="t('auto.views.crm.contact.ContactForm.kcac16f50')"
          prop="parentName"
        />
        <el-table-column align="center" :label="t('extra.k9d406e6a')" min-width="100" prop="master">
          <template #default="scope">
            <dict-tag :type="DICT_TYPE.INFRA_BOOLEAN_STRING" :value="scope.row.master" />
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页 -->
      <Pagination
        v-model:limit="queryParams.pageSize"
        v-model:page="queryParams.pageNum"
        :total="total"
        @pagination="getList"
      />
    </ContentWrap>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">{{
        t('extra.k008b8fcb')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.components.AppLinkInput.AppLinkSelectDialog.kd54aeadc')
      }}</el-button>
    </template>

    <!-- 表单弹窗：添加 -->
    <ContactForm ref="formRef" @success="getList" />
  </Dialog>
</template>
<script lang="ts" setup>
import * as ContactApi from '@/api/crm/contact'
import ContactForm from '../ContactForm.vue'
import { DICT_TYPE } from '@/utils/dict'
const { t } = useI18n()
const message = useMessage() // 消息弹窗
const props = defineProps<{
  customerId: string
}>()
defineOptions({ name: 'ContactListModal' })

const dialogVisible = ref(false) // 弹窗的是否展示
const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryFormRef = ref() // 搜索的表单
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  name: undefined,
  customerId: props.customerId
})

/** 打开弹窗 */
const open = async () => {
  dialogVisible.value = true
  queryParams.customerId = props.customerId // 解决 props.customerId 没更新到 queryParams 上的问题
  await getList()
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await ContactApi.getContactPageByCustomer(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.pageNum = 1
  getList()
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value.resetFields()
  handleQuery()
}

/** 添加操作 */
const formRef = ref()
const openForm = () => {
  formRef.value.open('create')
}

/** 关联联系人提交 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const contactRef = ref()
const submitForm = async () => {
  const contactIds = contactRef.value.getSelectionRows().map((row: ContactApi.ContactVO) => row.id)
  if (contactIds.length === 0) {
    return message.error(t('auto.views.crm.contact.components.ContactListModal.k58768a54'))
  }
  dialogVisible.value = false
  emit('success', contactIds, contactRef.value.getSelectionRows())
}

/** 打开联系人详情 */
const { push } = useRouter()
const openDetail = (id: string) => {
  push({ name: 'CrmContactDetail', params: { id } })
}
</script>
