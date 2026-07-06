"use strict";
const sheep_components_sShareModal_canvasPoster_poster_user = require("./user.js");
const sheep_components_sShareModal_canvasPoster_poster_goods = require("./goods.js");
const sheep_components_sShareModal_canvasPoster_poster_groupon = require("./groupon.js");
const sheep_api_member_social = require("../../../../api/member/social.js");
function getPosterData(options) {
  switch (options.shareInfo.poster.type) {
    case "user":
      return sheep_components_sShareModal_canvasPoster_poster_user.user(options);
    case "goods":
      return sheep_components_sShareModal_canvasPoster_poster_goods.goods(options);
    case "groupon":
      return sheep_components_sShareModal_canvasPoster_poster_groupon.groupon(options);
  }
}
function formatImageUrlProtocol(url) {
  if (url.indexOf("http:") === 0) {
    url = url.replace("http:", "https:");
  }
  return url;
}
async function getWxaQrcode(path, query) {
  const res = await sheep_api_member_social.SocialApi.getWxaQrcode(path, query);
  return "data:image/png;base64," + res.data;
}
exports.formatImageUrlProtocol = formatImageUrlProtocol;
exports.getPosterData = getPosterData;
exports.getWxaQrcode = getWxaQrcode;
//# sourceMappingURL=../../../../../../.sourcemap/mp-weixin/sheep/components/s-share-modal/canvas-poster/poster/index.js.map
