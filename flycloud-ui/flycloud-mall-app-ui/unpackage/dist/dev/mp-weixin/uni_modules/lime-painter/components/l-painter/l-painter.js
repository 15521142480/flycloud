"use strict";
const common_vendor = require("../../../../common/vendor.js");
const uni_modules_limePainter_components_common_relation = require("../common/relation.js");
const uni_modules_limePainter_components_lPainter_props = require("./props.js");
const uni_modules_limePainter_components_lPainter_utils = require("./utils.js");
const uni_modules_limePainter_components_lPainter_painter = require("./painter.js");
const nvue = {};
const _sfc_main = {
  name: "lime-painter",
  mixins: [uni_modules_limePainter_components_lPainter_props.props, uni_modules_limePainter_components_common_relation.parent("painter"), nvue],
  data() {
    return {
      use2dCanvas: false,
      canvasHeight: 150,
      canvasWidth: null,
      parentWidth: 0,
      inited: false,
      progress: 0,
      firstRender: 0,
      done: false,
      tasks: []
    };
  },
  computed: {
    styles() {
      return `${this.size}${this.customStyle || ""};` + (this.hidden && "position: fixed; left: 1500rpx;");
    },
    canvasId() {
      return `l-painter${this._ && this._.uid || this._uid}`;
    },
    size() {
      if (this.boardWidth && this.boardHeight) {
        return `width:${this.boardWidth}px; height: ${this.boardHeight}px;`;
      }
    },
    dpr() {
      return this.pixelRatio || common_vendor.index.getSystemInfoSync().pixelRatio;
    },
    boardWidth() {
      const { width = 0 } = this.elements && this.elements.css || this.elements || this;
      const w = uni_modules_limePainter_components_lPainter_utils.toPx(width || this.width);
      return w || Math.max(w, uni_modules_limePainter_components_lPainter_utils.toPx(this.canvasWidth));
    },
    boardHeight() {
      const { height = 0 } = this.elements && this.elements.css || this.elements || this;
      const h = uni_modules_limePainter_components_lPainter_utils.toPx(height || this.height);
      return h || Math.max(h, uni_modules_limePainter_components_lPainter_utils.toPx(this.canvasHeight));
    },
    hasBoard() {
      return this.board && Object.keys(this.board).length;
    },
    elements() {
      return this.hasBoard ? this.board : JSON.parse(JSON.stringify(this.el));
    }
  },
  created() {
    this.use2dCanvas = this.type === "2d" && uni_modules_limePainter_components_lPainter_utils.canIUseCanvas2d() && !uni_modules_limePainter_components_lPainter_utils.isPC;
  },
  async mounted() {
    await uni_modules_limePainter_components_lPainter_utils.sleep(30);
    await this.getParentWeith();
    this.$nextTick(() => {
      setTimeout(() => {
        this.$watch("elements", this.watchRender, {
          deep: true,
          immediate: true
        });
      }, 30);
    });
  },
  unmounted() {
    this.done = false;
    this.inited = false;
    this.firstRender = 0;
    this.progress = 0;
    this.painter = null;
    clearTimeout(this.rendertimer);
  },
  methods: {
    async watchRender(val, old) {
      if (!val || !val.views || (!this.firstRender ? !val.views.length : !this.firstRender) || !Object.keys(val).length || JSON.stringify(val) == JSON.stringify(old))
        return;
      this.firstRender = 1;
      this.progress = 0;
      this.done = false;
      clearTimeout(this.rendertimer);
      this.rendertimer = setTimeout(() => {
        this.render(val);
      }, this.beforeDelay);
    },
    async setFilePath(path, param) {
      let filePath = path;
      const { pathType = this.pathType } = param || this;
      if (pathType == "base64" && !uni_modules_limePainter_components_lPainter_utils.isBase64(path)) {
        filePath = await uni_modules_limePainter_components_lPainter_utils.pathToBase64(path);
      } else if (pathType == "url" && uni_modules_limePainter_components_lPainter_utils.isBase64(path)) {
        filePath = await uni_modules_limePainter_components_lPainter_utils.base64ToPath(path);
      }
      if (param && param.isEmit) {
        this.$emit("success", filePath);
      }
      return filePath;
    },
    async getSize(args) {
      const { width } = args.css || args;
      const { height } = args.css || args;
      if (!this.size) {
        if (width || height) {
          this.canvasWidth = width || this.canvasWidth;
          this.canvasHeight = height || this.canvasHeight;
          await uni_modules_limePainter_components_lPainter_utils.sleep(30);
        } else {
          await this.getParentWeith();
        }
      }
    },
    canvasToTempFilePathSync(args) {
      this.tasks.push(args);
      if (this.done) {
        this.runTask();
      }
    },
    runTask() {
      while (this.tasks.length) {
        const task = this.tasks.shift();
        this.canvasToTempFilePath(task);
      }
    },
    getParentWeith() {
      return new Promise((resolve) => {
        common_vendor.index.createSelectorQuery().in(this).select(`.lime-painter`).boundingClientRect().exec((res) => {
          const { width, height } = res[0] || {};
          this.parentWidth = Math.ceil(width || 0);
          this.canvasWidth = this.parentWidth || 300;
          this.canvasHeight = height || this.canvasHeight || 150;
          resolve(res[0]);
        });
      });
    },
    async render(args = {}) {
      if (!Object.keys(args).length) {
        return common_vendor.index.__f__("error", "at uni_modules/lime-painter/components/l-painter/l-painter.vue:193", "空对象");
      }
      this.progress = 0;
      this.done = false;
      await this.getSize(args);
      const ctx = await this.getContext();
      let {
        use2dCanvas,
        boardWidth,
        boardHeight,
        canvas,
        afterDelay
      } = this;
      if (use2dCanvas && !canvas) {
        return Promise.reject(new Error("canvas 没创建"));
      }
      this.boundary = {
        top: 0,
        left: 0,
        width: boardWidth,
        height: boardHeight
      };
      this.painter = null;
      if (!this.painter) {
        const { width } = args.css || args;
        args.css || args;
        if (!width && this.parentWidth) {
          Object.assign(args, { width: this.parentWidth });
        }
        const param = {
          context: ctx,
          canvas,
          width: boardWidth,
          height: boardHeight,
          pixelRatio: this.dpr,
          useCORS: this.useCORS,
          createImage: uni_modules_limePainter_components_lPainter_utils.getImageInfo.bind(this),
          performance: this.performance,
          listen: {
            onProgress: (v) => {
              this.progress = v;
              this.$emit("progress", v);
            },
            onEffectFail: (err) => {
              this.$emit("faill", err);
            }
          }
        };
        this.painter = new uni_modules_limePainter_components_lPainter_painter.Bt(param);
      }
      try {
        const { width, height } = await this.painter.source(JSON.parse(JSON.stringify(args)));
        this.boundary.height = this.canvasHeight = height;
        this.boundary.width = this.canvasWidth = width;
        await uni_modules_limePainter_components_lPainter_utils.sleep(this.sleep);
        await this.painter.render();
        await new Promise((resolve) => this.$nextTick(resolve));
        if (!use2dCanvas) {
          await this.canvasDraw();
        }
        if (afterDelay && use2dCanvas) {
          await uni_modules_limePainter_components_lPainter_utils.sleep(afterDelay);
        }
        this.$emit("done");
        this.done = true;
        if (this.isCanvasToTempFilePath) {
          this.canvasToTempFilePath().then((res) => {
            this.$emit("success", res.tempFilePath);
          }).catch((err) => {
            this.$emit("fail", new Error(JSON.stringify(err)));
          });
        }
        this.runTask();
        return Promise.resolve({
          ctx,
          draw: this.painter,
          node: this.node
        });
      } catch (e) {
      }
    },
    canvasDraw(flag = false) {
      return new Promise((resolve, reject) => this.ctx.draw(flag, () => setTimeout(() => resolve(), this.afterDelay)));
    },
    async getContext() {
      if (!this.canvasWidth) {
        this.$emit("fail", "painter no size");
        common_vendor.index.__f__("error", "at uni_modules/lime-painter/components/l-painter/l-painter.vue:290", "[lime-painter]: 给画板或父级设置尺寸");
        return Promise.reject();
      }
      if (this.ctx && this.inited) {
        return Promise.resolve(this.ctx);
      }
      const { type, use2dCanvas, dpr, boardWidth, boardHeight } = this;
      const _getContext = () => {
        return new Promise((resolve) => {
          common_vendor.index.createSelectorQuery().in(this).select(`#${this.canvasId}`).boundingClientRect().exec((res) => {
            if (res) {
              const ctx = common_vendor.index.createCanvasContext(this.canvasId, this);
              if (!this.inited) {
                this.inited = true;
                this.use2dCanvas = false;
                this.canvas = res;
              }
              if (!ctx.measureText) {
                let strLen = function(str) {
                  let len = 0;
                  for (let i = 0; i < str.length; i++) {
                    if (str.charCodeAt(i) > 0 && str.charCodeAt(i) < 128) {
                      len++;
                    } else {
                      len += 2;
                    }
                  }
                  return len;
                };
                ctx.measureText = (text) => {
                  let fontSize = ctx.state && ctx.state.fontSize || 12;
                  const font = ctx.__font;
                  if (font && fontSize == 12) {
                    fontSize = parseInt(font.split(" ")[3], 10);
                  }
                  fontSize /= 2;
                  return {
                    width: strLen(text) * fontSize
                  };
                };
              }
              this.ctx = ctx;
              resolve(this.ctx);
            } else {
              common_vendor.index.__f__("error", "at uni_modules/lime-painter/components/l-painter/l-painter.vue:344", "[lime-painter] no node");
            }
          });
        });
      };
      if (!use2dCanvas) {
        return _getContext();
      }
      return new Promise((resolve) => {
        common_vendor.index.createSelectorQuery().in(this).select(`#${this.canvasId}`).node().exec((res) => {
          let { node: canvas } = res && res[0] || {};
          if (canvas) {
            const ctx = canvas.getContext(type);
            if (!this.inited) {
              this.inited = true;
              this.use2dCanvas = true;
              this.canvas = canvas;
            }
            this.ctx = ctx;
            resolve(this.ctx);
          } else {
            common_vendor.index.__f__("error", "at uni_modules/lime-painter/components/l-painter/l-painter.vue:369", "[lime-painter]: no size");
          }
        });
      });
    },
    canvasToTempFilePath(args = {}) {
      return new Promise(async (resolve, reject) => {
        const { use2dCanvas, canvasId, dpr, fileType, quality } = this;
        const success = async (res) => {
          try {
            const tempFilePath = await this.setFilePath(res.tempFilePath || res, args);
            const result = Object.assign(res, { tempFilePath });
            args.success && args.success(result);
            resolve(result);
          } catch (e) {
            this.$emit("fail", e);
          }
        };
        this.boundary || this;
        const copyArgs = Object.assign({
          // x,
          // y,
          // width,
          // height,
          // destWidth,
          // destHeight,
          canvasId,
          id: canvasId,
          fileType,
          quality
        }, args, { success });
        if (use2dCanvas) {
          copyArgs.canvas = this.canvas;
          try {
            const oFilePath = this.canvas.toDataURL(`image/${args.fileType || fileType}`.replace(/pg/, "peg"), args.quality || quality);
            if (/data:,/.test(oFilePath)) {
              common_vendor.index.canvasToTempFilePath(copyArgs, this);
            } else {
              const tempFilePath = await this.setFilePath(oFilePath, args);
              args.success && args.success({ tempFilePath });
              resolve({ tempFilePath });
            }
          } catch (e) {
            args.fail && args.fail(e);
            reject(e);
          }
        } else {
          common_vendor.index.canvasToTempFilePath(copyArgs, this);
        }
      });
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return common_vendor.e({
    a: $options.canvasId && $options.size
  }, $options.canvasId && $options.size ? common_vendor.e({
    b: $data.use2dCanvas
  }, $data.use2dCanvas ? {
    c: $options.canvasId,
    d: common_vendor.s($options.size)
  } : {
    e: $options.canvasId,
    f: $options.canvasId,
    g: common_vendor.s($options.size),
    h: $options.boardWidth * $options.dpr,
    i: $options.boardHeight * $options.dpr,
    j: _ctx.hidpi
  }, {
    k: common_vendor.s($options.styles)
  }) : {});
}
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../../.sourcemap/mp-weixin/uni_modules/lime-painter/components/l-painter/l-painter.js.map
