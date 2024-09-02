<template>
  <div>
    <Modal
      :title="title"
      v-model="isShow"
      :closable="false"
      :styles="{top: '20px', textAlign: 'center'}"
    >
      <div class="file-option-modal-div">
        <div v-show="optionData.type === '0'">
          <Input v-model="optionParam.newFolderName" placeholder="输入文件夹名称" class="file-option-modal-input"/>
        </div>
        <div v-show="optionData.type === '3'">
          <Input v-model="optionParam.newFileName" placeholder="输入新的文件名" class="file-option-modal-input"/>
        </div>
        <div v-show="optionData.type === '5'">
          <Input v-model="optionParam.ysToFileName" placeholder="输入压缩后的文件名" class="file-option-modal-input"/>
          &nbsp;<span>.tar</span>
        </div>
      </div>
      <div slot="footer">
        <Button type="text" size="large" @click="isShow = false">取消</Button>
        <Button type="primary" size="large" @click="handleOk">确定</Button>
      </div>
    </Modal>
  </div>
</template>

<script>
export default {
  name: 'ftp-file-option-modal',
  components: {},
  props: {},
  data () {
    return {
      title: 'title',
      isShow: false,
      optionData: {},
      optionParam: {
        curPath: '', // 服务器当前路径
        type: '', // 操作类型(0:新建文件夹, 1:启动服务, 2:关闭服务, 3:文件重命名, 4:删除文件/夹, 5:压缩文件/夹, 6:解压文件/夹, 11:根据指定文件启动服务, 22:根据指定文件关闭服务)
        newFolderName: '', // 新建文件夹名称
        oldFileName: '', // 旧文件名名称
        newFileName: '', // 新文件名名称
        ysOrJyFileName: '', // 要压缩/解压文件/夹名称
        ysToFileName: '' // 压缩成包的名称
      }
    }
  },
  created () {

  },
  computed: {},
  methods: {
    // 父组件调用方法
    getFileOptionModalPop (optionData) {
      switch (optionData.type) {
        case '0':
          this.title = '新建文件夹'
          break
        case '3':
          this.title = '文件重命名'
          this.optionParam.oldFileName = optionData.oldFileName
          this.optionParam.newFileName = optionData.oldFileName
          break
        case '5':
          this.title = '压缩文件'
          this.optionParam.ysOrJyFileName = optionData.ysOrJyFileName
          // this.optionParam.ysToFileName = optionData.ysOrJyFileName
          break
      }
      this.optionData = optionData
      this.isShow = true
    },

    // 确认
    handleOk () {
      this.optionParam.type = this.optionData.type
      this.optionParam.curPath = this.optionData.curPath
      if (this.optionParam.type === '' || this.optionParam.curPath === '') {
        this.$Message.error('当前服务器路径 | 操作类型 -> 不能为空!')
        return
      }

      // 字段验证
      switch (this.optionData.type) {
        case '0':
          if (this.optionParam.newFolderName === '') {
            this.$Message.error('文件夹名称不能为空!')
            return
          }
          break
        case '3':
          if (this.optionParam.oldFileName === '' || this.optionParam.newFileName === '') {
            this.$Message.error('旧 | 新 -> 文件/夹名称不能为空!')
            return
          }
          break
        case '5':
          if (this.optionParam.ysOrJyFileName === '' || this.optionParam.ysToFileName === '') {
            this.$Message.error('目标压缩文件 | 新压缩文件 -> 的名称不能为空!')
            return
          }
          this.title = '压缩文件'
          break
      }
      this.executeOptionServer(this.optionParam)
    },

    // ========== 执行操作接口
    executeOptionServer (param) {
      this.$api.fileStp.executeOptionFtpApi(param).then((res) => {
        let resultCode = res.data.code
        let resultMsg = res.data.msg
        if (resultCode === 0) {
          this.$Notice.success({
            title: '操作提醒',
            desc: resultMsg
          })
        } else {
          this.$Notice.error({
            title: '操作提醒',
            desc: '操作失败!!!'
          })
        }
        this.isShow = false
        this.$emit('on-ok-cancel')
      }).catch((e) => {
      })
    }
  },
  watch: {
    isShow (newVal) {
      if (!newVal) {
        this.optionParam.curPath = ''
        this.optionParam.type = ''
        this.optionParam.newFolderName = ''
        this.optionParam.oldFileName = ''
        this.optionParam.newFileName = ''
        this.optionParam.ysOrJyFileName = ''
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.file-option-modal-div {
  // width: 80%;
  margin: 20px 0;

  .file-option-modal-input {
    width: 70%;
  }
}
</style>
