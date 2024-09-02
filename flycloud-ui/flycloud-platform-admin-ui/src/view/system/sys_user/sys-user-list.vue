<template>
  <div>
    <!-- 顶部筛选查询 -->
    <div style="margin-top: 15px">
      <Row class="search-container" type="flex" justify="space-between">
        <Col span="24">
          <Input placeholder="登录账号" v-model="params.account" clearable style="width: 200px;"><Icon type="ios-search" slot="prefix" /></Input>&nbsp;&nbsp;
          <Button type="primary" shape="circle" icon="ios-search" @click="search">查询</Button>&nbsp;&nbsp;
          <Button type="default" shape="circle" icon="ios-nuclear" @click="clear">清空</Button>&nbsp;&nbsp;
<!--          <Button type="primary" shape="circle" icon="md-body" @click="add" v-show="checkOperationAccess('system.user.add')">新增账号</Button>-->
          <Button type="primary" shape="circle" icon="md-body" @click="add">新增账号</Button>
        </Col>
      </Row>
    </div>

    <!-- 账号列表 -->
    <div style="margin-top: 20px;margin-bottom: 30px">
      <Row class="search-container" type="flex" justify="space-between">
        <Col span="24">
          <Table
                 style="height: calc(100vh - 305px)"
                 no-data-text="没有相关数据喔!"
                 :columns="columns"
                 :loading="loading"
                 :highlight-row="true"
                 :data="userList">
          </Table>
        </Col>
      </Row>
    </div>
    <!-- 分页 -->
    <Row type="flex" class="page-container" justify="end">
      <Col >
      <Page :total="totalCount" :current="params.pageNum" :page-sise="params.pageSize" loading show-sizer show-elevator show-total right @on-change="pageChange"
            @on-page-size-change="sizeChange"></Page>
      </Col>
    </Row>

    <!-- 编辑弹框 -->
    <sys-user-edit ref="sysUserEditPop" @saveSucCallBack="saveUserEdit"></sys-user-edit>
  </div>

</template>

<script>
// import { hasOperationAccess } from '@/libs/util'
import SysUserEdit from './sys-user-edit'
export default {
  name: 'sys-user-list',
  components: {
    SysUserEdit
  },
  data () {
    return {

      loading: false,
      params: {'pageNum': 1, 'pageSize': 10, 'account': null},
      userList: [],
      columns: [
        {title: '账号', align: 'center', key: 'account'},
        {title: '昵称', align: 'center', key: 'name'},
        {title: '用户类型', align: 'center', key: 'userTypeName', width: 115},
        {title: '联系方式', align: 'center', key: 'telephone', width: 115},
        {title: '创建时间', align: 'center', key: 'createTime'},
        {
          title: '状态',
          align: 'center',
          key: 'status',
          width: 120,
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
                // marginRight: '15px'
              }
            }, row.status === '0' ? '已启用' : '已禁用')
          }
        },
        {
          title: '操作',
          align: 'center',
          width: 200,
          key: 'status',
          render: (h, params) => {
            // return this.btnRender(h, params)

            const row = params.row

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
                  this.$refs.sysUserEditPop.getSysUserDetailData(row)
                  // this.isShowModal = true
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
                  this.updateStatus(row, value)
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
      ],
      totalCount: 0

    }
  },
  methods: {

    // 拼接按钮
    btnRender (h, params) {
      let btnList = []
      // if (this.checkOperationAccess('system.user.modify')) {
      let detail = this.createBtn(h, '修改', 'primary', function (that) {
        that.managerId = params.row.id
        that.isShowEdit = true
      })
      btnList.push(detail)
      // }
      // if (this.checkOperationAccess('system.user.status')) {
      let status = params.row.status
      let btnStr = this.createBtn(h, status ? '禁用' : '启用', status ? 'error' : 'success', function (that) {
        that.updateStatus(params.row)
      })
      btnList.push(btnStr)
      // }
      return btnList
    },
    createBtn (h, title, type, callback) {
      return h('Button', {
        props: {type: type, size: 'small'},
        style: {marginRight: '5px'},
        on: {
          click: () => {
            let that = this
            callback(that)
          }
        }
      }, title)
    },

    /** ***************************************** 点击事件 *******************************************/
    // 跳页
    pageChange: function (num) {
      this.params.pageNum = num
      this.loadUserList()
    },

    // 改变每页大小
    sizeChange: function (size) {
      this.params.pageSize = size
      this.loadUserList()
    },

    // 搜索
    search: function () {
      this.params.pageNum = 1
      this.loadUserList()
    },
    clear () {
      this.params.account = ''
      this.loadUserList()
    },
    add: function () {
      this.$refs.sysUserEditPop.getSysUserDetailData()
    },

    // 子调父的方法,此方法用来添加刷新
    saveUserEdit () { this.loadUserList() },

    /** ***************************************** 请求接口方法 *******************************************/
    // 获取账号列表
    loadUserList: function () {
      this.loading = true
      setTimeout(() => {
        this.loading = false
      }, 10000)
      this.$api.system.getUserListApi(this.params).then(res => {
        let data = res.data
        if (data.code === 0) {
          this.userList = data.data.list
          this.totalCount = data.data.total
        } else {
          this.$Message.error(data.msg)
        }
        this.loading = false
      })
    },

    // 启用 | 禁用
    updateStatus: function (data) {
      let status = data.status === '0' ? '1' : '0'
      let statusStr = data.status === '0' ? '禁用' : '启用'
      this.$api.system.saveOrUpdateUserApi({'id': data.id, 'status': status}).then(res => {
        let data = res.data
        if (data.code === 0) {
          this.$Message.info(statusStr + '成功')
          this.loadUserList()
        } else {
          this.$Message.error(data.msg)
        }
        this.loading = false
      })
      // this.$Modal.confirm({
      //   title: '更改账号状态!',
      //   content: '<p>确认要' + statusStr + '吗？</p>',
      //   onOk: () => {
      //     this.loading = true
      //     setTimeout(() => { this.loading = false }, 500)
      //     this.$api.system.saveOrUpdateUserApi({'id': data.id, 'status': status}).then(res => {
      //       let data = res.data
      //       if (data.code === 0) {
      //         this.$Message.info(statusStr + '成功')
      //         this.loadUserList()
      //       } else {
      //         this.$Message.error(data.msg)
      //       }
      //       this.loading = false
      //     })
      //   },
      //   onCancel: () => { this.$Message.info('已取消') }
      // })
    }

    // 检查操作权限
    // checkOperationAccess (requireAccess) {
    //   return hasOperationAccess(requireAccess)
    // }
  },

  // vue生命周期-组件创建完成执行
  created () {
    this.loadUserList()
  }
}
</script>
<style lang="scss">
.ivu-card-body {
  padding: 0;
}
.ivu-table-wrapper {
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
</style>
