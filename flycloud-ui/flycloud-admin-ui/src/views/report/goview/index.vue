<template>
  <!--  <doc-alert :title="t('extra.k2b06900c')" url="https://doc.iocoder.cn/report/screen/" />-->

  <ContentWrap :bodyStyle="{ padding: '0px' }" class="!mb-0">
    <IFrame :src="src" />
  </ContentWrap>
</template>
<script lang="ts" setup>
const { t } = useI18n()
defineOptions({ name: 'GoView' })

const src = 'http://127.0.0.1:3000'
</script>
