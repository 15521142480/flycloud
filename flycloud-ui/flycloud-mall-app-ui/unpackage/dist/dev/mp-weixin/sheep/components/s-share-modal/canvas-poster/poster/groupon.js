"use strict";
const sheep_index = require("../../../../index.js");
const sheep_components_sShareModal_canvasPoster_poster_index = require("./index.js");
const groupon = async (poster) => {
  debugger;
  const width = poster.width;
  const userInfo = sheep_index.sheep.$store("user").userInfo;
  const wxa_qrcode = await sheep_components_sShareModal_canvasPoster_poster_index.getWxaQrcode(poster.shareInfo.path, poster.shareInfo.query);
  return [
    {
      type: "image",
      src: sheep_components_sShareModal_canvasPoster_poster_index.formatImageUrlProtocol(
        sheep_index.sheep.$url.cdn(sheep_index.sheep.$store("app").platform.share.posterInfo.groupon_bg)
      ),
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
        fontSize: 16,
        fontFamily: "sans-serif",
        position: "fixed",
        top: width * 0.06,
        left: width * 0.22
      }
    },
    {
      type: "image",
      src: sheep_components_sShareModal_canvasPoster_poster_index.formatImageUrlProtocol(sheep_index.sheep.$url.cdn(userInfo.avatar)),
      css: {
        position: "fixed",
        left: width * 0.04,
        top: width * 0.04,
        width: width * 0.14,
        height: width * 0.14
      }
    },
    {
      type: "image",
      src: sheep_components_sShareModal_canvasPoster_poster_index.formatImageUrlProtocol(poster.shareInfo.poster.image),
      css: {
        position: "fixed",
        left: width * 0.03,
        top: width * 0.21,
        width: width * 0.94,
        height: width * 0.94,
        borderRadius: 10
      }
    },
    {
      type: "text",
      text: poster.shareInfo.poster.title,
      css: {
        color: "#333",
        fontSize: 14,
        position: "fixed",
        top: width * 1.18,
        left: width * 0.04,
        maxWidth: width * 0.91,
        lineHeight: 5
      }
    },
    {
      type: "text",
      text: "￥" + poster.shareInfo.poster.price,
      css: {
        color: "#ff0000",
        fontSize: 20,
        fontFamily: "OPPOSANS",
        position: "fixed",
        top: width * 1.3,
        left: width * 0.04
      }
    },
    {
      type: "text",
      text: poster.shareInfo.poster.grouponNum + "人团",
      css: {
        color: "#fff",
        fontSize: 12,
        fontFamily: "OPPOSANS",
        position: "fixed",
        left: width * 0.84,
        top: width * 1.3
      }
    },
    {
      type: "image",
      src: wxa_qrcode,
      css: {
        position: "fixed",
        left: width * 0.75,
        top: width * 1.4,
        width: width * 0.2,
        height: width * 0.2
      }
    }
  ];
};
exports.groupon = groupon;
//# sourceMappingURL=../../../../../../.sourcemap/mp-weixin/sheep/components/s-share-modal/canvas-poster/poster/groupon.js.map
