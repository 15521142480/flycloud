<template>
  <!-- 搜索工作栏 -->
  <ContentWrap>

    <el-row style="margin-left: 15px">
      <el-col :span="17">
        <span style="margin-left: 10px; color: red">
          1、数据库表有多个时用英文,隔开
          <span style="margin-left: 20px">2、生成后检查后端部署的根目录下的src文件夹是否生成了</span>
        </span>
      </el-col>
    </el-row>

    <el-row style="margin: 5px 20px">
      <el-col :span="10">
        <el-input
          v-model="queryParams.tables"
          placeholder="请输入需要生成代码的数据库表"
          clearable
        />
      </el-col>

      <el-col :span="6" style="margin-left: 20px">
        <el-button v-hasPermi="['infra:generator:generatorCode']" type="primary" @click="handleGeneratorCode" :loading="loading"><Icon class="mr-5px" icon="ep:plus" /> 生成</el-button>
      </el-col>
    </el-row>

  </ContentWrap>

  <!-- 列表 -->
<!--  <ContentWrap>-->
<!--    <div>-->

<!--    </div>-->
<!--  </ContentWrap>-->

</template>
<script lang="ts" setup>
import * as GeneratorApi from '@/api/infra/generator'
import {object} from "vue-types";

defineOptions({ name: 'Generator' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(false) // 列表的加载中
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10000,
  tables: ''
})

/** 生成代码 */
const handleGeneratorCode = async () => {
  if (!queryParams.tables) {
    message.notifyWarning('请输入需要生成代码的数据库表！')
    return
  }
  loading.value = true
  try {
    // 删除的二次确认
    await message.confirm('确认要生成表[' + queryParams.tables + ']代码吗?')
    const data = await GeneratorApi.handleGeneratorCode(queryParams.tables)
    message.notifySuccess('生成表[' + queryParams.tables + ']代码成功！')
  } catch (e) {
    // message.notifyError('生成代码失败！' + e.message)
  }finally {
    loading.value = false
  }
}


/** 初始化 **/
onMounted(async () => {
})
</script>
