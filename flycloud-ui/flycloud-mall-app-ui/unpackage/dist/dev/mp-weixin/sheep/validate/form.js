"use strict";
const sheep_helper_test = require("../helper/test.js");
const mobile = {
  rules: [
    {
      required: true,
      errorMessage: "请输入手机号"
    },
    {
      validateFunction: function(rule, value, data, callback) {
        if (!sheep_helper_test.test.mobile(value)) {
          callback("手机号码格式不正确");
        }
        return true;
      }
    }
  ]
};
const password = {
  rules: [
    {
      required: true,
      errorMessage: "请输入密码"
    },
    {
      validateFunction: function(rule, value, data, callback) {
        if (!/^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]+\S{5,12}$/.test(value)) {
          callback("需包含字母和数字,长度在6-12之间");
        }
        return true;
      }
    }
  ]
};
const code = {
  rules: [
    {
      required: true,
      errorMessage: "请输入验证码"
    }
  ]
};
exports.code = code;
exports.mobile = mobile;
exports.password = password;
//# sourceMappingURL=../../../.sourcemap/mp-weixin/sheep/validate/form.js.map
