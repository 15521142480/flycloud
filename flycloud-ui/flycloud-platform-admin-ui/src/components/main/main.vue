<template>
  <div class="layout" :class="{'layout-hide-text': spanLeft < 5}">
    <!--  左边的菜单导航  -->
    <Row type="flex">
      <i-col :span="spanLeft" class="layout-menu-left">
        <div class="layout-logo-left">
          飞翔云平台管理系统
        </div>
        <main-menu ref="mainMenuRef" :span-left="spanLeft" @on-menu-select="handleSelectMenu"/>
      </i-col>
      <!--  右边的头部/内容/底部  -->
      <i-col :span="spanRight">
        <!--  右边的头部  -->
        <Row style="z-index: 2">
          <Col span="24"> <!--style="background: #fff;"-->
            <div class="layout-header">
              <Row>
                <Col span="12">
                  <Button type="text" @click="toggleClick">
                    <Icon type="md-menu" size="30"/>
                  </Button>
                </Col>
                <Col span="6">
                  &nbsp;
                </Col>
                <Col span="6">
                  <div style="float: right;">
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
          </Col>
        </Row>
        <!--        <div class="layout-breadcrumb">-->
        <!--          <Breadcrumb>-->
        <!--            <Breadcrumb-item href="#">首页</Breadcrumb-item>-->
        <!--            <Breadcrumb-item href="#">应用中心</Breadcrumb-item>-->
        <!--            <Breadcrumb-item>某应用</Breadcrumb-item>-->
        <!--          </Breadcrumb>-->
        <!--        </div>-->
        <!--  右边的内容  -->
        <Card class="layout-content">
          <div class="layout-content-main">
            <!-- <keep-alive :include="cacheList"><router-view/></keep-alive>-->
            <router-view/>
          </div>
        </Card>
        <!--  右边的底部  -->
        <div class="layout-copy">
          2022-2024 &copy; 飞翔云-https://github.com/15521142480/flycloud
        </div>
      </i-col>
    </Row>
  </div>
</template>

<script>
import mainMenu from './components/main-menu'
import { getUserName, removeUserCache } from '../../util/cacheUtils'
export default {
  components: {
    mainMenu
  },
  props: {
  },
  data () {
    return {
      userName: getUserName(),
      spanLeft: 5,
      spanRight: 19
    }
  },

  computed: {
    iconSize () {
      return this.spanLeft === 5 ? 14 : 24
    }
  },
  methods: {
    toggleClick () {
      if (this.spanLeft === 5) {
        this.spanLeft = 2
        this.spanRight = 22
      } else {
        this.spanLeft = 5
        this.spanRight = 19
      }
    },
    handleSelectMenu (menuRouterPath) {
      // this.$router.push({
      //   'test',
      //   params,
      //   query
      // })
      this.$router.push(menuRouterPath)
    },
    // 退出登录 登出
    loginOut () {
      this.$Modal.confirm({
        title: '退出系统',
        content: '<p>确认要退出系统吗?</p>',
        onOk: () => {
          this.$api.system.loginOutApi().then((res) => {
            let resultCode = res.data.code
            let resultMsg = res.data.msg
            if (resultCode === 0) {
              this.$Notice.success({title: '操作提醒', desc: resultMsg})
            } else {
              this.$Notice.error({title: '操作提醒', desc: '操作失败' + resultMsg})
            }
            removeUserCache()
            this.$router.push('/login')
          }).catch((e) => {
            removeUserCache()
            this.$router.push('/login')
          })
        }
      })
    }
  },
  watch: {},
  created () {

  },
  mounted () {
  }
}
</script>

<style scoped lang="scss">
.layout{
  border: 1px solid #d7dde4;
  background: #f5f7f9;
  position: relative;
  border-radius: 4px;
  overflow: hidden;
}
.layout-breadcrumb{
  padding: 10px 15px 0;
}
.layout-content{
  height: calc(100vh - 122px);
  margin: 15px;
  overflow: hidden;
  background: #fff;
  border-radius: 4px;
}
.layout-content-main{
  padding: 10px;
}
.layout-copy{
  text-align: center;
  padding: 2px 0 10px;
  color: #9ea7b4;
}
.layout-menu-left{
  background: #464c5b;
}
.layout-header{
  height: 60px;
  background: #fff;
  box-shadow: 0 1px 1px rgba(0,0,0,.1);
}
.layout-logo-left{
  text-align: center;
  //background: #5b6270;
  background: #ececec;
  width: 92%;
  height: 40px;
  font-size: 17px;
  font-weight: bold;
  border-radius: 3px;
  margin: 15px auto;
  padding-top: 7px;
}
.layout-ceiling-main a{
  color: #9ba7b5;
}
.layout-hide-text .layout-text{
  display: none;
}
.ivu-col{
  transition: width .2s ease-in-out;
}
</style>
