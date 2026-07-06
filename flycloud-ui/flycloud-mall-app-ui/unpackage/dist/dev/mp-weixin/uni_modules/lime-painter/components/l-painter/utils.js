"use strict";
const common_vendor = require("../../../../common/vendor.js");
const isBase64 = (path) => /^data:image\/(\w+);base64/.test(path);
function sleep(delay) {
  return new Promise((resolve) => setTimeout(resolve, delay));
}
let { platform, SDKVersion } = common_vendor.index.getSystemInfoSync();
const isPC = /windows|mac/.test(platform);
let cache = {};
function isNumber(value) {
  return /^-?\d+(\.\d+)?$/.test(value);
}
function toPx(value, baseSize, isDecimal = false) {
  if (typeof value === "number") {
    return value;
  }
  if (isNumber(value)) {
    return value * 1;
  }
  if (typeof value === "string") {
    const reg = /^-?([0-9]+)?([.]{1}[0-9]+){0,1}(em|rpx|px|%)$/g;
    const results = reg.exec(value);
    if (!value || !results) {
      return 0;
    }
    const unit = results[3];
    value = parseFloat(value);
    let res = 0;
    if (unit === "rpx") {
      res = common_vendor.index.upx2px(value);
    } else if (unit === "px") {
      res = value * 1;
    } else if (unit === "%") {
      res = value * toPx(baseSize) / 100;
    } else if (unit === "em") {
      res = value * toPx(baseSize || 14);
    }
    return isDecimal ? res.toFixed(2) * 1 : Math.round(res);
  }
  return 0;
}
function compareVersion(v1, v2) {
  v1 = v1.split(".");
  v2 = v2.split(".");
  const len = Math.max(v1.length, v2.length);
  while (v1.length < len) {
    v1.push("0");
  }
  while (v2.length < len) {
    v2.push("0");
  }
  for (let i = 0; i < len; i++) {
    const num1 = parseInt(v1[i], 10);
    const num2 = parseInt(v2[i], 10);
    if (num1 > num2) {
      return 1;
    } else if (num1 < num2) {
      return -1;
    }
  }
  return 0;
}
function gte(version) {
  return compareVersion(SDKVersion, version) >= 0;
}
function canIUseCanvas2d() {
  return gte("2.9.2");
}
const prefix = () => {
  return common_vendor.wx$1;
};
function base64ToPath(base64) {
  const [, format] = /^data:image\/(\w+);base64,/.exec(base64) || [];
  return new Promise((resolve, reject) => {
    const fs = common_vendor.index.getFileSystemManager();
    if (!format) {
      reject(new Error("ERROR_BASE64SRC_PARSE"));
    }
    const time = (/* @__PURE__ */ new Date()).getTime();
    let pre = prefix();
    const filePath = `${pre.env.USER_DATA_PATH}/${time}.${format}`;
    fs.writeFile({
      filePath,
      data: base64.split(",")[1],
      encoding: "base64",
      success() {
        resolve(filePath);
      },
      fail(err) {
        common_vendor.index.__f__("error", "at uni_modules/lime-painter/components/l-painter/utils.js:144", err);
        reject(err);
      }
    });
  });
}
function pathToBase64(path) {
  if (/^data:/.test(path))
    return path;
  return new Promise((resolve, reject) => {
    if (common_vendor.index.canIUse("getFileSystemManager")) {
      common_vendor.index.getFileSystemManager().readFile({
        filePath: path,
        encoding: "base64",
        success: (res) => {
          resolve("data:image/png;base64," + res.data);
        },
        fail: (error) => {
          common_vendor.index.__f__("error", "at uni_modules/lime-painter/components/l-painter/utils.js:226", { error, path });
          reject(error);
        }
      });
    }
  });
}
function getImageInfo(path, useCORS) {
  const isCanvas2D = this && this.canvas && this.canvas.createImage;
  return new Promise(async (resolve, reject) => {
    let src = path.replace(/^@\//, "/");
    if (cache[path] && cache[path].errMsg) {
      resolve(cache[path]);
    } else {
      try {
        if (isBase64(path) && (isCanvas2D ? isPC : true)) {
          src = await base64ToPath(path);
        }
      } catch (error) {
        reject({
          ...error,
          src
        });
      }
      if (isCanvas2D && !isPC) {
        const img = this.canvas.createImage();
        img.onload = function() {
          const image = {
            path: img,
            width: img.width,
            height: img.height
          };
          cache[path] = image;
          resolve(cache[path]);
        };
        img.onerror = function(err) {
          reject({ err, path });
        };
        img.src = src;
        return;
      }
      common_vendor.index.getImageInfo({
        src,
        success: (image) => {
          const localReg = /^\.|^\/(?=[^\/])/;
          image.path = localReg.test(src) ? `/${image.path}` : image.path;
          if (isCanvas2D) {
            const img = this.canvas.createImage();
            img.onload = function() {
              image.path = img;
              cache[path] = image;
              resolve(cache[path]);
            };
            img.onerror = function(err) {
              reject({ err, path });
            };
            img.src = src;
            return;
          }
          cache[path] = image;
          resolve(cache[path]);
        },
        fail(err) {
          common_vendor.index.__f__("error", "at uni_modules/lime-painter/components/l-painter/utils.js:335", { err, path });
          reject({ err, path });
        }
      });
    }
  });
}
exports.base64ToPath = base64ToPath;
exports.canIUseCanvas2d = canIUseCanvas2d;
exports.getImageInfo = getImageInfo;
exports.isBase64 = isBase64;
exports.isPC = isPC;
exports.pathToBase64 = pathToBase64;
exports.sleep = sleep;
exports.toPx = toPx;
//# sourceMappingURL=../../../../../.sourcemap/mp-weixin/uni_modules/lime-painter/components/l-painter/utils.js.map
