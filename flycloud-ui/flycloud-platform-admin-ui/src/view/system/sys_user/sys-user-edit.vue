<template>
  <Modal
    v-model="editModal"
    class-name="vertical-center-modal"
    :closable='false'
    :mask-closable=false
    @on-visible-change="resetData"
    width="50%"
    :styles="{top: '80px'}"
  >
    <div slot="header" style="color:#2D8CF0;font-size: 16px">
      <Button v-show="!isUpdate" type="default" shape="circle" icon="md-person-add">添加用户</Button>
      <Button v-show="isUpdate" type="default" shape="circle" icon="ios-create">编辑用户</Button>
      <div style="float: right;font-size: 30px;line-height: 14px;cursor:pointer;color: #999999" @click="cancel">×</div>
    </div>

    <Form
      ref="dataFrom"
      :model="dataFrom"
      :rules="ruleValidate"
      :label-width="150"
    >
        <FormItem label="管理员账号" prop="account" v-show="!isUpdate">
          <Input v-model="dataFrom.account" placeholder="登录账号" style="width: 85%;"/>
        </FormItem>

        <FormItem label="管理员账号" prop="account" v-show="isUpdate">
          <Input v-model="dataFrom.account" disabled placeholder="登录账号" style="width: 85%;"/>
        </FormItem>

        <FormItem label="昵称" prop="name">
          <Input v-model="dataFrom.name" placeholder="昵称" style="width: 85%;"/>
        </FormItem>

        <FormItem label="用户类型" prop="type" v-show="isUpdate">
          <Select v-model="dataFrom.userType" disabled style="width:85%;" placeholder="选择类型">
            <Option :value="0" :key="0">平台管理系统</Option>
            <Option :value="1" :key="1">商家管理系统</Option>
            <Option :value="2" :key="2">音乐平台管理系统</Option>
            <Option :value="3" :key="3">音乐歌手管理系统</Option>
          </Select>
        </FormItem>
        <FormItem label="用户类型" prop="type" v-show="!isUpdate">
          <Select v-model="dataFrom.userType" style="width:85%;" placeholder="选择类型" @on-change="onChangeByUserType">
            <Option :value="0" :key="0">平台管理系统</Option>
            <Option :value="1" :key="1">商家管理系统</Option>
            <Option :value="2" :key="2">音乐平台管理系统</Option>
            <Option :value="3" :key="3">音乐歌手管理系统</Option>
          </Select>
        </FormItem>

        <FormItem label="角色权限" prop="userRoleList">
          <Select v-model="dataFrom.userRoleList" filterable multiple style="width: 85%;">
            <Option v-for="item in sysRoleDataList" :value="item.id.toString()" :key="item.id.toString()">{{ item.name }}</Option>
          </Select>
        </FormItem>

        <FormItem label="联系方式" prop="telephone">
          <Input v-model="dataFrom.telephone" placeholder="联系方式" style="width: 85%;"/>
        </FormItem>

        <FormItem label="E-mail" prop="email">
          <Input v-model="dataFrom.email" placeholder="E-mail" style="width: 85%;"/>
        </FormItem>

<!--        <FormItem label="紧急联系人">-->
<!--          <Input v-model="dataFrom.urgentLinkman" placeholder="紧急联系人" style="width: 85%;"/>-->
<!--        </FormItem>-->

<!--        <FormItem label="紧急联系人电话">-->
<!--          <Input v-model="dataFrom.urgentPhone" placeholder="联系人电话" style="width: 85%;"/>-->
<!--        </FormItem>-->

        <FormItem label="管理员密码" v-show="isUpdate">
          <Input type="password" v-model="dataFrom.password" placeholder="密码" style="width: 85%;"/>
        </FormItem>

        <FormItem label="管理员密码" prop="password" v-show="!isUpdate">
          <Input type="password" v-model="dataFrom.password" placeholder="密码" style="width: 85%;"/>
        </FormItem>

        <FormItem label="确认密码" v-show="isUpdate" >
          <Input type="password" v-model="dataFrom.cfirmpswd" placeholder="确认密码" style="width: 85%;"/>
        </FormItem>

        <FormItem label="确认密码" prop="cfirmpswd" v-show="!isUpdate" >
          <Input type="password" v-model="dataFrom.cfirmpswd" placeholder="确认密码" style="width: 85%;"/>
        </FormItem>

        <FormItem label="是否启用:" prop="status">
          <i-switch v-model="dataFrom.status" true-value="0" false-value="1" />
        </FormItem>

        <FormItem label="备注" prop="remark">
          <Input v-model="dataFrom.remark" type="textarea" :autosize="{minRows: 2,maxRows: 3}" placeholder="备注" style="width: 85%;"/>
        </FormItem>
    </Form>

    <div slot="footer">
      <Button type="default" size="large" @click="cancel">取消</Button>
      <Button type="primary" :loading="modalLoading" size="large" @click="checkSave" style="margin-right: 30px">保存</Button>
    </div>
    <Spin size="large" fix v-if="showLoading"></Spin>
  </Modal>

</template>

<script>
import md5 from 'js-md5'
export default {
  name: 'sys-user-edit',
  props: {
    value: {type: Boolean, default: false}
  },
  data () {
    // // 验证是否输入密码
    // const validatePass = (rule, value, callback) => {
    //   if (value === '' && !this.isUpdate) {
    //     callback(new Error('请输入管理员密码...'))
    //   } else {
    //     if (this.dataFrom.cfirmpswd !== '') {
    //       // 对第二个密码框单独验证
    //       this.$refs.dataFrom.validateField('cfirmpswd')
    //     }
    //     callback()
    //   }
    // }
    // // 判断是否输入二次确认密码&两次输入的密码是否一致
    // const validatePassCheck = (rule, value, callback) => {
    //   if (value === '' && !this.isUpdate) {
    //     callback(new Error('请输入二次确认密码'))
    //   } else if (value !== this.dataFrom.password) {
    //     callback(new Error('两次输入的密码不一致'))
    //   } else {
    //     callback()
    //   }
    // }

    return {

      // editModal: this.value,
      editModal: false,
      isUpdate: false,
      showLoading: false,

      o_password: '',

      dataFrom: {
        id: null,
        account: '',
        userType: 0,
        password: '',
        status: '0',
        cfirmpswd: '',
        userRoleList: []
      },
      sysRoleDataList: [],
      modalLoading: false,

      ruleValidate: {
        account: [
          { required: true, message: '账号不能为空', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '昵称不能为空', trigger: 'blur' }
        ],
        userRoleList: [
          { required: true, type: 'array', min: 1, message: '角色不能为空', trigger: 'change' }
        ],
        // telephone: [
        //   // { required: true, message: '联系电话必填不能为空', trigger: 'blur' },
        //   { type: 'number',
        //     message: '请填写正确的手机号',
        //     trigger: 'blur',
        //     transform (value) {
        //       let myreg = /^[1][3,4,5,7,8][0-9]{9}$/
        //       if (!myreg.test(value)) {
        //         return false
        //       } else {
        //         return Number(value)
        //       }
        //     }}
        // ],
        // password: [
        //   { required: true, validator: validatePass, trigger: 'blur' }
        // ],
        // cfirmpswd: [
        //   { required: true, validator: validatePassCheck, trigger: 'blur' }
        // ],
        email: [
          { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
        ]
      }
    }
  },
  created () {
    this.initRoleDataList()
  },
  methods: {

    /**
     * 详情页面
     */
    getSysUserDetailData (rowData) {
      this.editModal = true
      this.showLoading = true
      setTimeout(() => {
        this.showLoading = false
      }, 600)
      if (rowData !== null && rowData !== undefined) { // 修改
        this.isUpdate = true
        this.$api.system.getUserDetailApi(rowData.id).then(res => {
          let data = res.data
          if (data.code === 0) {
            this.dataFrom = data.data
            this.o_password = this.dataFrom.password
            if (rowData.roleIds !== undefined && rowData.roleIds !== '') {
              this.dataFrom.userRoleList = rowData.roleIds.split(',')
            }
          } else {
            this.$Message.error(data.msg)
          }
          this.loading = false
        })
      } else { // 新增

      }
    },

    /** ***************************************** 点击事件 *******************************************/
    // 取消
    cancel: function () {
      this.editModal = false
      // this.$Modal.confirm({
      //   title: '确认操作',
      //   content: '确定要放弃本次编辑吗',
      //   onOk: () => {
      //     this.editModal = false
      //   }
      // })
    },
    // 用户类型
    onChangeByUserType () {
      this.initRoleDataList()
    },
    // 保存
    checkSave: function () {
      this.$refs['dataFrom'].validate((valid) => {
        if (valid) {
          this.save() // 修改保存
        } else {
          this.$Message.error('请填写完整的管理员信息!')
        }
      })
    },
    // 清空数据
    resetData: function (val) {
      if (!val) {
        this.isUpdate = false
        this.editModal = false
        this.dataFrom.userRoleList = []
        this.$refs['dataFrom'].resetFields()
        this.dataFrom.id = null
        this.o_password = ''
        // this.sysRoleDataList = []
        this.modalLoading = false
      }
    },
    /** ***************************************** 请求接口方法 *******************************************/
    // 请求角色列表
    initRoleDataList () {
      this.$api.system.getRoleSelectListApi({'type': this.dataFrom.userType}).then(res => {
        let data = res.data
        if (data.code === 0) {
          this.sysRoleDataList = data.data
        } else {
          this.$Message.error(data.msg)
        }
      })
    },
    // 修改或保存管理员信息
    save () {
      // 权限
      let userRoleList = this.dataFrom.userRoleList
      if (userRoleList.length < 1) {
        this.$Message.error('请选择角色权限！')
        return
      }
      this.dataFrom.roleIds = userRoleList.join(',')
      // 密码
      let password = this.dataFrom.password
      let cfirmpswd = this.dataFrom.cfirmpswd
      if (this.isUpdate) {
        if (cfirmpswd !== undefined && cfirmpswd !== '' && password !== cfirmpswd) {
          this.$Message.error('两次密码不一样！')
          return
        }
      } else {
        if (password === '' || cfirmpswd === '') {
          this.$Message.error('请输入密码！')
          return
        }
        if (password !== cfirmpswd) {
          this.$Message.error('两次密码不一样！')
          return
        }
      }
      this.modalLoading = true
      if (!this.isUpdate) {
        this.dataFrom.password = md5(password)
      } else {
        if (this.o_password !== password) {
          alert(this.o_password)
          alert(password)
          this.dataFrom.password = md5(password)
        }
      }
      this.$api.system.saveOrUpdateUserApi(this.dataFrom).then(res => {
        let data = res.data
        if (data.code === 0) {
          this.editModal = false
          this.$Message.success(this.isUpdate ? '修改成功' : '新增成功')
          this.$emit('saveSucCallBack')
        } else {
          this.$Message.error(data.msg)
          this.dataFrom.password = cfirmpswd
        }
        this.modalLoading = false
      })
    }

  },
  watch: {
    // value (val) {
    //   this.editModal = val
    // },
    editModal (val) {
      this.$emit('input', val)
    }
  }
}
</script>
