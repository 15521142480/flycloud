<template>
    <div>
      <div>
        <Modal
          v-model="editModal"
          :closable='true' :mask-closable=false
          @on-visible-change="resetData"
          :styles="{top: '20px'}"
          width="80%"
          >
          <div slot="header" style="margin-bottom: -20px;">

            <div style="position: absolute;top: 16px;">
              <Button v-show="!isUpdate" type="default" shape="circle" icon="ios-contact">新增角色</Button>
              <Button v-show="isUpdate" type="default" shape="circle" icon="ios-create">编辑角色</Button>
            </div>
            <Divider orientation="right">
              <Button type="primary" shape="circle" icon="md-checkmark" style="margin-right: 20px;float:right;" @click="saveEdit">保存</Button>
            </Divider>
          </div>
          <div class="demo-split" style="height: calc(100vh - 190px)">
            <Split v-model="splitNum">
              <!-- 左边 -->
              <div slot="left" class="demo-split-pane">
                <Card>
                  <p slot="title" style="text-align: center">基础信息</p>
                  <Form ref="roleEditFrom"
                        :model="roleEditFrom"
                        :label-width="90"
                        style="margin-top: 18px"
                        label-position="right"
                        :rules="ruleValidate"
                        @submit.native.prevent="saveEdit">

                    <FormItem label="角色名称:" prop="name">
                      <Input v-model="roleEditFrom.name" placeholder="请输入角色名称" style="width:250px"></Input>
                    </FormItem>
                    <FormItem label="角色编码:" prop="code">
                      <Input v-model="roleEditFrom.code" placeholder="角色编码" style="width:250px"></Input>
                    </FormItem>
                    <FormItem label="角色类型:"  inline>
                      <FormItem prop="menuType" style="display:inline-block;">
                        <Select v-model="roleEditFrom.type" disabled style="width:250px;" placeholder="选择类型">
                          <Option :value="0" :key="0">平台管理系统</Option>
                          <Option :value="1" :key="1">商家管理系统</Option>
                          <Option :value="2" :key="2">音乐平台管理系统</Option>
                          <Option :value="3" :key="3">音乐歌手管理系统</Option>
                        </Select>
                      </FormItem>
                    </FormItem>
                    <FormItem label="排序:" prop="sort">
                      <Input v-model="roleEditFrom.sort" type="number" placeholder="" style="width:250px;"></Input>
                    </FormItem>
                    <FormItem label="是否启用:" prop="status">
                      <i-switch v-model="roleEditFrom.status" true-value="0" false-value="1" />
                    </FormItem>
                    <FormItem label="备注:">
                      <Input v-model="roleEditFrom.remark" type="textarea" :autosize="{minRows: 2,maxRows: 5}" placeholder="描述" style="width:250px;"></Input>
                    </FormItem>
                  </Form>
                </Card>
              </div>
              <!-- 右边 -->
              <div slot="right" class="demo-split-pane">
                <Card>
                  <p slot="title" style="text-align: center">权限管理</p>
                  <div>
                  <div style="margin-top: 10px;height: calc(100vh - 260px);overflow-y:auto;overflow-x:hidden;padding-left: 15px">
                    <Tree
                      :data="sysRoleMenuData"
                      :render="renderContent"
                      style="width: 40%;font-size: 18px;">
                  </Tree>
                  </div>
                  </div>
                </Card>
              </div>
            </Split>
          </div>
          <div slot="footer" style="text-align: center">
            注：只要含有该菜单的其中一个按钮权限则有该菜单的本身权限
          </div>
        </Modal>
      </div>
    </div>
</template>

<script>
export default {
  name: 'sys-role-edit-pop',
  components: {},
  props: {
    value: ''
  },
  data () {
    return {

      editModal: this.value,
      showLoading: false,
      isUpdate: false,
      splitNum: 0.35,

      roleEditFrom: {
        id: '',
        type: 0,
        name: '',
        code: '',
        sort: 1,
        status: '0',
        remark: '',
        roleMenuPermissionJson: '' // menuId跟permission组装的json字符串
      },

      rolePermissions: [],

      roleId: '',

      sysRoleMenuData: [
        {
          id: '',
          title: '',
          expand: true,
          children: [],
          showSysMenuData: ''
        }
      ],

      // 表单验证
      ruleValidate: {
        name: [
          {required: true, message: '请输入角色名称', trigger: 'blur'}
        ]
      }
    }
  },
  created () {
    // this.init()
  },
  methods: {

    getSysRoleDetailData (roleData) {
      if (roleData !== null && roleData !== undefined) {
        this.roleEditFrom = roleData
        this.roleEditFrom.type = roleData.type
        this.isUpdate = true
        this.roleId = roleData.id
      } else {
        this.roleEditFrom = { // 在这个清空
          id: '',
          name: '',
          type: 0,
          remark: '',
          status: '0',
          roleMenuPermissionJson: ''
        }
      }
      this.init()
    },

    init () {
      this.splitNum = 0.35
      this.showLoading = true
      setTimeout(() => {
        this.showLoading = false
      }, 1000)
      this.$api.system.getRoleMenuTreeListApi(this.roleId).then(res => {
        let data = res.data
        if (data.code === 0) {
          this.sysRoleMenuData = data.data
          for (let i = 0; i < this.sysRoleMenuData.length; i++) {
            var roleData = {}
            let btnData = JSON.parse(this.sysRoleMenuData[i].buttonPermission)
            if (btnData !== undefined && btnData !== null && btnData.length > 0) {
              for (let j = 0; j < btnData.length; j++) {
                if (btnData[j].flag === '1') {
                  roleData.menuId = this.sysRoleMenuData[i].id
                  roleData.permission = btnData[j].btnPermission
                  this.rolePermissions.push(JSON.stringify(roleData))
                }
              }
            }

            if (this.sysRoleMenuData[i].children !== null && this.sysRoleMenuData[i].children !== undefined) {
              for (let k = 0; k < this.sysRoleMenuData[i].children.length; k++) {
                let btnData = JSON.parse(this.sysRoleMenuData[i].children[k].buttonPermission)
                if (btnData !== undefined && btnData !== null && btnData.length > 0) {
                  for (let t = 0; t < btnData.length; t++) {
                    if (btnData[t].flag === '1') {
                      roleData.menuId = this.sysRoleMenuData[i].children[k].id
                      roleData.permission = btnData[t].btnPermission
                      this.rolePermissions.push(JSON.stringify(roleData))
                    }
                  }
                }
              }
            }
          }
        } else {
          this.$Message.error(data.msg)
        }
        this.showLoading = false
      })
    },

    saveEdit () {
      this.$refs.roleEditFrom.validate((valid) => {
        if (valid) {
          if (this.rolePermissions === [] || this.rolePermissions.length === 0) {
            this.$Message.error('请给角色赋予按钮权限喔!')
            return
          }
          this.showLoading = true
          this.$api.system.saveOrUpdateByRoleApi(this.roleEditFrom).then(res => {
            let data = res.data
            if (data.code === 0) {
              this.$Message.info('操作成功!')
              this.$emit('on-ok-click')
            } else {
              this.$Message.error(data.msg)
            }
            this.editModal = false
            this.showLoading = false
          })
        } else {
          this.$Message.error('请填写完信息喔!')
        }
      })
    },

    renderContent (h, { root, node, data }) {
      let bindHtml = []

      if (data.buttonPermission !== undefined) {
        let d = JSON.parse(data.buttonPermission)

        for (let item in d) {
          let bindBtnName = h('span', {
            style: {
              marginRight: '3px'
            }
          }, d[item].btnName) // 按钮权限名称
          let bindSwitch = h('i-switch', {
            props: {
              type: 'primary',
              'true-value': 1,
              'false-value': 0,
              value: d[item].flag === '1' ? 1 : 0
            },
            style: {
              marginRight: '10px'
            },
            on: {
              'on-change': (value) => { // 触发事件去拼装json字符串数据
                var roleData = {}
                roleData.menuId = data.id
                roleData.permission = d[item].btnPermission
                if (value === 1) {
                  if (this.rolePermissions.indexOf(JSON.stringify(roleData)) === -1) {
                    this.rolePermissions.push(JSON.stringify(roleData))
                  }
                } else {
                  if (this.rolePermissions.indexOf(JSON.stringify(roleData)) !== -1) {
                    this.rolePermissions.splice(this.rolePermissions.indexOf(JSON.stringify(roleData)), 1)
                  }
                }
              }
            }
          })

          bindHtml.push(bindBtnName)
          bindHtml.push(bindSwitch)
        }
      }
      let bindBtnSwitch = h('span', bindHtml)

      return h('span', {
        style: {
          display: 'inline-block',
          width: '240%',
          marginBottom: '8px'
        }
      }, [
        h('span', [
          h('Icon', {
            props: {
              type: 'logo-apple',
              size: '25'
            },
            style: {
              marginLeft: '5px',
              marginBottom: '5px'
            }
          }),
          h('span', {
            style: {
              fontSize: '13px'
            }
          }, data.title)
        ]),
        h('span', {
          style: {
            display: 'inline-block',
            float: 'right',
            marginLeft: '22px',
            marginTop: '3px'
          }
        }, [bindBtnSwitch])
      ])
    },
    // 清空数据
    resetData (val) {
      if (!val) {
        this.roleId = ''
        this.rolePermissions = []
        this.isUpdate = false
        this.isUpdate = false
      } else {
        if (this.roleEditFrom.id !== undefined && this.roleEditFrom.id !== '' && this.roleEditFrom.id !== null) {
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
    },
    rolePermissions (val) {
      this.roleEditFrom.roleMenuPermissionJson = '[' + val + ']'
    }

  }
}
</script>

<style scoped>

</style>
