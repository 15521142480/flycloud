<template>
  <div>

    <div class="demo-split">
      <Split v-model="split1">

        <div slot="left" class="demo-split-pane">
          <Row>
            <Col span="14">
              Left Pane Left Pane Left Pane Left Pane Left Pane Left Pane Left Pane Left Pane Left Pane Left Pane Left
              Pane
            </Col>
            <Col span="10">
              Left Pane Left Pane Left
            </Col>
          </Row>
        </div>

        <div slot="right" class="demo-split-pane">
          123123
        </div>
      </Split>
    </div>

    <div>
      <button @click="treeTest">树数据测试</button>
      <div>{{ treeData }}</div>
    </div>

  </div>
</template>

<script>
export default {
  name: 'test-list',
  components: {},
  props: {},
  created () {
  },
  mounted () {
  },
  data () {
    return {

      split1: 0.5,

      dataList: [
        {id: '1', pId: null, name: 'a'},
        {id: '2', pId: null, name: 'b'},
        {id: '3', pId: '1', name: 'c'},
        {id: '4', pId: '2', name: 'd'},
        {id: '5', pId: '1', name: 'e'},
        {id: '6', pId: '3', name: 'f'},
        {id: '7', pId: '4', name: 'g'},
        {id: '8', pId: '7', name: 'h'}
      ],
      treeData: []

    }
  },
  methods: {

    treeTest () {
      const idListMap = new Map() // id列表map 优化递归时所处理的数据
      const resultTreeList = [] // 结果树数据

      this.dataList.forEach(item => {
        const parentId = item.pId
        if (idListMap.get(parentId) === undefined || idListMap.get(parentId) === null) {
          idListMap.set(parentId, [])
        }
        idListMap.get(parentId).push(item) // 在其原有id列表追加; 即记录没一个单独的父id的下级集合(如父id为1下级有1.1和1.2, 其结构为 map -> {1: [1-1, 1-2]} )
      })

      this.dataList.forEach((item, index) => {
        const parentId2 = item.pId
        if (parentId2 === undefined || parentId2 === null) { // 当前目录的的父分类(即只有一个最大父级)
          item.children = this.handleChild(item.id, idListMap)
          resultTreeList.push(item)
        }
      })
      this.treeData = resultTreeList
    },

    /**
     * 递归children
     * @param id
     * @param idListMap
     */
    handleChild (id, idListMap) {
      const dataList = idListMap.get(id)
      if (dataList !== undefined && dataList !== null) {
        dataList.forEach(item => {
          item.children = this.handleChild(item.id, idListMap)
        })
      }
      return dataList
    }
  }

}
</script>

<style lang="scss">
.demo-split {
  height: 200px;
  border: 1px solid #dcdee2;
}

.demo-split-pane {
  padding: 10px;
}
</style>
