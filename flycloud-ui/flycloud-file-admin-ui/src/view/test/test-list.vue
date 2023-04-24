<template>

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

      split1: 0.5

    }
  },
  methods: {}

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
