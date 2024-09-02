<template>
    <div>
<!--      <Card style="height: calc(100vh - 145px)">-->
      <div style="margin-top: 15px">
        <Button v-show="checkHasPermission('sys.menu.saveOrUpdate')" style="font-size:13px;margin-left: 20px"  type="primary" shape="circle" @click="showEditModal"><Icon size="14" type="md-body" /> 新增一级菜单</Button>
      </div>
      <Divider />
      <div style="height: calc(100vh - 200px); overflow-y:auto; overflow-x:hidden;">
        <Tree
          :data="sysMenuData"
          :render="renderContent"
          style="width: 40%;font-size: 18px;margin-left: 20px;"
        >
        </Tree>
      </div>
      <Spin size="large" fix v-if="showLoading"></Spin>
<!--      </Card>-->
      <div>
        <SysMenuEditPop ref="sysMenuEditPop"
          v-model="isShowModal"
          @on-ok-click="confirmSysMenuEdit">
        </SysMenuEditPop>
        <!--  :sys-menu-data="showSysMenuData"-->
      </div>
    </div>
</template>

<script>
import SysMenuEditPop from './sys-menu-edit-pop'
import { hasPermission } from '../../../util/cacheUtils'

export default {
  name: 'sys-menu-list',
  components: {
    SysMenuEditPop
  },
  props: {

  },
  data () {
    return {
      sysMenuData: [
        {
          id: '',
          title: '',
          expand: true,
          children: [],
          showSysMenuData: ''
        }
      ],
      showLoading: false,
      isShowModal: false,

      updateEnableDate: {
        id: null,
        status: null
      }

    }
  },
  created () {
    this.init()
  },
  methods: {
    init () {
      this.showLoading = true
      setTimeout(() => {
        this.showLoading = false
      }, 1500)
      this.$api.system.getMenuTreeListApi().then(res => {
        let data = res.data
        if (data.code === 0) {
          this.sysMenuData = data.data
        } else {
          this.$Message.error(data.msg)
        }
        this.showLoading = false
      })
    },
    // 弹框
    showEditModal () {
      this.isShowModal = true
    },

    renderContent (h, { root, node, data }) {
      var canMenuBtn = false
      /* var canMoveTop = false
      var canMoveBottom = false */
      if (data.level >= 2) {
        canMenuBtn = true
      }
      /* if (data.topChangeId === null) {
        canMoveTop = true
      }
      if (data.bottomChangeId === null) {
        canMoveBottom = true
      } */

      let btnList = []

      if (this.checkHasPermission('sys.menu.saveOrUpdate')) {
        let addBtn = h('Button', {
          props: Object.assign({}, this.buttonProps, {
            type: 'primary',
            size: 'default',
            icon: 'md-person-add',
            shape: 'circle',
            disabled: canMenuBtn
          }),
          style: {
            marginRight: '15px'
          },
          on: {
            click: () => {
              // 传 id 是因为下个子类信息的parentId就是此id
              this.$refs.sysMenuEditPop.getSysMenuCurrentIdAndMenuLevel(data.id, data.level)
              this.showEditModal()
            }
          }
        }, '添加子类')
        btnList.push(addBtn)
      }

      if (this.checkHasPermission('sys.menu.saveOrUpdate')) {
        let editBtn = h('Button', {
          props: {
            type: 'primary',
            size: 'default',
            shape: 'circle',
            icon: 'ios-create'
          },
          style: {
            marginRight: '15px'
          },
          on: {
            click: () => {
              this.$refs.sysMenuEditPop.getSysMenuDetailData(data.id)
              this.showEditModal()
            }
          }
        }, '编辑')
        btnList.push(editBtn)
      }

      if (this.checkHasPermission('sys.menu.enable')) {
        let enableBtn = h('Button', {
          props: Object.assign({}, this.buttonProps, {
            type: data.status === '0' ? 'success' : 'error',
            size: 'default',
            shape: 'circle',
            icon: data.status === '0' ? 'md-checkmark-circle' : 'md-eye-off'
          }),
          style: {
            marginRight: '15px'
          },
          on: {
            click: () => {
              this.updateEnable(data)
            }
          }
        }, data.status === '0' ? '已启' : '已禁') // 后台返回回来的short类型,所以直接是1不是"1"
        btnList.push(enableBtn)
      }

      return h('span', {
        style: {
          display: 'inline-block',
          // width: '100%',
          minWidth: '500px',
          maxWidth: '1500px',
          marginBottom: '16px'
        }
      }, [
        h('span', [
          h('Icon', {
            props: {
              type: 'logo-apple',
              size: '28'
            },
            style: {
              marginRight: '8px',
              marginBottom: '5px'
            }
          }),
          h('span', {
            style: {
              fontSize: '15px'
            }
          }, data.title)
        ]),
        h('span', {
          style: {
            display: 'inline-block',
            float: 'right',
            marginRight: '12px'
          }
        }, [

          btnList

        ])
      ])
    },

    // 修改状态
    updateEnable (data) {
      var alertTitle = data.status === '0' ? '禁用' : '启用'
      this.updateEnableDate.id = data.id
      this.updateEnableDate.status = data.status === '0' ? '1' : '0'
      this.$Modal.confirm({
        title: alertTitle + ' !',
        content: '<p>确认要 ' + alertTitle + ' 此菜单吗?</p>',
        onOk: () => {
          this.$api.system.saveOrUpdateByMenuApi(this.updateEnableDate).then(res => {
            let data = res.data
            if (data.code === 0) {
              this.init()
              this.$Message.success(alertTitle + '成功!')
            } else {
              this.$Message.error(data.msg)
            }
          })
        },
        onCancel: () => {
          this.$Message.info('已取消')
        }
      })
    },
    confirmSysMenuEdit () {
      this.init()
    },

    // 检查操作权限
    checkHasPermission (btnPermission) {
      return hasPermission(btnPermission)
    }

  },
  watch: {

  }

}
</script>

<style scoped>

</style>
