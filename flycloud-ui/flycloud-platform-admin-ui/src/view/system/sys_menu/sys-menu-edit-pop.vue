<template>
    <div>
      <div>
        <Modal v-model="editModal"
               :styles="{top: '30px'}"
               :closable='true' :mask-closable=false
               @on-visible-change="resetData"
               width="50%">
          <div slot="header" >
            <Button v-show="!isUpdate" type="default" shape="circle" icon="md-person-add">添加菜单</Button>
            <Button v-show="isUpdate" type="default" shape="circle" icon="ios-create">编辑菜单</Button>
          </div>
          <Form ref="editForm"
                :model="editForm"
                :label-width="130"
                label-position="right"
                :rules="formValidate"
                @submit.native.prevent="saveEdit">

            <FormItem label="菜单名称:" prop="name">
              <Input v-model="editForm.name" placeholder="" style="width:85%;"></Input>
            </FormItem>
            <FormItem label="菜单类型:"  inline>
              <FormItem prop="type" style="width:85%;">
                <Select v-model="editForm.type" disabled style="width:100%" placeholder="选择类型">
                  <Option :value="0" :key="0">平台管理系统</Option>
                  <Option :value="1" :key="1">商家管理系统</Option>
                  <Option :value="2" :key="2">音乐平台管理系统</Option>
                  <Option :value="3" :key="3">音乐歌手管理系统</Option>
                </Select>
             </FormItem>
            </FormItem>
            <FormItem label="菜单权限:" prop="permission">
              <Input v-model="editForm.permission" placeholder="" style="width:85%;"></Input>
            </FormItem>

            <FormItem label="按钮权限:" prop="">
              <card style="width: 85%;">
                <table align="center" width="100%" style="text-align:center;font-size: 13px;margin-bottom: 7px">
                  <tr>
                    <th style="padding-right: 30px;">按钮名称</th> <!-- style="padding-right: 70px;padding-left: 10px;padding-bottom: 8px"-->
                    <th style="padding-right: 30px;">按钮权限</th>
                    <th><Button type="primary" size="small" shape="circle" @click="clickBtnPermission"><Icon size="14" type="md-power" /> 新增 </Button></th>
                  </tr>
                  <tr v-for='item in btnPermissions' :key="item.btnName">
                    <td style="padding-right: 30px;"> {{item.btnName}} </td>
                    <td style="padding-right: 30px;"> {{item.btnPermission}} </td>
                    <td><Button type="default" size="small" shape="circle" @click="clickDelBtnPermission(item.btnPermission)"><Icon size="14" type="ios-trash" /> 删除 </Button></td>
                  </tr>
                </table>
              </card>
            </FormItem>

            <FormItem label="路由路径:" prop="path">
              <Input v-model="editForm.path" placeholder="" style="width:85%;"></Input>
            </FormItem>

            <FormItem label="页面路径:" prop="component">
              <Input v-model="editForm.component" placeholder="" style="width:85%;"></Input>
            </FormItem>

            <FormItem label="排序:" prop="sort">
              <Input v-model="editForm.sort" type="number" placeholder="" style="width:85%;"></Input>
            </FormItem>
            <FormItem label="是否启用:" prop="status">
              <i-switch v-model="editForm.status" true-value="0" false-value="1" @on-change="handleStatusChange" />
            </FormItem>
<!--            <FormItem label="是否隐藏:" prop="hidden">-->
<!--              <i-switch v-model="editForm.hidden" :true-value="1" :false-value="0" />-->
<!--            </FormItem>-->
            <FormItem label="备注:" prop="remark">
              <Input v-model="editForm.remark" type="textarea" placeholder="描述" style="width:85%;"></Input>
            </FormItem>

          </Form>
          <div slot="footer">
            <Button type="default" size="large" @click="cancelEdit">取消</Button>
            <Button type="primary" size="large" @click="saveEdit" style="margin-right: 30px">确定</Button>
          </div>
          <Spin size="large" fix v-if="showLoading"></Spin>
        </Modal>
      </div>

      <div>
        <Modal v-model="btnModal" width="400">
          <div slot="header"> <!--md-happy-->
            <Button type="default" shape="circle" icon="logo-windows">按钮权限</Button>
          </div>
          <Form ref="btnData"
                :model="btnFrom"
                :label-width="90"
                label-position="right"
                :rules="ruleBtnValidate"
                @submit.native.prevent="saveBtnEdit">

            <FormItem label="按钮名称" prop="btnName">
              <Input v-model="btnFrom.btnName" placeholder="请输入按钮名称" style="width:250px;"></Input>
            </FormItem>
            <FormItem label="按钮权限" prop="btnPermission">
              <Input v-model="btnFrom.btnPermission" placeholder="请输入按钮权限" style="width:250px"></Input>
            </FormItem>
          </Form>

          <div slot="footer">
            <Button type="default" size="large" @click="cancelBtnEdit">取消</Button>
            <Button type="primary" size="large" @click="saveBtnEdit">保存</Button>
          </div>
        </Modal>
      </div>
    </div>
</template>

<script>
export default {
  name: 'sys-menu-edit-pop',
  components: {},
  props: {
    value: ''
  },
  data () {
    return {
      editModal: this.value,
      isUpdate: false,
      showLoading: false,
      btnModal: false,

      editForm: {
        id: '',
        type: 0,
        name: '',
        parentId: 0,
        permission: '',
        buttonPermission: '',
        level: 1,
        path: '',
        component: '',
        icon: '',
        keepAlive: 0,
        hidden: 0,
        sort: 1,
        status: '0',
        remark: ''
      },

      /* btnPermissions: [{
        btnName: '',
        btnPermission: ''
      }], */
      btnPermissions: [],
      btnData: {
        btnName: '',
        btnPermission: ''
      },
      btnFrom: {
        btnName: '',
        btnPermission: ''
      },
      // key值用于下标判断
      btnKeys: [],

      // 表单验证
      formValidate: {
        name: [
          {required: true, message: '请输入菜单名称', trigger: 'blur'}
        ],
        /* type: [
          {required: true, message: '请选择菜单类型', trigger: 'change'}
        ], */
        permission: [
          {required: true, message: '请输入菜单权限', trigger: 'blur'}
        ]
        /* status: [
          {required: true, message: '请选择是否显示', trigger: 'change'}
        ], */
      },
      // 按钮权限验证
      ruleBtnValidate: {
        btnName: [
          {required: true, message: '请输入按钮名称', trigger: 'blur'},
          { type: 'string', max: 10, message: '10以下', trigger: 'blur' }
        ],
        btnPermission: [
          {required: true, message: '请输入按钮权限', trigger: 'blur'},
          { type: 'string', max: 10, message: '10以下', trigger: 'blur' }
        ]
      }
    }
  },
  created () {

  },
  methods: {

    // 父组件传来的 parentId
    getSysMenuCurrentIdAndMenuLevel (id, level) {
      this.editForm.parentId = id // 传 id 是因为下个子类信息的parentId就是此id
      this.editForm.level = level + 1
    },

    // 查询菜单详情信息
    getSysMenuDetailData (id) {
      this.showLoading = true
      setTimeout(() => {
        this.showLoading = false
      }, 600)
      this.$api.system.getMenuDetailApi(id).then(res => {
        let data = res.data
        if (data.code === 0) {
          this.editForm = data.data
          this.isUpdate = true
          this.btnPermissions = JSON.parse(this.editForm.buttonPermission)
          // 赋值
          if (this.btnPermissions !== null && this.btnPermissions !== undefined) {
            for (let i = 0; i < this.btnPermissions.length; i++) {
              this.btnKeys.push(this.btnPermissions[i].btnPermission)
            }
          }
        } else {
          this.$Message.error(data.msg)
        }
        this.showLoading = false
      })
    },

    // 保存
    saveEdit () {
      this.$refs.editForm.validate((valid) => {
        if (valid) {
          this.editForm.buttonPermission = JSON.stringify(this.btnPermissions)
          this.$api.system.saveOrUpdateByMenuApi(this.editForm).then(res => {
            let data = res.data
            if (data.code === 0) {
              this.$Message.info('操作成功!')
              this.$emit('on-ok-click')
            } else {
              this.$Message.error(data.msg)
            }
            this.editModal = false
          })
        }
      })
    },

    // 取消
    cancelEdit () {
      this.editModal = false
    },
    // -------------------------------------------  按钮 ------------------------------------------
    // 按钮新增
    clickBtnPermission () {
      this.btnModal = true
    },

    // 是否启用
    handleStatusChange (status) {
      if (status === '0') {
        this.$Message.info('启用')
      } else {
        this.$Message.warning('禁用')
      }
    },

    // 按钮确定
    saveBtnEdit () {
      var btnName = this.btnFrom.btnName
      var btnPermission = this.btnFrom.btnPermission

      // 判断权限是否重复
      var index = this.btnKeys.indexOf(btnPermission)
      if (index >= 0) {
        this.$Message.error('按钮权限已经存在啦!')
        return
      }
      if (btnName === '' || btnPermission === '') {
        this.$Message.error('请输出完整的按钮信息喔!')
        return
      }
      var btnNewData = {}
      btnNewData.btnName = btnName
      btnNewData.btnPermission = btnPermission
      this.btnKeys.push(btnPermission) // 添加 key
      if (this.btnPermissions !== null && this.btnPermissions !== undefined) {
        this.btnPermissions.push(btnNewData)
      } else {
        this.btnPermissions = []
        this.btnPermissions.push(btnNewData)
      }
      // 操作完清空modal的数据
      this.btnFrom.btnName = ''
      this.btnFrom.btnPermission = ''
      this.btnModal = false
    },

    // 按钮删除
    clickDelBtnPermission (btnPermission) {
      var index = this.btnKeys.indexOf(btnPermission)
      // alert(btnPermission + " --- " + index)
      this.$Modal.confirm({
        title: '删除按钮!',
        content: '<p>确认删除该按钮及权限吗?</p>',
        onOk: () => {
          if (index >= 0) {
            // splice(要删除的位置,要删除的数量) (第三个参数是你要替换的值,选填的这个可以做成修改功能)
            this.btnKeys.splice(index, 1)
            this.btnPermissions.splice(index, 1)
          }
        },
        onCancel: () => {
          this.$Message.info('已取消')
        }
      })
    },
    // 按钮取消
    cancelBtnEdit () {
      this.btnModal = false
    },

    // 清空数据
    resetData (val) {
      if (!val) {
        setTimeout(() => {
          this.editForm = {
            id: '',
            type: 0,
            name: '',
            parentId: 0,
            permission: '',
            buttonPermission: '',
            level: 1,
            path: '',
            component: '',
            icon: '',
            keepAlive: 0,
            hidden: 0,
            sort: 1,
            status: '0',
            remark: ''
          }
          this.btnPermissions = []
          this.btnKeys = []
          this.btnFrom = {
            btnName: '',
            btnPermission: ''
          }
          this.$refs.editForm.resetFields()
          this.isUpdate = false
        }, 500)
      } else {
        if (this.editForm.id !== undefined && this.editForm.id !== '' && this.editForm.id !== null) {
          this.isUpdate = true
        }
      }
    }

  },

  watch: {
    value (newVal) {
      this.editModal = newVal
    },
    editModal (newVal) {
      this.$emit('input', newVal)
    }
  }
}
</script>

<style scoped>

</style>
