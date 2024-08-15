<template>
  <div class="file">
    <div>

      <Row>
        <Col span="12" style="padding: 13px 20px 10px 25px">
          <!--          <Card>-->
          <div class="file-login-div" v-show="isFtpStatus === '0'" style="margin-left: 33px">
<!--            <Button type="success" style="margin-right: 20px">正在使用接口服务连接...</Button>-->
            <Button @click="loginFtp" type="primary" icon="ios-jet-outline">使用SFTP连接</Button>
          </div>
          <div class="file-login-div" v-show="isFtpStatus === '1'">
<!--            <Button type="success" style="margin-right: 20px">正在使用FTP连接...</Button>-->
            <Button @click="loginOutFtp" type="warning">退出FTP连接, 使用接口服务连接</Button>
          </div>
          <!--          </Card>-->
        </Col>
        <Col span="12">
          <div class="file-user">
            <Menu @on-select="loginOut" mode="horizontal" theme="light" active-name="1">
              <Submenu name="3">
                <template slot="title">
                  <Icon type="md-person"/>
                  <span>{{ userName }}</span> &nbsp;
                </template>
                <MenuGroup title="系统">
                  <MenuItem name="3-1">退出</MenuItem>
                </MenuGroup>
              </Submenu>
            </Menu>
          </div>
        </Col>
      </Row>

    </div>
    <div style="clear:both"/>
    <hr/>

    <div>
      <file-list2 ref="fileListRef" v-if="isFtpStatus === '0'" @on-handle="handleIsFtpStatus"/>
      <ftp-file-list ref="ftpFileListRef" v-if="isFtpStatus === '1'" @on-handle="handleIsFtpStatus"/>
      <login-ftp-form ref="loginFtpFormRef" @on-handle="handleIsFtpStatus"/>
    </div>

  </div>
</template>

<script>
// import axios from 'axios'
// import fileList from '../file/file-list'
import fileList2 from '../file/file-list2'
import ftpFileList from '../ftp_file/ftp-file-list'
import LoginFtpForm from './component/login-ftp-form'
import {mapGetters, mapMutations} from 'vuex'

export default {
  name: 'home',
  components: {
    fileList2,
    ftpFileList,
    LoginFtpForm
  },
  props: {},
  data () {
    return {
      isFtpStatus: localStorage.getItem('isFtpStatus'),
      userName: this.$cookies.get('userName'),
      userToken: localStorage.getItem('userToken')
    }
  },
  created () {
    // todo 解决页面刷新store的state失效问题
    // todo 在页面加载时读取sessionStorage里的状态信息
    // this.handleFtpConnect()
    // if (sessionStorage.getItem('store')) {
    //   this.$store.replaceState(
    //     Object.assign(
    //       {},
    //       this.$store.state,
    //       JSON.parse(sessionStorage.getItem('store'))
    //     )
    //   )
    // }
    // // 在页面刷新时将vuex里的信息保存到sessionStorage里
    // window.addEventListener('beforeunload', () => {
    //   sessionStorage.setItem('store', JSON.stringify(this.$store.state))
    // })
  },
  computed: {
    ...mapGetters([
      'isFtpModel'
    ])
  },
  mounted () {
    this.tableHeight = window.innerHeight - 220 // 整个浏览器的高度 - table以上的内容高度
  },
  methods: {
    ...mapMutations([
      'set_isFtpModel'
    ]),

    // 登陆到ftp
    loginFtp () {
      this.$refs.loginFtpFormRef.isShow = true
    },
    // 处理状态
    handleIsFtpStatus (status) {
      this.isFtpStatus = status
      localStorage.setItem('isFtpStatus', status)
    },
    // 登出ftp
    loginOutFtp () {
      this.$Modal.confirm({
        title: '退出ftp连接',
        content: '<p>确认要退出ftp连接，使用接口服务连接吗?</p>',
        onOk: () => {
          this.$api.fileStp.ftpLoginOut().then((res) => {
            let resultCode = res.data.code
            // let resultMsg = res.data.msg
            if (resultCode === 0) {
              this.$Notice.success({
                title: '系统提醒',
                desc: '退出ftp连接成功',
                duration: 10
              })
              localStorage.setItem('isFtpStatus', '0')
              this.isFtpStatus = '0'
              // this.set_isFtpModel(false)
              this.isShow = false
            } else {
              this.$Message.error('退出ftp连接异常')
              this.set_isFtpModel(false)
              this.isShow = false
            }
          }).catch((e) => {
            this.$Message.error('接口异常!')
          })
        }
      })
    },
    // 处理ftp连接情况
    handleFtpConnect () {
      this.$api.fileStp.isConnect().then((res) => {
        let resultCode = res.data.code
        // let resultMsg = res.data.msg
        if (resultCode === 0) {
          this.set_isFtpModel(true)
          this.isShow = false
        } else {
          this.set_isFtpModel(false)
          // this.$Notice.warning({
          //   title: '系统提醒',
          //   desc: resultMsg + ' 进入接口服务模式!',
          //   duration: 10
          // });
        }
      }).catch((e) => {
        this.$Message.error('接口异常!')
      })
    },
    // 退出登录
    loginOut () {
      this.$Modal.confirm({
        title: '退出系统',
        content: '<p>确认要退出系统吗?</p>',
        onOk: () => {
          localStorage.removeItem('userToken')
          this.$cookies.remove('userName')
          this.$api.system.loginOutApi().then((res) => {
            let resultCode = res.data.code
            let resultMsg = res.data.msg
            if (resultCode === 0) {
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
  }
}
</script>

<style lang="scss">/*scoped*/
.file {
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
    }

    .file-option-card {
      // height: 500px;
      .file-option-title {
        text-align: center;
        font-size: 20px;
      }

      .file-option-option {
        .file-option-path {
          margin-top: 10px;
          margin-bottom: 25px;
        }

        /*.file-option-path :nth-last-child(1){*/
        /*  margin-top: 5px;*/
        /*  font-size: 16px;*/
        /*  color: #000;*/
        /*  font-weight: 500;*/
        /*  // color: red !important;*/
        /*}*/
        .file-option-refresh {
          margin: 20px 0 50px 20px;
        }

        .file-option-upload {
          margin: 20px 0 50px 20px;
        }

        .file-option-server {
          margin: 20px 0 50px 20px;
        }
      }
    }
  }

}
</style>
