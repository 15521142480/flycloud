"use strict";
const sheep_index = require("../../../../index.js");
const sheep_components_sShareModal_canvasPoster_poster_index = require("./index.js");
const utils_textUtils = require("../../../../../utils/textUtils.js");
const user = async (poster) => {
  const width = poster.width;
  const userInfo = sheep_index.sheep.$store("user").userInfo;
  const wxa_qrcode = await sheep_components_sShareModal_canvasPoster_poster_index.getWxaQrcode(poster.shareInfo.path, poster.shareInfo.query);
  const widthNickName = utils_textUtils.measureTextWidth(userInfo.nickname, 14);
  return [
    {
      type: "image",
      src: sheep_components_sShareModal_canvasPoster_poster_index.formatImageUrlProtocol(sheep_index.sheep.$url.cdn(sheep_index.sheep.$store("app").platform.share.posterInfo.user_bg)),
      css: {
        width,
        position: "fixed",
        "object-fit": "contain",
        top: "0",
        left: "0",
        zIndex: -1
      }
    },
    {
      type: "text",
      text: userInfo.nickname,
      css: {
        color: "#333",
        fontSize: 14,
        textAlign: "center",
        fontFamily: "sans-serif",
        position: "fixed",
        top: width * 0.4,
        left: (width - widthNickName) / 2
      }
    },
    {
      type: "image",
      src: sheep_components_sShareModal_canvasPoster_poster_index.formatImageUrlProtocol(sheep_index.sheep.$url.cdn(userInfo.avatar)),
      css: {
        position: "fixed",
        left: width * 0.4,
        top: width * 0.16,
        width: width * 0.2,
        height: width * 0.2
      }
    },
    {
      type: "image",
      src: wxa_qrcode,
      css: {
        position: "fixed",
        left: width * 0.35,
        top: width * 0.84,
        width: width * 0.3,
        height: width * 0.3
      }
    }
  ];
};
exports.user = user;
//# sourceMappingURL=../../../../../../.sourcemap/mp-weixin/sheep/components/s-share-modal/canvas-poster/poster/user.js.map
