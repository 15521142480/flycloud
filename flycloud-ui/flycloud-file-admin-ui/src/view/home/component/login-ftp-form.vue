<template>
  <div>
    <Drawer
      v-model="isShow"
      title="连接服务"
      placement="left"
      :closable="false"
      width="30%"
      class="login-ftp-title"
    >
      <div class="login-ftp-form">
        <Form ref="dataForm" :model="dataForm" :rules="ruleValidate" :label-width="80">
          <FormItem label="ip" prop="host">
            <Input v-model="dataForm.host" type="password" placeholder="ip"/>
          </FormItem>
          <FormItem label="用户" prop="user">
            <Input v-model="dataForm.user" placeholder="用户"/>
          </FormItem>
          <FormItem label="密码" prop="password">
            <Input v-model="dataForm.password" type="password" placeholder="密码"/>
          </FormItem>
          <FormItem label="端口号" prop="port">
            <Input v-model="dataForm.port" placeholder="端口号"/>
          </FormItem>
          <FormItem>
            <Button type="primary" @click="handleSubmit()">连接</Button>
            <Button @click="handleReset()" style="margin-left: 8px">重置</Button>
          </FormItem>
        </Form>
      </div>
    </Drawer>
  </div>
</template>

<script>
import {mapGetters, mapMutations} from 'vuex'
import {Base64} from 'js-base64'

export default {
  name: 'login-ftp-form',
  components: {},
  props: {},
  data () {
    return {
      isShow: false,
      // dataForm: {
      //   host: '',
      //   user: '',
      //   password: '',
      //   port: '',
      // },
      dataForm: {
        host: '39.98.125.88',
        user: 'root',
        password: 'Wwlxs@#$',
        port: '22'
      },
      ruleValidate: {
        host: [{required: true, message: 'ip不能为空!', trigger: 'blur'}],
        user: [{required: true, message: '用户不能为空!', trigger: 'blur'}],
        password: [{required: true, message: '密码不能为空!', trigger: 'blur'}],
        port: [{required: true, message: '端口号不能为空!', trigger: 'blur'}]
      }
    }
  },
  created () {

  },
  methods: {
    ...mapMutations([
      'set_isFtpModel'
    ]),

    /**
     * 提交ftp信息连接
     */
    handleSubmit () {
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          // ip和密码 baseb4组合 (顺序打乱, 后四位移动到了最前面)
          let nameAndPassword = this.dataForm.host + '@@@' + this.dataForm.password
          let baseStr = Base64.encode(nameAndPassword)
          let last = baseStr.substring(baseStr.length - 4, baseStr.length) // 截取后4位
          let first = baseStr.substring(0, baseStr.length - 4) // 截取从0到倒数第4位
          let newBaseStr = last + first

          let newDataForm = {
            baseKey: newBaseStr,
            user: this.dataForm.user,
            port: this.dataForm.port
          }

          this.$api.fileStp.ftpLogin(newDataForm).then((res) => {
            let resultCode = res.data.resultCode
            let resultMsg = res.data.resultMsg
            if (resultCode === '1') {
              this.$Notice.success({
                title: '系统提醒',
                desc: resultMsg,
                duration: 5
              })
              this.set_isFtpModel(true)
              this.isShow = false
            } else {
              this.$Notice.error({
                title: '系统提醒',
                desc: resultMsg,
                duration: 5
              })
            }
          }).catch((e) => {
            this.$Message.error('接口异常!' + e.getMessages())
          })
        } else {
          this.$Message.error('验证失败!')
        }
      })
    },

    /**
     * 重置
     */
    handleReset () {
      this.dataForm = {
        host: '',
        user: '',
        password: '',
        port: ''
      }
      this.$refs['dataForm'].resetFields()
    }
  },
  computed: {
    ...mapGetters([
      'isFtpModel'
    ])
  },
  watch: {}
}
</script>

<style lang="scss" scoped>
.login-ftp-title {
  text-align: center;
}

.login-ftp-form {
  padding: 50px 60px 0 10px;
}
</style>
