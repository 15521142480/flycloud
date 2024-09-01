<template>
  <div>
    <Card>
      <div style="margin-top: 15px">
        <div style="margin-bottom: 25px">
          <Input v-model="params.name" placeholder="角色名称" style="width: 200px"><Icon type="ios-search" slot="prefix" /></Input>&nbsp;&nbsp;
          <Button type="primary" shape="circle" icon="ios-search" @click="search" >查询</Button>&nbsp;&nbsp;
          <Button type="default" shape="circle" icon="ios-nuclear" @click="clear">清空</Button>&nbsp;&nbsp;
<!--          <Button v-show="checkOperationAccess('system.role.add')" type="primary" shape="circle" icon="md-body" @click="addRoleInfo">新增</Button>-->
          <Button type="primary" shape="circle" icon="md-body" @click="addRoleInfo">新增</Button>
        </div>
      </div>
      <div>
        <Table border
               style="min-height: 525px;"
               no-data-text="没有相关数据喔!"
               :columns="sysRoleColumns"
               :loading="loading"
               :highlight-row="true"
               :current="params.pageNum"
               :data="sysRoleData">
        </Table>
      </div>

      <div>
        <sys-role-edit-pop v-model="isShowModal" ref="sysRoleEditPop" @on-ok-click="confirmSysRoleEdit"></sys-role-edit-pop>
      </div>

      <div>
        <Row type="flex" style="margin-top: 30px;" justify="end">
          <Col >
            <Page :total="totalCount" :current="params.pageNum" :page-sise="params.pageSize" loading show-sizer show-elevator show-total right @on-change="pageChange"
                  @on-page-size-change="sizeChange"></Page>
          </Col>
        </Row>
      </div>
    </Card>
  </div>
</template>

<script>
import SysRoleEditPop from './sys-role-edit-pop'
// import { hasOperationAccess } from '@/libs/util'
export default {
  name: 'sys-role-list',
  components: {
    SysRoleEditPop
  },
  data () {
    return {
      params: {
        name: '',
        pageNum: 1,
        pageSize: 10
      },
      totalCount: 0,

      loading: false,
      sysRoleData: [],
      isShowModal: false,

      updateEnableDate: {
        id: null,
        status: null
      },

      sysRoleColumns: [
        {
          title: '角色名称',
          key: 'name',
          align: 'center'
        },
        {
          title: '角色类型',
          key: 'roleType',
          align: 'center',
          render: (h, params) => {
            const row = params.row
            const text = row.roleType === '2' ? '廪仓角色' : '系统角色'
            return h('div', {
              props: {

              }
            }, text)
          }
        },
        {
          title: '备注',
          key: 'remark',
          align: 'center'
        },
        {
          title: '状态',
          key: 'status',
          align: 'center',
          render: (h, params) => {
            const row = params.row
            return h('Button', {
              props: Object.assign({}, this.buttonProps, {
                type: row.status === '0' ? 'success' : 'error',
                size: 'small',
                shape: 'circle',
                icon: row.status === '0' ? 'md-checkmark-circle' : 'md-eye-off'
              }),
              style: {
                marginRight: '15px'
              }
            }, row.status === '0' ? '已启用' : '已禁用')
          }

        },
        {
          title: '操作',
          key: 'operation',
          // width: 180,
          align: 'center',
          render: (h, params) => {
            const row = this.sysRoleData[params.index]

            let btnList = []

            // if (this.checkOperationAccess('system.role.modify')) {
            let editBtn = h('Button', {
              props: {
                type: 'primary',
                size: 'small',
                shape: 'circle',
                icon: 'ios-create'
              },
              style: {
                marginRight: '5px'
              },
              on: {
                click: () => {
                  this.$refs.sysRoleEditPop.getSysRoleDetailData(row)
                  this.isShowModal = true
                }
              }
            }, '编辑')
            btnList.push(editBtn)
            // }

            // if (this.checkOperationAccess('system.role.status')) {
            let enableSwitch = h('i-switch', {
              props: {
                size: 'large',
                type: 'primary',
                'true-value': '0',
                'false-value': '1',
                value: row.status === '0' ? '0' : '1'
              },
              style: {
                marginRight: '5px'
              },
              scopedSlots: {
                open: () => h('span', '已启'),
                close: () => h('span', '已禁')
              },
              on: {
                'on-change': (value) => {
                  this.updateEnable(row, value)
                }
              }
            })
            btnList.push(enableSwitch)
            // }

            return h('div',
              btnList
            )
          }
        }
      ]
    }
  },
  created () {
    this.init()
  },
  methods: {
    init () {
      this.loading = true
      setTimeout(() => {
        this.loading = false
      }, 30000)
      this.$api.system.getRoleListApi(this.params).then(res => {
        let data = res.data
        if (data.code === 0) {
          this.sysRoleData = data.data.list
          this.totalCount = data.data.total
        } else {
          this.$Message.error(data.msg)
        }
        this.loading = false
      }).catch(err => {
        console.log(err)
        this.$Message.error('连接失败，请检查网络！')
        this.loading = false
      })
    },

    // 修改角色信息
    updateEnable (data) {
      let alertTitle = data.status === '0' ? '禁用' : '启用'
      this.updateEnableDate.id = data.id
      this.updateEnableDate.status = data.status === '0' ? '1' : '0'
      this.loading = true
      setTimeout(() => {
        this.loading = false
      }, 500)
      this.$api.system.updateRoleByIdApi(this.updateEnableDate).then(res => {
        let data = res.data
        if (data.code === 0) {
          this.init()
          this.$Message.success(alertTitle + '成功!')
        } else {
          this.$Message.error(data.msg)
        }
      }).catch(err => {
        console.log(err)
        this.$Message.error('连接失败，请检查网络！')
        this.loading = false
      })
    },

    addRoleInfo () {
      this.$refs.sysRoleEditPop.getSysRoleDetailData()
      this.isShowModal = true
    },
    confirmSysRoleEdit () {
      this.init()
    },

    search () {
      this.params.pageNum = 1
      this.init()
    },
    clear () {
      this.params.name = ''
      this.init()
    },

    pageChange (num) {
      this.params.pageNum = num
      this.init()
    },
    sizeChange (size) {
      this.params.pageSize = size
      this.init()
    }

    // 检查操作权限
    // checkOperationAccess (requireAccess) {
    //   return hasOperationAccess(requireAccess)
    // }

  },
  watch: {

  }
}
</script>

<style scoped>

</style>
