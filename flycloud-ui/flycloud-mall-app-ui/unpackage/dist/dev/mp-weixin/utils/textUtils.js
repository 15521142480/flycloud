"use strict";
const common_vendor = require("../common/vendor.js");
function measureTextWidth(text, fontSize = 14, fontFamily = "sans-serif") {
  if (typeof common_vendor.index === "undefined" || typeof common_vendor.index.createCanvasContext !== "function") {
    return estimateTextWidth(text, fontSize);
  }
  try {
    const ctx = common_vendor.index.createCanvasContext("tempCanvasForText");
    ctx.setFontSize(fontSize);
    ctx.font = `${fontSize}px ${fontFamily}`;
    const metrics = ctx.measureText(text);
    return metrics.width;
  } catch (e) {
    return estimateTextWidth(text, fontSize);
  }
}
function estimateTextWidth(text, fontSize = 14) {
  let width = 0;
  for (let i = 0; i < text.length; i++) {
    const charCode = text.charCodeAt(i);
    if (charCode >= 19968 && charCode <= 40959) {
      width += fontSize;
    } else {
      width += fontSize * 0.5;
    }
  }
  return width;
}
exports.measureTextWidth = measureTextWidth;
//# sourceMappingURL=../../.sourcemap/mp-weixin/utils/textUtils.js.map
