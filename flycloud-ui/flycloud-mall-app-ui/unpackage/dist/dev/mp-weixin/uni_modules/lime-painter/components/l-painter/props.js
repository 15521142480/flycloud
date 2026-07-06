"use strict";
const props = {
  props: {
    board: Object,
    pathType: String,
    // 'base64'、'url'
    fileType: {
      type: String,
      default: "png"
    },
    hidden: Boolean,
    quality: {
      type: Number,
      default: 1
    },
    css: [String, Object],
    // styles: [String, Object],
    width: [Number, String],
    height: [Number, String],
    pixelRatio: Number,
    customStyle: String,
    isCanvasToTempFilePath: Boolean,
    // useCanvasToTempFilePath: Boolean,
    sleep: {
      type: Number,
      default: 1e3 / 30
    },
    beforeDelay: {
      type: Number,
      default: 100
    },
    afterDelay: {
      type: Number,
      default: 100
    },
    performance: Boolean,
    type: {
      type: String,
      default: "2d"
    }
  }
};
exports.props = props;
//# sourceMappingURL=../../../../../.sourcemap/mp-weixin/uni_modules/lime-painter/components/l-painter/props.js.map
