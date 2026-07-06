"use strict";
const sheep_request_index = require("../../request/index.js");
const sheep_config_server = require("../../config/server.js");
const DictApi = {
  // 根据字典类型查询字典数据信息
  getDictDataListByType: (type) => {
    return sheep_request_index.request({
      url: sheep_config_server.getSystemBaseUrl() + `/dictData/getList`,
      method: "GET",
      params: {
        type
      }
    });
  }
};
const __vite_glob_0_30 = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: DictApi
}, Symbol.toStringTag, { value: "Module" }));
exports.DictApi = DictApi;
exports.__vite_glob_0_30 = __vite_glob_0_30;
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/api/system/dict.js.map
