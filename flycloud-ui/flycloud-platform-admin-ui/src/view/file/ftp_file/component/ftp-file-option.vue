<template>
  <div>
    <Drawer
      v-model="isShow"
      :title="title"
      placement="right"
      :closable="false"
      width="30%"
      class="file-option-modal"
    >
      <div class="file-option-list">
        <div class="file-option-comm">
          <Button @click="reNameFile" type="primary" shape="circle" icon="md-albums">重命名</Button>
        </div>
        <div v-show="fileData.fileType === '2'" class="file-option-comm">
          <Button @click="downloadFile" type="primary" shape="circle" icon="md-cloud-download">下载</Button>
        </div>
        <div class="file-option-comm">
          <Button @click="compressFile" type="primary" shape="circle" icon="md-attach">压缩</Button>
        </div>
        <div class="file-option-comm">
          <Button @click="deleteFile" type="error" shape="circle" icon="md-trash">删除</Button>
        </div>
        <div v-show="fileData.fileType === '2' && isOptionServerShow" class="file-option-comm">
          <Button @click="execute" type="success" shape="circle" icon="md-play">执行</Button>
        </div>
      </div>
    </Drawer>
    <div>
      <div>
        <ftp-file-option-modal ref="fileOptionModalRef" @on-ok-cancel="onOkCancel"/>
      </div>
    </div>
  </div>
</template>

<script>
import FtpFileOptionModal from './ftp-file-option-modal.vue'
import {isNull} from '../../../../util/stringUtils'
import {download} from '../../../../util/common'

export default {
  name: 'ftp-file-option',
  components: {
    FtpFileOptionModal
  },
  props: {},
  data () {
    return {
      isShow: false,
      title: '操作',
      fileData: {
        fileType: '',
        fileName: '',
        type: '' // 文件类型 只记录有.后缀的文件类型
      },
      optionServerFile: ['sh'], // 允许启动/关闭服务的指定文件类型
      isOptionServerShow: false
    }
  },
  created () {

  },
  methods: {

    // 父组件调用方法
    getFileOptionPop (fileData) {
      if (this.optionServerFile.indexOf(fileData.type) !== -1) {
        this.isOptionServerShow = true
      }
      this.fileData = fileData
      let fileTypeText = this.fileData.fileType === '1' ? '文件夹' : '文件'
      this.title = '你要对该' + fileTypeText + ' ' + this.fileData.fileName + ' 进行如下操作'
      this.isShow = true
    },
    // 子组件回调方法
    onOkCancel () {
      this.isShow = false
      this.$emit('on-ok-cancel')
    },

    // 重命名文件/夹
    reNameFile () {
      let optionData = {
        type: '3',
        curPath: this.fileData.curPath,
        oldFileName: this.fileData.fileName
      }
      this.$refs.fileOptionModalRef.getFileOptionModalPop(optionData)
    },

    // 压缩文件/夹
    compressFile () {
      let optionData = {
        type: '5',
        curPath: this.fileData.curPath,
        ysOrJyFileName: this.fileData.fileName
      }
      this.$refs.fileOptionModalRef.getFileOptionModalPop(optionData)
    },

    // 下载文件
    downloadFile () {
      let curPath = this.fileData.curPath
      let fileName = this.fileData.fileName
      if (isNull(curPath) || isNull(fileName)) {
        this.$Message.error('当前服务器路径 | 文件 不能为空!')
        return
      }
      this.$Modal.confirm({
        title: '下载',
        content: '<p">确认下载该文件 ' + fileName + ' 吗?</p>',
        onOk: () => {
          // window.location.href = this.$api.fileStp.downloadFtpFileApi(curPath, fileName)

          let params = {
            path: curPath,
            fileName: fileName
          }
          download(this.$api.fileStp.downloadFtpFilePath, params, fileName)
        }
      })
    },

    // 删除文件
    deleteFile () {
      let curPath = this.fileData.curPath
      let fileName = this.fileData.fileName
      if (isNull(curPath) || isNull(fileName)) {
        this.$Message.error('当前服务器路径 | 文件 不能为空!')
        return
      }
      let fileTypeText = this.fileData.fileType === '1' ? '文件夹' : '文件'
      this.$Modal.confirm({
        title: '删除',
        content: '<p style="color: red">确认删除该' + fileTypeText + ' ' + fileName + ' 吗?</p>',
        onOk: () => {
          let optionData = {
            type: '4',
            curPath: curPath,
            deleteFileName: fileName
          }
          this.executeOptionServer(optionData)
        }
      })
    },

    // 执行文件
    execute () {
      let curPath = this.fileData.curPath
      let fileName = this.fileData.fileName
      if (isNull(curPath) || isNull(fileName)) {
        this.$Message.error('当前服务器路径 | 文件 不能为空!')
        return
      }
      this.$Modal.confirm({
        title: '执行',
        content: '<p style="color: red">确认要执行该文件 ' + fileName + ' 吗?</p>',
        onOk: () => {
          let optionData = {
            type: '10',
            curPath: curPath,
            executeFileName: fileName
          }
          this.executeOptionServer(optionData)
        },
        onCancel: () => {
          // this.$Message.info('已取消')
        }
      })
    },

    /**
     * 执行操作
     * curPath: 服务器当前路径
     * type: 操作类型(0:新建文件夹, 1:启动服务, 2:关闭服务, 3:文件重命名, 4:删除文件/夹, 5:压缩文件/夹, 6:解压文件/夹,10:执行文件)
     * newFolderName: 新建文件夹名称
     * optionServerFileName: 指定文件启动|关闭服务的名称
     * oldFileName: 旧文件名名称
     * newFileName: 新文件名名称
     * deleteFileName: 删除文件名名称
     * ysOrJyFileName: 要压缩/解压文件/夹名称
     * ysToFileName: 压缩成包的名称
     * @param optionData
     */
    executeOptionServer (optionData) {
      this.$api.fileStp.executeOptionFtpApi(optionData).then((res) => {
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
            desc: resultMsg
          })
        }
        this.isShow = false
        this.$emit('on-ok-cancel')
      }).catch((e) => {
      })
    }
  },
  computed: {},
  watch: {
    isShow (newVal) {
      if (!newVal) {
        this.isOptionServerShow = false
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.file-option-modal {
  text-align: center;

  .file-option-list {
    padding: 20px 60px 0 10px;

    .file-option-comm {
      margin: 20px 0 50px 20px;
    }
  }
}
</style>
