<template>
  <div class="login">
    <div class="form-tag">
      <Card>
        <div class="head">
          <h2 class="title">飞翔云平台管理系统</h2>
        </div>
        <Form class="form-form" ref="dataForm" :model="dataForm" :rules="ruleInline">
          <FormItem prop="loginName">
            <Input class="form-item-input" type="text" v-model="dataForm.loginName" placeholder="账号">
              <Icon type="ios-person-outline" slot="prepend"></Icon>
            </Input>
          </FormItem>
          <FormItem prop="password">
            <Input type="password" v-model="dataForm.password" placeholder="密码">
              <Icon type="ios-lock-outline" slot="prepend"></Icon>
            </Input>
          </FormItem>
          <FormItem prop="code">
            <Row>
              <Col span="14" style="margin-top: 3px">
                <Input v-model="dataForm.code" placeholder="验证码"></Input>
              </Col>
              <Col span="1" style="text-align: center">&nbsp;</Col>
              <Col span="8">
                <img v-if="codeUrl" :src="codeUrl" style="cursor: pointer;" @click="getCode" />
                <Button v-else @click="getCode()">点击获取</Button>
              </Col>
            </Row>
          </FormItem>
          <FormItem>
            <Row>
              <Col span="15">
                <Button type="primary" style="margin-left: 120px" @keyup.enter.native="enterLogin()" @click="handleSubmit()">登录</Button>
              </Col>
              <Col span="6">
                <Button type="info" style="margin-left: 60px">注册</Button>
              </Col>
            </Row>
          </FormItem>

        </Form>
      </Card>
    </div>
  </div>
</template>

<script>
// let Base64 = require('js-base64').Base64
import md5 from 'js-md5'
import axios from 'axios'
export default {
  name: 'login',
  components: {},
  props: {},
  data () {
    return {
      dataForm: {
        loginName: 'adminfile',
        password: 'admin123456',
        code: ''
      },
      codeKey: '',
      codeUrl: '',
      ruleInline: {
        loginName: [{required: true, message: '用户不能为空!', trigger: 'blur'}],
        password: [{required: true, message: '密码不能为空!', trigger: 'blur'}],
        code: [{required: true, message: '验证码不能为空!', trigger: 'blur'}]
      }
    }
  },
  created () {
    // this.enterLogin()
    // this.initLoginInfo()
    this.getCode()
  },
  computed: {
  },
  methods: {

    // 初始化登陆信息，自动化赋值
    initLoginInfo () {
      // todo 旧
      // let date = new Date()
      // const week = date.getDay() // 0 表示周日，1 到 6 表示周一到周六
      // // week = date.getDay() === 0 ? '7' : date.getDay();
      // let curMonth = date.getMonth() + 1
      // curMonth = curMonth.toString().replace(/0/g, '')
      // let curDay = date.getDate()
      // curDay = curDay.toString().replace(/0/g, '')
      // // this.dataForm.loginName = '23023'.replace(/0/g, '')
      // this.dataForm.loginName = 'admin' + week
      // this.dataForm.password = 'admin' + (week * curMonth * curDay)
    },
    // 这是定义的触发回车的事件 此函数必须初始化的时候执行一次此函数 否则回车事件不生效
    enterLogin () {
      document.onkeydown = (e) => {
        e = window.event || e
        if (this.$route.path === '/login' && (e.code === 'Enter' || e.code === 'enter')) {
          // 调用登录事件方法
          this.handleSubmit()
        }
      }
    },

    // 获取验证码
    getCode () {
      localStorage.removeItem('userToken')
      this.$api.system.getCodeApi().then((res) => {
        let data = res.data.data
        let resultCode = res.data.code
        let resultMsg = res.data.msg

        if (resultCode === 0) {
          this.codeKey = data.key
          this.codeUrl = data.codeUrl
        } else {
          this.$Notice.error({title: '操作提醒', desc: resultMsg})
        }
      }).catch((e) => {
        this.$Notice.error({title: '操作提醒', desc: '异常!'})
      })
    },

    // 登录
    handleSubmit () {
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          // todo 去除所有的旧缓存
          localStorage.removeItem('userToken')
          this.$cookies.remove('userName')
          // 旧
          // 账号密码 baseb4组合 (顺序打乱, 后四位移动到了最前面)
          // let nameAndPassword = this.dataForm.loginName + '#' + this.dataForm.password
          // let baseStr = Base64.encode(nameAndPassword)
          // let last = baseStr.substring(baseStr.length - 4, baseStr.length) // 截取后4位
          // let first = baseStr.substring(0, baseStr.length - 4) // 截取从0到倒数第4位
          // let newBaseStr = last + first
          // todo 新
          let loginParams = {
            username: this.dataForm.loginName,
            password: md5(this.dataForm.password),
            grant_type: 'captcha',
            // grant_type: 'password',
            scope: 'all',
            codeKey: this.codeKey,
            codeValue: this.dataForm.code
          }

          this.$api.system.loginApi(loginParams).then((res) => {
            let data = res.data.data
            let resultCode = res.data.code
            let resultMsg = res.data.msg

            if (resultCode === 0) {
              localStorage.setItem('userToken', data.accessToken)
              // 设置axios全局默认头，后续所有请求都会带上这个token
              axios.defaults.headers.common['Authorization'] = 'Bearer ' + data.accessToken
              // this.$cookies.set('userToken', data.userToken);
              this.$cookies.set('userName', data.userName)
              // isFtpStatus 是否ftp模式状态
              localStorage.setItem('isFtpStatus', '0')

              this.$Notice.success({title: '操作提醒', desc: '登录成功！'})
              this.$router.push('/home')
            } else {
              this.$Notice.error({title: '操作提醒', desc: resultMsg})
            }
          }).catch((e) => {
            this.$Notice.error({title: '操作提醒', desc: '异常!'})
          })
        } else {
          this.$Notice.error({title: '操作提醒', desc: '请验证字段!'})
        }
      })
    }
  },
  watch: {}
}
</script>

<style lang="scss" scoped>
// 公共样式
.form-item-input {
}

.login {
  background-image: url('../../assets/launch.jpg');
  background-repeat: no-repeat;
  background-size: 100% 100%;
  //height: calc(100vh - 100px);
  height: calc(100vh);
  text-align: center;
  .form-tag {
    position: fixed;
    top: 23%;
    right: 10%;
    .head {
      .title {
        font-size: 25px;
      }
    }
  }

  .form-form {
    margin: 30px 50px 20px 50px;
  }
}
</style>
