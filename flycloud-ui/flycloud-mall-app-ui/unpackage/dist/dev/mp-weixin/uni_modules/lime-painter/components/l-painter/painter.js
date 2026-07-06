"use strict";
const common_vendor = require("../../../../common/vendor.js");
var t = function() {
  return t = Object.assign || function(t2) {
    for (var e2, i2 = 1, n2 = arguments.length; i2 < n2; i2++)
      for (var r2 in e2 = arguments[i2])
        Object.prototype.hasOwnProperty.call(e2, r2) && (t2[r2] = e2[r2]);
    return t2;
  }, t.apply(this, arguments);
};
function e(t2, e2, i2, n2) {
  return new (i2 || (i2 = Promise))(function(r2, o2) {
    function s2(t3) {
      try {
        h2(n2.next(t3));
      } catch (t4) {
        o2(t4);
      }
    }
    function a2(t3) {
      try {
        h2(n2.throw(t3));
      } catch (t4) {
        o2(t4);
      }
    }
    function h2(t3) {
      var e3;
      t3.done ? r2(t3.value) : (e3 = t3.value, e3 instanceof i2 ? e3 : new i2(function(t4) {
        t4(e3);
      })).then(s2, a2);
    }
    h2((n2 = n2.apply(t2, e2 || [])).next());
  });
}
function i(t2, e2) {
  var i2, n2, r2, o2, s2 = { label: 0, sent: function() {
    if (1 & r2[0])
      throw r2[1];
    return r2[1];
  }, trys: [], ops: [] };
  return o2 = { next: a2(0), throw: a2(1), return: a2(2) }, "function" == typeof Symbol && (o2[Symbol.iterator] = function() {
    return this;
  }), o2;
  function a2(o3) {
    return function(a3) {
      return function(o4) {
        if (i2)
          throw new TypeError("Generator is already executing.");
        for (; s2; )
          try {
            if (i2 = 1, n2 && (r2 = 2 & o4[0] ? n2.return : o4[0] ? n2.throw || ((r2 = n2.return) && r2.call(n2), 0) : n2.next) && !(r2 = r2.call(n2, o4[1])).done)
              return r2;
            switch (n2 = 0, r2 && (o4 = [2 & o4[0], r2.value]), o4[0]) {
              case 0:
              case 1:
                r2 = o4;
                break;
              case 4:
                return s2.label++, { value: o4[1], done: false };
              case 5:
                s2.label++, n2 = o4[1], o4 = [0];
                continue;
              case 7:
                o4 = s2.ops.pop(), s2.trys.pop();
                continue;
              default:
                if (!(r2 = s2.trys, (r2 = r2.length > 0 && r2[r2.length - 1]) || 6 !== o4[0] && 2 !== o4[0])) {
                  s2 = 0;
                  continue;
                }
                if (3 === o4[0] && (!r2 || o4[1] > r2[0] && o4[1] < r2[3])) {
                  s2.label = o4[1];
                  break;
                }
                if (6 === o4[0] && s2.label < r2[1]) {
                  s2.label = r2[1], r2 = o4;
                  break;
                }
                if (r2 && s2.label < r2[2]) {
                  s2.label = r2[2], s2.ops.push(o4);
                  break;
                }
                r2[2] && s2.ops.pop(), s2.trys.pop();
                continue;
            }
            o4 = e2.call(t2, s2);
          } catch (t3) {
            o4 = [6, t3], n2 = 0;
          } finally {
            i2 = r2 = 0;
          }
        if (5 & o4[0])
          throw o4[1];
        return { value: o4[0] ? o4[1] : void 0, done: true };
      }([o3, a3]);
    };
  }
}
var n = { MP_WEIXIN: "mp-weixin", MP_QQ: "mp-qq", MP_ALIPAY: "mp-alipay", MP_BAIDU: "mp-baidu", MP_TOUTIAO: "mp-toutiao", MP_DINGDING: "mp-dingding", H5: "h5", WEB: "web", PLUS: "plus" }, r = ["Top", "Right", "Bottom", "Left"], o = "right", s = "bottom", a = ["contentSize", "clientSize", "borderSize", "offsetSize"], h = "row", c = "column", f = { TOP: "top", MIDDLE: "middle", BOTTOM: s }, l = { LEFT: "left", CENTER: "center", RIGHT: o }, d = "view", u = "text", p = "image", g = "qrcode", v = "block", y = "inline-block", x = "none", b = "flex", w = "absolute", m = "fixed", S = { display: v, color: "#000000", lineHeight: "1.4em", fontSize: 14, fontWeight: 400, fontFamily: "sans-serif", lineCap: "butt", flexDirection: h, flexWrap: "nowrap", textAlign: "left", alignItems: "flex-start", justifyContent: "flex-start", position: "static", transformOrigin: "".concat("center", " ").concat("center") }, z = { upx2px: function(t2) {
  return window.innerWidth / 750 * t2;
}, getSystemInfoSync: function() {
  return { screenWidth: window.innerWidth, screenHeight: window.innerHeight };
}, getImageInfo: function(t2) {
  var e2 = t2.src, i2 = t2.success, n2 = t2.fail, r2 = new Image();
  r2.onload = function() {
    i2({ width: r2.naturalWidth, height: r2.naturalHeight, path: r2.src, src: e2 });
  }, r2.onerror = n2, r2.src = e2;
} }, I = "object" == typeof window ? "undefined" == typeof common_vendor.index || "undefined" != typeof common_vendor.index && !common_vendor.index.addInterceptor ? n.WEB : n.H5 : "object" == typeof swan ? n.MP_BAIDU : "object" == typeof tt ? n.MP_TOUTIAO : "object" == typeof plus ? n.PLUS : "object" == typeof common_vendor.wx$1 ? n.MP_WEIXIN : void 0, M = I == n.MP_WEIXIN ? common_vendor.wx$1 : "undefined" != typeof common_vendor.index ? common_vendor.index.getImageInfo ? { upx2px: function(t2) {
  return common_vendor.index.upx2px(t2);
}, getSystemInfoSync: function() {
  return common_vendor.index.getSystemInfoSync();
}, getImageInfo: function(t2) {
  return common_vendor.index.getImageInfo(t2);
}, downloadFile: function(t2) {
  return common_vendor.index.downloadFile(t2);
} } : Object.assign(common_vendor.index, z) : "undefined" != typeof window ? z : common_vendor.index;
if (!M.upx2px) {
  var k = ((M.getSystemInfoSync && M.getSystemInfoSync()).screenWidth || 375) / 750;
  M.upx2px = function(t2) {
    return k * t2;
  };
}
function B(t2) {
  return /^-?\d+(\.\d+)?$/.test(t2);
}
function W(t2, e2, i2) {
  if (B(t2))
    return 1 * t2;
  if ("string" == typeof t2) {
    var n2 = /^-?([0-9]+)?([.]{1}[0-9]+){0,1}(em|rpx|vw|vh|px|%)$/g.exec(t2);
    if (!t2 || !n2)
      return 0;
    var r2 = n2[3];
    t2 = parseFloat(t2);
    var o2 = 0;
    if ("rpx" === r2)
      o2 = M.upx2px(t2);
    else if ("px" === r2)
      o2 = 1 * t2;
    else if ("%" === r2 && e2)
      o2 = t2 * W(e2) / 100;
    else if ("em" === r2 && e2)
      o2 = t2 * W(e2 || 14);
    else if (["vw", "vh"].includes(r2)) {
      var s2 = M.getSystemInfoSync(), a2 = s2.screenWidth, h2 = s2.screenHeight;
      o2 = t2 * ("vw" == r2 ? a2 : h2) / 100;
    }
    return 1 * o2.toFixed(2);
  }
  return 0;
}
function P(t2) {
  return /%$/.test(t2);
}
function O(t2) {
  return /^-?([0-9]+)?([.]{1}[0-9]+){0,1}(rpx|px)$/.test(t2);
}
var T = function(t2) {
  return !(!t2 || !t2.startsWith("linear") && !t2.startsWith("radial"));
}, L = function(t2, e2, i2, n2, r2, o2) {
  t2.startsWith("linear") ? function(t3, e3, i3, n3, r3, o3) {
    for (var s2 = function(t4, e4, i4, n4, r4) {
      void 0 === n4 && (n4 = 0);
      void 0 === r4 && (r4 = 0);
      var o4 = t4.match(/([-]?\d{1,3})deg/), s3 = o4 && o4[1] ? parseFloat(o4[1]) : 0;
      s3 >= 360 && (s3 -= 360);
      s3 < 0 && (s3 += 360);
      if (0 === (s3 = Math.round(s3)))
        return { x0: Math.round(e4 / 2) + n4, y0: i4 + r4, x1: Math.round(e4 / 2) + n4, y1: r4 };
      if (180 === s3)
        return { x0: Math.round(e4 / 2) + n4, y0: r4, x1: Math.round(e4 / 2) + n4, y1: i4 + r4 };
      if (90 === s3)
        return { x0: n4, y0: Math.round(i4 / 2) + r4, x1: e4 + n4, y1: Math.round(i4 / 2) + r4 };
      if (270 === s3)
        return { x0: e4 + n4, y0: Math.round(i4 / 2) + r4, x1: n4, y1: Math.round(i4 / 2) + r4 };
      var a3 = Math.round(180 * Math.asin(e4 / Math.sqrt(Math.pow(e4, 2) + Math.pow(i4, 2))) / Math.PI);
      if (s3 === a3)
        return { x0: n4, y0: i4 + r4, x1: e4 + n4, y1: r4 };
      if (s3 === 180 - a3)
        return { x0: n4, y0: r4, x1: e4 + n4, y1: i4 + r4 };
      if (s3 === 180 + a3)
        return { x0: e4 + n4, y0: r4, x1: n4, y1: i4 + r4 };
      if (s3 === 360 - a3)
        return { x0: e4 + n4, y0: i4 + r4, x1: n4, y1: r4 };
      var h3 = 0, c3 = 0, f3 = 0, l3 = 0;
      if (s3 < a3 || s3 > 180 - a3 && s3 < 180 || s3 > 180 && s3 < 180 + a3 || s3 > 360 - a3) {
        var d3 = s3 * Math.PI / 180, u3 = s3 < a3 || s3 > 360 - a3 ? i4 / 2 : -i4 / 2, p3 = Math.tan(d3) * u3, g2 = s3 < a3 || s3 > 180 - a3 && s3 < 180 ? e4 / 2 - p3 : -e4 / 2 - p3;
        h3 = -(f3 = p3 + (v2 = Math.pow(Math.sin(d3), 2) * g2)), c3 = -(l3 = u3 + v2 / Math.tan(d3));
      }
      if (s3 > a3 && s3 < 90 || s3 > 90 && s3 < 90 + a3 || s3 > 180 + a3 && s3 < 270 || s3 > 270 && s3 < 360 - a3) {
        var v2;
        d3 = (90 - s3) * Math.PI / 180, p3 = s3 > a3 && s3 < 90 || s3 > 90 && s3 < 90 + a3 ? e4 / 2 : -e4 / 2, u3 = Math.tan(d3) * p3, g2 = s3 > a3 && s3 < 90 || s3 > 270 && s3 < 360 - a3 ? i4 / 2 - u3 : -i4 / 2 - u3;
        h3 = -(f3 = p3 + (v2 = Math.pow(Math.sin(d3), 2) * g2) / Math.tan(d3)), c3 = -(l3 = u3 + v2);
      }
      return h3 = Math.round(h3 + e4 / 2) + n4, c3 = Math.round(i4 / 2 - c3) + r4, f3 = Math.round(f3 + e4 / 2) + n4, l3 = Math.round(i4 / 2 - l3) + r4, { x0: h3, y0: c3, x1: f3, y1: l3 };
    }(r3, t3, e3, i3, n3), a2 = s2.x0, h2 = s2.y0, c2 = s2.x1, f2 = s2.y1, l2 = o3.createLinearGradient(a2, h2, c2, f2), d2 = r3.match(/linear-gradient\((.+)\)/)[1], u2 = R(d2.substring(d2.indexOf(",") + 1)), p2 = 0; p2 < u2.colors.length; p2++)
      l2.addColorStop(u2.percents[p2], u2.colors[p2]);
    o3.setFillStyle(l2);
  }(e2, i2, n2, r2, t2, o2) : t2.startsWith("radial") && function(t3, e3, i3, n3, r3, o3) {
    for (var s2 = R(r3.match(/radial-gradient\((.+)\)/)[1]), a2 = Math.round(t3 / 2) + i3, h2 = Math.round(e3 / 2) + n3, c2 = o3.createRadialGradient(a2, h2, 0, a2, h2, Math.max(t3, e3) / 2), f2 = 0; f2 < s2.colors.length; f2++)
      c2.addColorStop(s2.percents[f2], s2.colors[f2]);
    o3.setFillStyle(c2);
  }(e2, i2, n2, r2, t2, o2);
};
function R(t2) {
  for (var e2 = [], i2 = [], n2 = 0, r2 = t2.substring(0, t2.length - 1).split("%,"); n2 < r2.length; n2++) {
    var o2 = r2[n2];
    e2.push(o2.substring(0, o2.lastIndexOf(" ")).trim()), i2.push(o2.substring(o2.lastIndexOf(" "), o2.length) / 100);
  }
  return { colors: e2, percents: i2 };
}
function F(t2, e2, i2) {
  return e2 in t2 ? Object.defineProperty(t2, e2, { value: i2, enumerable: true, configurable: true, writable: true }) : t2[e2] = i2, t2;
}
function A() {
  return A = Object.assign ? Object.assign.bind() : function(t2) {
    for (var e2 = 1; e2 < arguments.length; e2++) {
      var i2 = arguments[e2];
      for (var n2 in i2)
        Object.prototype.hasOwnProperty.call(i2, n2) && (t2[n2] = i2[n2]);
    }
    return t2;
  }, A.apply(this, arguments);
}
function j(t2, e2) {
  return j = Object.setPrototypeOf ? Object.setPrototypeOf.bind() : function(t3, e3) {
    return t3.__proto__ = e3, t3;
  }, j(t2, e2);
}
function E(t2, e2) {
  (null == e2 || e2 > t2.length) && (e2 = t2.length);
  for (var i2 = 0, n2 = new Array(e2); i2 < e2; i2++)
    n2[i2] = t2[i2];
  return n2;
}
function C(t2, e2) {
  var i2 = "undefined" != typeof Symbol && t2[Symbol.iterator] || t2["@@iterator"];
  if (i2)
    return (i2 = i2.call(t2)).next.bind(i2);
  if (Array.isArray(t2) || (i2 = function(t3, e3) {
    if (t3) {
      if ("string" == typeof t3)
        return E(t3, e3);
      var i3 = Object.prototype.toString.call(t3).slice(8, -1);
      return "Object" === i3 && t3.constructor && (i3 = t3.constructor.name), "Map" === i3 || "Set" === i3 ? Array.from(t3) : "Arguments" === i3 || /^(?:Ui|I)nt(?:8|16|32)(?:Clamped)?Array$/.test(i3) ? E(t3, e3) : void 0;
    }
  }(t2)) || e2 && t2 && "number" == typeof t2.length) {
    i2 && (t2 = i2);
    var n2 = 0;
    return function() {
      return n2 >= t2.length ? { done: true } : { done: false, value: t2[n2++] };
    };
  }
  throw new TypeError("Invalid attempt to iterate non-iterable instance.\nIn order to be iterable, non-array objects must have a [Symbol.iterator]() method.");
}
function H(t2) {
  return "number" == typeof t2;
}
function D(t2) {
  return "auto" === t2 || null === t2;
}
function $(t2) {
  return /%$/.test(t2);
}
var Y = p, U = u, N = d, X = g, _ = y, q = w, G = m;
function V(t2) {
  return t2.replace(/-([a-z])/g, function(t3, e2) {
    return e2.toUpperCase();
  });
}
function J(t2, e2) {
  var i2, n2, o2 = function(t3) {
    var e3 = t3.match(/([a-z]+)/)[1];
    return [e3, V(t3.split(e3)[1])];
  }(t2), s2 = o2[0], a2 = o2[1], h2 = e2.split(" ");
  if (a2)
    return (i2 = {})[s2 + a2] = e2, i2;
  if (h2.length && !a2) {
    var c2 = h2[0], f2 = h2[1], l2 = h2[2], d2 = h2[3];
    return (n2 = {})[s2 + r[0]] = c2, n2[s2 + r[1]] = f2 || c2, n2[s2 + r[2]] = l2 || c2, n2[s2 + r[3]] = d2 || f2 || c2, n2;
  }
}
function Q(t2) {
  t2 = t2.trim();
  for (var e2 = new Array(), i2 = "+", n2 = "", r2 = t2.length, o2 = 0; o2 < r2; ++o2) {
    if ("." === t2[o2] || !isNaN(Number(t2[o2])) && " " !== t2[o2])
      n2 += t2[o2];
    else if ("(" === t2[o2]) {
      for (var s2 = 1, a2 = o2; s2 > 0; )
        "(" === t2[a2 += 1] && (s2 += 1), ")" === t2[a2] && (s2 -= 1);
      n2 = "".concat(Q(t2.slice(o2 + 1, a2))), o2 = a2;
    }
    if (isNaN(Number(t2[o2])) && "." !== t2[o2] || o2 === r2 - 1) {
      var h2 = parseFloat(n2);
      switch (i2) {
        case "+":
          e2.push(h2);
          break;
        case "-":
          e2.push(-h2);
          break;
        case "*":
          e2.push(e2.pop() * h2);
          break;
        case "/":
          e2.push(e2.pop() / h2);
      }
      i2 = t2[o2], n2 = "";
    }
  }
  for (var c2 = 0; e2.length; )
    c2 += e2.pop();
  return c2;
}
var Z, K = 0, et = function() {
  function t2() {
    F(this, "elements", []), F(this, "afterElements", []), F(this, "beforeElements", []), F(this, "ids", []), F(this, "width", 0), F(this, "height", 0), F(this, "top", 0), F(this, "left", 0), F(this, "pre", null), F(this, "offsetX", 0), F(this, "offsetY", 0), K++, this.id = K;
  }
  var e2 = t2.prototype;
  return e2.fixedBind = function(t3, e3) {
    void 0 === e3 && (e3 = 0), this.container = e3 ? t3.parent : t3.root, this.container.fixedLine = this, this.fixedAdd(t3);
  }, e2.fixedAdd = function(t3) {
    if (!this.ids.includes(t3.id)) {
      this.ids.push(t3.id), this.elements.push(t3);
      var e3 = t3.computedStyle.zIndex;
      (void 0 === e3 ? 0 : e3) >= 0 ? this.afterElements.push(t3) : this.beforeElements.push(t3), this.refreshLayout();
    }
  }, e2.bind = function(t3) {
    this.container = t3.parent, this.container.line = null, this.container.lines ? (this.container.lines.push(this), this.pre = this.getPreLine(), this.top = this.pre.top + this.pre.height, this.left = this.container.contentSize.left) : (this.top = this.container.contentSize.top, this.left = this.container.contentSize.left, this.container.lines = [this]), this.isInline = t3.isInline(), this.container.line = this, this.outerWidth = t3.parent && t3.parent.contentSize.width ? t3.parent.contentSize.width : 1 / 0, this.add(t3);
  }, e2.getPreLine = function() {
    return this.container.lines[this.container.lines.length - 2];
  }, e2.canIEnter = function(t3) {
    return !((100 * t3.offsetSize.width + 100 * this.width) / 100 > this.outerWidth) || (this.closeLine(), false);
  }, e2.closeLine = function() {
    delete this.container.line;
  }, e2.add = function(t3) {
    this.ids.includes(t3.id) || (this.ids.push(t3.id), this.elements.push(t3), this.refreshWidthHeight(t3));
  }, e2.refreshWidthHeight = function(t3) {
    t3.offsetSize.height > this.height && (this.height = t3.offsetSize.height), this.width += t3.offsetSize.width || 0, (this.container.lineMaxWidth || 0) < this.width && (this.container.lineMaxWidth = this.width);
  }, e2.refreshXAlign = function() {
    if (this.isInline) {
      var t3 = this.container.contentSize.width - this.width, e3 = this.container.style.textAlign;
      "center" === e3 ? t3 /= 2 : "left" === e3 && (t3 = 0), this.offsetX = t3;
    }
  }, e2.getOffsetY = function(t3) {
    if (!t3 || !t3.style)
      return 0;
    var e3 = (t3.style || {}).verticalAlign;
    return e3 === s ? this.height - t3.contentSize.height : "middle" === e3 ? (this.height - t3.contentSize.height) / 2 : 0;
  }, e2.setIndent = function(t3) {
    var e3 = t3.style.textIndent;
    if (e3 && /^calc/.test(e3)) {
      var i2 = /^calc\((.+)\)$/.exec(e3);
      if (i2 && i2[1]) {
        var n2 = i2[1].replace(/([^\s\(\+\-\*\/]+)\.(left|right|bottom|top|width|height)/g, function(e4) {
          var i3 = e4.split("."), n3 = i3[0], r3 = i3[1], o2 = t3.parent.querySelector(n3);
          if (o2 && o2.offsetSize) {
            var s2 = { right: o2.offsetSize.left + o2.offsetSize.width, bottom: o2.offsetSize.top + o2.offsetSize.height };
            return o2.offsetSize[r3] || s2[r3] || 0;
          }
        }), r2 = Q(n2.replace(new RegExp(/-?[0-9]+(\.[0-9]+)?(rpx|px|%)/, "g"), W));
        t3.style.textIndent = r2;
      }
    }
  }, e2.layout = function(t3, e3) {
    var i2 = this;
    this.refreshXAlign(), this.pre ? (this.top = this.pre.top + this.pre.height + this.offsetY, this.left = e3 + this.offsetX) : (this.top = Math.max(this.top, this.container.contentSize.top, t3) + this.offsetY, this.left = Math.max(this.left, this.container.contentSize.left, e3) + this.offsetX), this.elements.forEach(function(t4, e4) {
      i2.setIndent(t4);
      var n2 = i2.elements[e4 - 1], r2 = i2.getOffsetY(t4);
      t4.style.top = i2.top + r2, t4.style.left = n2 ? n2.offsetSize.left + n2.offsetSize.width : i2.left, t4.getBoxPosition();
    });
  }, e2.refreshLayout = function() {
    this.afterElements = this.afterElements.sort(function(t3, e3) {
      return t3.computedStyle.zIndex - e3.computedStyle.zIndex;
    }), this.beforeElements = this.beforeElements.sort(function(t3, e3) {
      return t3.computedStyle.zIndex - e3.computedStyle.zIndex;
    });
  }, t2;
}(), it = ((Z = {})[h] = { width: "width", contentWidth: "width", lineMaxWidth: "lineMaxWidth", left: "left", top: "top", height: "height", lineMaxHeight: "lineMaxHeight", marginLeft: "marginLeft" }, Z[c] = { width: "height", contentWidth: "height", lineMaxWidth: "lineMaxWidth", left: "top", top: "left", height: "width", lineMaxHeight: "lineMaxHeight", marginLeft: "marginTop" }, Z), nt = function(t2) {
  var e2, i2;
  function n2() {
    var e3;
    return F(function(t3) {
      if (void 0 === t3)
        throw new ReferenceError("this hasn't been initialised - super() hasn't been called");
      return t3;
    }(e3 = t2.call(this) || this), "outerWidth", 0), e3.exactValue = 0, e3.flexTotal = 0, e3.width = 0, e3.key = null, e3.flexDirection = "row", e3;
  }
  i2 = t2, (e2 = n2).prototype = Object.create(i2.prototype), e2.prototype.constructor = e2, j(e2, i2);
  var r2 = n2.prototype;
  return r2.bind = function(t3) {
    this.container = t3.parent, this.container.line = this, this.container.lines ? (this.container.lines.push(this), this.pre = this.getPreLine(), this.top = this.pre.top + this.pre.height, this.left = this.container.contentSize.left) : (this.top = this.container.contentSize.top, this.left = this.container.contentSize.left, this.container.lines = [this]), t3.parent && (this.flexDirection = t3.parent.style.flexDirection, this.key = it[this.flexDirection]), this.initHeight(t3), this.outerWidth = t3.parent && t3.parent.contentSize[this.key.contentWidth] ? t3.parent.contentSize[this.key.contentWidth] : 1 / 0, this.add(t3);
  }, r2.add = function(t3) {
    this.ids.push(t3.id);
    var e3 = t3.style.flex;
    H(e3) ? this.flexTotal += e3 : H(this.getWidth(t3.style)) && (this.exactValue += this.getWidth(t3.offsetSize)), this.elements.push(t3), this.refreshWidthHeight(t3), t3.next || this.closeLine();
  }, r2.closeLine = function() {
    this.calcFlex();
  }, r2.initHeight = function(t3) {
    this[this.key.height] = 0;
  }, r2.getWidth = function(t3) {
    return t3[this.key.width] || 0;
  }, r2.getHeight = function(t3) {
    return t3[this.key.height] || 0;
  }, r2.setWidth = function(t3, e3) {
    t3[this.key.width] = e3;
  }, r2.setHeight = function(t3, e3) {
    t3[this.key.height] = e3;
  }, r2.calcFlex = function() {
    var t3 = this, e3 = this.container.contentSize[this.key.contentWidth], i3 = 0;
    this.elements.forEach(function(n3) {
      var r3 = n3.style, o2 = n3.contentSize, s2 = t3.getWidth(r3) || t3.getWidth(o2);
      H(r3.flex) && (s2 = r3.flex / t3.flexTotal * (e3 - t3.exactValue)), t3.setWidth(n3.computedStyle, s2), n3.isFlexCalc = true, delete n3.line, delete n3.lines, delete n3.lineMaxWidth, n3.getBoxWidthHeight(), i3 = Math.max(i3, t3.getHeight(n3.offsetSize));
    }), this.setHeight(this, i3);
  }, r2.refreshWidthHeight = function(t3) {
    var e3 = this.container.style.alignItems;
    e3 && !t3.style.alignSelf && (t3.style.alignSelf = e3);
    var i3 = this.getHeight(t3.offsetSize);
    i3 > this[this.key.height] && (this.container[this.key.lineMaxHeight] = this[this.key.height] = i3), this[this.key.width] += this.getWidth(t3.offsetSize);
    var n3 = Math.min(this.getWidth(this), !this.getWidth(this.container.contentSize) && 1 / 0);
    (this.container[this.key.lineMaxWidth] || 0) < n3 && (this.container[this.key.lineMaxWidth] = n3);
  }, r2.refreshXAlign = function() {
    var t3 = this, e3 = this.elements.reduce(function(e4, i4) {
      return e4 + t3.getWidth(i4.offsetSize);
    }, 0), i3 = (this.outerWidth == 1 / 0 ? 0 : this.outerWidth - e3) || 0, n3 = this.container.style.justifyContent;
    "center" === n3 ? i3 /= 2 : "flex-start" === n3 ? i3 = 0 : ["space-between", "space-around"].includes(n3) && (!function(e4, i4) {
      void 0 === i4 && (i4 = 0), i4 /= t3.elements.length + (e4 ? -1 : 1), t3.elements.forEach(function(n4, r3) {
        var o2;
        e4 && !r3 || (n4.style.margin ? n4.style.margin[t3.key.marginLeft] += i4 : n4.style.margin = ((o2 = {})[t3.key.marginLeft] = i4, o2), n4.getBoxPosition());
      }), i4 = 0;
    }("space-between" == n3, i3), i3 = 0), this.offsetX = i3 || 0, this.refreshYAlign();
  }, r2.refreshYAlign = function() {
    var t3 = this;
    if (1 == this.container.lines.length)
      return 0;
    var e3 = this.container.lines.reduce(function(e4, i4) {
      return e4 + t3.getHeight(i4);
    }, 0), i3 = this.container.style.alignItems, n3 = this.getHeight(this.container.contentSize);
    if ("center" === i3) {
      var r3 = (n3 - e3) / (this.container.lines.length + 1);
      this.container.lines.forEach(function(t4) {
        t4.offsetY = r3;
      });
    }
    if ("flex-end" === i3) {
      var o2 = n3 - e3;
      this.container.lines[0].offsetY = o2;
    }
  }, r2.getOffsetY = function(t3) {
    if (this.container.lines.length > 1)
      return 0;
    var e3 = t3.style.alignSelf, i3 = this.getHeight(this.container.contentSize), n3 = i3 - this.getHeight(t3.offsetSize);
    return "flex-end" === e3 ? n3 : "center" === e3 ? n3 / 2 : "stretch" === e3 ? (n3 && t3.name == d && (t3.style[this.key.width] = this.getWidth(t3.offsetSize), t3.style[this.key.height] = i3, delete t3.line, delete t3.lines, t3.getBoxWidthHeight()), 0) : 0;
  }, r2.layout = function(t3, e3) {
    var i3 = this;
    this.refreshXAlign(), this.pre ? (this.top = this.pre.top + this.pre.height + this.offsetY, this.left = e3 + this.offsetX) : (this.top = Math.max(this.top, this.container.contentSize.top, t3) + this.offsetY, this.left = Math.max(this.left, this.container.contentSize.left, e3) + this.offsetX), this.elements.forEach(function(t4, e4) {
      i3.setIndent(t4);
      var n3 = i3.elements[e4 - 1], r3 = i3.getOffsetY(t4);
      t4.style[i3.key.top] = i3[i3.key.top] + r3, t4.style[i3.key.left] = n3 ? n3.offsetSize[i3.key.left] + i3.getWidth(n3.offsetSize) : i3[i3.key.left], t4.getBoxPosition();
    });
  }, n2;
}(et), rt = p, ot = u, st = d, at = v, ht = y, ct = b, ft = w, lt = m, dt = 0, ut = { left: null, top: null, width: null, height: null }, pt = /* @__PURE__ */ new Map(), gt = function() {
  function t2(t3, e3, i2, n2) {
    var o2 = this;
    F(this, "id", dt++), F(this, "style", { left: null, top: null, width: null, height: null }), F(this, "computedStyle", {}), F(this, "originStyle", {}), F(this, "children", {}), F(this, "layoutBox", A({}, ut)), F(this, "contentSize", A({}, ut)), F(this, "clientSize", A({}, ut)), F(this, "borderSize", A({}, ut)), F(this, "offsetSize", A({}, ut)), this.ctx = n2, this.root = i2, e3 && (this.parent = e3), this.name = t3.type || t3.name, this.attributes = this.getAttributes(t3);
    var s2 = function(t4, e4) {
      var i3, n3 = ["color", "fontSize", "lineHeight", "verticalAlign", "fontWeight", "textAlign"], o3 = t4.type, s3 = void 0 === o3 ? N : o3, a3 = t4.styles, h2 = void 0 === a3 ? {} : a3, c2 = (e4 || {}).computedStyle, f2 = Object.assign({}, S);
      if ([U, Y, X].includes(s3) && !h2.display && (f2.display = _), c2)
        for (var l2 = 0; l2 < n3.length; l2++) {
          var d2 = n3[l2];
          (h2[d2] || c2[d2]) && (h2[d2] = h2[i3 = d2, i3.replace(/([A-Z])/g, "-$1").toLowerCase()] || h2[d2] || c2[d2]);
        }
      for (var u2 = function(t5) {
        var e5, i4, n4, o4, a4 = h2[t5];
        if (/-/.test(t5) && (t5 = V(t5), f2[t5] = a4), /^(box|text)?shadow$/i.test(t5)) {
          var c3 = [];
          return a4.replace(/((-?\d+(rpx|px|vw|vh)?\s+?){3})(.+)/, function() {
            for (var t6 = [], e6 = 0; e6 < arguments.length; e6++)
              t6[e6] = arguments[e6];
            c3 = t6[1].match(/-?\d+(rpx|px|vw|vh)?/g).map(function(t7) {
              return W(t7);
            }).concat(t6[4]);
          }), /^text/.test(t5) ? f2.textShadow = c3 : f2.boxShadow = c3, "continue";
        }
        if (/^border/i.test(t5) && !/radius$/i.test(t5)) {
          var l3 = t5.match(/^border([BTRLa-z]+)?/)[0], d3 = t5.match(/[W|S|C][a-z]+/), u3 = a4.replace(/([\(,])\s+|\s+([\),])/g, "$1$2").split(" ").map(function(t6) {
            return /^\d/.test(t6) ? W(t6, "") : t6;
          });
          return f2[l3] || (f2[l3] = {}), 1 == u3.length && d3 ? f2[l3][l3 + d3[0]] = u3[0] : f2[l3] = ((e5 = {})[l3 + "Width"] = B(u3[0]) ? u3[0] : 0, e5[l3 + "Style"] = u3[1] || "solid", e5[l3 + "Color"] = u3[2] || "black", e5), "continue";
        }
        if (/^background(color)?$/i.test(t5))
          return f2.backgroundColor = a4, "continue";
        if (/^objectPosition$/i.test(t5))
          return f2[t5] = a4.split(" "), "continue";
        if (/^backgroundPosition$/i.test(t5))
          return f2[t5] = a4.split(" "), "continue";
        if (/padding|margin|radius/i.test(t5)) {
          var p3 = /radius$/i.test(t5), g3 = "borderRadius", v2 = p3 ? g3 : t5.match(/[a-z]+/)[0], y2 = function(t6, e6) {
            return "border".concat(t6).concat(e6, "Radius");
          }, x2 = [0, 0, 0, 0].map(function(t6, e6) {
            return p3 ? [y2(r[0], r[3]), y2(r[0], r[1]), y2(r[2], r[1]), y2(r[2], r[3])][e6] : v2 + r[e6];
          });
          if ("padding" === t5 || "margin" === t5 || /^(border)?radius$/i.test(t5)) {
            u3 = "".concat(a4).split(" ").map(function(e6) {
              return /^-?\d+(rpx|px|vh|vw)?$/.test(e6) ? W(e6) : "margin" != t5 && /auto/.test(e6) ? 0 : e6;
            }, []) || [0];
            var b2 = p3 ? g3 : t5, w2 = u3[0], m2 = u3[1], S2 = u3[2], z2 = u3[3];
            f2[b2] = ((i4 = {})[x2[0]] = D(w2) ? 0 : w2, i4[x2[1]] = B(m2) || D(m2) ? m2 : w2, i4[x2[2]] = D(B(S2) ? S2 : w2) ? 0 : B(S2) ? S2 : w2, i4[x2[3]] = B(z2) ? z2 : null != m2 ? m2 : w2, i4);
          } else
            "object" == typeof f2[v2] || (f2[v2] = ((n4 = {})[x2[0]] = f2[v2] || 0, n4[x2[1]] = f2[v2] || 0, n4[x2[2]] = f2[v2] || 0, n4[x2[3]] = f2[v2] || 0, n4)), f2[v2][t5] = "margin" == v2 && D(a4) || $(a4) ? a4 : W(a4);
          return "continue";
        }
        if (/^transform$/i.test(t5))
          return f2[t5] = {}, a4.replace(/([a-zA-Z]+)\(([0-9,-\.%rpxdeg\s]+)\)/g, function(e6, i5, n5) {
            var r2 = n5.split(",").map(function(t6) {
              return t6.replace(/(^\s*)|(\s*$)/g, "");
            }), o5 = function(t6, e7) {
              return t6.includes("deg") ? 1 * t6 : e7 && !$(e7) ? W(t6, e7) : t6;
            };
            i5.includes("matrix") ? f2[t5][i5] = r2.map(function(t6) {
              return 1 * t6;
            }) : i5.includes("rotate") ? f2[t5][i5] = 1 * n5.match(/^-?\d+(\.\d+)?/)[0] : /[X, Y]/.test(i5) ? f2[t5][i5] = /[X]/.test(i5) ? o5(r2[0], h2.width) : o5(r2[0], h2.height) : (f2[t5][i5 + "X"] = o5(r2[0], h2.width), f2[t5][i5 + "Y"] = o5(r2[1] || r2[0], h2.height));
          }), "continue";
        if (/^font$/i.test(t5) && common_vendor.index.__f__("warn", "at uni_modules/lime-painter/components/l-painter/painter.js:1", "font 不支持简写"), /^textindent/i.test(t5) && (f2[t5] = /^calc/.test(a4) ? a4 : W(a4)), /^textstroke/i.test(t5)) {
          var I2 = t5.match(/color|width|type$/i), M2 = (l3 = "textStroke", a4.split(" ").map(function(t6) {
            return /^\d+(rpx|px|vh|vw)?$/.test(t6) ? W(t6) : t6;
          }));
          return I2 ? f2[l3] ? f2[l3][I2[0]] = M2[0] : f2[l3] = ((o4 = {})[I2[0]] = M2[0], o4) : f2[l3] = { width: M2[0], color: M2[1], type: M2[2] }, "continue";
        }
        /^left|top$/i.test(t5) && ![q, G].includes(h2.position) ? f2[t5] = 0 : f2[t5] = /^-?[\d\.]+(px|rpx|vw|vh)?$/.test(a4) ? W(a4) : /em$/.test(a4) && s3 == U ? W(a4, h2.fontSize) : a4;
      }, p2 = 0, g2 = Object.keys(h2); p2 < g2.length; p2++)
        u2(g2[p2]);
      return f2;
    }(t3, e3);
    this.isAbsolute = s2.position == ft, this.isFixed = s2.position == lt, this.originStyle = s2, this.styles = t3.styles, Object.keys(s2).forEach(function(t4) {
      Object.defineProperty(o2.style, t4, { configurable: true, enumerable: true, get: function() {
        return s2[t4];
      }, set: function(e4) {
        s2[t4] = e4;
      } });
    });
    var a2 = { contentSize: A({}, this.contentSize), clientSize: A({}, this.clientSize), borderSize: A({}, this.borderSize), offsetSize: A({}, this.offsetSize) };
    Object.keys(a2).forEach(function(t4) {
      Object.keys(o2[t4]).forEach(function(e4) {
        Object.defineProperty(o2[t4], e4, { configurable: true, enumerable: true, get: function() {
          return a2[t4][e4];
        }, set: function(i3) {
          a2[t4][e4] = i3;
        } });
      });
    }), this.computedStyle = this.style;
  }
  var e2 = t2.prototype;
  return e2.add = function(t3) {
    t3.parent = this, this.children[t3.id] = t3;
  }, e2.getChildren = function() {
    var t3 = this;
    return Object.keys(this.children).map(function(e3) {
      return t3.children[e3];
    });
  }, e2.prev = function(t3) {
    void 0 === t3 && (t3 = this);
    var e3 = t3.parent.getChildren();
    return e3[e3.findIndex(function(e4) {
      return e4.id == t3.id;
    }) - 1];
  }, e2.querySelector = function(t3) {
    var e3 = this.getChildren();
    if ("string" != typeof t3)
      return null;
    var i2 = e3.find(function(e4) {
      var i3 = e4.id, n2 = e4.attributes;
      return i3 == t3 || n2 && n2.uid == t3;
    });
    return i2 || (this.parent && this.parent.querySelector && this.parent.querySelector(t3) || null);
  }, e2.getLineRect = function(t3, e3) {
    var i2 = { width: 0, height: 0 }, n2 = e3 ? e3.lines : this.parent && this.parent.lines;
    return n2 && n2.find(function(e4) {
      return e4.ids.includes(t3);
    }) || i2;
  }, e2.setPosition = function(t3, e3) {
    var i2 = { left: "width", top: "height", right: "width", bottom: "height" };
    Object.keys(i2).forEach(function(n2) {
      var r2 = n2 == o ? "left" : "top";
      [o, s].includes(n2) && void 0 !== t3.style[n2] && !B(t3.originStyle[r2]) ? t3.style[r2] = e3[i2[n2]] - t3.offsetSize[i2[n2]] - W(t3.style[n2], e3[i2[n2]]) : t3.style[n2] = W(t3.style[n2], e3[i2[n2]]);
    });
  }, e2.getAttributes = function(t3) {
    var e3 = t3.attributes, i2 = void 0 === e3 ? {} : e3, n2 = t3.uid, r2 = t3.url, o2 = t3.src, s2 = t3.replace, a2 = t3.text;
    return n2 && (i2.uid = n2), (r2 || o2) && (i2.src = i2.src || r2 || o2), s2 && (i2.replace = s2), a2 && (i2.text = a2), i2;
  }, e2.getOffsetSize = function(t3, e3, i2) {
    void 0 === i2 && (i2 = a[3]);
    var n2 = e3 || {}, r2 = n2.margin, o2 = (r2 = void 0 === r2 ? {} : r2).marginLeft, s2 = void 0 === o2 ? 0 : o2, h2 = r2.marginTop, c2 = void 0 === h2 ? 0 : h2, f2 = r2.marginRight, l2 = void 0 === f2 ? 0 : f2, d2 = r2.marginBottom, u2 = void 0 === d2 ? 0 : d2, p2 = n2.padding, g2 = (p2 = void 0 === p2 ? {} : p2).paddingLeft, v2 = void 0 === g2 ? 0 : g2, y2 = p2.paddingTop, x2 = void 0 === y2 ? 0 : y2, b2 = p2.paddingRight, w2 = void 0 === b2 ? 0 : b2, m2 = p2.paddingBottom, S2 = void 0 === m2 ? 0 : m2, z2 = n2.border, I2 = (z2 = void 0 === z2 ? {} : z2).borderWidth, M2 = void 0 === I2 ? 0 : I2, k = n2.borderTop, B2 = (k = void 0 === k ? {} : k).borderTopWidth, W2 = void 0 === B2 ? M2 : B2, P2 = n2.borderBottom, O2 = (P2 = void 0 === P2 ? {} : P2).borderBottomWidth, T2 = void 0 === O2 ? M2 : O2, L2 = n2.borderRight, R2 = (L2 = void 0 === L2 ? {} : L2).borderRightWidth, F2 = void 0 === R2 ? M2 : R2, A2 = n2.borderLeft, j2 = (A2 = void 0 === A2 ? {} : A2).borderLeftWidth, E2 = void 0 === j2 ? M2 : j2, C2 = s2 < 0 && l2 < 0 ? Math.abs(s2 + l2) : 0, H2 = c2 < 0 && u2 < 0 ? Math.abs(c2 + u2) : 0, D2 = s2 >= 0 && l2 < 0, $2 = c2 >= 0 && u2 < 0;
    return i2 == a[0] && (this[i2].left = t3.left + s2 + v2 + E2 + (D2 ? 2 * -l2 : 0), this[i2].top = t3.top + c2 + x2 + W2 + ($2 ? 2 * -u2 : 0), this[i2].width = t3.width + (this[i2].widthAdd ? 0 : C2), this[i2].height = t3.height + (this[i2].heightAdd ? 0 : H2), this[i2].widthAdd = C2, this[i2].heightAdd = H2), i2 == a[1] && (this[i2].left = t3.left + s2 + E2 + (D2 < 0 ? -l2 : 0), this[i2].top = t3.top + c2 + W2 + ($2 ? -u2 : 0), this[i2].width = t3.width + v2 + w2, this[i2].height = t3.height + x2 + S2), i2 == a[2] && (this[i2].left = t3.left + s2 + E2 / 2 + (D2 < 0 ? -l2 : 0), this[i2].top = t3.top + c2 + W2 / 2 + ($2 ? -u2 : 0), this[i2].width = t3.width + v2 + w2 + E2 / 2 + F2 / 2, this[i2].height = t3.height + x2 + S2 + T2 / 2 + W2 / 2), i2 == a[3] && (this[i2].left = t3.left + (D2 < 0 ? -l2 : 0), this[i2].top = t3.top + ($2 ? -u2 : 0), this[i2].width = t3.width + v2 + w2 + E2 + F2 + s2 + l2, this[i2].height = t3.height + x2 + S2 + T2 + W2 + u2 + c2), this[i2];
  }, e2.layoutBoxUpdate = function(t3, e3, i2, n2) {
    var r2 = this;
    if (void 0 === i2 && (i2 = -1), "border-box" == e3.boxSizing) {
      var o2 = e3 || {}, s2 = o2.border, h2 = (s2 = void 0 === s2 ? {} : s2).borderWidth, c2 = void 0 === h2 ? 0 : h2, f2 = o2.borderTop, l2 = (f2 = void 0 === f2 ? {} : f2).borderTopWidth, d2 = void 0 === l2 ? c2 : l2, u2 = o2.borderBottom, p2 = (u2 = void 0 === u2 ? {} : u2).borderBottomWidth, g2 = void 0 === p2 ? c2 : p2, v2 = o2.borderRight, y2 = (v2 = void 0 === v2 ? {} : v2).borderRightWidth, x2 = void 0 === y2 ? c2 : y2, b2 = o2.borderLeft, w2 = (b2 = void 0 === b2 ? {} : b2).borderLeftWidth, m2 = void 0 === w2 ? c2 : w2, S2 = o2.padding, z2 = (S2 = void 0 === S2 ? {} : S2).paddingTop, I2 = void 0 === z2 ? 0 : z2, M2 = S2.paddingRight, k = void 0 === M2 ? 0 : M2, B2 = S2.paddingBottom, W2 = void 0 === B2 ? 0 : B2, P2 = S2.paddingLeft, O2 = void 0 === P2 ? 0 : P2;
      i2 || (t3.width -= O2 + k + x2 + m2), 1 !== i2 || n2 || (t3.height -= I2 + W2 + d2 + g2);
    }
    this.layoutBox && (a.forEach(function(i3) {
      return r2.layoutBox[i3] = r2.getOffsetSize(t3, e3, i3);
    }), this.layoutBox = Object.assign({}, this.layoutBox, this.layoutBox.borderSize));
  }, e2.getBoxPosition = function() {
    var t3 = this.computedStyle, e3 = this.fixedLine, i2 = this.lines, n2 = t3.left, r2 = void 0 === n2 ? 0 : n2, o2 = t3.top, s2 = void 0 === o2 ? 0 : o2, a2 = A({}, this.contentSize, { left: r2, top: s2 }), h2 = this.contentSize.top - this.offsetSize.top, c2 = this.contentSize.left - this.offsetSize.left;
    if (this.root.fixedLine && !this.root.isDone) {
      this.root.isDone = true;
      for (var f2, l2 = C(this.root.fixedLine.elements); !(f2 = l2()).done; ) {
        var d2 = f2.value;
        d2.setPosition(d2, this.root.offsetSize), d2.getBoxPosition();
      }
    }
    if (e3)
      for (var u2, p2 = C(e3.elements); !(u2 = p2()).done; ) {
        var g2 = u2.value, v2 = A({}, this.borderSize, { left: r2, top: s2 });
        g2.setPosition(g2, v2);
        var y2 = this.borderSize.top - this.offsetSize.top, x2 = this.borderSize.left - this.offsetSize.left;
        g2.style.left += r2 + x2, g2.style.top += s2 + y2, g2.getBoxPosition();
      }
    if (i2)
      for (var b2, w2 = C(i2); !(b2 = w2()).done; ) {
        b2.value.layout(a2.top + h2, a2.left + c2);
      }
    return this.layoutBoxUpdate(a2, t3), this.layoutBox;
  }, e2.getBoxState = function(t3, e3) {
    return this.isBlock(t3) || this.isBlock(e3);
  }, e2.isBlock = function(t3) {
    return void 0 === t3 && (t3 = this), t3 && t3.style.display == at;
  }, e2.isFlex = function(t3) {
    return void 0 === t3 && (t3 = this), t3 && t3.style.display == ct;
  }, e2.isInFlow = function() {
    return !(this.isAbsolute || this.isFixed);
  }, e2.inFlexBox = function(t3) {
    return void 0 === t3 && (t3 = this), !!t3.isInFlow() && (!!t3.parent && (!(!t3.parent || t3.parent.style.display !== ct) || void 0));
  }, e2.isInline = function(t3) {
    return void 0 === t3 && (t3 = this), t3 && t3.style.display == ht;
  }, e2.contrastSize = function(t3, e3, i2) {
    var n2 = t3;
    return i2 && (n2 = Math.min(n2, i2)), e3 && (n2 = Math.max(n2, e3)), n2;
  }, e2.measureText = function(t3, e3) {
    var i2 = this.ctx.measureText(t3), n2 = i2.width, r2 = i2.actualBoundingBoxAscent, o2 = i2.actualBoundingBoxDescent;
    return { ascent: r2, descent: o2, width: n2, fontHeight: r2 + o2 || 0.7 * e3 + 1 };
  }, e2.getParentSize = function(t3, e3) {
    if (void 0 === t3 && (t3 = this), void 0 === e3 && (e3 = false), t3 && t3.parent) {
      if (t3.parent.contentSize.width)
        return t3.parent.contentSize;
      if (e3)
        return this.getParentSize(t3.parent, e3);
    }
    return null;
  }, e2.getBoxWidthHeight = function() {
    var t3 = this, e3 = this.name, i2 = this.computedStyle, n2 = this.attributes, r2 = this.parent, o2 = void 0 === r2 ? {} : r2, s2 = this.ctx, a2 = this.getChildren(), h2 = i2.left, c2 = void 0 === h2 ? 0 : h2, f2 = i2.top, l2 = void 0 === f2 ? 0 : f2, d2 = i2.bottom, u2 = i2.right, p2 = i2.width, g2 = void 0 === p2 ? 0 : p2, v2 = i2.minWidth, y2 = i2.maxWidth, x2 = i2.minHeight, b2 = i2.maxHeight, w2 = i2.height, m2 = void 0 === w2 ? 0 : w2, S2 = i2.fontSize, z2 = i2.fontWeight, I2 = i2.fontFamily, M2 = i2.fontStyle, k = i2.position;
    i2.textIndent;
    var B2 = i2.lineClamp, P2 = i2.lineHeight, O2 = i2.padding, T2 = void 0 === O2 ? {} : O2, L2 = i2.margin, R2 = void 0 === L2 ? {} : L2, F2 = i2.border, A2 = (F2 = void 0 === F2 ? {} : F2).borderWidth, j2 = void 0 === A2 ? 0 : A2, E2 = i2.borderRight, C2 = (E2 = void 0 === E2 ? {} : E2).borderRightWidth, H2 = void 0 === C2 ? j2 : C2, Y2 = i2.borderLeft, U2 = (Y2 = void 0 === Y2 ? {} : Y2).borderLeftWidth, N2 = void 0 === U2 ? j2 : U2, X2 = o2.contentSize && o2.contentSize.width, _2 = o2.contentSize && o2.contentSize.height;
    if ($(g2) && X2 && (g2 = W(g2, X2)), $(g2) && !X2 && (g2 = null), $(m2) && _2 && (m2 = W(m2, _2)), $(m2) && !_2 && (m2 = null), $(v2) && X2 && (v2 = W(v2, X2)), $(y2) && X2 && (y2 = W(y2, X2)), $(x2) && _2 && (x2 = W(x2, _2)), $(b2) && _2 && (b2 = W(b2, _2)), i2.padding && X2)
      for (var q2 in i2.padding)
        Object.hasOwnProperty.call(T2, q2) && (T2[q2] = W(T2[q2], X2));
    var G2 = T2.paddingRight, V2 = void 0 === G2 ? 0 : G2, J2 = T2.paddingLeft, Q2 = void 0 === J2 ? 0 : J2;
    if (i2.margin && [R2.marginLeft, R2.marginRight].includes("auto"))
      if (g2) {
        var Z2 = X2 && X2 - g2 - V2 - Q2 - N2 - H2 || 0;
        R2.marginLeft == R2.marginRight ? R2.marginLeft = R2.marginRight = Z2 / 2 : D(R2.marginLeft) ? R2.marginLeft = Z2 : R2.marginRight = Z2;
      } else
        R2.marginLeft = R2.marginRight = 0;
    var K2 = R2.marginRight, tt2 = void 0 === K2 ? 0 : K2, it2 = R2.marginLeft, at2 = { width: g2, height: m2, left: 0, top: 0 }, ht2 = Q2 + V2 + N2 + H2 + (void 0 === it2 ? 0 : it2) + tt2;
    if (this.offsetWidth = ht2, e3 == ot && !this.attributes.widths) {
      var ct2 = n2.text || "";
      s2.save(), s2.setFonts({ fontFamily: I2, fontSize: S2, fontWeight: z2, fontStyle: M2 }), ct2.length, "\n" == ct2 && (ct2 = "", this.isBr = true), ("" + ct2).split("\n").map(function(e4) {
        var i3 = Array.from(e4).map(function(e5) {
          var i4 = "" + (/^[\u4e00-\u9fa5]+$/.test(e5) ? "cn" : e5) + I2 + S2 + z2 + M2, n4 = pt.get(i4);
          if (n4)
            return { width: n4, text: e5 };
          var r4 = t3.measureText(e5, S2).width;
          return pt.set(i4, r4), { width: r4, text: e5 };
        }), n3 = t3.measureText(e4, S2), r3 = n3.fontHeight, o3 = n3.ascent, s3 = n3.descent;
        t3.attributes.fontHeight = r3, t3.attributes.ascent = o3, t3.attributes.descent = s3, t3.attributes.widths || (t3.attributes.widths = []), t3.attributes.widths.push({ widths: i3, total: i3.reduce(function(t4, e5) {
          return t4 + e5.width;
        }, 0) });
      }), s2.restore();
    }
    if (e3 == rt && null == g2) {
      var lt2 = n2.width, dt2 = n2.height;
      at2.width = this.contrastSize(Math.round(lt2 * m2 / dt2) || 0, v2, y2), this.layoutBoxUpdate(at2, i2, 0);
    }
    if (e3 == ot && null == g2) {
      var ut2 = this.attributes.widths, gt2 = Math.max.apply(Math, ut2.map(function(t4) {
        return t4.total;
      }));
      if (o2 && X2 > 0 && (gt2 > X2 || this.isBlock(this)) && !this.isAbsolute && !this.isFixed)
        gt2 = X2;
      at2.width = this.contrastSize(gt2, v2, y2), this.layoutBoxUpdate(at2, i2, 0);
    }
    if (e3 == ot && (o2.style.flex || !this.attributes.lines)) {
      var vt2 = this.attributes.widths.length;
      this.attributes.widths.forEach(function(t4) {
        return t4.widths.reduce(function(t5, e4, i3) {
          return t5 + e4.width > at2.width ? (vt2++, e4.width) : t5 + e4.width;
        }, 0);
      }), vt2 = B2 && vt2 > B2 ? B2 : vt2, this.attributes.lines = vt2;
    }
    if (e3 == rt && null == m2) {
      var yt2 = n2.width, xt2 = n2.height;
      n2.text, at2.height = this.contrastSize(W(at2.width * xt2 / yt2) || 0, x2, b2), this.layoutBoxUpdate(at2, i2, 1);
    }
    e3 == ot && null == m2 && (P2 = W(P2, S2), at2.height = this.contrastSize(W(this.attributes.lines * P2), x2, b2), this.layoutBoxUpdate(at2, i2, 1, true)), !g2 && o2 && o2.children && X2 && (!this.isFlex(o2) || o2.isFlexCalc) && ([st, ot].includes(e3) && this.isFlex() || e3 == st && this.isBlock(this) && this.isInFlow()) && (at2.width = this.contrastSize(X2 - (o2.isFlexCalc ? 0 : ht2), v2, y2), this.layoutBoxUpdate(at2, i2)), g2 && !$(g2) && (at2.width = this.contrastSize(g2, v2, y2), this.layoutBoxUpdate(at2, i2, 0)), m2 && !$(m2) && (at2.height = this.contrastSize(at2.height, x2, b2), this.layoutBoxUpdate(at2, i2, 1));
    var bt2 = 0;
    if (a2.length) {
      var wt2 = null, mt2 = false;
      a2.forEach(function(e4, n3) {
        e4.getBoxWidthHeight();
        var r3 = a2[n3 + 1];
        if (r3 && r3.isInFlow() && (e4.next = r3), !t3.line || !t3.line.ids.includes(e4.id))
          if (e4.isInFlow() && !e4.inFlexBox()) {
            var o3 = t3.getBoxState(wt2, e4);
            if (e4.isBr)
              return mt2 = true;
            t3.line && t3.line.canIEnter(e4) && !o3 && !mt2 ? t3.line.add(e4) : (mt2 = false, new et().bind(e4)), wt2 = e4;
          } else
            e4.inFlexBox() ? t3.line && (t3.line.canIEnter(e4) || "nowrap" == i2.flexWrap) ? t3.line.add(e4) : new nt().bind(e4) : e4.isFixed ? t3.root.fixedLine ? t3.root.fixedLine.fixedAdd(e4) : new et().fixedBind(e4) : t3.fixedLine ? t3.fixedLine.fixedAdd(e4) : new et().fixedBind(e4, 1);
      }), this.lines && (bt2 = this.lines.reduce(function(t4, e4) {
        return t4 + e4.height;
      }, 0));
    }
    var St2 = 0, zt2 = 0;
    if (!g2 && (this.isAbsolute || this.isFixed) && X2) {
      var It2 = k == ft ? X2 : this.root.width, Mt2 = It2 - ($(c2) ? W(c2, It2) : c2) - ($(u2) ? W(u2, It2) : u2);
      St2 = i2.left ? Mt2 : this.lineMaxWidth;
    }
    if (!m2 && (null != l2 ? l2 : this.isAbsolute || this.isFixed && _2)) {
      var kt2 = k == ft ? _2 : this.root.height, Bt2 = kt2 - ($(l2) ? W(l2, kt2) : l2) - ($(d2) ? W(d2, kt2) : d2);
      zt2 = i2.top ? Bt2 : 0;
    }
    if (g2 && !$(g2) || at2.width || (at2.width = St2 || this.contrastSize((this.isBlock(this) && !this.isInFlow() ? X2 || o2.lineMaxWidth : this.lineMaxWidth) || this.lineMaxWidth, v2, y2), this.layoutBoxUpdate(at2, i2, 0)), m2 || !bt2 && !zt2 || (at2.height = zt2 || this.contrastSize(bt2, x2, b2), this.layoutBoxUpdate(at2, i2)), i2.borderRadius && this.borderSize && this.borderSize.width)
      for (var q2 in i2.borderRadius)
        Object.hasOwnProperty.call(i2.borderRadius, q2) && (i2.borderRadius[q2] = W(i2.borderRadius[q2], this.borderSize.width));
    return this.layoutBox;
  }, e2.layout = function() {
    return this.getBoxWidthHeight(), this.root.offsetSize = this.offsetSize, this.root.contentSize = this.contentSize, this.getBoxPosition(), this.offsetSize;
  }, t2;
}(), vt = /* @__PURE__ */ function() {
  var t2, e2, i2, n2, r2, o2, s2 = [0, 11, 15, 19, 23, 27, 31, 16, 18, 20, 22, 24, 26, 28, 20, 22, 24, 24, 26, 28, 28, 22, 24, 24, 26, 26, 28, 28, 24, 24, 26, 26, 26, 28, 28, 24, 26, 26, 26, 28, 28], a2 = [3220, 1468, 2713, 1235, 3062, 1890, 2119, 1549, 2344, 2936, 1117, 2583, 1330, 2470, 1667, 2249, 2028, 3780, 481, 4011, 142, 3098, 831, 3445, 592, 2517, 1776, 2234, 1951, 2827, 1070, 2660, 1345, 3177], h2 = [30660, 29427, 32170, 30877, 26159, 25368, 27713, 26998, 21522, 20773, 24188, 23371, 17913, 16590, 20375, 19104, 13663, 12392, 16177, 14854, 9396, 8579, 11994, 11245, 5769, 5054, 7399, 6608, 1890, 597, 3340, 2107], c2 = [1, 0, 19, 7, 1, 0, 16, 10, 1, 0, 13, 13, 1, 0, 9, 17, 1, 0, 34, 10, 1, 0, 28, 16, 1, 0, 22, 22, 1, 0, 16, 28, 1, 0, 55, 15, 1, 0, 44, 26, 2, 0, 17, 18, 2, 0, 13, 22, 1, 0, 80, 20, 2, 0, 32, 18, 2, 0, 24, 26, 4, 0, 9, 16, 1, 0, 108, 26, 2, 0, 43, 24, 2, 2, 15, 18, 2, 2, 11, 22, 2, 0, 68, 18, 4, 0, 27, 16, 4, 0, 19, 24, 4, 0, 15, 28, 2, 0, 78, 20, 4, 0, 31, 18, 2, 4, 14, 18, 4, 1, 13, 26, 2, 0, 97, 24, 2, 2, 38, 22, 4, 2, 18, 22, 4, 2, 14, 26, 2, 0, 116, 30, 3, 2, 36, 22, 4, 4, 16, 20, 4, 4, 12, 24, 2, 2, 68, 18, 4, 1, 43, 26, 6, 2, 19, 24, 6, 2, 15, 28, 4, 0, 81, 20, 1, 4, 50, 30, 4, 4, 22, 28, 3, 8, 12, 24, 2, 2, 92, 24, 6, 2, 36, 22, 4, 6, 20, 26, 7, 4, 14, 28, 4, 0, 107, 26, 8, 1, 37, 22, 8, 4, 20, 24, 12, 4, 11, 22, 3, 1, 115, 30, 4, 5, 40, 24, 11, 5, 16, 20, 11, 5, 12, 24, 5, 1, 87, 22, 5, 5, 41, 24, 5, 7, 24, 30, 11, 7, 12, 24, 5, 1, 98, 24, 7, 3, 45, 28, 15, 2, 19, 24, 3, 13, 15, 30, 1, 5, 107, 28, 10, 1, 46, 28, 1, 15, 22, 28, 2, 17, 14, 28, 5, 1, 120, 30, 9, 4, 43, 26, 17, 1, 22, 28, 2, 19, 14, 28, 3, 4, 113, 28, 3, 11, 44, 26, 17, 4, 21, 26, 9, 16, 13, 26, 3, 5, 107, 28, 3, 13, 41, 26, 15, 5, 24, 30, 15, 10, 15, 28, 4, 4, 116, 28, 17, 0, 42, 26, 17, 6, 22, 28, 19, 6, 16, 30, 2, 7, 111, 28, 17, 0, 46, 28, 7, 16, 24, 30, 34, 0, 13, 24, 4, 5, 121, 30, 4, 14, 47, 28, 11, 14, 24, 30, 16, 14, 15, 30, 6, 4, 117, 30, 6, 14, 45, 28, 11, 16, 24, 30, 30, 2, 16, 30, 8, 4, 106, 26, 8, 13, 47, 28, 7, 22, 24, 30, 22, 13, 15, 30, 10, 2, 114, 28, 19, 4, 46, 28, 28, 6, 22, 28, 33, 4, 16, 30, 8, 4, 122, 30, 22, 3, 45, 28, 8, 26, 23, 30, 12, 28, 15, 30, 3, 10, 117, 30, 3, 23, 45, 28, 4, 31, 24, 30, 11, 31, 15, 30, 7, 7, 116, 30, 21, 7, 45, 28, 1, 37, 23, 30, 19, 26, 15, 30, 5, 10, 115, 30, 19, 10, 47, 28, 15, 25, 24, 30, 23, 25, 15, 30, 13, 3, 115, 30, 2, 29, 46, 28, 42, 1, 24, 30, 23, 28, 15, 30, 17, 0, 115, 30, 10, 23, 46, 28, 10, 35, 24, 30, 19, 35, 15, 30, 17, 1, 115, 30, 14, 21, 46, 28, 29, 19, 24, 30, 11, 46, 15, 30, 13, 6, 115, 30, 14, 23, 46, 28, 44, 7, 24, 30, 59, 1, 16, 30, 12, 7, 121, 30, 12, 26, 47, 28, 39, 14, 24, 30, 22, 41, 15, 30, 6, 14, 121, 30, 6, 34, 47, 28, 46, 10, 24, 30, 2, 64, 15, 30, 17, 4, 122, 30, 29, 14, 46, 28, 49, 10, 24, 30, 24, 46, 15, 30, 4, 18, 122, 30, 13, 32, 46, 28, 48, 14, 24, 30, 42, 32, 15, 30, 20, 4, 117, 30, 40, 7, 47, 28, 43, 22, 24, 30, 10, 67, 15, 30, 19, 6, 118, 30, 18, 31, 47, 28, 34, 34, 24, 30, 20, 61, 15, 30], f2 = [255, 0, 1, 25, 2, 50, 26, 198, 3, 223, 51, 238, 27, 104, 199, 75, 4, 100, 224, 14, 52, 141, 239, 129, 28, 193, 105, 248, 200, 8, 76, 113, 5, 138, 101, 47, 225, 36, 15, 33, 53, 147, 142, 218, 240, 18, 130, 69, 29, 181, 194, 125, 106, 39, 249, 185, 201, 154, 9, 120, 77, 228, 114, 166, 6, 191, 139, 98, 102, 221, 48, 253, 226, 152, 37, 179, 16, 145, 34, 136, 54, 208, 148, 206, 143, 150, 219, 189, 241, 210, 19, 92, 131, 56, 70, 64, 30, 66, 182, 163, 195, 72, 126, 110, 107, 58, 40, 84, 250, 133, 186, 61, 202, 94, 155, 159, 10, 21, 121, 43, 78, 212, 229, 172, 115, 243, 167, 87, 7, 112, 192, 247, 140, 128, 99, 13, 103, 74, 222, 237, 49, 197, 254, 24, 227, 165, 153, 119, 38, 184, 180, 124, 17, 68, 146, 217, 35, 32, 137, 46, 55, 63, 209, 91, 149, 188, 207, 205, 144, 135, 151, 178, 220, 252, 190, 97, 242, 86, 211, 171, 20, 42, 93, 158, 132, 60, 57, 83, 71, 109, 65, 162, 31, 45, 67, 216, 183, 123, 164, 118, 196, 23, 73, 236, 127, 12, 111, 246, 108, 161, 59, 82, 41, 157, 85, 170, 251, 96, 134, 177, 187, 204, 62, 90, 203, 89, 95, 176, 156, 169, 160, 81, 11, 245, 22, 235, 122, 117, 44, 215, 79, 174, 213, 233, 230, 231, 173, 232, 116, 214, 244, 234, 168, 80, 88, 175], l2 = [1, 2, 4, 8, 16, 32, 64, 128, 29, 58, 116, 232, 205, 135, 19, 38, 76, 152, 45, 90, 180, 117, 234, 201, 143, 3, 6, 12, 24, 48, 96, 192, 157, 39, 78, 156, 37, 74, 148, 53, 106, 212, 181, 119, 238, 193, 159, 35, 70, 140, 5, 10, 20, 40, 80, 160, 93, 186, 105, 210, 185, 111, 222, 161, 95, 190, 97, 194, 153, 47, 94, 188, 101, 202, 137, 15, 30, 60, 120, 240, 253, 231, 211, 187, 107, 214, 177, 127, 254, 225, 223, 163, 91, 182, 113, 226, 217, 175, 67, 134, 17, 34, 68, 136, 13, 26, 52, 104, 208, 189, 103, 206, 129, 31, 62, 124, 248, 237, 199, 147, 59, 118, 236, 197, 151, 51, 102, 204, 133, 23, 46, 92, 184, 109, 218, 169, 79, 158, 33, 66, 132, 21, 42, 84, 168, 77, 154, 41, 82, 164, 85, 170, 73, 146, 57, 114, 228, 213, 183, 115, 230, 209, 191, 99, 198, 145, 63, 126, 252, 229, 215, 179, 123, 246, 241, 255, 227, 219, 171, 75, 150, 49, 98, 196, 149, 55, 110, 220, 165, 87, 174, 65, 130, 25, 50, 100, 200, 141, 7, 14, 28, 56, 112, 224, 221, 167, 83, 166, 81, 162, 89, 178, 121, 242, 249, 239, 195, 155, 43, 86, 172, 69, 138, 9, 18, 36, 72, 144, 61, 122, 244, 245, 247, 243, 251, 235, 203, 139, 11, 22, 44, 88, 176, 125, 250, 233, 207, 131, 27, 54, 108, 216, 173, 71, 142, 0], d2 = [], u2 = [], p2 = [], g2 = [], v2 = [], y2 = 2;
  function x2(t3, e3) {
    var i3;
    t3 > e3 && (i3 = t3, t3 = e3, e3 = i3), i3 = e3, i3 *= e3, i3 += e3, i3 >>= 1, g2[i3 += t3] = 1;
  }
  function b2(t3, i3) {
    var n3;
    for (p2[t3 + e2 * i3] = 1, n3 = -2; n3 < 2; n3++)
      p2[t3 + n3 + e2 * (i3 - 2)] = 1, p2[t3 - 2 + e2 * (i3 + n3 + 1)] = 1, p2[t3 + 2 + e2 * (i3 + n3)] = 1, p2[t3 + n3 + 1 + e2 * (i3 + 2)] = 1;
    for (n3 = 0; n3 < 2; n3++)
      x2(t3 - 1, i3 + n3), x2(t3 + 1, i3 - n3), x2(t3 - n3, i3 - 1), x2(t3 + n3, i3 + 1);
  }
  function w2(t3) {
    for (; t3 >= 255; )
      t3 = ((t3 -= 255) >> 8) + (255 & t3);
    return t3;
  }
  var m2 = [];
  function S2(t3, e3, i3, n3) {
    var r3, o3, s3;
    for (r3 = 0; r3 < n3; r3++)
      d2[i3 + r3] = 0;
    for (r3 = 0; r3 < e3; r3++) {
      if (255 != (s3 = f2[d2[t3 + r3] ^ d2[i3]]))
        for (o3 = 1; o3 < n3; o3++)
          d2[i3 + o3 - 1] = d2[i3 + o3] ^ l2[w2(s3 + m2[n3 - o3])];
      else
        for (o3 = i3; o3 < i3 + n3; o3++)
          d2[o3] = d2[o3 + 1];
      d2[i3 + n3 - 1] = 255 == s3 ? 0 : l2[w2(s3 + m2[0])];
    }
  }
  function z2(t3, e3) {
    var i3;
    return t3 > e3 && (i3 = t3, t3 = e3, e3 = i3), i3 = e3, i3 += e3 * e3, i3 >>= 1, g2[i3 += t3];
  }
  function I2(t3) {
    var i3, n3, r3, o3;
    switch (t3) {
      case 0:
        for (n3 = 0; n3 < e2; n3++)
          for (i3 = 0; i3 < e2; i3++)
            i3 + n3 & 1 || z2(i3, n3) || (p2[i3 + n3 * e2] ^= 1);
        break;
      case 1:
        for (n3 = 0; n3 < e2; n3++)
          for (i3 = 0; i3 < e2; i3++)
            1 & n3 || z2(i3, n3) || (p2[i3 + n3 * e2] ^= 1);
        break;
      case 2:
        for (n3 = 0; n3 < e2; n3++)
          for (r3 = 0, i3 = 0; i3 < e2; i3++, r3++)
            3 == r3 && (r3 = 0), r3 || z2(i3, n3) || (p2[i3 + n3 * e2] ^= 1);
        break;
      case 3:
        for (o3 = 0, n3 = 0; n3 < e2; n3++, o3++)
          for (3 == o3 && (o3 = 0), r3 = o3, i3 = 0; i3 < e2; i3++, r3++)
            3 == r3 && (r3 = 0), r3 || z2(i3, n3) || (p2[i3 + n3 * e2] ^= 1);
        break;
      case 4:
        for (n3 = 0; n3 < e2; n3++)
          for (r3 = 0, o3 = n3 >> 1 & 1, i3 = 0; i3 < e2; i3++, r3++)
            3 == r3 && (r3 = 0, o3 = !o3), o3 || z2(i3, n3) || (p2[i3 + n3 * e2] ^= 1);
        break;
      case 5:
        for (o3 = 0, n3 = 0; n3 < e2; n3++, o3++)
          for (3 == o3 && (o3 = 0), r3 = 0, i3 = 0; i3 < e2; i3++, r3++)
            3 == r3 && (r3 = 0), (i3 & n3 & 1) + !(!r3 | !o3) || z2(i3, n3) || (p2[i3 + n3 * e2] ^= 1);
        break;
      case 6:
        for (o3 = 0, n3 = 0; n3 < e2; n3++, o3++)
          for (3 == o3 && (o3 = 0), r3 = 0, i3 = 0; i3 < e2; i3++, r3++)
            3 == r3 && (r3 = 0), (i3 & n3 & 1) + (r3 && r3 == o3) & 1 || z2(i3, n3) || (p2[i3 + n3 * e2] ^= 1);
        break;
      case 7:
        for (o3 = 0, n3 = 0; n3 < e2; n3++, o3++)
          for (3 == o3 && (o3 = 0), r3 = 0, i3 = 0; i3 < e2; i3++, r3++)
            3 == r3 && (r3 = 0), (r3 && r3 == o3) + (i3 + n3 & 1) & 1 || z2(i3, n3) || (p2[i3 + n3 * e2] ^= 1);
    }
  }
  function M2(t3) {
    var e3, i3 = 0;
    for (e3 = 0; e3 <= t3; e3++)
      v2[e3] >= 5 && (i3 += 3 + v2[e3] - 5);
    for (e3 = 3; e3 < t3 - 1; e3 += 2)
      v2[e3 - 2] == v2[e3 + 2] && v2[e3 + 2] == v2[e3 - 1] && v2[e3 - 1] == v2[e3 + 1] && 3 * v2[e3 - 1] == v2[e3] && (0 == v2[e3 - 3] || e3 + 3 > t3 || 3 * v2[e3 - 3] >= 4 * v2[e3] || 3 * v2[e3 + 3] >= 4 * v2[e3]) && (i3 += 40);
    return i3;
  }
  function k() {
    var t3, i3, n3, r3, o3, s3 = 0, a3 = 0;
    for (i3 = 0; i3 < e2 - 1; i3++)
      for (t3 = 0; t3 < e2 - 1; t3++)
        (p2[t3 + e2 * i3] && p2[t3 + 1 + e2 * i3] && p2[t3 + e2 * (i3 + 1)] && p2[t3 + 1 + e2 * (i3 + 1)] || !(p2[t3 + e2 * i3] || p2[t3 + 1 + e2 * i3] || p2[t3 + e2 * (i3 + 1)] || p2[t3 + 1 + e2 * (i3 + 1)])) && (s3 += 3);
    for (i3 = 0; i3 < e2; i3++) {
      for (v2[0] = 0, n3 = r3 = t3 = 0; t3 < e2; t3++)
        (o3 = p2[t3 + e2 * i3]) == r3 ? v2[n3]++ : v2[++n3] = 1, a3 += (r3 = o3) ? 1 : -1;
      s3 += M2(n3);
    }
    a3 < 0 && (a3 = -a3);
    var h3 = a3, c3 = 0;
    for (h3 += h3 << 2, h3 <<= 1; h3 > e2 * e2; )
      h3 -= e2 * e2, c3++;
    for (s3 += 10 * c3, t3 = 0; t3 < e2; t3++) {
      for (v2[0] = 0, n3 = r3 = i3 = 0; i3 < e2; i3++)
        (o3 = p2[t3 + e2 * i3]) == r3 ? v2[n3]++ : v2[++n3] = 1, r3 = o3;
      s3 += M2(n3);
    }
    return s3;
  }
  var B2 = null;
  return { api: { get ecclevel() {
    return y2;
  }, set ecclevel(t3) {
    y2 = t3;
  }, get size() {
    return _size;
  }, set size(t3) {
    _size = t3;
  }, get canvas() {
    return B2;
  }, set canvas(t3) {
    B2 = t3;
  }, getFrame: function(v3) {
    return function(v4) {
      var M3, B3, W2, P2, O2, T2, L2, R2;
      P2 = v4.length, t2 = 0;
      do {
        if (t2++, W2 = 4 * (y2 - 1) + 16 * (t2 - 1), i2 = c2[W2++], n2 = c2[W2++], r2 = c2[W2++], o2 = c2[W2], P2 <= (W2 = r2 * (i2 + n2) + n2 - 3 + (t2 <= 9)))
          break;
      } while (t2 < 40);
      for (e2 = 17 + 4 * t2, O2 = r2 + (r2 + o2) * (i2 + n2) + n2, P2 = 0; P2 < O2; P2++)
        u2[P2] = 0;
      for (d2 = v4.slice(0), P2 = 0; P2 < e2 * e2; P2++)
        p2[P2] = 0;
      for (P2 = 0; P2 < (e2 * (e2 + 1) + 1) / 2; P2++)
        g2[P2] = 0;
      for (P2 = 0; P2 < 3; P2++) {
        for (W2 = 0, B3 = 0, 1 == P2 && (W2 = e2 - 7), 2 == P2 && (B3 = e2 - 7), p2[B3 + 3 + e2 * (W2 + 3)] = 1, M3 = 0; M3 < 6; M3++)
          p2[B3 + M3 + e2 * W2] = 1, p2[B3 + e2 * (W2 + M3 + 1)] = 1, p2[B3 + 6 + e2 * (W2 + M3)] = 1, p2[B3 + M3 + 1 + e2 * (W2 + 6)] = 1;
        for (M3 = 1; M3 < 5; M3++)
          x2(B3 + M3, W2 + 1), x2(B3 + 1, W2 + M3 + 1), x2(B3 + 5, W2 + M3), x2(B3 + M3 + 1, W2 + 5);
        for (M3 = 2; M3 < 4; M3++)
          p2[B3 + M3 + e2 * (W2 + 2)] = 1, p2[B3 + 2 + e2 * (W2 + M3 + 1)] = 1, p2[B3 + 4 + e2 * (W2 + M3)] = 1, p2[B3 + M3 + 1 + e2 * (W2 + 4)] = 1;
      }
      if (t2 > 1)
        for (P2 = s2[t2], B3 = e2 - 7; ; ) {
          for (M3 = e2 - 7; M3 > P2 - 3 && (b2(M3, B3), !(M3 < P2)); )
            M3 -= P2;
          if (B3 <= P2 + 9)
            break;
          b2(6, B3 -= P2), b2(B3, 6);
        }
      for (p2[8 + e2 * (e2 - 8)] = 1, B3 = 0; B3 < 7; B3++)
        x2(7, B3), x2(e2 - 8, B3), x2(7, B3 + e2 - 7);
      for (M3 = 0; M3 < 8; M3++)
        x2(M3, 7), x2(M3 + e2 - 8, 7), x2(M3, e2 - 8);
      for (M3 = 0; M3 < 9; M3++)
        x2(M3, 8);
      for (M3 = 0; M3 < 8; M3++)
        x2(M3 + e2 - 8, 8), x2(8, M3);
      for (B3 = 0; B3 < 7; B3++)
        x2(8, B3 + e2 - 7);
      for (M3 = 0; M3 < e2 - 14; M3++)
        1 & M3 ? (x2(8 + M3, 6), x2(6, 8 + M3)) : (p2[8 + M3 + 6 * e2] = 1, p2[6 + e2 * (8 + M3)] = 1);
      if (t2 > 6)
        for (P2 = a2[t2 - 7], W2 = 17, M3 = 0; M3 < 6; M3++)
          for (B3 = 0; B3 < 3; B3++, W2--)
            1 & (W2 > 11 ? t2 >> W2 - 12 : P2 >> W2) ? (p2[5 - M3 + e2 * (2 - B3 + e2 - 11)] = 1, p2[2 - B3 + e2 - 11 + e2 * (5 - M3)] = 1) : (x2(5 - M3, 2 - B3 + e2 - 11), x2(2 - B3 + e2 - 11, 5 - M3));
      for (B3 = 0; B3 < e2; B3++)
        for (M3 = 0; M3 <= B3; M3++)
          p2[M3 + e2 * B3] && x2(M3, B3);
      for (O2 = d2.length, T2 = 0; T2 < O2; T2++)
        u2[T2] = d2.charCodeAt(T2);
      if (d2 = u2.slice(0), O2 >= (M3 = r2 * (i2 + n2) + n2) - 2 && (O2 = M3 - 2, t2 > 9 && O2--), T2 = O2, t2 > 9) {
        for (d2[T2 + 2] = 0, d2[T2 + 3] = 0; T2--; )
          P2 = d2[T2], d2[T2 + 3] |= 255 & P2 << 4, d2[T2 + 2] = P2 >> 4;
        d2[2] |= 255 & O2 << 4, d2[1] = O2 >> 4, d2[0] = 64 | O2 >> 12;
      } else {
        for (d2[T2 + 1] = 0, d2[T2 + 2] = 0; T2--; )
          P2 = d2[T2], d2[T2 + 2] |= 255 & P2 << 4, d2[T2 + 1] = P2 >> 4;
        d2[1] |= 255 & O2 << 4, d2[0] = 64 | O2 >> 4;
      }
      for (T2 = O2 + 3 - (t2 < 10); T2 < M3; )
        d2[T2++] = 236, d2[T2++] = 17;
      for (m2[0] = 1, T2 = 0; T2 < o2; T2++) {
        for (m2[T2 + 1] = 1, L2 = T2; L2 > 0; L2--)
          m2[L2] = m2[L2] ? m2[L2 - 1] ^ l2[w2(f2[m2[L2]] + T2)] : m2[L2 - 1];
        m2[0] = l2[w2(f2[m2[0]] + T2)];
      }
      for (T2 = 0; T2 <= o2; T2++)
        m2[T2] = f2[m2[T2]];
      for (W2 = M3, B3 = 0, T2 = 0; T2 < i2; T2++)
        S2(B3, r2, W2, o2), B3 += r2, W2 += o2;
      for (T2 = 0; T2 < n2; T2++)
        S2(B3, r2 + 1, W2, o2), B3 += r2 + 1, W2 += o2;
      for (B3 = 0, T2 = 0; T2 < r2; T2++) {
        for (L2 = 0; L2 < i2; L2++)
          u2[B3++] = d2[T2 + L2 * r2];
        for (L2 = 0; L2 < n2; L2++)
          u2[B3++] = d2[i2 * r2 + T2 + L2 * (r2 + 1)];
      }
      for (L2 = 0; L2 < n2; L2++)
        u2[B3++] = d2[i2 * r2 + T2 + L2 * (r2 + 1)];
      for (T2 = 0; T2 < o2; T2++)
        for (L2 = 0; L2 < i2 + n2; L2++)
          u2[B3++] = d2[M3 + T2 + L2 * o2];
      for (d2 = u2, M3 = B3 = e2 - 1, W2 = O2 = 1, R2 = (r2 + o2) * (i2 + n2) + n2, T2 = 0; T2 < R2; T2++)
        for (P2 = d2[T2], L2 = 0; L2 < 8; L2++, P2 <<= 1) {
          128 & P2 && (p2[M3 + e2 * B3] = 1);
          do {
            O2 ? M3-- : (M3++, W2 ? 0 != B3 ? B3-- : (W2 = !W2, 6 == (M3 -= 2) && (M3--, B3 = 9)) : B3 != e2 - 1 ? B3++ : (W2 = !W2, 6 == (M3 -= 2) && (M3--, B3 -= 8))), O2 = !O2;
          } while (z2(M3, B3));
        }
      for (d2 = p2.slice(0), P2 = 0, B3 = 3e4, W2 = 0; W2 < 8 && (I2(W2), (M3 = k()) < B3 && (B3 = M3, P2 = W2), 7 != P2); W2++)
        p2 = d2.slice(0);
      for (P2 != W2 && I2(P2), B3 = h2[P2 + (y2 - 1 << 3)], W2 = 0; W2 < 8; W2++, B3 >>= 1)
        1 & B3 && (p2[e2 - 1 - W2 + 8 * e2] = 1, W2 < 6 ? p2[8 + e2 * W2] = 1 : p2[8 + e2 * (W2 + 1)] = 1);
      for (W2 = 0; W2 < 7; W2++, B3 >>= 1)
        1 & B3 && (p2[8 + e2 * (e2 - 7 + W2)] = 1, W2 ? p2[6 - W2 + 8 * e2] = 1 : p2[7 + 8 * e2] = 1);
      return p2;
    }(v3);
  }, utf16to8: function(t3) {
    var e3, i3, n3, r3;
    for (e3 = "", n3 = t3.length, i3 = 0; i3 < n3; i3++)
      (r3 = t3.charCodeAt(i3)) >= 1 && r3 <= 127 ? e3 += t3.charAt(i3) : r3 > 2047 ? (e3 += String.fromCharCode(224 | r3 >> 12 & 15), e3 += String.fromCharCode(128 | r3 >> 6 & 63), e3 += String.fromCharCode(128 | r3 >> 0 & 63)) : (e3 += String.fromCharCode(192 | r3 >> 6 & 31), e3 += String.fromCharCode(128 | r3 >> 0 & 63));
    return e3;
  }, draw: function(t3, i3, n3, r3, o3) {
    i3.drawView(n3, r3);
    var s3 = i3.ctx, a3 = n3.contentSize, h3 = a3.width, c3 = a3.height, f3 = a3.left, l3 = a3.top;
    r3.borderRadius, r3.backgroundColor;
    var d3 = r3.color, u3 = void 0 === d3 ? "#000000" : d3;
    r3.border, n3.contentSize.left, n3.borderSize.left, n3.contentSize.top, n3.borderSize.top;
    if (y2 = o3 || y2, s3) {
      s3.save(), i3.setOpacity(r3), i3.setTransform(n3, r3);
      var p3 = Math.min(h3, c3);
      t3 = this.utf16to8(t3);
      var g3 = this.getFrame(t3), v3 = p3 / e2;
      s3.setFillStyle(u3);
      for (var x3 = 0; x3 < e2; x3++)
        for (var b3 = 0; b3 < e2; b3++)
          g3[b3 * e2 + x3] && s3.fillRect(f3 + v3 * x3, l3 + v3 * b3, v3, v3);
      s3.restore(), i3.setBorder(n3, r3);
    } else
      common_vendor.index.__f__("warn", "at uni_modules/lime-painter/components/l-painter/painter.js:1", "No canvas provided to draw QR code in!");
  } } };
}(), yt = p, xt = u, bt = g, wt = d, mt = f.TOP, St = f.MIDDLE, zt = f.BOTTOM, It = l.LEFT, Mt = l.CENTER, kt = l.RIGHT, Bt = function() {
  function r2(t2) {
    var e2, i2, r3 = this;
    this.v = "1.9.5.1", this.id = null, this.pixelRatio = 1, this.width = 0, this.height = 0, this.sleep = 1e3 / 30, this.count = 0, this.isRate = false, this.isDraw = true, this.isCache = true, this.fixed = "", this.useCORS = false, this.performance = false, this.imageBus = [], this.createImage = function(t3, e3) {
      return new Promise(function(i3, n2) {
        var o3 = null;
        window || r3.canvas.createImage ? (o3 = r3.canvas && r3.canvas.createImage ? r3.canvas.createImage() : new Image(), e3 && o3.setAttribute("crossOrigin", "Anonymous"), o3.src = t3, o3.onload = function() {
          i3({ width: o3.naturalWidth || o3.width, height: o3.naturalHeight || o3.height, path: o3, src: this.src });
        }, o3.onerror = function(t4) {
          n2(t4);
        }) : n2({ fail: "getImageInfo fail", src: t3 });
      });
    }, this.options = t2, Object.assign(this, t2), this.ctx = (e2 = t2.context, i2 = { get: function(t3, i3) {
      if ("setFonts" === i3)
        return function(t4) {
          var i4 = t4.fontFamily, r4 = void 0 === i4 ? "sans-serif" : i4, o3 = t4.fontSize, s2 = void 0 === o3 ? 14 : o3, a2 = t4.fontWeight, h2 = void 0 === a2 ? "normal" : a2, c2 = t4.fontStyle, f2 = void 0 === c2 ? "normal" : c2;
          I == n.MP_TOUTIAO && (h2 = "bold" == h2 ? "bold" : "", f2 = "italic" == f2 ? "italic" : ""), e2.font = "".concat(f2, " ").concat(h2, " ").concat(Math.round(s2), "px ").concat(r4);
        };
      if (!e2.draw || !e2.setFillStyle) {
        if ("setFillStyle" === i3)
          return function(t4) {
            e2.fillStyle = t4;
          };
        if ("setStrokeStyle" === i3)
          return function(t4) {
            e2.strokeStyle = t4;
          };
        if ("setLineWidth" === i3)
          return function(t4) {
            e2.lineWidth = t4;
          };
        if ("setLineCap" === i3)
          return function(t4) {
            e2.lineCap = t4;
          };
        if ("setFontSize" === i3)
          return function(t4) {
            e2.font = "".concat(String(t4), "px sans-serif");
          };
        if ("setGlobalAlpha" === i3)
          return function(t4) {
            e2.globalAlpha = t4;
          };
        if ("setLineJoin" === i3)
          return function(t4) {
            e2.lineJoin = t4;
          };
        if ("setTextAlign" === i3)
          return function(t4) {
            e2.textAlign = t4;
          };
        if ("setMiterLimit" === i3)
          return function(t4) {
            e2.miterLimit = t4;
          };
        if ("setShadow" === i3)
          return function(t4, i4, n2, r4) {
            e2.shadowOffsetX = t4, e2.shadowOffsetY = i4, e2.shadowBlur = n2, e2.shadowColor = r4;
          };
        if ("setTextBaseline" === i3)
          return function(t4) {
            e2.textBaseline = t4;
          };
        if ("createCircularGradient" === i3)
          return function() {
          };
        if ("draw" === i3)
          return function() {
          };
        if ("function" == typeof e2[i3])
          return function() {
            for (var t4 = [], n2 = 0; n2 < arguments.length; n2++)
              t4[n2] = arguments[n2];
            return e2[i3].apply(e2, t4);
          };
      }
      return t3[i3];
    }, set: function(t3, i3, n2) {
      return e2[i3] = n2, true;
    } }, new Proxy(e2, i2)), this.progress = 0, this.root = { width: t2.width, height: t2.height, fontSizeRate: 1, fixedLine: null }, this.size = this.root;
    var o2 = 0;
    Object.defineProperty(this, "progress", { configurable: true, set: function(t3) {
      o2 = t3, r3.lifecycle("onProgress", t3 / r3.count);
    }, get: function() {
      return o2 || 0;
    } });
  }
  return r2.prototype.lifecycle = function(t2, e2) {
    this.options.listen && this.options.listen[t2] && this.options.listen[t2](e2);
  }, r2.prototype.setContext = function(t2) {
    t2 && (this.ctx = t2);
  }, r2.prototype.init = function() {
    if (this.canvas.height || n.WEB == I) {
      this.ctx.setTransform(1, 0, 0, 1, 0, 0);
      var t2 = this.size.height * this.pixelRatio, e2 = this.size.width * this.pixelRatio;
      this.canvas.height = t2, this.canvas.width = e2, this.ctx.scale(this.pixelRatio, this.pixelRatio);
    }
  }, r2.prototype.clear = function() {
    this.ctx.clearRect(0, 0, this.size.width, this.size.height);
  }, r2.prototype.clipPath = function(t2, e2, i2, n2, r3, o2, s2) {
    void 0 === o2 && (o2 = false), void 0 === s2 && (s2 = false);
    var a2 = this.ctx;
    if (/polygon/.test(r3)) {
      var h2 = r3.match(/-?\d+(rpx|px|%)?\s+-?\d+(rpx|px|%)?/g) || [];
      a2.beginPath(), h2.map(function(r4) {
        var o3 = r4.split(" "), s3 = o3[0], a3 = o3[1];
        return [W(s3, i2) + t2, W(a3, n2) + e2];
      }).forEach(function(t3, e3) {
        0 == e3 ? a2.moveTo(t3[0], t3[1]) : a2.lineTo(t3[0], t3[1]);
      }), a2.closePath(), s2 && a2.stroke(), o2 && a2.fill();
    }
  }, r2.prototype.roundRect = function(t2, e2, i2, n2, r3, o2, s2) {
    if (void 0 === o2 && (o2 = false), void 0 === s2 && (s2 = false), !(r3 < 0)) {
      var a2 = this.ctx;
      if (a2.beginPath(), r3) {
        var h2 = r3 || {}, c2 = h2.borderTopLeftRadius, f2 = void 0 === c2 ? r3 || 0 : c2, l2 = h2.borderTopRightRadius, d2 = void 0 === l2 ? r3 || 0 : l2, u2 = h2.borderBottomRightRadius, p2 = void 0 === u2 ? r3 || 0 : u2, g2 = h2.borderBottomLeftRadius, v2 = void 0 === g2 ? r3 || 0 : g2;
        a2.arc(t2 + i2 - p2, e2 + n2 - p2, p2, 0, 0.5 * Math.PI), a2.lineTo(t2 + v2, e2 + n2), a2.arc(t2 + v2, e2 + n2 - v2, v2, 0.5 * Math.PI, Math.PI), a2.lineTo(t2, e2 + f2), a2.arc(t2 + f2, e2 + f2, f2, Math.PI, 1.5 * Math.PI), a2.lineTo(t2 + i2 - d2, e2), a2.arc(t2 + i2 - d2, e2 + d2, d2, 1.5 * Math.PI, 2 * Math.PI), a2.lineTo(t2 + i2, e2 + n2 - p2);
      } else
        a2.rect(t2, e2, i2, n2);
      a2.closePath(), s2 && a2.stroke(), o2 && a2.fill();
    }
  }, r2.prototype.setTransform = function(t2, e2) {
    var i2 = e2.transform, n2 = e2.transformOrigin, r3 = this.ctx, o2 = i2 || {}, s2 = o2.scaleX, a2 = void 0 === s2 ? 1 : s2, h2 = o2.scaleY, c2 = void 0 === h2 ? 1 : h2, f2 = o2.translateX, l2 = void 0 === f2 ? 0 : f2, d2 = o2.translateY, u2 = void 0 === d2 ? 0 : d2, p2 = o2.rotate, g2 = void 0 === p2 ? 0 : p2, v2 = o2.skewX, y2 = void 0 === v2 ? 0 : v2, x2 = o2.skewY, b2 = void 0 === x2 ? 0 : x2, w2 = t2.left, m2 = t2.top, S2 = t2.width, z2 = t2.height;
    l2 = W(l2, S2) || 0, u2 = W(u2, z2) || 0;
    var I2 = W("0%", 1), M2 = W("50%", 1), k = W("100%", 1), P2 = { top: I2, center: M2, bottom: k }, O2 = { left: I2, center: M2, right: k };
    if (n2 = n2.split(" ").filter(function(t3, e3) {
      return e3 < 2;
    }).reduce(function(t3, e3) {
      if (/\d+/.test(e3)) {
        var i3 = W(e3, 1) / (/px|rpx$/.test(e3) ? B(t3.x) ? z2 : S2 : 1);
        return B(t3.x) ? Object.assign(t3, { y: i3 }) : Object.assign(t3, { x: i3 });
      }
      return B(O2[e3]) && !B(t3.x) ? Object.assign(t3, { x: O2[e3] }) : Object.assign(t3, { y: P2[e3] || 0.5 });
    }, {}), (l2 || u2) && r3.translate(l2, u2), (a2 || c2) && r3.scale(a2, c2), g2) {
      var T2 = w2 + S2 * n2.x, L2 = m2 + z2 * n2.y;
      r3.translate(T2, L2), r3.rotate(g2 * Math.PI / 180), r3.translate(-T2, -L2);
    }
    (y2 || b2) && r3.transform(1, Math.tan(b2 * Math.PI / 180), Math.tan(y2 * Math.PI / 180), 1, 0, 0);
  }, r2.prototype.setBackground = function(t2, e2, i2, r3, o2) {
    var s2 = this.ctx;
    t2 && "transparent" != t2 ? T(t2) ? L(t2, e2, i2, r3, o2, s2) : s2.setFillStyle(t2) : [n.MP_TOUTIAO, n.MP_BAIDU].includes(I) ? s2.setFillStyle("rgba(0,0,0,0)") : s2.setFillStyle("transparent");
  }, r2.prototype.setShadow = function(t2) {
    var e2 = t2.boxShadow, i2 = void 0 === e2 ? [] : e2, n2 = this.ctx;
    if (i2.length) {
      var r3 = i2[0], o2 = i2[1], s2 = i2[2], a2 = i2[3];
      n2.setShadow(r3, o2, s2, a2);
    }
  }, r2.prototype.setBorder = function(t2, e2) {
    var i2 = this.ctx, n2 = t2.width, r3 = t2.height, o2 = t2.left, s2 = t2.top, a2 = e2.border, h2 = e2.borderBottom, c2 = e2.borderTop, f2 = e2.borderRight, l2 = e2.borderLeft, d2 = e2.borderRadius, u2 = e2.lineCap, p2 = a2 || {}, g2 = p2.borderWidth, v2 = void 0 === g2 ? 0 : g2, y2 = p2.borderStyle, x2 = p2.borderColor, b2 = h2 || {}, w2 = b2.borderBottomWidth, m2 = void 0 === w2 ? v2 : w2, S2 = b2.borderBottomStyle, z2 = void 0 === S2 ? y2 : S2, M2 = b2.borderBottomColor, k = void 0 === M2 ? x2 : M2, B2 = c2 || {}, W2 = B2.borderTopWidth, P2 = void 0 === W2 ? v2 : W2, O2 = B2.borderTopStyle, T2 = void 0 === O2 ? y2 : O2, L2 = B2.borderTopColor, R2 = void 0 === L2 ? x2 : L2, F2 = f2 || {}, A2 = F2.borderRightWidth, j2 = void 0 === A2 ? v2 : A2, E2 = F2.borderRightStyle, C2 = void 0 === E2 ? y2 : E2, H2 = F2.borderRightColor, D2 = void 0 === H2 ? x2 : H2, $2 = l2 || {}, Y2 = $2.borderLeftWidth, U2 = void 0 === Y2 ? v2 : Y2, N2 = $2.borderLeftStyle, X2 = void 0 === N2 ? y2 : N2, _2 = $2.borderLeftColor, q2 = void 0 === _2 ? x2 : _2, G2 = d2 || {}, V2 = G2.borderTopLeftRadius, J2 = void 0 === V2 ? d2 || 0 : V2, Q2 = G2.borderTopRightRadius, Z2 = void 0 === Q2 ? d2 || 0 : Q2, K2 = G2.borderBottomRightRadius, tt2 = void 0 === K2 ? d2 || 0 : K2, et2 = G2.borderBottomLeftRadius, it2 = void 0 === et2 ? d2 || 0 : et2;
    if (h2 || l2 || c2 || f2 || a2) {
      var nt2 = function(t3, e3, n3) {
        "dashed" == e3 ? /mp/.test(I) ? i2.setLineDash([Math.ceil(4 * t3 / 3), Math.ceil(4 * t3 / 3)]) : i2.setLineDash([Math.ceil(6 * t3), Math.ceil(6 * t3)]) : "dotted" == e3 && i2.setLineDash([t3, t3]), i2.setStrokeStyle(n3);
      }, rt2 = function(t3, e3, n3, r4, o3, s3, a3, h3, c3, f3, l3, d3, p3, g3, v3) {
        i2.save(), i2.setLineCap(v3 ? "square" : u2), i2.setLineWidth(d3), nt2(d3, p3, g3), i2.beginPath(), i2.arc(t3, e3, a3, Math.PI * c3, Math.PI * f3), i2.lineTo(n3, r4), i2.arc(o3, s3, h3, Math.PI * f3, Math.PI * l3), i2.stroke(), i2.restore();
      };
      if (i2.save(), a2 && !h2 && !l2 && !c2 && !f2)
        return i2.setLineWidth(v2), nt2(v2, y2, x2), this.roundRect(o2, s2, n2, r3, d2, false, !!x2), void i2.restore();
      m2 && rt2(o2 + n2 - tt2, s2 + r3 - tt2, o2 + it2, s2 + r3, o2 + it2, s2 + r3 - it2, tt2, it2, 0.25, 0.5, 0.75, m2, z2, k, U2 && j2), U2 && rt2(o2 + it2, s2 + r3 - it2, o2, s2 + J2, o2 + J2, s2 + J2, it2, J2, 0.75, 1, 1.25, U2, X2, q2, P2 && m2), P2 && rt2(o2 + J2, s2 + J2, o2 + n2 - Z2, s2, o2 + n2 - Z2, s2 + Z2, J2, Z2, 1.25, 1.5, 1.75, P2, T2, R2, U2 && j2), j2 && rt2(o2 + n2 - Z2, s2 + Z2, o2 + n2, s2 + r3 - tt2, o2 + n2 - tt2, s2 + r3 - tt2, Z2, tt2, 1.75, 2, 0.25, j2, C2, D2, P2 && m2);
    }
  }, r2.prototype.setOpacity = function(t2) {
    var e2 = t2.opacity, i2 = void 0 === e2 ? 1 : e2;
    this.ctx.setGlobalAlpha(i2);
  }, r2.prototype.drawPattern = function(t2, n2, r3) {
    return e(this, void 0, void 0, function() {
      var e2 = this;
      return i(this, function(i2) {
        return [2, new Promise(function(i3, o2) {
          e2.drawView(n2, r3, true, false, true);
          var s2 = e2, a2 = s2.ctx;
          s2.canvas;
          var h2 = n2.width, c2 = n2.height, f2 = n2.left, l2 = n2.top, d2 = r3 || {}, u2 = d2.borderRadius, p2 = void 0 === u2 ? 0 : u2, g2 = d2.backgroundImage, v2 = d2.backgroundRepeat, y2 = void 0 === v2 ? "repeat" : v2;
          g2 && function(t3) {
            var o3 = a2.createPattern(t3.src, y2);
            a2.setFillStyle(o3), e2.roundRect(f2, l2, h2, c2, p2, true, false), e2.setBorder(n2, r3), i3();
          }(t2);
        })];
      });
    });
  }, r2.prototype.drawView = function(t2, e2, i2, n2, r3) {
    void 0 === i2 && (i2 = true), void 0 === n2 && (n2 = true), void 0 === r3 && (r3 = true);
    var o2 = this.ctx, s2 = t2.width, a2 = t2.height, h2 = t2.left, c2 = t2.top, f2 = e2 || {}, l2 = f2.borderRadius, d2 = void 0 === l2 ? 0 : l2, u2 = f2.backgroundColor, p2 = void 0 === u2 ? "transparent" : u2, g2 = f2.overflow;
    e2.opacity && this.setOpacity(e2), this.setTransform(t2, e2), r3 && (o2.save(), this.setShadow(e2)), i2 && this.setBackground(p2, s2, a2, h2, c2), e2.clipPath ? this.clipPath(h2, c2, s2, a2, e2.clipPath, i2, false) : this.roundRect(h2, c2, s2, a2, d2, i2, false), r3 && o2.restore(), n2 && this.setBorder(t2, e2), "hidden" == g2 && o2.clip();
  }, r2.prototype.drawImage = function(t2, r3, o2, s2) {
    return void 0 === r3 && (r3 = {}), void 0 === o2 && (o2 = {}), void 0 === s2 && (s2 = true), e(this, void 0, void 0, function() {
      var a2 = this;
      return i(this, function(h2) {
        switch (h2.label) {
          case 0:
            return [4, new Promise(function(h3, c2) {
              return e(a2, void 0, void 0, function() {
                var e2, a3, c3, f2, l2, d2, u2, p2, g2, v2, y2, x2, b2, w2, m2, S2, z2, M2, k, B2, T2, L2 = this;
                return i(this, function(i2) {
                  return e2 = this.ctx, a3 = o2.borderRadius, c3 = void 0 === a3 ? 0 : a3, f2 = o2.backgroundColor, l2 = void 0 === f2 ? "transparent" : f2, d2 = o2.objectFit, u2 = void 0 === d2 ? "fill" : d2, p2 = o2.backgroundSize, g2 = void 0 === p2 ? "fill" : p2, v2 = o2.objectPosition, y2 = o2.backgroundPosition, x2 = o2.boxShadow, o2.backgroundImage && (u2 = g2, v2 = y2), x2 && this.drawView(r3, Object.assign(o2, { backgroundColor: l2 || x2 && (l2 || "#ffffff") }), true, false, true), b2 = r3.width, w2 = r3.height, m2 = r3.left, S2 = r3.top, e2.save(), z2 = r3.contentSize.left - r3.borderSize.left, M2 = r3.contentSize.top - r3.borderSize.top, s2 || (this.setOpacity(o2), this.setTransform(r3, o2), this.setBackground(l2, b2, w2, m2, S2), this.roundRect(m2, S2, b2, w2, c3, !!(c3 || !x2 && l2), false)), m2 += z2, S2 += M2, e2.clip(), k = function(t3) {
                    if ("fill" !== u2) {
                      var i3 = function(t4, e3, i4) {
                        var n2 = t4.objectFit, r4 = t4.objectPosition, o4 = e3.width / e3.height, s4 = i4.width / i4.height, a5 = 1, h5 = "contain", c5 = "cover";
                        n2 == h5 && o4 >= s4 || n2 == c5 && o4 < s4 ? a5 = e3.height / i4.height : (n2 == h5 && o4 < s4 || n2 == c5 && o4 >= s4) && (a5 = e3.width / i4.width);
                        var f4 = i4.width * a5, l4 = i4.height * a5, d4 = r4 || [], u3 = d4[0], p3 = d4[1], g3 = O(u3) ? W(u3, e3.width) : (e3.width - f4) * (P(u3) ? W(u3, 1) : { left: 0, center: 0.5, right: 1 }[u3 || "center"]), v3 = O(p3) ? W(p3, e3.height) : (e3.height - l4) * (P(p3) ? W(p3, 1) : { top: 0, center: 0.5, bottom: 1 }[p3 || "center"]), y3 = function(t5, e4) {
                          return [(t5 - g3) / a5, (e4 - v3) / a5];
                        }, x3 = y3(0, 0), b3 = x3[0], w3 = x3[1], m3 = y3(e3.width, e3.height), S3 = m3[0], z3 = m3[1], I2 = Math.max, M3 = Math.min;
                        return { sx: I2(b3, 0), sy: I2(w3, 0), sw: M3(S3 - b3, i4.width), sh: M3(z3 - w3, i4.height), dx: I2(g3, 0), dy: I2(v3, 0), dw: M3(f4, e3.width), dh: M3(l4, e3.height) };
                      }({ objectFit: u2, objectPosition: v2 }, r3.contentSize, t3), o3 = i3.sx, s3 = i3.sy, a4 = i3.sh, h4 = i3.sw, c4 = i3.dx, f3 = i3.dy, l3 = i3.dh, d3 = i3.dw;
                      I == n.MP_BAIDU ? e2.drawImage(t3.src, c4 + m2, f3 + S2, d3, l3, o3, s3, h4, a4) : e2.drawImage(t3.src, o3, s3, h4, a4, c4 + m2, f3 + S2, d3, l3);
                    } else
                      e2.drawImage(t3.src, m2, S2, b2, w2);
                  }, B2 = function() {
                    e2.restore(), L2.drawView(r3, o2, false, true, false), h3(1);
                  }, T2 = function(t3) {
                    k(t3), B2();
                  }, T2(t2), [2];
                });
              });
            })];
          case 1:
            return h2.sent(), [2];
        }
      });
    });
  }, r2.prototype.drawText = function(t2, e2, i2, n2) {
    var r3 = this, o2 = this.ctx, s2 = e2.borderSize, a2 = e2.contentSize, h2 = e2.left, c2 = e2.top, f2 = a2.width, l2 = a2.height, d2 = a2.left - s2.left || 0, u2 = a2.top - s2.top || 0, p2 = i2.color, g2 = i2.lineHeight, v2 = i2.fontSize, y2 = i2.fontWeight, x2 = i2.fontFamily, b2 = i2.fontStyle, w2 = i2.textIndent, m2 = void 0 === w2 ? 0 : w2, S2 = i2.textAlign, z2 = i2.textStroke, I2 = i2.verticalAlign, M2 = void 0 === I2 ? St : I2, k = i2.backgroundColor, P2 = i2.lineClamp, O2 = i2.backgroundClip, T2 = i2.textShadow, L2 = i2.textDecoration;
    if (m2 = B(m2) ? m2 : 0, this.drawView(e2, i2, O2 != xt), g2 = W(g2, v2), t2) {
      o2.save(), h2 += d2, c2 += u2;
      var R2 = n2.fontHeight, F2 = n2.descent, A2 = void 0 === F2 ? 0 : F2, j2 = n2.ascent, E2 = A2 + (void 0 === j2 ? 0 : j2);
      switch (o2.setFonts({ fontFamily: x2, fontSize: v2, fontWeight: y2, fontStyle: b2 }), o2.setTextBaseline(St), o2.setTextAlign(S2), O2 ? this.setBackground(k, f2, l2, h2, c2) : o2.setFillStyle(p2), S2) {
        case It:
          break;
        case Mt:
          h2 += 0.5 * f2;
          break;
        case kt:
          h2 += f2;
      }
      var C2 = n2.lines * g2, H2 = Math.ceil((l2 - C2) / 2);
      switch (H2 < 0 && (H2 = 0), M2) {
        case mt:
          break;
        case St:
          c2 += H2;
          break;
        case zt:
          c2 += 2 * H2;
      }
      var D2 = (g2 - R2) / 2, $2 = g2 / 2, Y2 = function(t3) {
        var e3 = o2.measureText(t3), i3 = e3.actualBoundingBoxDescent, n3 = void 0 === i3 ? 0 : i3, r4 = e3.actualBoundingBoxAscent;
        return M2 == mt ? { fix: E2 ? void 0 === r4 ? 0 : r4 : $2 - D2 / 2, lineY: E2 ? 0 : D2 - D2 / 2 } : M2 == St ? { fix: E2 ? $2 + n3 / 4 : $2, lineY: E2 ? 0 : D2 } : M2 == zt ? { fix: E2 ? g2 - n3 : $2 + D2 / 2, lineY: E2 ? 2 * D2 : D2 + D2 / 2 } : { fix: 0, height: 0, lineY: 0 };
      }, U2 = function(t3, e3, i3) {
        var r4 = t3;
        switch (S2) {
          case It:
            r4 += i3;
            break;
          case Mt:
            r4 = (t3 -= i3 / 2) + i3;
            break;
          case kt:
            r4 = t3, t3 -= i3;
        }
        if (L2) {
          o2.setLineWidth(v2 / 13), o2.beginPath();
          var s3 = 0.1 * n2.fontHeight;
          /\bunderline\b/.test(L2) && (o2.moveTo(t3, e3 + n2.fontHeight + s3), o2.lineTo(r4, e3 + n2.fontHeight + s3)), /\boverline\b/.test(L2) && (o2.moveTo(t3, e3 - s3), o2.lineTo(r4, e3 - s3)), /\bline-through\b/.test(L2) && (o2.moveTo(t3, e3 + 0.5 * n2.fontHeight), o2.lineTo(r4, e3 + 0.5 * n2.fontHeight)), o2.closePath(), o2.setStrokeStyle(p2), o2.stroke();
        }
      }, N2 = function(t3, e3, i3) {
        var n3 = function() {
          o2.setLineWidth(z2.width), o2.setStrokeStyle(z2.color), o2.strokeText(t3, e3, i3);
        }, s3 = "outset";
        z2 && z2.type !== s3 ? (o2.save(), r3.setShadow({ boxShadow: T2 }), o2.fillText(t3, e3, i3), o2.restore(), n3()) : z2 && z2.type == s3 ? (o2.save(), r3.setShadow({ boxShadow: T2 }), n3(), o2.restore(), o2.save(), o2.fillText(t3, e3, i3), o2.restore()) : (r3.setShadow({ boxShadow: T2 }), o2.fillText(t3, e3, i3));
      };
      if (!n2.widths || 1 == n2.widths.length && n2.widths[0].total + m2 <= a2.width) {
        var X2 = Y2(t2), _2 = X2.fix, q2 = void 0 === _2 ? 0 : _2, G2 = X2.lineY;
        return N2(t2, h2 + m2, c2 + q2), U2(h2 + m2, c2 + G2, n2 && n2.widths && n2.widths[0].total || n2.text), c2 += g2, o2.restore(), void this.setBorder(e2, i2);
      }
      for (var V2 = c2, J2 = h2, Q2 = "", Z2 = 0, K2 = o2.measureText("...").width, tt2 = n2.widths, et2 = 0; et2 < tt2.length; et2++) {
        var it2 = tt2[et2].widths, nt2 = 0;
        Q2 = "", c2 += 1 == (Z2 += 1) ? 0 : g2, 1 == Z2 && m2 && (nt2 = m2, J2 = h2 + m2);
        for (var rt2 = 0; rt2 < it2.length; rt2++) {
          1 !== Z2 && m2 && (J2 = h2);
          var ot2 = it2[rt2], st2 = ot2.width, at2 = ot2.text, ht2 = (it2[rt2 + 1] || {}).width;
          if (Q2 += at2, (nt2 += st2) + (void 0 === ht2 ? 0 : ht2) + (0 == Z2 ? m2 : 0) + (Z2 == P2 ? K2 : 0) > a2.width) {
            Z2 >= P2 && (Q2 += "…"), Z2++, nt2 = 0;
            var ct2 = Y2(Q2);
            q2 = ct2.fix, G2 = ct2.lineY;
            N2(Q2, J2, c2 + q2), U2(J2, c2 + G2, nt2), c2 += g2, Q2 = "";
          } else if (rt2 == it2.length - 1) {
            et2 != tt2.length - 1 && Z2 == P2 && K2 + nt2 < a2.width && (Q2 += "…");
            var ft2 = Y2(Q2);
            q2 = ft2.fix, G2 = ft2.lineY;
            N2(Q2, J2, c2 + q2), U2(J2, c2 + G2, nt2);
          }
          if (c2 > V2 + l2 || Z2 > P2)
            break;
        }
      }
      o2.restore();
    }
  }, r2.prototype.source = function(t2) {
    return e(this, void 0, void 0, function() {
      var e2, n2, r3, o2, s2 = this;
      return i(this, function(i2) {
        switch (i2.label) {
          case 0:
            if (this.node = null, e2 = +/* @__PURE__ */ new Date(), "{}" == JSON.stringify(t2))
              return [2];
            if (t2.styles = t2.styles || t2.css || {}, !t2.type)
              for (n2 in t2.type = wt, t2)
                ["views", "children", "type", "css", "styles"].includes(n2) || (t2.styles[n2] = t2[n2], delete t2[n2]);
            return t2.styles.boxSizing || (t2.styles.boxSizing = "border-box"), [4, this.create(t2)];
          case 1:
            return (r3 = i2.sent()) ? (o2 = r3.layout() || {}, this.size = o2, this.node = r3, this.onEffectFinished().then(function(t3) {
              return s2.lifecycle("onEffectSuccess", t3);
            }).catch(function(t3) {
              return s2.lifecycle("onEffectFail", t3);
            }), this.performance && common_vendor.index.__f__("log", "at uni_modules/lime-painter/components/l-painter/painter.js:1", "布局用时：" + (+/* @__PURE__ */ new Date() - e2) + "ms"), [2, this.size]) : [2, common_vendor.index.__f__("warn", "at uni_modules/lime-painter/components/l-painter/painter.js:1", "no node")];
        }
      });
    });
  }, r2.prototype.getImageInfo = function(t2) {
    return this.imageBus[t2] || (this.imageBus[t2] = this.createImage(t2, this.useCORS)), this.imageBus[t2];
  }, r2.prototype.create = function(n2, r3) {
    return e(this, void 0, void 0, function() {
      function e2(i2, n3, r4) {
        void 0 === n3 && (n3 = {}), void 0 === r4 && (r4 = true);
        var o3 = [];
        return i2.forEach(function(i3) {
          var s3 = i3.styles, a3 = void 0 === s3 ? {} : s3, h3 = i3.css, c3 = void 0 === h3 ? {} : h3, f3 = i3.children, l3 = void 0 === f3 ? [] : f3, d3 = i3.views, u3 = void 0 === d3 ? [] : d3, p3 = i3.text, g3 = void 0 === p3 ? "" : p3, v3 = i3.type, y3 = void 0 === v3 ? "" : v3;
          !l3 && u3 && (i3.children = l3 = u3);
          var x2 = {};
          x2 = t(t(r4 ? t({}, n3) : {}, a3), c3);
          var b3 = {}, w3 = {}, m3 = {};
          Object.keys(x2).map(function(t2) {
            if (t2.includes("padding") || t2.includes("margin")) {
              var e3 = J(t2, x2[t2]);
              Object.keys(e3).map(function(t3) {
                t3.includes("Left") ? w3[t3] = e3[t3] : t3.includes("Right") ? m3[t3] = e3[t3] : b3[t3] = e3[t3];
              });
            }
          });
          if (x2.textIndent && (w3.textIndent = x2.textIndent, delete n3.textIndent), "" !== g3) {
            var S3 = Array.from(g3);
            S3.forEach(function(t2, e3) {
              var i4 = Object.assign({}, x2, b3);
              0 === e3 ? Object.assign(i4, w3) : e3 == S3.length - 1 && Object.assign(i4, m3), delete i4.padding, delete i4.margin, o3.push({ type: "text", text: t2, styles: i4 });
            });
          }
          if (y3 == yt || y3 == bt)
            o3.push(i3);
          else if ("block" === a3.display && l3.length > 0) {
            var z3 = e2(l3, x2, false);
            i3.children = z3, i3.flattened = true, o3.push(i3);
          } else if (l3.length > 0) {
            z3 = e2(l3, x2, r4);
            o3 = o3.concat(z3);
          }
        }), o3;
      }
      var o2, s2, a2, h2, c2, f2, l2, d2, u2, p2, g2, v2, y2, b2, w2, m2, S2, z2, I2, M2, k, B2, W2, P2;
      return i(this, function(i2) {
        switch (i2.label) {
          case 0:
            if (!n2)
              return [2];
            if (n2.styles || (n2.styles = n2.css || {}), o2 = n2.type, s2 = n2.show, a2 = void 0 === s2 || s2, h2 = o2 == yt, c2 = [xt, bt].includes(o2), f2 = "textBox" == o2, l2 = n2.styles || {}, d2 = l2.backgroundImage, u2 = l2.display, h2 && !n2.src && !n2.url)
              return [2];
            if (u2 == x || !a2)
              return [2];
            if (c2 || f2) {
              if (p2 = n2.children, g2 = n2.views, !p2 && g2 && (n2.children = p2 = g2), !n2.text && (!p2 || p2 && !p2.length))
                return [2];
              p2 && p2.length && !n2.flattened && (v2 = e2(n2.children || n2.views), n2.type = "view", n2.children = v2);
            }
            if (!(h2 || n2.type == wt && d2))
              return [3, 4];
            y2 = h2 ? n2.src : "", b2 = /url\(['"]?(.*?)['"]?\)/.exec(d2), d2 && b2 && b2[1] && (y2 = b2[1] || ""), i2.label = 1;
          case 1:
            return i2.trys.push([1, 3, , 4]), [4, this.getImageInfo(y2)];
          case 2:
            return w2 = i2.sent(), m2 = w2.width, S2 = w2.height, !(z2 = w2.path) && h2 ? [2] : (z2 && (n2.attributes = Object.assign(n2.attributes || {}, { width: m2, height: S2, path: z2, src: z2, naturalSrc: y2 })), [3, 4]);
          case 3:
            return I2 = i2.sent(), n2.type != wt ? [2] : (this.lifecycle("onEffectFail", t(t({}, I2), { src: y2 })), [3, 4]);
          case 4:
            if (this.count += 1, M2 = new gt(n2, r3, this.root, this.ctx), !(k = n2.children || n2.views))
              return [3, 8];
            B2 = 0, i2.label = 5;
          case 5:
            return B2 < k.length ? (W2 = k[B2], [4, this.create(W2, M2)]) : [3, 8];
          case 6:
            (P2 = i2.sent()) && M2.add(P2), i2.label = 7;
          case 7:
            return B2++, [3, 5];
          case 8:
            return [2, M2];
        }
      });
    });
  }, r2.prototype.drawNode = function(t2, n2) {
    return void 0 === n2 && (n2 = false), e(this, void 0, void 0, function() {
      var e2, r3, o2, s2, a2, h2, c2, f2, l2, d2, u2, p2, g2, v2, y2, x2, b2, w2, m2, S2, z2, I2, M2;
      return i(this, function(i2) {
        switch (i2.label) {
          case 0:
            return e2 = t2.layoutBox, r3 = t2.computedStyle, o2 = t2.attributes, s2 = t2.name, a2 = t2.children, h2 = t2.fixedLine, c2 = t2.attributes, f2 = c2.src, l2 = c2.text, d2 = r3.position, u2 = r3.backgroundImage, p2 = r3.backgroundRepeat, ["fixed"].includes(d2) && !n2 ? [2] : (this.ctx.save(), s2 !== wt ? [3, 7] : f2 && u2 ? p2 ? [4, this.drawPattern(o2, e2, r3)] : [3, 2] : [3, 5]);
          case 1:
            return i2.sent(), [3, 4];
          case 2:
            return [4, this.drawImage(o2, e2, r3, false)];
          case 3:
            i2.sent(), i2.label = 4;
          case 4:
            return [3, 6];
          case 5:
            this.drawView(e2, r3), i2.label = 6;
          case 6:
            return [3, 10];
          case 7:
            return s2 === yt && f2 ? [4, this.drawImage(o2, e2, r3, false)] : [3, 9];
          case 8:
            return i2.sent(), [3, 10];
          case 9:
            s2 === xt ? this.drawText(l2, e2, r3, o2) : s2 === bt && vt.api && vt.api.draw(l2, this, e2, r3), i2.label = 10;
          case 10:
            if (this.progress += 1, v2 = (g2 = h2 || {}).beforeElements, y2 = g2.afterElements, !v2)
              return [3, 14];
            x2 = 0, b2 = v2, i2.label = 11;
          case 11:
            return x2 < b2.length ? (M2 = b2[x2], [4, this.drawNode(M2)]) : [3, 14];
          case 12:
            i2.sent(), i2.label = 13;
          case 13:
            return x2++, [3, 11];
          case 14:
            if (!a2)
              return [3, 18];
            w2 = Object.values ? Object.values(a2) : Object.keys(a2).map(function(t3) {
              return a2[t3];
            }), m2 = 0, S2 = w2, i2.label = 15;
          case 15:
            return m2 < S2.length ? "absolute" === (M2 = S2[m2]).computedStyle.position ? [3, 17] : [4, this.drawNode(M2)] : [3, 18];
          case 16:
            i2.sent(), i2.label = 17;
          case 17:
            return m2++, [3, 15];
          case 18:
            if (!y2)
              return [3, 22];
            z2 = 0, I2 = y2, i2.label = 19;
          case 19:
            return z2 < I2.length ? (M2 = I2[z2], [4, this.drawNode(M2)]) : [3, 22];
          case 20:
            i2.sent(), i2.label = 21;
          case 21:
            return z2++, [3, 19];
          case 22:
            return this.ctx.restore(), [2];
        }
      });
    });
  }, r2.prototype.render = function(t2) {
    var n2 = this;
    return void 0 === t2 && (t2 = 30), new Promise(function(r3, o2) {
      return e(n2, void 0, void 0, function() {
        var e2, n3, s2, a2, h2, c2, f2, l2, d2, u2;
        return i(this, function(i2) {
          switch (i2.label) {
            case 0:
              return e2 = +/* @__PURE__ */ new Date(), this.init(), [4, (p2 = t2, void 0 === p2 && (p2 = 0), new Promise(function(t3) {
                return setTimeout(t3, p2);
              }))];
            case 1:
              i2.sent(), i2.label = 2;
            case 2:
              if (i2.trys.push([2, 14, , 15]), !this.node)
                return [3, 12];
              if (n3 = this.root.fixedLine || {}, s2 = n3.beforeElements, a2 = n3.afterElements, !s2)
                return [3, 6];
              h2 = 0, c2 = s2, i2.label = 3;
            case 3:
              return h2 < c2.length ? (d2 = c2[h2], [4, this.drawNode(d2, true)]) : [3, 6];
            case 4:
              i2.sent(), i2.label = 5;
            case 5:
              return h2++, [3, 3];
            case 6:
              return [4, this.drawNode(this.node)];
            case 7:
              if (i2.sent(), !a2)
                return [3, 11];
              f2 = 0, l2 = a2, i2.label = 8;
            case 8:
              return f2 < l2.length ? (d2 = l2[f2], [4, this.drawNode(d2, true)]) : [3, 11];
            case 9:
              i2.sent(), i2.label = 10;
            case 10:
              return f2++, [3, 8];
            case 11:
              return r3(this.node), [3, 13];
            case 12:
              this.lifecycle("onEffectFail", "node is empty"), i2.label = 13;
            case 13:
              return [3, 15];
            case 14:
              return u2 = i2.sent(), this.lifecycle("onEffectFail", u2), o2(u2), [3, 15];
            case 15:
              return this.performance && common_vendor.index.__f__("log", "at uni_modules/lime-painter/components/l-painter/painter.js:1", "渲染用时：" + (+/* @__PURE__ */ new Date() - e2 - 30) + "ms"), [2];
          }
          var p2;
        });
      });
    });
  }, r2.prototype.onEffectFinished = function() {
    var t2 = this, e2 = Object.keys(this.imageBus).map(function(e3) {
      return t2.imageBus[e3];
    });
    return Promise.all(e2);
  }, r2.prototype.destroy = function() {
    this.node = [];
  }, r2.prototype.save = function(t2) {
    try {
      var e2 = t2 || {}, i2 = e2.fileType, n2 = void 0 === i2 ? "png" : i2, r3 = e2.quality, o2 = void 0 === r3 ? 1 : r3;
      return this.canvas.toDataURL("image/".concat(n2), o2);
    } catch (t3) {
      return this.lifecycle("onEffectFail", "image cross domain"), t3;
    }
  }, r2;
}();
n.WEB == I && (window.Painter = Bt);
exports.Bt = Bt;
//# sourceMappingURL=../../../../../.sourcemap/mp-weixin/uni_modules/lime-painter/components/l-painter/painter.js.map
