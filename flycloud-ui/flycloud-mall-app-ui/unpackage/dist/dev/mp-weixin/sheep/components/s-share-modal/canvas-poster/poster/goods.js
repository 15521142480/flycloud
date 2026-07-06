"use strict";
const sheep_index = require("../../../../index.js");
const sheep_components_sShareModal_canvasPoster_poster_index = require("./index.js");
const goods = async (poster) => {
  const width = poster.width;
  const userInfo = sheep_index.sheep.$store("user").userInfo;
  const wxa_qrcode = await sheep_components_sShareModal_canvasPoster_poster_index.getWxaQrcode(poster.shareInfo.path, poster.shareInfo.query);
  return [
    {
      type: "image",
      src: sheep_components_sShareModal_canvasPoster_poster_index.formatImageUrlProtocol(sheep_index.sheep.$url.cdn(sheep_index.sheep.$store("app").platform.share.posterInfo.goods_bg)),
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
        height: width * 0.94
      }
    },
    {
      type: "text",
      text: poster.shareInfo.poster.title,
      css: {
        position: "fixed",
        left: width * 0.04,
        top: width * 1.18,
        color: "#333",
        fontSize: 14,
        lineHeight: 15,
        maxWidth: width * 0.91
      }
    },
    {
      type: "text",
      text: "￥" + poster.shareInfo.poster.price,
      css: {
        position: "fixed",
        left: width * 0.04,
        top: width * 1.31,
        fontSize: 20,
        fontFamily: "OPPOSANS",
        color: "#333"
      }
    },
    {
      type: "text",
      text: poster.shareInfo.poster.original_price > 0 ? "￥" + poster.shareInfo.poster.original_price : "",
      css: {
        position: "fixed",
        left: width * 0.3,
        top: width * 1.33,
        color: "#999",
        fontSize: 10,
        fontFamily: "OPPOSANS",
        textDecoration: "line-through"
      }
    },
    {
      type: "image",
      src: wxa_qrcode,
      css: {
        position: "fixed",
        left: width * 0.75,
        top: width * 1.3,
        width: width * 0.2,
        height: width * 0.2
      }
    }
  ];
};
exports.goods = goods;
//# sourceMappingURL=../../../../../../.sourcemap/mp-weixin/sheep/components/s-share-modal/canvas-poster/poster/goods.js.map
