<template>
  <div class="login">
    <div class="head">
      <h1 class="title">文件管理系统</h1>
    </div>

    <div class="form-tag">
      <Card>
        <Form class="form-form" ref="dataForm" :model="dataForm" :rules="ruleInline">
          <FormItem prop="loginName">
            <Input class="form-item-input" type="text" v-model="dataForm.loginName" placeholder="用户">
              <Icon type="ios-person-outline" slot="prepend"></Icon>
            </Input>
          </FormItem>
          <FormItem prop="password">
            <Input type="password" v-model="dataForm.password" placeholder="密码">
              <Icon type="ios-lock-outline" slot="prepend"></Icon>
            </Input>
          </FormItem>
          <FormItem>
            <Button type="primary" @keyup.enter.native="enterLogin()" @click="handleSubmit()">登录</Button>
          </FormItem>
        </Form>
      </Card>
    </div>
  </div>
</template>

<script>
let Base64 = require('js-base64').Base64
export default {
  name: 'login',
  components: {},
  props: {},
  data () {
    return {
      dataForm: {
        loginName: 'admin',
        password: 'admin'
      },
      ruleInline: {
        loginName: [{required: true, message: '用户不能为空!', trigger: 'blur'}],
        password: [{required: true, message: '密码不嫩为空!', trigger: 'blur'}]
      }
    }
  },
  created () {
    this.enterLogin()
  },
  computed: {},
  methods: {

    //这是定义的触发回车的事件 此函数必须初始化的时候执行一次此函数 否则回车事件不生效
    enterLogin () {
      document.onkeydown = (e) => {
        e = window.event || e
        if (this.$route.path == '/login' && (e.code == 'Enter' || e.code == 'enter')) {
          //调用登录事件方法
          this.handleSubmit()
        }
      }
    },

    handleSubmit () {
      this.$refs.dataForm.validate((valid) => {
        if (valid) {

          // 账号密码 baseb4组合 (顺序打乱, 后四位移动到了最前面)
          let nameAndPassword = this.dataForm.loginName + '#' + this.dataForm.password
          let baseStr = Base64.encode(nameAndPassword)
          let last = baseStr.substring(baseStr.length - 4, baseStr.length) // 截取后4位
          let first = baseStr.substring(0, baseStr.length - 4) // 截取从0到倒数第4位
          let newBaseStr = last + first

          this.$api.system.loginApi({base64: newBaseStr}).then((res) => {

            let data = res.data.data
            let resultCode = res.data.resultCode
            let resultMsg = res.data.resultMsg

            if (resultCode === '1') {

              // this.$cookies.set('userToken', data.userToken); 后端服务已设置
              this.$cookies.set('userName', data.userName)
              this.$Notice.success({title: '操作提醒', desc: resultMsg})
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
  text-align: center;

  .head {
    margin-top: 50px;

    .title {
      font-size: 35px;
    }
  }

  .form-tag {
    width: 500px;
    height: 300px;
    margin: auto;
    margin-top: 100px;
  }

  .form-form {
    margin: 30px 50px 20px 50px;
  }
}
</style>
