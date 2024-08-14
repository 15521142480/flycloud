<template>
  <div class="file" :style="{height: contentHeight + 'px'}">

    <Split v-model="splitNum">

      <div slot="left">
        <Row>
          <Col span="24">
            <Card class="file-list-card">
              <Breadcrumb class="file-path-title">
                <BreadcrumbItem v-for="(item, index) in pathList" :key="index">
                  <span v-if="index === 0" class="file-path-text" @click="getFileListByIndex(index)">根目录</span>
                  <span v-else class="file-path-text" @click="getFileListByIndex(index)">{{ item }}</span>
                </BreadcrumbItem>
              </Breadcrumb>

              <Table
                :columns="tableColumns"
                :data="tableData"
                no-data-text="暂无文件"
                :loading="loading"
                :height="tableHeight"
              />
            </Card>
          </Col>
        </Row>
      </div>

      <div slot="right">
        <Row>
          <Col span="24">
            <p style="font-size: 16px; text-align: center; font-weight: bold; padding: 10px 10px">接口服务器</p>
            <Card class="file-option-card" :style="{height: (tableHeight + 58) + 'px'}">
              <p slot="title" class="file-option-title">操作</p>

              <div class="file-option-option">
                <h3>
                  <div>当前目录:</div>
                  <div class="file-option-path">{{ curPath }}</div>
                </h3>

                <div class="file-option-refresh">
                  <Button @click="init" type="primary" shape="circle" icon="md-refresh" style="margin-right: 45px">刷新
                  </Button>
                </div>
                <div class="file-option-refresh">
                  <Button @click="newFolder" type="primary" shape="circle" icon="md-add-circle">新建文件夹</Button>
                </div>
                <div class="file-option-upload">

                  <!-- //是否支持多文件上传 //显示上传的文件 //上传前需要的一些操作 //上传的文件保存的地方 //上传成功时调用的方法 //这里是设置的请求头token //这是上传时的后台接口 -->
                  <Upload
                    ref="upload"
                    action="api"
                    :max-size="fileMaxSize"
                    :format="['jpg']"
                    :show-upload-list="false"
                    :before-upload="handleUpload"
                    :on-success="uploadSuccess"
                    :headers='{"userToken": userToken}'
                  >
                    <Button type="primary" shape="circle" icon="md-cloud-upload">上传文件</Button>
                  </Upload>
                </div>
              </div>

              <Row>
                <Col span="6">
                  &nbsp;
                </Col>
                <Col span="12">
                  <Poptip trigger="hover" style="margin-top: 150px; line-height: 25px">
                    <Button style="font-size: 16px; color: #eaa335">接口服务使用提醒</Button>
                    <div class="api" slot="content">
                      <Card style="line-height: 25px">
                        <p>1. 如题所示, 该功能是将后端所在的服务器的文件夹进行操作</p>
                        <p>2. 与sftp相比, 无需连接通讯信息即可访问文件夹</p>
                        <p>3. 功能: 路由跳转/上传/下载/运行文件...</p>
                        <p>4. 更多功能请点击左上角的sftp连接 (可sftp与shell同时使用)</p>
                      </Card>
                    </div>
                  </Poptip>
                </Col>
                <Col span="6">
                  &nbsp;
                </Col>
              </Row>
            </Card>
          </Col>
        </Row>
      </div>
    </Split>

    <div>
      <file-option ref="fileOptionRef" @on-ok-cancel="init"/>
      <file-option-modal ref="fileOptionModalRef" @on-ok-cancel="init"/>
    </div>

    <!-- 上传loading(整个页面) -->
    <Spin fix v-show="uploadLoading">
      <Icon type="ios-loading" size=18 class="demo-spin-icon-load"></Icon>
      <div>上传中,请耐心等待...</div>
    </Spin>

  </div>
</template>

<script>
import axios from 'axios'
import FileOption from './component/file-option'
import FileOptionModal from './component/file-option-modal'

export default {
  name: 'file-list',
  components: {
    FileOption,
    FileOptionModal
  },
  props: {},
  data () {
    return {
      userName: this.$cookies.get('userName'),
      userToken: this.$cookies.get('userToken'),
      splitNum: 0.75,
      curPath: '/',
      pathList: ['/'],
      params: {
        path: '/'
        // path: '/Users/laixueshi/'
      },
      contentHeight: 0,
      file: [],
      fileList: [],
      permissionUploadFileTypeList: ['jar', 'war', 'sh', 'yml', 'conf', 'out', 'png', 'jpg'], // 允许上传文件的类型
      fileMaxSize: 1000 * 1024,
      uploadLoading: false,
      tableHeight: 0,
      loading: false,
      percent: 0, // 上传的进度
      getUploadPercentTimer: '', // 获取上传进度定时器
      isUploadSuccess: false, // 是否上传成功
      tableColumns: [
        {
          title: '文件名',
          key: 'fileName',
          render: (h, params) => {
            let fileName = params.row.fileName
            let fileType = params.row.fileType
            let src = ''
            if (fileType === '1') {
              src = require('../../assets/folder_images/folder-min.png')
            } else {
              src = require('../../assets/folder_images/file.png')
            }
            return h('div',
              {
                style: 'display: flex; cursor: pointer',
                on: {
                  click: () => {
                    this.getFileListPath(fileName, fileType)
                  }
                }
              },
              [
                h('img', {
                  attrs: {
                    src: src, style: 'width: 30px;height: 30px;margin-top: 5px;'
                  }
                }),
                h('span', {
                  style: {
                    // alignSelf: center,
                    marginTop: '11px',
                    marginLeft: '5px',
                    fontSize: '15px',
                    fontWeight: 500
                  }
                }, fileName)
              ])
          }
        },
        {
          title: '大小',
          key: 'fileSize',
          width: 80
        },
        {
          title: '类型',
          key: 'fileTypeText',
          width: 80
        },
        {
          title: '修改时间',
          key: 'updateTime',
          width: 150
        },
        {
          title: '权限',
          key: 'permission',
          width: 100
        },
        {
          title: '用户/用户组',
          key: 'userOrGroup',
          width: 110
        },
        {
          title: '操作',
          key: 'operation',
          width: 110,
          align: 'center',
          render: (h, params) => {
            const fileData = params.row
            let btnList = []

            // if (fileData.fileType === '2') {
            let optionBtn = h('Button', {
              props: {
                type: 'default',
                size: 'small',
                shape: 'circle',
                icon: 'md-options'
              },
              style: {
                marginRight: '5px'
              },
              on: {
                click: () => {
                  fileData.curPath = this.curPath
                  this.$refs.fileOptionRef.getFileOptionPop(fileData)
                }
              }
            }, '操作')
            btnList.push(optionBtn)
            // }

            return h('div', btnList)
          }
        }
      ],
      tableData: []
    }
  },
  created () {
    this.init()
  },
  computed: {
    percentColor () {
      let color = '#2db7f5'
      if (this.percent === 100) {
        color = '#5cb85c'
      }
      return color
    },
    percentTitle () {
      let title = '上传中...'
      if (this.percent === 100) {
        title = '上传成功!'
      }
      return title
    }
  },
  mounted () {
    this.tableHeight = window.innerHeight - 180 // 整个浏览器的高度 - table以上的内容高度
    this.contentHeight = window.innerHeight - 80
  },
  methods: {
    // 初始化信息
    init () {
      this.loading = true
      // this.$api.fileStp.isConnectApi().then((res) => {
      //   let resultCode = res.data.resultCode
      //   let conCode = res.data.connectCode
      //   let resultMsg = res.data.resultMsg
      //   if (resultCode === '1') {
      //     if (conCode === '1') {
      //       this.$api.fileStp.getFileListApi(this.params).then((res) => {
      //         let resultCode = res.data.resultCode
      //         if (resultCode === "1") {
      //           this.tableData = res.data.data
      //         }
      //       }).catch((e) => {
      //         this.loading = false
      //       });
      //     } else if (conCode === '2') {
      //       alert(resultMsg)
      //     } else if (conCode === '3') {
      //       alert(resultMsg)
      //     } else {
      //       alert('未知状态!')
      //     }
      //   }
      // }).catch((e) => {
      //   this.loading = false
      // });

      this.$api.file.getFileListApi(this.params).then((res) => {
        let resultCode = res.data.resultCode
        if (resultCode === '1') {
          this.tableData = res.data.data
        }
        this.loading = false
        this.uploadLoading = false
      }).catch((e) => {
        this.loading = false
      })
    },
    // 文件列表跳转
    getFileListPath (fileName, fileType) {
      if (fileType === '1') { // 文件夹
        if (this.curPath === '/') {
          this.curPath += fileName
        } else {
          this.curPath += '/' + fileName
        }
        // this.curPath = this.curPath.slice(0, this.curPath.length - 1)
        this.pathList.push(fileName)
        this.params.path = this.curPath
        this.init()
      }
    },
    // 文件导航跳转
    getFileListByIndex (index) {
      let curPath = ''
      let newPathList = this.pathList.splice(0, index + 1)
      newPathList.forEach((item, index) => {
        if (index === 0) {
          curPath = item
        } else if (index === 1) {
          curPath += item
        } else {
          curPath += '/' + item
        }
      })
      this.curPath = curPath
      this.pathList = newPathList
      this.params.path = curPath
      this.init()
    },
    // 新建文件夹
    newFolder () {
      let optionData = {
        type: '0',
        curPath: this.curPath
      }
      this.$refs.fileOptionModalRef.getFileOptionModalPop(optionData)
    },

    // ========== 上传文件 start
    uploadFile (file) {
      // this.uploadLoading = true
      this.percent = 0
      let formData = new FormData()
      formData.append('file', file)
      formData.append('curPath', this.curPath)
      this.executeGetUploadPercentTimer() // 执行获取上传进度定时器
      axios({
        url: this.$api.file.uploadFileApiPath,
        method: 'post',
        headers: {
          'Content-Type': 'multipart/form-data',
          userToken: this.userToken
        },
        data: formData
      }).then((res) => {
        let resultCode = res.data.resultCode
        // let resultMsg = res.data.resultMsg
        if (resultCode === '1') {
          // this.isUploadSuccess = true
          this.init()
          // this.uploadLoading = false
          // this.$Notice.success({title: '操作提醒', desc: resultMsg})
        }
        // this.uploadLoading = false
      })
    },
    uploadSuccess (response, file, fileList) {
    },
    clear () {
      this.$refs.upload.clearFiles() // 清除上次上传记录
    },

    /**
     * 获取上传进度接口
     */
    getUploadPercent () {
      this.$api.file.uploadFilePercentApi().then((res) => {
        this.percent = parseInt(res.data)
        console.log(this.percent)
        if (this.percent === 100) {
          clearInterval(this.getUploadPercentTimer)
          this.init()
          // this.isUploadSuccess = false
          // if (!this.isUploadSuccess) { // 控制接口过快导致的问题
          //   this.percent = 50
          // } else {
          //   clearInterval(this.getUploadPercentTimer);
          //   this.init()
          //   this.isUploadSuccess = false
          // }
        }
      }).catch((e) => {
      })
    },

    /**
     * 执行获取上传进度定时器
     */
    executeGetUploadPercentTimer () {
      // let typeShow = this.percent === 100 ? 'success' : 'info'
      this.$Notice.open({ // success info
        // title: this.percentTitle,
        title: '上传文件',
        duration: 0,
        // desc: '上传进度...',
        render: h => {
          let percentTextList = []
          let notSuccess = h('span', {
            style: {fontSize: '24px'},
            class: 'demo-Circle-inner'
          }, this.percent + '%')

          let success = h('Icon', {
            props: {
              type: 'ios-checkmark',
              size: 60
            },
            style: {color: '#5cb85c'}
          })

          if (this.percent === 100) {
            percentTextList.push(success)
          } else {
            percentTextList.push(notSuccess)
          }

          return h('i-circle',
            {
              props: { // props: 自定义的属性对象
                percent: this.percent,
                strokeColor: this.percentColor
              },
              style: {marginLeft: '80px'}
            },
            percentTextList
          )
        }
      })
      this.getUploadPercentTimer = setInterval(this.getUploadPercent, 250)
    },

    // 上传文件前的事件钩子
    handleUpload (file) {
      // 大小校验(单位: kb)
      let size = file.size / 1000
      if (size > this.fileMaxSize) {
        this.$Notice.error({
          title: '文件大小超限!',
          desc: '文件  ' + file.name + ' 太大，不能超过1000M!'
        })
        return false
      }

      // 文件类型校验
      // let fileType = file.name.substring(file.name.lastIndexOf('.') + 1)
      // if (this.permissionUploadFileTypeList.indexOf(fileType) === -1) {
      //   this.$Notice.error({
      //     title: '文件类型超限!',
      //     desc: '文件  ' + file.name + ' 类型超出限制!'
      //   });
      //   return false
      // }

      // 文件过大提醒
      // if (size > 10 * 1024) {
      //   this.$Notice.warning({
      //     title: '正在上传...',
      //     desc: '文件过大,请耐心等待...',
      //     duration: 5
      //   });
      // }

      // 选择文件后 这里判断文件类型 保存文件 自定义一个keyid 值 方便后面删除操作
      // let keyID = Math.random().toString().substr(2);
      // file['keyID'] = keyID;

      this.file.push(file) // 保存文件到总展示文件数据里
      this.fileList.push(file) // 保存文件到需要上传的文件数组里
      this.uploadFile(file) // 上传接口

      return false // 手动上传
    },
    // ========== 上传文件 end

    // 退出登录
    loginOut () {
      this.$Modal.confirm({
        title: '退出系统',
        content: '<p>确认要退出系统吗?</p>',
        onOk: () => {
          this.$api.system.loginOutApi().then((res) => {
            let resultCode = res.data.resultCode
            let resultMsg = res.data.resultMsg
            if (resultCode === '1') {
              this.$Notice.success({title: '操作提醒', desc: resultMsg})
            } else {
              this.$Notice.error({title: '操作提醒', desc: '操作失败'})
            }
            this.$cookies.remove('userToken')
            this.$cookies.remove('userName')
          }).catch((e) => {
          })
          this.$router.push('/')
        }
      })
    }
  },
  watch: {
    curPath (newVal) {
      // this.params.path = newVal
    }
    // percent (newVal) {
    //   if (newVal === 100) {
    //     if (!this.isUploadSuccess) {
    //       this.percent = 80
    //     } else {
    //       this.isUploadSuccess = false
    //       this.percent = 0
    //     }
    //   }
    // }
  }
}
</script>

<style lang="scss">/*scoped*/
.file {

}

// 上传loading(整个页面)
.demo-spin-icon-load {
  animation: ani-file 1s linear infinite;
}

@keyframes ani-file {
  from {
    transform: rotate(0deg);
  }
  50% {
    transform: rotate(180deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.file-user {
  float: right;
}

.file-row {
  padding: 0 50px 0 50px;

  .file-user-card {
    padding-top: 200%;

    .file-login-div {
      font-size: 20px;
    }
  }
}

.file-list-card {
  padding: 30px 40px 40px 40px;

  .file-path-title {
    font-size: 16px;
    margin-bottom: 7px;

    .file-path-text {
      cursor: pointer;
    }

    .file-path-text:hover {
      color: cornflowerblue;
    }
  }

  div.ivu-card-body {
    padding: 0;
  }

  div.ivu-table-wrapper {
    border: none;
  }
}

.ivu-table:before {
  content: '';
  width: 100%;
  height: 0px;
  position: absolute;
  left: 0;
  bottom: 0;
  z-index: 1
}

.ivu-table:after {
  content: '';
  width: 0px;
  height: 100%;
  position: absolute;
  top: 0;
  right: 0;
  z-index: 3
}

.file-option-card {
  // height: 500px;
  .file-option-title {
    text-align: center;
    font-size: 20px;
  }
}

.file-option-option {
  .file-option-path {
    margin-top: 10px;
    margin-bottom: 25px;
  }
}

.file-option-refresh {
  margin: 20px 0 50px 20px;
}

.file-option-upload {
  margin: 20px 0 50px 20px;
}

.file-option-server {
  margin: 20px 0 50px 20px;
}
</style>
