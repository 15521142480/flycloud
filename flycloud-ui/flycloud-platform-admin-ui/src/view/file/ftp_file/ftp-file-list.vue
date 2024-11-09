<template>
  <div class="file" :style="{height: contentHeight + 'px'}">

    <Split v-model="splitNum">

      <div slot="left">
        <Row>
          <Col span="24">

            <Row>
              <Col span="24">
                <p style="font-size: 16px; text-align: center; font-weight: bold;margin-bottom: 10px;margin-top: 8px">ftp服务器</p>
                <p slot="title" class="file-option-title">操作</p>
                <div class="file-option-option">
<!--                    <h3>-->
<!--                      <div>当前目录:</div>-->
<!--                      <div style="margin-top: 7px; margin-bottom: 6px;">{{ curPath }}</div>-->
<!--                    </h3>-->
                  <Row>
                    <Col span="1">
                      &nbsp;
                    </Col>
                    <Col span="4">
                      <div>
                        <Button @click="init" type="primary" shape="circle" icon="md-refresh"
                                style="margin-right: 45px">
                          刷新
                        </Button>
                      </div>
                    </Col>
                    <Col span="5">
                      <div>
                        <Button v-show="checkHasPermission('file.create')" @click="newFolder" type="primary" shape="circle" icon="md-add-circle">新建文件夹
                        </Button>
                      </div>
                    </Col>
                    <Col span="5">
                      <div v-show="checkHasPermission('file.upload')">
                        <!-- //是否支持多文件上传 //显示上传的文件 //上传前需要的一些操作 //上传的文件保存的地方 //上传成功时调用的方法 //这里是设置的请求头token //这是上传时的后台接口 -->
                        <Upload
                          ref="upload"
                          action="api"
                          :max-size="fileMaxSize"
                          :show-upload-list="false"
                          :before-upload="handleUpload"
                          :on-success="uploadSuccess"
                          :headers='{"userToken": userToken}'
                        >
                          <Button type="primary" shape="circle" icon="md-cloud-upload">上传文件</Button>
                        </Upload>
                      </div>
                    </Col>
                  </Row>
                </div>
              </Col>
            </Row>

            <div style="margin: 15px 30px 20px 30px">
<!--                <hr>-->
            </div>

            <Row>
              <Breadcrumb class="ftp-file-path-title">
                <BreadcrumbItem v-for="(item, index) in pathList" :key="index">
                  <span v-if="index === 0" class="file-path-text" @click="getFileListByIndex(index)">根目录</span>
                  <span v-else class="file-path-text" @click="getFileListByIndex(index)">{{ item }}</span>
                </BreadcrumbItem>
              </Breadcrumb>

              <Table
                class="file-table"
                :columns="tableColumns"
                :data="tableData"
                no-data-text="暂无文件"
                :loading="loading"
                :height="tableHeight"
              />

            </Row>

          </Col>
        </Row>
      </div>

      <div slot="right" style="padding: 10px 10px 10px 15px">
        <!--        <Card class="api_file-option-card">-->
        <Row>
          <Col span="24">
            <div id="xterm"></div>
          </Col>
        </Row>
        <br/>
        <Input v-model="value" :autofocus="true" :search="true" @on-search="check">
          <span class="textbox" slot="prepend">{{ text }}</span>
        </Input>

        <Row style="margin-top: 40px">
          <Col span="8">
            &nbsp;
          </Col>
          <Col span="8">
            <Poptip trigger="hover">
              <Button style="font-size: 16px; color: #eaa335">sftp远程与shell指令使用提醒</Button>
              <div class="api" slot="content">
                <Card style="line-height: 25px">
                  <p>1. 支持sftp与shell操作 (目前只支持Linux，输入指令按回车键)</p>
                  <p>3. 支持cd指令左边的文件目录自动跳转</p>
                  <p>4. 过滤敏感指令</p>
                  <p>5. 不支持如docker、redis等长链接会话</p>
                </Card>
              </div>
            </Poptip>
          </Col>
          <Col span="8">
            &nbsp;
          </Col>
        </Row>
        <!--        </Card>-->
      </div>
    </Split>

    <div>
      <ftp-file-option ref="fileOptionRef" @on-ok-cancel="init"/>
      <ftp-file-option-modal ref="fileOptionModalRef" @on-ok-cancel="init"/>
    </div>

    <!-- 上传loading(整个页面) -->
    <Spin fix v-show="uploadLoading">
      <Icon type="ios-loading" size=18 class="demo-spin-icon-load"></Icon>
      <div>上传中,请耐心等待...</div>
    </Spin>

  </div>
</template>

<script>
import { getUserToken, hasPermission } from '../../../util/cacheUtils'
import axios from 'axios'
import FtpFileOption from './component/ftp-file-option.vue'
import FtpFileOptionModal from './component/ftp-file-option-modal.vue'
import 'xterm/css/xterm.css'
import {Terminal} from 'xterm'
import {FitAddon} from 'xterm-addon-fit'

export default {
  name: 'ftp-file-list',
  components: {
    FtpFileOption,
    FtpFileOptionModal
  },
  props: {},
  data () {
    return {
      userToken: getUserToken(),
      splitNum: 0.58,
      value: '',
      text: '[root@ ~]#',
      term: null,
      curPath: '/',
      pathList: ['/'],
      params: {
        path: '/'
        // path: '/Users/laixueshi/'
      },
      file: [],
      fileList: [],
      permissionUploadFileTypeList: ['jar', 'war', 'sh', 'yml', 'conf', 'out', 'png', 'jpg'], // 允许上传文件的类型
      fileMaxSize: 1000 * 1024,
      uploadLoading: false,
      contentHeight: 0,
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
              src = require('../../../assets/folder_images/folder-min.png')
            } else {
              src = require('../../../assets/folder_images/file.png')
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
                    src: src, style: 'width: 22px;height: 26px;margin-top: 5px;'
                  }
                }),
                h('span', {
                  style: {
                    // alignSelf: center,
                    marginTop: '9px',
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
          width: 75
        },
        {
          title: '类型',
          key: 'fileTypeText',
          width: 75
        },
        {
          title: '修改时间',
          key: 'updateTime',
          width: 100
        },
        {
          title: '权限',
          key: 'permission',
          width: 100
        },
        // {
        //   title: '用户/组',
        //   key: 'userOrGroup'
        // },
        {
          title: '操作',
          key: 'operation',
          width: 90,
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
    this.initTerm()
    this.tableHeight = window.innerHeight - 220 // 整个浏览器的高度 - table以上的内容高度
    this.contentHeight = window.innerHeight - 80
  },
  methods: {
    // 初始化信息
    init () {
      this.loading = true
      // this.$api.fileStp.isConnectApi().then((res) => {
      //   let resultCode = res.data.code
      //   let conCode = res.data.connectCode
      //   let resultMsg = res.data.msg
      //   if (resultCode === 0) {
      //     if (conCode === '1') {
      //       this.$api.fileStp.getFileListApi(this.params).then((res) => {
      //         let resultCode = res.data.code
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

      this.$api.fileStp.getFtpFileListApi(this.params.path).then((res) => {
        let resultCode = res.data.code
        if (resultCode === 0) {
          this.tableData = res.data.data
        } else {
          this.$Message.error(res.data.msg)
          this.$emit('on-handle', '0')
        }
        this.loading = false
        this.uploadLoading = false
      }).catch((e) => {
        this.$Message.error('接口异常')
        this.$emit('on-handle', '0')
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
    uploadFile2 (file) {
      // this.uploadLoading = true
      this.percent = 0
      let formData = new FormData()
      formData.append('file', file)
      formData.append('curPath', this.curPath)
      // this.executeGetUploadPercentTimer() // 执行获取上传进度定时器

      axios({
        url: this.$api.fileStp.uploadFtpFileApiPath,
        method: 'post',
        headers: {
          'Content-Type': 'multipart/form-data',
          Authorization: 'Bearer ' + this.userToken
        },
        data: formData
      }).then((res) => {
        let resultCode = res.data.code
        // let resultMsg = res.data.msg
        if (resultCode === 0) {
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
      // this.$api.api_file.uploadFilePercentApi().then((res) => {
      //   this.percent = parseInt(res.data)
      //   console.log(this.percent)
      //   if (this.percent === 100) {
      //     clearInterval(this.getUploadPercentTimer);
      //     this.init()
      //     // this.isUploadSuccess = false
      //     // if (!this.isUploadSuccess) { // 控制接口过快导致的问题
      //     //   this.percent = 50
      //     // } else {
      //     //   clearInterval(this.getUploadPercentTimer);
      //     //   this.init()
      //     //   this.isUploadSuccess = false
      //     // }
      //   }
      // }).catch((e) => {
      // });
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

      let judgeFn = new RegExp(/\s+/g)
      if (judgeFn.test(file.name)) {
        alert('文件名包含有空格!')
        return false
      }

      // 文件类型校验
      // let fileType = api_file.name.substring(api_file.name.lastIndexOf('.') + 1)
      // if (this.permissionUploadFileTypeList.indexOf(fileType) === -1) {
      //   this.$Notice.error({
      //     title: '文件类型超限!',
      //     desc: '文件  ' + api_file.name + ' 类型超出限制!'
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
      // api_file['keyID'] = keyID;

      this.file.push(file) // 保存文件到总展示文件数据里
      this.fileList.push(file) // 保存文件到需要上传的文件数组里
      this.uploadFile2(file) // 上传接口

      return false // 手动上传
    },
    // ========== 上传文件 end

    /**
     * 初始化 term
     */
    initTerm () {
      const term = new Terminal({
        fontSize: 14,
        rendererType: 'canvas', // 渲染类型
        rows: 35, // 行数
        convertEol: true, // 启用时，光标将设置为下一行的开头
        // scrollback: 10, // 终端中的回滚量
        disableStdin: false, // 是否应禁用输入
        cursorStyle: 'underline', // 光标样式
        // cursorBlink: true, // 光标闪烁
        theme: {
          foreground: 'yellow', // 字体
          background: '#060101', // 背景色
          cursor: 'help' // 设置光标
        }

      })
      // const attachAddon = new AttachAddon(this.socket);
      const fitAddon = new FitAddon()
      // term.loadAddon(attachAddon);
      term.loadAddon(fitAddon)
      term.open(document.getElementById('xterm'))
      fitAddon.fit()
      term.focus()

      // let that = this
      function runFakeTerminal () {
        if (term._initialized) {
          return
        }

        term._initialized = true
      }

      runFakeTerminal()
      this.term = term

      setTimeout(() => {
        this.write('连接主机中...')
        setTimeout(() => {
          this.write('连接主机成功')
          setTimeout(() => {
            this.write('Welcome to Person Web Shell Command Service ! \n')
          }, 300)
        }, 500)
      }, 300)
    },

    /**
     * 改变input
     */
    check () {
      if (this.value) {
        this.term.write(this.text + '  ' + this.value)
        this.term.writeln('')
        this.executeCommand()
      }
    },

    /**
     * 执行命令
     */
    executeCommand () {
      let params = {
        curPath: this.curPath,
        cmd: this.value
      }
      this.$api.fileStp.executeCommandFtpApi(params).then((res) => {
        this.value = ''
        let data = res.data.data
        let resultCode = res.data.code
        let resultMsg = res.data.msg

        let newCurPath = data.newCurPath
        if (resultCode === 0) {
          // if (Object.prototype.hasOwnProperty.call(info, 'prefix')) {
          //   this.text = info.prefix
          // }
          // this.write(info.content)
          this.write(data.cmdResult)

          // todo 处理新路径
          this.initListByCmdToCd(newCurPath)

          this.$Notice.success({title: '操作提醒', desc: '指令 ' + params.cmd + ' ' + resultMsg, duration: 1.5})
        } else {
          this.$Notice.error({
            title: '操作提醒',
            desc: '指令 ' + params.cmd + ' 执行失败! 原因' + '  -->  ' + resultMsg
          })
          this.show = true
        }
      }).catch((e) => {
        this.$Notice.error({title: '操作提醒', desc: '异常!'})
      })
    },

    write (msg) {
      this.term.writeln(msg)
    },

    containsStr (subStr, str) {
      // eslint-disable-next-line no-eval
      let reg = eval('/' + subStr + '/ig')
      return reg.test(str)
    },

    // eslint-disable-next-line camelcase
    initListByCmdToCd (cmd_cd_path) {
      // eslint-disable-next-line camelcase
      if (cmd_cd_path !== '') {
        // eslint-disable-next-line camelcase
        this.curPath = cmd_cd_path
        // eslint-disable-next-line camelcase
        this.params.path = cmd_cd_path
        this.pathList = cmd_cd_path.split('/')
        this.pathList[0] = '/'

        // eslint-disable-next-line camelcase
        if (cmd_cd_path === '/') {
          this.pathList = ['/']
        }
        this.init()
      }
    },

    // 检查操作权限
    checkHasPermission (btnPermission) {
      return hasPermission(btnPermission)
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

div.ivu-card-body {
  padding: 10px 15px 0 15px;
}

.file-table {
  border: none;
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
  padding: 15px 10px 0 10px;
}

.file-user-card {
  padding-top: 200%;

  .file-login-div {
    font-size: 20px;
  }
}

.ftp-file-path-title {
  font-size: 16px;
  margin-bottom: 10px;
  margin-left: 10px;

  .file-path-text {
    cursor: pointer;
  }

  .file-path-text:hover {
    color: cornflowerblue;
  }
}

.file-option-card {
  // height: 500px;
  margin: 15px 15px 15px 15px ;
  .file-option-title {
    text-align: center;
    font-size: 20px;
  }
}
</style>
