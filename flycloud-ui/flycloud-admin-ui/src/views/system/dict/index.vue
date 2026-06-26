<template>
  <div class="system-page">
    <div class="dict-layout">
      <section class="table-panel dict-types">
        <div class="mini-toolbar" @keyup.enter="loadTypes">
          <div class="mini-fields">
            <el-input v-model.trim="typeQuery.name" clearable :placeholder="t('system.dict.typeName')" @keyup.enter="loadTypes" />
          </div>
          <div class="mini-actions">
            <el-button @click="loadTypes">{{ t('common.search') }}</el-button>
            <el-button @click="resetTypeQuery">{{ t('common.reset') }}</el-button>
            <el-button v-hasPermi="['sys:dict:saveOrUpdate']" type="primary" @click="openTypeCreate">{{ t('system.dict.addType') }}</el-button>
          </div>
        </div>
        <el-table v-loading="typeLoading" :data="typeList" row-key="id" height="calc(100vh - 330px)" highlight-current-row @current-change="selectType">
          <el-table-column prop="name" :label="t('system.dict.typeName')" min-width="140" />
          <el-table-column prop="type" :label="t('system.dict.typeCode')" min-width="160" />
          <el-table-column :label="t('common.status')" width="90">
            <template #default="{ row }">
              <el-tag :type="row.status === 0 ? 'success' : 'info'" effect="plain">{{ row.status === 0 ? t('common.normal') : t('common.disabled') }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column :label="t('common.operation')" width="170" align="center">
            <template #default="{ row }">
              <el-button v-hasPermi="['sys:dict:saveOrUpdate']" text type="primary" @click.stop="openTypeEdit(row)">{{ t('common.edit') }}</el-button>
              <el-button v-hasPermi="['sys:dict:delete']" text type="danger" @click.stop="removeType(row)">{{ t('common.delete') }}</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          v-model:current-page="typeQuery.pageNum"
          v-model:page-size="typeQuery.pageSize"
          layout="total, prev, pager, next"
          :total="typeTotal"
          @change="loadTypes"
        />
      </section>

      <section class="table-panel">
        <div class="mini-toolbar" @keyup.enter="loadData">
          <div class="mini-fields">
            <strong>{{ currentType?.name || t('system.dict.selectType') }}</strong>
            <el-input v-model.trim="dataQuery.label" clearable :placeholder="t('system.dict.dataLabel')" @keyup.enter="loadData" />
          </div>
          <div class="mini-actions">
            <el-button :disabled="!currentType" @click="loadData">{{ t('common.search') }}</el-button>
            <el-button :disabled="!currentType" @click="resetDataQuery">{{ t('common.reset') }}</el-button>
            <el-button v-hasPermi="['sys:dict:optionDictData']" type="primary" :disabled="!currentType" @click="openDataCreate">{{ t('system.dict.addData') }}</el-button>
          </div>
        </div>
        <el-table v-loading="dataLoading" :data="dataList" row-key="id" height="calc(100vh - 330px)">
          <el-table-column prop="label" :label="t('system.dict.label')" min-width="130" />
          <el-table-column prop="value" :label="t('system.dict.value')" min-width="130" />
          <el-table-column prop="sort" :label="t('common.sort')" width="80" />
          <el-table-column prop="colorType" :label="t('system.dict.color')" width="100" />
          <el-table-column :label="t('common.status')" width="90">
            <template #default="{ row }">
              <el-tag :type="row.status === 0 ? 'success' : 'info'" effect="plain">{{ row.status === 0 ? t('common.normal') : t('common.disabled') }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column :label="t('common.operation')" width="170" align="center" fixed="right">
            <template #default="{ row }">
              <el-button v-hasPermi="['sys:dict:optionDictData']" text type="primary" @click="openDataEdit(row)">{{ t('common.edit') }}</el-button>
              <el-button v-hasPermi="['sys:dict:delete']" text type="danger" @click="removeData(row)">{{ t('common.delete') }}</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination v-model:current-page="dataQuery.pageNum" v-model:page-size="dataQuery.pageSize" layout="total, prev, pager, next" :total="dataTotal" @change="loadData" />
      </section>
    </div>

    <el-dialog v-model="typeDialogVisible" :title="typeForm.id ? t('system.dict.editTypeTitle') : t('system.dict.createTypeTitle')" width="520px">
      <el-form label-width="92px" :model="typeForm">
        <el-form-item :label="t('system.dict.typeName')" required><el-input v-model.trim="typeForm.name" /></el-form-item>
        <el-form-item :label="t('system.dict.typeCode')" required><el-input v-model.trim="typeForm.type" /></el-form-item>
        <el-form-item :label="t('common.status')">
          <el-radio-group v-model="typeForm.status">
            <el-radio-button :value="0">{{ t('common.normal') }}</el-radio-button>
            <el-radio-button :value="1">{{ t('common.disabled') }}</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item :label="t('common.remark')"><el-input v-model="typeForm.remark" type="textarea" :rows="3" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="typeDialogVisible = false">{{ t('common.cancel') }}</el-button>
        <el-button type="primary" :loading="saving" @click="submitType">{{ t('common.save') }}</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="dataDialogVisible" :title="dataForm.id ? t('system.dict.editDataTitle') : t('system.dict.createDataTitle')" width="560px">
      <el-form label-width="92px" :model="dataForm">
        <el-form-item :label="t('system.dict.typeCode')"><el-input v-model="dataForm.dictType" disabled /></el-form-item>
        <el-form-item :label="t('system.dict.label')" required><el-input v-model.trim="dataForm.label" /></el-form-item>
        <el-form-item :label="t('system.dict.value')" required><el-input v-model.trim="dataForm.value" /></el-form-item>
        <el-form-item :label="t('common.sort')"><el-input-number v-model="dataForm.sort" :min="0" /></el-form-item>
        <el-form-item :label="t('system.dict.color')"><el-input v-model.trim="dataForm.colorType" placeholder="success / warning / danger" /></el-form-item>
        <el-form-item :label="t('common.status')">
          <el-radio-group v-model="dataForm.status">
            <el-radio-button :value="0">{{ t('common.normal') }}</el-radio-button>
            <el-radio-button :value="1">{{ t('common.disabled') }}</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item :label="t('common.remark')"><el-input v-model="dataForm.remark" type="textarea" :rows="3" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dataDialogVisible = false">{{ t('common.cancel') }}</el-button>
        <el-button type="primary" :loading="saving" @click="submitData">{{ t('common.save') }}</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import * as DictTypeApi from '@/api/system/dict/dict.type'
import * as DictDataApi from '@/api/system/dict/dict.data'
import type { PageParam } from '@/entity/base/base-entity'
import type { SysDictData, SysDictType } from '@/entity/system'
import { useI18n } from '@/hooks/web/useI18n'
const { t } = useI18n()

const typeLoading = ref(false)
const dataLoading = ref(false)
const saving = ref(false)
const typeDialogVisible = ref(false)
const dataDialogVisible = ref(false)
const typeList = ref<SysDictType[]>([])
const dataList = ref<SysDictData[]>([])
const currentType = ref<SysDictType>()
const typeTotal = ref(0)
const dataTotal = ref(0)

const typeQuery = reactive<PageParam>({ pageNum: 1, pageSize: 10, name: '', type: '' })
const dataQuery = reactive<PageParam>({ pageNum: 1, pageSize: 10, label: '', dictType: '' })
const typeForm = reactive<SysDictType>(createTypeForm())
const dataForm = reactive<SysDictData>(createDataForm())

onMounted(loadTypes)

async function loadTypes() {
  typeLoading.value = true
  try {
    const result = await DictTypeApi.getDictTypePage(typeQuery)
    typeList.value = result.list
    typeTotal.value = Number(result.total || 0)
    if (!currentType.value && typeList.value.length) {
      selectType(typeList.value[0])
    }
  } finally {
    typeLoading.value = false
  }
}

async function loadData() {
  if (!currentType.value) return
  dataLoading.value = true
  try {
    dataQuery.dictType = currentType.value.type
    const result = await DictDataApi.getDictDataPage(dataQuery)
    dataList.value = result.list
    dataTotal.value = Number(result.total || 0)
  } finally {
    dataLoading.value = false
  }
}

function selectType(row?: SysDictType) {
  if (!row) return
  currentType.value = row
  dataQuery.pageNum = 1
  loadData()
}

function resetTypeQuery() {
  Object.assign(typeQuery, { pageNum: 1, name: '', type: '' })
  loadTypes()
}

function resetDataQuery() {
  Object.assign(dataQuery, { pageNum: 1, label: '' })
  loadData()
}

function openTypeCreate() {
  Object.assign(typeForm, createTypeForm())
  typeDialogVisible.value = true
}

function openTypeEdit(row: SysDictType) {
  Object.assign(typeForm, createTypeForm(), row)
  typeDialogVisible.value = true
}

function openDataCreate() {
  Object.assign(dataForm, createDataForm(), { dictType: currentType.value?.type || '' })
  dataDialogVisible.value = true
}

function openDataEdit(row: SysDictData) {
  Object.assign(dataForm, createDataForm(), row)
  dataDialogVisible.value = true
}

async function submitType() {
  saving.value = true
  try {
    await DictTypeApi.saveOrUpdate(typeForm)
    ElMessage.success(t('common.saveSuccess'))
    typeDialogVisible.value = false
    await loadTypes()
  } finally {
    saving.value = false
  }
}

async function submitData() {
  saving.value = true
  try {
    await DictDataApi.saveOrUpdate(dataForm)
    ElMessage.success(t('common.saveSuccess'))
    dataDialogVisible.value = false
    await loadData()
  } finally {
    saving.value = false
  }
}

async function removeType(row: SysDictType) {
  await ElMessageBox.confirm(t('system.dict.deleteTypeConfirm', { name: row.name }), t('common.deleteConfirmTitle'), { type: 'warning' })
  await DictTypeApi.deleteDictType(row.id!)
  currentType.value = undefined
  ElMessage.success(t('common.deleteSuccess'))
  await loadTypes()
}

async function removeData(row: SysDictData) {
  await ElMessageBox.confirm(t('system.dict.deleteDataConfirm', { name: row.label }), t('common.deleteConfirmTitle'), { type: 'warning' })
  await DictDataApi.deleteDictData(row.id!)
  ElMessage.success(t('common.deleteSuccess'))
  await loadData()
}

function createTypeForm(): SysDictType {
  return { name: '', type: '', status: 0, remark: '' }
}

function createDataForm(): SysDictData {
  return { label: '', value: '', dictType: '', sort: 0, status: 0, colorType: '', cssClass: '', remark: '' }
}
</script>

<style scoped lang="scss">
.dict-layout {
  display: grid;
  grid-template-columns: minmax(0, 0.8fr) minmax(0, 1fr);
  gap: 18px;
}

.dict-types {
  min-width: 0;
}

.mini-toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 10px;
  margin-bottom: 14px;

  strong {
    min-width: 130px;
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
  .dict-layout {
    grid-template-columns: 1fr;
  }
}
</style>
