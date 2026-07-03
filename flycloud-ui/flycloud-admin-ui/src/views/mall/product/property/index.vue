<template>
  <div class="product-property-page">
    <div class="property-layout">
      <section class="table-panel property-list">
        <div class="mini-toolbar" @keyup.enter="getPropertyList">
          <div class="mini-fields">
            <el-input
              v-model.trim="propertyQuery.name"
              clearable
              placeholder="属性名称"
              @keyup.enter="getPropertyList"
            />
          </div>
          <div class="mini-actions">
            <el-button @click="getPropertyList">查询</el-button>
            <el-button @click="resetPropertyQuery">重置</el-button>
            <el-button
              v-hasPermi="['mall:product:property:saveOrUpdate']"
              type="primary"
              @click="openPropertyForm('create')"
            >
              新增属性
            </el-button>
          </div>
        </div>

        <el-table
          ref="propertyTableRef"
          v-loading="propertyLoading"
          :data="propertyList"
          height="calc(100vh - 330px)"
          highlight-current-row
          row-key="id"
          @current-change="selectProperty"
        >
          <el-table-column label="属性编号" min-width="100" prop="id" align="center" />
          <el-table-column label="属性名称" min-width="150" prop="name" />
          <el-table-column
            :show-overflow-tooltip="true"
            label="备注"
            min-width="120"
            prop="remark"
          />
          <el-table-column align="center" label="操作" width="120">
            <template #default="{ row }">
              <el-button
                v-hasPermi="['mall:product:property:saveOrUpdate']"
                link
                type="primary"
                @click.stop="openPropertyForm('update', row.id)"
              >
                编辑
              </el-button>
              <el-button
                v-hasPermi="['mall:product:property:delete']"
                link
                type="danger"
                @click.stop="handleDeleteProperty(row.id)"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination
          v-model:current-page="propertyQuery.pageNum"
          v-model:page-size="propertyQuery.pageSize"
          :total="propertyTotal"
          layout="total, prev, pager, next"
          @change="getPropertyList"
        />
      </section>

      <section class="table-panel property-values">
        <div class="mini-toolbar" @keyup.enter="getValueList">
          <div class="mini-fields">
            <strong>{{ currentProperty?.name || '请选择属性' }}</strong>
            <el-input
              v-model.trim="valueQuery.name"
              clearable
              placeholder="属性值名称"
              @keyup.enter="getValueList"
            />
          </div>
          <div class="mini-actions">
            <el-button :disabled="!currentProperty" @click="getValueList">查询</el-button>
            <el-button :disabled="!currentProperty" @click="resetValueQuery">重置</el-button>
            <el-button
              v-hasPermi="['mall:product:property:saveOrUpdate']"
              :disabled="!currentProperty"
              type="primary"
              @click="openValueForm('create')"
            >
              新增属性值
            </el-button>
          </div>
        </div>

        <el-table
          v-loading="valueLoading"
          :data="valueList"
          height="calc(100vh - 330px)"
          row-key="id"
        >
          <el-table-column label="属性值名称" min-width="170" prop="name" />
          <el-table-column
            :show-overflow-tooltip="true"
            label="备注"
            min-width="140"
            prop="remark"
          />
          <el-table-column
            :formatter="dateFormatter"
            label="创建时间"
            prop="createTime"
            width="180"
          />
          <el-table-column align="center" fixed="right" label="操作" width="130">
            <template #default="{ row }">
              <el-button
                v-hasPermi="['mall:product:property:saveOrUpdate']"
                link
                type="primary"
                @click="openValueForm('update', row.id)"
              >
                编辑
              </el-button>
              <el-button
                v-hasPermi="['mall:product:property:delete']"
                link
                type="danger"
                @click="handleDeleteValue(row.id)"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination
          v-model:current-page="valueQuery.pageNum"
          v-model:page-size="valueQuery.pageSize"
          :total="valueTotal"
          layout="total, prev, pager, next"
          @change="getValueList"
        />
      </section>
    </div>

    <PropertyForm ref="propertyFormRef" @success="handlePropertyFormSuccess" />
    <ValueForm ref="valueFormRef" @success="getValueList" />
  </div>
</template>

<script lang="ts" setup>
import { dateFormatter } from '@/utils/formatTime'
import * as PropertyApi from '@/api/mall/product/property'
import PropertyForm from './PropertyForm.vue'
import ValueForm from './value/ValueForm.vue'

defineOptions({ name: 'ProductProperty' })

const message = useMessage()
const { t } = useI18n()

const propertyLoading = ref(false)
const valueLoading = ref(false)
const propertyList = ref<PropertyApi.PropertyVO[]>([])
const valueList = ref<PropertyApi.PropertyValueVO[]>([])
const currentProperty = ref<PropertyApi.PropertyVO>()
const propertyTotal = ref(0)
const valueTotal = ref(0)
const propertyTableRef = ref()
const propertyFormRef = ref()
const valueFormRef = ref()

const propertyQuery = reactive({
  pageNum: 1,
  pageSize: 10,
  name: ''
})

const valueQuery = reactive({
  pageNum: 1,
  pageSize: 10,
  propertyId: undefined as number | undefined,
  name: ''
})

const getPropertyList = async () => {
  propertyLoading.value = true
  try {
    const data = await PropertyApi.getPropertyPage(propertyQuery)
    propertyList.value = data.list
    propertyTotal.value = Number(data.total || 0)
    refreshCurrentProperty()
  } finally {
    propertyLoading.value = false
  }
}

const getValueList = async () => {
  if (!currentProperty.value?.id) {
    valueList.value = []
    valueTotal.value = 0
    return
  }
  valueLoading.value = true
  try {
    valueQuery.propertyId = currentProperty.value.id
    const data = await PropertyApi.getPropertyValuePage(valueQuery)
    valueList.value = data.list
    valueTotal.value = Number(data.total || 0)
  } finally {
    valueLoading.value = false
  }
}

const refreshCurrentProperty = async () => {
  if (!propertyList.value.length) {
    currentProperty.value = undefined
    valueList.value = []
    valueTotal.value = 0
    return
  }
  const currentId = currentProperty.value?.id
  const nextProperty = propertyList.value.find((item) => item.id === currentId) || propertyList.value[0]
  currentProperty.value = nextProperty
  valueQuery.pageNum = 1
  await nextTick()
  propertyTableRef.value?.setCurrentRow(nextProperty)
  await getValueList()
}

const selectProperty = (row?: PropertyApi.PropertyVO) => {
  if (!row?.id || row.id === currentProperty.value?.id) return
  currentProperty.value = row
  valueQuery.pageNum = 1
  getValueList()
}

const resetPropertyQuery = () => {
  Object.assign(propertyQuery, { pageNum: 1, name: '' })
  getPropertyList()
}

const resetValueQuery = () => {
  Object.assign(valueQuery, { pageNum: 1, name: '' })
  getValueList()
}

const openPropertyForm = (type: string, id?: number) => {
  propertyFormRef.value.open(type, id)
}

const handlePropertyFormSuccess = async () => {
  await getPropertyList()
}

const openValueForm = (type: string, id?: number) => {
  if (!currentProperty.value?.id) return
  valueFormRef.value.open(type, currentProperty.value.id, id)
}

const handleDeleteProperty = async (id: number) => {
  try {
    await message.delConfirm()
    await PropertyApi.deleteProperty(id)
    message.success(t('common.delSuccess'))
    if (currentProperty.value?.id === id) {
      currentProperty.value = undefined
    }
    await getPropertyList()
  } catch {}
}

const handleDeleteValue = async (id: number) => {
  try {
    await message.delConfirm()
    await PropertyApi.deletePropertyValue(id)
    message.success(t('common.delSuccess'))
    await getValueList()
  } catch {}
}

onMounted(() => {
  getPropertyList()
})
</script>

<style lang="scss" scoped>
.property-layout {
  display: grid;
  grid-template-columns: minmax(0, 0.8fr) minmax(0, 1fr);
  gap: 18px;
}

.property-list,
.property-values {
  min-width: 0;
}

.mini-toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 10px;
  margin-bottom: 14px;

  strong {
    min-width: 120px;
  }
}

.mini-fields,
.mini-actions {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 10px;
}

.mini-fields {
  min-width: 0;

  .el-input {
    width: 188px;
  }
}

.mini-actions {
  justify-content: flex-end;

  .el-button + .el-button {
    margin-left: 0;
  }
}

@media (max-width: 1100px) {
  .property-layout {
    grid-template-columns: 1fr;
  }
}
</style>
