// vite.config.ts
import { loadEnv } from "file:///Users/lxs/applicationApp/git/person/flycloud-server/flycloud-ui/flycloud-admin-ui/node_modules/.pnpm/vite@5.4.21_@types+node@20.19.43_sass@1.101.0_terser@5.48.0/node_modules/vite/dist/node/index.js";

// build/vite/index.ts
import { resolve } from "path";
import { createRequire } from "node:module";
import Vue from "file:///Users/lxs/applicationApp/git/person/flycloud-server/flycloud-ui/flycloud-admin-ui/node_modules/.pnpm/@vitejs+plugin-vue@5.0.4_vite@5.4.21_@types+node@20.19.43_sass@1.101.0_terser@5.48.0__vue@3.5.12_typescript@5.3.3_/node_modules/@vitejs/plugin-vue/dist/index.mjs";
import VueJsx from "file:///Users/lxs/applicationApp/git/person/flycloud-server/flycloud-ui/flycloud-admin-ui/node_modules/.pnpm/@vitejs+plugin-vue-jsx@3.1.0_vite@5.4.21_@types+node@20.19.43_sass@1.101.0_terser@5.48.0__vue@3.5.12_typescript@5.3.3_/node_modules/@vitejs/plugin-vue-jsx/dist/index.mjs";
import progress from "file:///Users/lxs/applicationApp/git/person/flycloud-server/flycloud-ui/flycloud-admin-ui/node_modules/.pnpm/vite-plugin-progress@0.0.7_vite@5.4.21_@types+node@20.19.43_sass@1.101.0_terser@5.48.0_/node_modules/vite-plugin-progress/dist/index.mjs";
import EslintPlugin from "file:///Users/lxs/applicationApp/git/person/flycloud-server/flycloud-ui/flycloud-admin-ui/node_modules/.pnpm/vite-plugin-eslint@1.8.1_eslint@8.57.0_vite@5.4.21_@types+node@20.19.43_sass@1.101.0_terser@5.48.0_/node_modules/vite-plugin-eslint/dist/index.mjs";
import { ViteEjsPlugin } from "file:///Users/lxs/applicationApp/git/person/flycloud-server/flycloud-ui/flycloud-admin-ui/node_modules/.pnpm/vite-plugin-ejs@1.8.0_vite@5.4.21_@types+node@20.19.43_sass@1.101.0_terser@5.48.0_/node_modules/vite-plugin-ejs/index.js";
import ElementPlus from "file:///Users/lxs/applicationApp/git/person/flycloud-server/flycloud-ui/flycloud-admin-ui/node_modules/.pnpm/unplugin-element-plus@0.8.0_rollup@4.62.2/node_modules/unplugin-element-plus/dist/vite.mjs";
import AutoImport from "file:///Users/lxs/applicationApp/git/person/flycloud-server/flycloud-ui/flycloud-admin-ui/node_modules/.pnpm/unplugin-auto-import@0.16.7_@vueuse+core@10.9.0_vue@3.5.12_typescript@5.3.3___rollup@4.62.2/node_modules/unplugin-auto-import/dist/vite.js";
import Components from "file:///Users/lxs/applicationApp/git/person/flycloud-server/flycloud-ui/flycloud-admin-ui/node_modules/.pnpm/unplugin-vue-components@0.25.2_@babel+parser@7.25.8_rollup@4.62.2_vue@3.5.12_typescript@5.3.3_/node_modules/unplugin-vue-components/dist/vite.mjs";
import { ElementPlusResolver } from "file:///Users/lxs/applicationApp/git/person/flycloud-server/flycloud-ui/flycloud-admin-ui/node_modules/.pnpm/unplugin-vue-components@0.25.2_@babel+parser@7.25.8_rollup@4.62.2_vue@3.5.12_typescript@5.3.3_/node_modules/unplugin-vue-components/dist/resolvers.mjs";
import viteCompression from "file:///Users/lxs/applicationApp/git/person/flycloud-server/flycloud-ui/flycloud-admin-ui/node_modules/.pnpm/vite-plugin-compression@0.5.1_vite@5.4.21_@types+node@20.19.43_sass@1.101.0_terser@5.48.0_/node_modules/vite-plugin-compression/dist/index.mjs";
import topLevelAwait from "file:///Users/lxs/applicationApp/git/person/flycloud-server/flycloud-ui/flycloud-admin-ui/node_modules/.pnpm/vite-plugin-top-level-await@1.4.4_rollup@4.62.2_vite@5.4.21_@types+node@20.19.43_sass@1.101.0_terser@5.48.0_/node_modules/vite-plugin-top-level-await/exports/import.mjs";
import VueI18nPlugin from "file:///Users/lxs/applicationApp/git/person/flycloud-server/flycloud-ui/flycloud-admin-ui/node_modules/.pnpm/@intlify+unplugin-vue-i18n@2.0.0_rollup@4.62.2_vue-i18n@9.10.2_vue@3.5.12_typescript@5.3.3__/node_modules/@intlify/unplugin-vue-i18n/lib/vite.mjs";
import { createSvgIconsPlugin } from "file:///Users/lxs/applicationApp/git/person/flycloud-server/flycloud-ui/flycloud-admin-ui/node_modules/.pnpm/vite-plugin-svg-icons@2.0.1_vite@5.4.21_@types+node@20.19.43_sass@1.101.0_terser@5.48.0_/node_modules/vite-plugin-svg-icons/dist/index.mjs";
import UnoCSS from "file:///Users/lxs/applicationApp/git/person/flycloud-server/flycloud-ui/flycloud-admin-ui/node_modules/.pnpm/unocss@0.58.9_postcss@8.5.16_rollup@4.62.2_vite@5.4.21_@types+node@20.19.43_sass@1.101.0_terser@5.48.0_/node_modules/unocss/dist/vite.mjs";
var __vite_injected_original_import_meta_url = "file:///Users/lxs/applicationApp/git/person/flycloud-server/flycloud-ui/flycloud-admin-ui/build/vite/index.ts";
var require2 = createRequire(__vite_injected_original_import_meta_url);
var PurgeIcons = require2("vite-plugin-purge-icons").default;
function createVitePlugins() {
  const root2 = process.cwd();
  function pathResolve(dir) {
    return resolve(root2, ".", dir);
  }
  return [
    Vue(),
    VueJsx(),
    UnoCSS(),
    progress(),
    PurgeIcons({
      content: ["index.html", "src/**/*.{vue,ts,tsx,js,jsx}"],
      iconSource: "local"
    }),
    ElementPlus({}),
    AutoImport({
      include: [
        /\.[tj]sx?$/,
        // .ts, .tsx, .js, .jsx
        /\.vue$/,
        /\.vue\?vue/,
        // .vue
        /\.md$/
        // .md
      ],
      imports: [
        "vue",
        "vue-router",
        // 可额外添加需要 autoImport 的组件
        {
          "@/hooks/web/useI18n": ["useI18n"],
          "@/hooks/web/useMessage": ["useMessage"],
          "@/hooks/web/useTable": ["useTable"],
          "@/hooks/web/useCrudSchemas": ["useCrudSchemas"],
          "@/utils/formRules": ["required"],
          "@/utils/dict": ["DICT_TYPE"]
        }
      ],
      dts: "src/types/auto-imports.d.ts",
      resolvers: [ElementPlusResolver()],
      eslintrc: {
        enabled: false,
        // Default `false`
        filepath: "./.eslintrc-auto-import.json",
        // Default `./.eslintrc-auto-import.json`
        globalsPropValue: true
        // Default `true`, (true | false | 'readonly' | 'readable' | 'writable' | 'writeable')
      }
    }),
    Components({
      // 生成自定义 `auto-components.d.ts` 全局声明
      dts: "src/types/auto-components.d.ts",
      // 自定义组件的解析器
      resolvers: [ElementPlusResolver()],
      globs: ["src/components/**/**.{vue, md}", "!src/components/DiyEditor/components/mobile/**"]
    }),
    EslintPlugin({
      cache: false,
      include: ["src/**/*.vue", "src/**/*.ts", "src/**/*.tsx"]
      // 检查的文件
    }),
    VueI18nPlugin({
      runtimeOnly: true,
      compositionOnly: true,
      strictMessage: false,
      include: [pathResolve("src/locales/*.ts")]
    }),
    createSvgIconsPlugin({
      iconDirs: [pathResolve("src/assets/svgs")],
      symbolId: "icon-[dir]-[name]",
      svgoOptions: true
    }),
    viteCompression({
      verbose: true,
      // 是否在控制台输出压缩结果
      disable: false,
      // 是否禁用
      threshold: 10240,
      // 体积大于 threshold 才会被压缩,单位 b
      algorithm: "gzip",
      // 压缩算法,可选 [ 'gzip' , 'brotliCompress' ,'deflate' , 'deflateRaw']
      ext: ".gz",
      // 生成的压缩包后缀
      deleteOriginFile: false
      //压缩后是否删除源文件
    }),
    ViteEjsPlugin(),
    topLevelAwait({
      // https://juejin.cn/post/7152191742513512485
      // The export name of top-level await promise for each chunk module
      promiseExportName: "__tla",
      // The function to generate import names of top-level await promise in each chunk module
      promiseImportName: (i) => `__tla_${i}`
    })
  ];
}

// build/vite/optimize.ts
var include = [
  "qs",
  "url",
  "vue",
  "sass",
  "mitt",
  "axios",
  "pinia",
  "dayjs",
  "qrcode",
  "unocss",
  "vue-router",
  "vue-types",
  "vue-i18n",
  "crypto-js",
  "cropperjs",
  "lodash-es",
  "nprogress",
  "web-storage-cache",
  "@iconify/iconify",
  "@vueuse/core",
  "@zxcvbn-ts/core",
  "echarts/core",
  "echarts/charts",
  "echarts/components",
  "echarts/renderers",
  "echarts-wordcloud",
  "@wangeditor/editor",
  "@wangeditor/editor-for-vue",
  "@microsoft/fetch-event-source",
  "markdown-it",
  "markmap-view",
  "markmap-lib",
  "markmap-toolbar",
  "highlight.js",
  "element-plus",
  "element-plus/es",
  "element-plus/es/locale/lang/zh-cn",
  "element-plus/es/locale/lang/en",
  "element-plus/es/components/avatar/style/css",
  "element-plus/es/components/space/style/css",
  "element-plus/es/components/backtop/style/css",
  "element-plus/es/components/form/style/css",
  "element-plus/es/components/radio-group/style/css",
  "element-plus/es/components/radio/style/css",
  "element-plus/es/components/checkbox/style/css",
  "element-plus/es/components/checkbox-group/style/css",
  "element-plus/es/components/switch/style/css",
  "element-plus/es/components/time-picker/style/css",
  "element-plus/es/components/date-picker/style/css",
  "element-plus/es/components/descriptions/style/css",
  "element-plus/es/components/descriptions-item/style/css",
  "element-plus/es/components/link/style/css",
  "element-plus/es/components/tooltip/style/css",
  "element-plus/es/components/drawer/style/css",
  "element-plus/es/components/dialog/style/css",
  "element-plus/es/components/checkbox-button/style/css",
  "element-plus/es/components/option-group/style/css",
  "element-plus/es/components/radio-button/style/css",
  "element-plus/es/components/cascader/style/css",
  "element-plus/es/components/color-picker/style/css",
  "element-plus/es/components/input-number/style/css",
  "element-plus/es/components/rate/style/css",
  "element-plus/es/components/select-v2/style/css",
  "element-plus/es/components/tree-select/style/css",
  "element-plus/es/components/slider/style/css",
  "element-plus/es/components/time-select/style/css",
  "element-plus/es/components/autocomplete/style/css",
  "element-plus/es/components/image-viewer/style/css",
  "element-plus/es/components/upload/style/css",
  "element-plus/es/components/col/style/css",
  "element-plus/es/components/form-item/style/css",
  "element-plus/es/components/alert/style/css",
  "element-plus/es/components/breadcrumb/style/css",
  "element-plus/es/components/select/style/css",
  "element-plus/es/components/input/style/css",
  "element-plus/es/components/breadcrumb-item/style/css",
  "element-plus/es/components/tag/style/css",
  "element-plus/es/components/pagination/style/css",
  "element-plus/es/components/table/style/css",
  "element-plus/es/components/table-v2/style/css",
  "element-plus/es/components/table-column/style/css",
  "element-plus/es/components/card/style/css",
  "element-plus/es/components/row/style/css",
  "element-plus/es/components/button/style/css",
  "element-plus/es/components/menu/style/css",
  "element-plus/es/components/sub-menu/style/css",
  "element-plus/es/components/menu-item/style/css",
  "element-plus/es/components/option/style/css",
  "element-plus/es/components/dropdown/style/css",
  "element-plus/es/components/dropdown-menu/style/css",
  "element-plus/es/components/dropdown-item/style/css",
  "element-plus/es/components/skeleton/style/css",
  "element-plus/es/components/skeleton/style/css",
  "element-plus/es/components/backtop/style/css",
  "element-plus/es/components/menu/style/css",
  "element-plus/es/components/sub-menu/style/css",
  "element-plus/es/components/menu-item/style/css",
  "element-plus/es/components/dropdown/style/css",
  "element-plus/es/components/tree/style/css",
  "element-plus/es/components/dropdown-menu/style/css",
  "element-plus/es/components/dropdown-item/style/css",
  "element-plus/es/components/badge/style/css",
  "element-plus/es/components/breadcrumb/style/css",
  "element-plus/es/components/breadcrumb-item/style/css",
  "element-plus/es/components/image/style/css",
  "element-plus/es/components/collapse-transition/style/css",
  "element-plus/es/components/timeline/style/css",
  "element-plus/es/components/timeline-item/style/css",
  "element-plus/es/components/collapse/style/css",
  "element-plus/es/components/collapse-item/style/css",
  "element-plus/es/components/button-group/style/css",
  "element-plus/es/components/text/style/css",
  "element-plus/es/components/segmented/style/css",
  "@element-plus/icons-vue",
  "element-plus/es/components/footer/style/css",
  "element-plus/es/components/empty/style/css"
];
var exclude = ["@iconify/json"];

// vite.config.ts
import { fileURLToPath, URL } from "node:url";
var __vite_injected_original_import_meta_url2 = "file:///Users/lxs/applicationApp/git/person/flycloud-server/flycloud-ui/flycloud-admin-ui/vite.config.ts";
var root = process.cwd();
var vite_config_default = ({ command, mode }) => {
  let env = {};
  const isBuild = command === "build";
  if (!isBuild) {
    env = loadEnv(process.argv[3] === "--mode" ? process.argv[4] : process.argv[3], root);
  } else {
    env = loadEnv(mode, root);
  }
  return {
    base: env.VITE_BASE_PATH,
    root,
    // todo 代理, 使用场景: 一部分接口使用测试服, 一部分接口使用本地服务
    server: {
      port: env.VITE_PORT,
      // 端口号
      host: "0.0.0.0",
      open: env.VITE_OPEN === "true",
      strictPort: true,
      proxy: {
        // 授权服务
        ["/flycloud-auth"]: {
          // target: env.VITE_BASE_URL + '/' + env.VITE_AUTH_SERVER,
          target: "http://localhost:8080/flycloud-auth",
          // target: 'http://101.34.33.33:8080/flycloud-auth',
          ws: false,
          changeOrigin: true,
          rewrite: (path) => path.replace(new RegExp(`^/flycloud-auth`), "")
        },
        // 系统服务
        ["/flycloud-system"]: {
          target: "http://localhost:8080/flycloud-system",
          // target: 'http://101.34.33.33:8080/flycloud-system',
          ws: false,
          changeOrigin: true,
          rewrite: (path) => path.replace(new RegExp(`^/flycloud-system`), "")
        },
        // 工作流服务
        ["/flycloud-bpm"]: {
          target: "http://localhost:8080/flycloud-bpm",
          // target: 'http://101.34.33.33:8080/flycloud-bpm',
          ws: false,
          changeOrigin: true,
          rewrite: (path) => path.replace(new RegExp(`^/flycloud-bpm`), "")
        },
        // 商城服务
        ["/flycloud-mall"]: {
          target: "http://localhost:8080/flycloud-mall",
          // target: 'http://101.34.33.33:8080/flycloud-mall',
          ws: false,
          changeOrigin: true,
          rewrite: (path) => path.replace(new RegExp(`^/flycloud-mall`), "")
        },
        // 自动生成服务
        ["/flycloud-generator"]: {
          target: "http://localhost:8080/flycloud-generator",
          // target: 'http://101.34.33.33:8080/flycloud-generator',
          ws: false,
          changeOrigin: true,
          rewrite: (path) => path.replace(new RegExp(`^/flycloud-generator`), "")
        }
      }
    },
    // 项目使用的vite插件。 单独提取到build/vite/plugin中管理
    plugins: createVitePlugins(),
    css: {
      preprocessorOptions: {
        scss: {
          additionalData: '@use "@/styles/variables.scss" as *;',
          api: "modern-compiler",
          javascriptEnabled: true
        }
      }
    },
    resolve: {
      extensions: [".mjs", ".js", ".ts", ".jsx", ".tsx", ".json", ".scss", ".css"],
      alias: [
        {
          find: "vue-i18n",
          replacement: "vue-i18n/dist/vue-i18n.cjs.js"
        },
        {
          find: "@",
          replacement: fileURLToPath(new URL("./src", __vite_injected_original_import_meta_url2))
        }
      ]
    },
    // todo 构建
    build: {
      minify: "terser",
      outDir: env.VITE_OUT_DIR || "dist",
      sourcemap: env.VITE_SOURCEMAP === "true" ? "inline" : false,
      // brotliSize: false,
      terserOptions: {
        compress: {
          drop_debugger: env.VITE_DROP_DEBUGGER === "true",
          drop_console: env.VITE_DROP_CONSOLE === "true"
        }
      },
      rollupOptions: {
        output: {
          manualChunks: {
            echarts: ["echarts"]
            // 将 echarts 单独打包，参考 https://gitee.com/yudaocode/yudao-ui-admin-vue3/issues/IAB1SX 讨论
          }
        }
      }
    },
    optimizeDeps: { include, exclude }
  };
};
export {
  vite_config_default as default
};
//# sourceMappingURL=data:application/json;base64,ewogICJ2ZXJzaW9uIjogMywKICAic291cmNlcyI6IFsidml0ZS5jb25maWcudHMiLCAiYnVpbGQvdml0ZS9pbmRleC50cyIsICJidWlsZC92aXRlL29wdGltaXplLnRzIl0sCiAgInNvdXJjZXNDb250ZW50IjogWyJjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfZGlybmFtZSA9IFwiL1VzZXJzL2x4cy9hcHBsaWNhdGlvbkFwcC9naXQvcGVyc29uL2ZseWNsb3VkLXNlcnZlci9mbHljbG91ZC11aS9mbHljbG91ZC1hZG1pbi11aVwiO2NvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9maWxlbmFtZSA9IFwiL1VzZXJzL2x4cy9hcHBsaWNhdGlvbkFwcC9naXQvcGVyc29uL2ZseWNsb3VkLXNlcnZlci9mbHljbG91ZC11aS9mbHljbG91ZC1hZG1pbi11aS92aXRlLmNvbmZpZy50c1wiO2NvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9pbXBvcnRfbWV0YV91cmwgPSBcImZpbGU6Ly8vVXNlcnMvbHhzL2FwcGxpY2F0aW9uQXBwL2dpdC9wZXJzb24vZmx5Y2xvdWQtc2VydmVyL2ZseWNsb3VkLXVpL2ZseWNsb3VkLWFkbWluLXVpL3ZpdGUuY29uZmlnLnRzXCI7Ly8gaW1wb3J0IHtyZXNvbHZlfSBmcm9tICdwYXRoJ1xuaW1wb3J0IHR5cGUge0NvbmZpZ0VudiwgVXNlckNvbmZpZ30gZnJvbSAndml0ZSdcbmltcG9ydCB7bG9hZEVudn0gZnJvbSAndml0ZSdcbmltcG9ydCB7Y3JlYXRlVml0ZVBsdWdpbnN9IGZyb20gJy4vYnVpbGQvdml0ZSdcbmltcG9ydCB7ZXhjbHVkZSwgaW5jbHVkZX0gZnJvbSBcIi4vYnVpbGQvdml0ZS9vcHRpbWl6ZVwiXG5pbXBvcnQge2ZpbGVVUkxUb1BhdGgsIFVSTH0gZnJvbSBcIm5vZGU6dXJsXCI7XG5cbi8vIFx1NUY1M1x1NTI0RFx1NjI2N1x1ODg0Q25vZGVcdTU0N0RcdTRFRTRcdTY1RjZcdTY1ODdcdTRFRjZcdTU5MzlcdTc2ODRcdTU3MzBcdTU3NDAoXHU1REU1XHU0RjVDXHU3NkVFXHU1RjU1KVxuY29uc3Qgcm9vdCA9IHByb2Nlc3MuY3dkKClcblxuLy8gXHU1NDBFXHU3QUVGXHU2NzBEXHU1MkExXG4vLyBjb25zdCBBVVRIX0JBU0VfVVJMID0gaW1wb3J0Lm1ldGEuZW52LlZJVEVfQVVUSF9TRVJWRVJcbi8vIGNvbnN0IEFVVEhfQkFTRV9VUkwgPSBpbXBvcnQubWV0YS5lbnYuVklURV9BVVRIX1NFUlZFUlxuXG4vLyBcdThERUZcdTVGODRcdTY3RTVcdTYyN0Vcbi8vIGZ1bmN0aW9uIHBhdGhSZXNvbHZlKGRpcjogc3RyaW5nKSB7XG4vLyAgICAgcmV0dXJuIHJlc29sdmUocm9vdCwgJy4nLCBkaXIpXG4vLyB9XG5cbi8vIGV4cG9ydCBkZWZhdWx0IGRlZmluZUNvbmZpZyh7XG4vLyAgIGJhc2U6IFwiLi9cIlxuLy8gfSlcblxuLy8gaHR0cHM6Ly92aXRlanMuZGV2L2NvbmZpZy9cbmV4cG9ydCBkZWZhdWx0ICh7Y29tbWFuZCwgbW9kZX06IENvbmZpZ0Vudik6IFVzZXJDb25maWcgPT4ge1xuICAgIGxldCBlbnYgPSB7fSBhcyBhbnlcbiAgICBjb25zdCBpc0J1aWxkID0gY29tbWFuZCA9PT0gJ2J1aWxkJ1xuICAgIGlmICghaXNCdWlsZCkge1xuICAgICAgICBlbnYgPSBsb2FkRW52KChwcm9jZXNzLmFyZ3ZbM10gPT09ICctLW1vZGUnID8gcHJvY2Vzcy5hcmd2WzRdIDogcHJvY2Vzcy5hcmd2WzNdKSwgcm9vdClcbiAgICB9IGVsc2Uge1xuICAgICAgICBlbnYgPSBsb2FkRW52KG1vZGUsIHJvb3QpXG4gICAgfVxuICAgIHJldHVybiB7XG5cbiAgICAgICAgYmFzZTogZW52LlZJVEVfQkFTRV9QQVRILFxuICAgICAgICByb290OiByb290LFxuXG4gICAgICAgIC8vIHRvZG8gXHU0RUUzXHU3NDA2LCBcdTRGN0ZcdTc1MjhcdTU3M0FcdTY2NkY6IFx1NEUwMFx1OTBFOFx1NTIwNlx1NjNBNVx1NTNFM1x1NEY3Rlx1NzUyOFx1NkQ0Qlx1OEJENVx1NjcwRCwgXHU0RTAwXHU5MEU4XHU1MjA2XHU2M0E1XHU1M0UzXHU0RjdGXHU3NTI4XHU2NzJDXHU1NzMwXHU2NzBEXHU1MkExXG4gICAgICAgIHNlcnZlcjoge1xuICAgICAgICAgICAgcG9ydDogZW52LlZJVEVfUE9SVCwgLy8gXHU3QUVGXHU1M0UzXHU1M0Y3XG4gICAgICAgICAgICBob3N0OiBcIjAuMC4wLjBcIixcbiAgICAgICAgICAgIG9wZW46IGVudi5WSVRFX09QRU4gPT09ICd0cnVlJyxcbiAgICAgICAgICAgIHN0cmljdFBvcnQ6IHRydWUsXG4gICAgICAgICAgICBwcm94eToge1xuXG4gICAgICAgICAgICAgIC8vIFx1NjM4OFx1Njc0M1x1NjcwRFx1NTJBMVxuICAgICAgICAgICAgICBbJy9mbHljbG91ZC1hdXRoJ106IHtcbiAgICAgICAgICAgICAgICAvLyB0YXJnZXQ6IGVudi5WSVRFX0JBU0VfVVJMICsgJy8nICsgZW52LlZJVEVfQVVUSF9TRVJWRVIsXG4gICAgICAgICAgICAgICAgdGFyZ2V0OiAnaHR0cDovL2xvY2FsaG9zdDo4MDgwL2ZseWNsb3VkLWF1dGgnLFxuICAgICAgICAgICAgICAgIC8vIHRhcmdldDogJ2h0dHA6Ly8xMDEuMzQuMzMuMzM6ODA4MC9mbHljbG91ZC1hdXRoJyxcbiAgICAgICAgICAgICAgICB3czogZmFsc2UsXG4gICAgICAgICAgICAgICAgY2hhbmdlT3JpZ2luOiB0cnVlLFxuICAgICAgICAgICAgICAgIHJld3JpdGU6IChwYXRoKSA9PiBwYXRoLnJlcGxhY2UobmV3IFJlZ0V4cChgXi9mbHljbG91ZC1hdXRoYCksICcnKSxcbiAgICAgICAgICAgICAgfSxcblxuICAgICAgICAgICAgICAvLyBcdTdDRkJcdTdFREZcdTY3MERcdTUyQTFcbiAgICAgICAgICAgICAgWycvZmx5Y2xvdWQtc3lzdGVtJ106IHtcbiAgICAgICAgICAgICAgICB0YXJnZXQ6ICdodHRwOi8vbG9jYWxob3N0OjgwODAvZmx5Y2xvdWQtc3lzdGVtJyxcbiAgICAgICAgICAgICAgICAvLyB0YXJnZXQ6ICdodHRwOi8vMTAxLjM0LjMzLjMzOjgwODAvZmx5Y2xvdWQtc3lzdGVtJyxcbiAgICAgICAgICAgICAgICB3czogZmFsc2UsXG4gICAgICAgICAgICAgICAgY2hhbmdlT3JpZ2luOiB0cnVlLFxuICAgICAgICAgICAgICAgIHJld3JpdGU6IChwYXRoKSA9PiBwYXRoLnJlcGxhY2UobmV3IFJlZ0V4cChgXi9mbHljbG91ZC1zeXN0ZW1gKSwgJycpLFxuICAgICAgICAgICAgICB9LFxuXG4gICAgICAgICAgICAgIC8vIFx1NURFNVx1NEY1Q1x1NkQ0MVx1NjcwRFx1NTJBMVxuICAgICAgICAgICAgICBbJy9mbHljbG91ZC1icG0nXToge1xuICAgICAgICAgICAgICAgIHRhcmdldDogJ2h0dHA6Ly9sb2NhbGhvc3Q6ODA4MC9mbHljbG91ZC1icG0nLFxuICAgICAgICAgICAgICAgIC8vIHRhcmdldDogJ2h0dHA6Ly8xMDEuMzQuMzMuMzM6ODA4MC9mbHljbG91ZC1icG0nLFxuICAgICAgICAgICAgICAgIHdzOiBmYWxzZSxcbiAgICAgICAgICAgICAgICBjaGFuZ2VPcmlnaW46IHRydWUsXG4gICAgICAgICAgICAgICAgcmV3cml0ZTogKHBhdGgpID0+IHBhdGgucmVwbGFjZShuZXcgUmVnRXhwKGBeL2ZseWNsb3VkLWJwbWApLCAnJyksXG4gICAgICAgICAgICAgIH0sXG5cbiAgICAgICAgICAgICAgLy8gXHU1NTQ2XHU1N0NFXHU2NzBEXHU1MkExXG4gICAgICAgICAgICAgIFsnL2ZseWNsb3VkLW1hbGwnXToge1xuICAgICAgICAgICAgICAgIHRhcmdldDogJ2h0dHA6Ly9sb2NhbGhvc3Q6ODA4MC9mbHljbG91ZC1tYWxsJyxcbiAgICAgICAgICAgICAgICAvLyB0YXJnZXQ6ICdodHRwOi8vMTAxLjM0LjMzLjMzOjgwODAvZmx5Y2xvdWQtbWFsbCcsXG4gICAgICAgICAgICAgICAgd3M6IGZhbHNlLFxuICAgICAgICAgICAgICAgIGNoYW5nZU9yaWdpbjogdHJ1ZSxcbiAgICAgICAgICAgICAgICByZXdyaXRlOiAocGF0aCkgPT4gcGF0aC5yZXBsYWNlKG5ldyBSZWdFeHAoYF4vZmx5Y2xvdWQtbWFsbGApLCAnJyksXG4gICAgICAgICAgICAgIH0sXG5cbiAgICAgICAgICAgICAgLy8gXHU4MUVBXHU1MkE4XHU3NTFGXHU2MjEwXHU2NzBEXHU1MkExXG4gICAgICAgICAgICAgIFsnL2ZseWNsb3VkLWdlbmVyYXRvciddOiB7XG4gICAgICAgICAgICAgICAgdGFyZ2V0OiAnaHR0cDovL2xvY2FsaG9zdDo4MDgwL2ZseWNsb3VkLWdlbmVyYXRvcicsXG4gICAgICAgICAgICAgICAgLy8gdGFyZ2V0OiAnaHR0cDovLzEwMS4zNC4zMy4zMzo4MDgwL2ZseWNsb3VkLWdlbmVyYXRvcicsXG4gICAgICAgICAgICAgICAgd3M6IGZhbHNlLFxuICAgICAgICAgICAgICAgIGNoYW5nZU9yaWdpbjogdHJ1ZSxcbiAgICAgICAgICAgICAgICByZXdyaXRlOiAocGF0aCkgPT4gcGF0aC5yZXBsYWNlKG5ldyBSZWdFeHAoYF4vZmx5Y2xvdWQtZ2VuZXJhdG9yYCksICcnKSxcbiAgICAgICAgICAgICAgfSxcbiAgICAgICAgICAgIH0sXG4gICAgICAgIH0sXG5cbiAgICAgICAgLy8gXHU5ODc5XHU3NkVFXHU0RjdGXHU3NTI4XHU3Njg0dml0ZVx1NjNEMlx1NEVGNlx1MzAwMiBcdTUzNTVcdTcyRUNcdTYzRDBcdTUzRDZcdTUyMzBidWlsZC92aXRlL3BsdWdpblx1NEUyRFx1N0JBMVx1NzQwNlxuICAgICAgICBwbHVnaW5zOiBjcmVhdGVWaXRlUGx1Z2lucygpLFxuICAgICAgICBjc3M6IHtcbiAgICAgICAgICBwcmVwcm9jZXNzb3JPcHRpb25zOiB7XG4gICAgICAgICAgICBzY3NzOiB7XG4gICAgICAgICAgICAgIGFkZGl0aW9uYWxEYXRhOiAnQHVzZSBcIkAvc3R5bGVzL3ZhcmlhYmxlcy5zY3NzXCIgYXMgKjsnLFxuICAgICAgICAgICAgICBhcGk6ICdtb2Rlcm4tY29tcGlsZXInLFxuICAgICAgICAgICAgICBqYXZhc2NyaXB0RW5hYmxlZDogdHJ1ZVxuICAgICAgICAgICAgfVxuICAgICAgICAgIH1cbiAgICAgICAgfSxcblxuICAgICAgICByZXNvbHZlOiB7XG4gICAgICAgICAgICBleHRlbnNpb25zOiBbJy5tanMnLCAnLmpzJywgJy50cycsICcuanN4JywgJy50c3gnLCAnLmpzb24nLCAnLnNjc3MnLCAnLmNzcyddLFxuICAgICAgICAgICAgYWxpYXM6IFtcbiAgICAgICAgICAgICAgICB7XG4gICAgICAgICAgICAgICAgICAgIGZpbmQ6ICd2dWUtaTE4bicsXG4gICAgICAgICAgICAgICAgICAgIHJlcGxhY2VtZW50OiAndnVlLWkxOG4vZGlzdC92dWUtaTE4bi5janMuanMnXG4gICAgICAgICAgICAgICAgfSxcbiAgICAgICAgICAgICAge1xuICAgICAgICAgICAgICAgIGZpbmQ6ICdAJyxcbiAgICAgICAgICAgICAgICByZXBsYWNlbWVudDogZmlsZVVSTFRvUGF0aChuZXcgVVJMKCcuL3NyYycsIGltcG9ydC5tZXRhLnVybCkpXG4gICAgICAgICAgICAgIH1cbiAgICAgICAgICAgIF1cbiAgICAgICAgfSxcblxuICAgICAgICAvLyB0b2RvIFx1Njc4NFx1NUVGQVxuICAgICAgICBidWlsZDoge1xuICAgICAgICAgICAgbWluaWZ5OiAndGVyc2VyJyxcbiAgICAgICAgICAgIG91dERpcjogZW52LlZJVEVfT1VUX0RJUiB8fCAnZGlzdCcsXG4gICAgICAgICAgICBzb3VyY2VtYXA6IGVudi5WSVRFX1NPVVJDRU1BUCA9PT0gJ3RydWUnID8gJ2lubGluZScgOiBmYWxzZSxcbiAgICAgICAgICAgIC8vIGJyb3RsaVNpemU6IGZhbHNlLFxuICAgICAgICAgICAgdGVyc2VyT3B0aW9uczoge1xuICAgICAgICAgICAgICAgIGNvbXByZXNzOiB7XG4gICAgICAgICAgICAgICAgICAgIGRyb3BfZGVidWdnZXI6IGVudi5WSVRFX0RST1BfREVCVUdHRVIgPT09ICd0cnVlJyxcbiAgICAgICAgICAgICAgICAgICAgZHJvcF9jb25zb2xlOiBlbnYuVklURV9EUk9QX0NPTlNPTEUgPT09ICd0cnVlJ1xuICAgICAgICAgICAgICAgIH1cbiAgICAgICAgICAgIH0sXG4gICAgICAgICAgICByb2xsdXBPcHRpb25zOiB7XG4gICAgICAgICAgICAgICAgb3V0cHV0OiB7XG4gICAgICAgICAgICAgICAgICAgIG1hbnVhbENodW5rczoge1xuICAgICAgICAgICAgICAgICAgICAgICAgZWNoYXJ0czogWydlY2hhcnRzJ10gLy8gXHU1QzA2IGVjaGFydHMgXHU1MzU1XHU3MkVDXHU2MjUzXHU1MzA1XHVGRjBDXHU1M0MyXHU4MDAzIGh0dHBzOi8vZ2l0ZWUuY29tL3l1ZGFvY29kZS95dWRhby11aS1hZG1pbi12dWUzL2lzc3Vlcy9JQUIxU1ggXHU4QkE4XHU4QkJBXG4gICAgICAgICAgICAgICAgICAgIH1cbiAgICAgICAgICAgICAgICB9LFxuICAgICAgICAgICAgfSxcbiAgICAgICAgfSxcbiAgICAgICAgb3B0aW1pemVEZXBzOiB7aW5jbHVkZSwgZXhjbHVkZX1cbiAgICB9XG59XG4iLCAiY29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2Rpcm5hbWUgPSBcIi9Vc2Vycy9seHMvYXBwbGljYXRpb25BcHAvZ2l0L3BlcnNvbi9mbHljbG91ZC1zZXJ2ZXIvZmx5Y2xvdWQtdWkvZmx5Y2xvdWQtYWRtaW4tdWkvYnVpbGQvdml0ZVwiO2NvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9maWxlbmFtZSA9IFwiL1VzZXJzL2x4cy9hcHBsaWNhdGlvbkFwcC9naXQvcGVyc29uL2ZseWNsb3VkLXNlcnZlci9mbHljbG91ZC11aS9mbHljbG91ZC1hZG1pbi11aS9idWlsZC92aXRlL2luZGV4LnRzXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ltcG9ydF9tZXRhX3VybCA9IFwiZmlsZTovLy9Vc2Vycy9seHMvYXBwbGljYXRpb25BcHAvZ2l0L3BlcnNvbi9mbHljbG91ZC1zZXJ2ZXIvZmx5Y2xvdWQtdWkvZmx5Y2xvdWQtYWRtaW4tdWkvYnVpbGQvdml0ZS9pbmRleC50c1wiO2ltcG9ydCB7IHJlc29sdmUgfSBmcm9tICdwYXRoJ1xuaW1wb3J0IHsgY3JlYXRlUmVxdWlyZSB9IGZyb20gJ25vZGU6bW9kdWxlJ1xuaW1wb3J0IFZ1ZSBmcm9tICdAdml0ZWpzL3BsdWdpbi12dWUnXG5pbXBvcnQgVnVlSnN4IGZyb20gJ0B2aXRlanMvcGx1Z2luLXZ1ZS1qc3gnXG5pbXBvcnQgcHJvZ3Jlc3MgZnJvbSAndml0ZS1wbHVnaW4tcHJvZ3Jlc3MnXG5pbXBvcnQgRXNsaW50UGx1Z2luIGZyb20gJ3ZpdGUtcGx1Z2luLWVzbGludCdcbmltcG9ydCB7IFZpdGVFanNQbHVnaW4gfSBmcm9tICd2aXRlLXBsdWdpbi1lanMnXG4vLyBAdHMtaWdub3JlXG5pbXBvcnQgRWxlbWVudFBsdXMgZnJvbSAndW5wbHVnaW4tZWxlbWVudC1wbHVzL3ZpdGUnXG5pbXBvcnQgQXV0b0ltcG9ydCBmcm9tICd1bnBsdWdpbi1hdXRvLWltcG9ydC92aXRlJ1xuaW1wb3J0IENvbXBvbmVudHMgZnJvbSAndW5wbHVnaW4tdnVlLWNvbXBvbmVudHMvdml0ZSdcbmltcG9ydCB7IEVsZW1lbnRQbHVzUmVzb2x2ZXIgfSBmcm9tICd1bnBsdWdpbi12dWUtY29tcG9uZW50cy9yZXNvbHZlcnMnXG5pbXBvcnQgdml0ZUNvbXByZXNzaW9uIGZyb20gJ3ZpdGUtcGx1Z2luLWNvbXByZXNzaW9uJ1xuaW1wb3J0IHRvcExldmVsQXdhaXQgZnJvbSAndml0ZS1wbHVnaW4tdG9wLWxldmVsLWF3YWl0J1xuaW1wb3J0IFZ1ZUkxOG5QbHVnaW4gZnJvbSAnQGludGxpZnkvdW5wbHVnaW4tdnVlLWkxOG4vdml0ZSdcbmltcG9ydCB7IGNyZWF0ZVN2Z0ljb25zUGx1Z2luIH0gZnJvbSAndml0ZS1wbHVnaW4tc3ZnLWljb25zJ1xuaW1wb3J0IFVub0NTUyBmcm9tICd1bm9jc3Mvdml0ZSdcblxuY29uc3QgcmVxdWlyZSA9IGNyZWF0ZVJlcXVpcmUoaW1wb3J0Lm1ldGEudXJsKVxuY29uc3QgUHVyZ2VJY29ucyA9IHJlcXVpcmUoJ3ZpdGUtcGx1Z2luLXB1cmdlLWljb25zJykuZGVmYXVsdFxuXG5leHBvcnQgZnVuY3Rpb24gY3JlYXRlVml0ZVBsdWdpbnMoKSB7XG4gIGNvbnN0IHJvb3QgPSBwcm9jZXNzLmN3ZCgpXG5cbiAgLy8gXHU4REVGXHU1Rjg0XHU2N0U1XHU2MjdFXG4gIGZ1bmN0aW9uIHBhdGhSZXNvbHZlKGRpcjogc3RyaW5nKSB7XG4gICAgcmV0dXJuIHJlc29sdmUocm9vdCwgJy4nLCBkaXIpXG4gIH1cblxuICByZXR1cm4gW1xuICAgIFZ1ZSgpLFxuICAgIFZ1ZUpzeCgpLFxuICAgIFVub0NTUygpLFxuICAgIHByb2dyZXNzKCksXG4gICAgUHVyZ2VJY29ucyh7XG4gICAgICBjb250ZW50OiBbJ2luZGV4Lmh0bWwnLCAnc3JjLyoqLyoue3Z1ZSx0cyx0c3gsanMsanN4fSddLFxuICAgICAgaWNvblNvdXJjZTogJ2xvY2FsJ1xuICAgIH0pLFxuICAgIEVsZW1lbnRQbHVzKHt9KSxcbiAgICBBdXRvSW1wb3J0KHtcbiAgICAgIGluY2x1ZGU6IFtcbiAgICAgICAgL1xcLlt0al1zeD8kLywgLy8gLnRzLCAudHN4LCAuanMsIC5qc3hcbiAgICAgICAgL1xcLnZ1ZSQvLFxuICAgICAgICAvXFwudnVlXFw/dnVlLywgLy8gLnZ1ZVxuICAgICAgICAvXFwubWQkLyAvLyAubWRcbiAgICAgIF0sXG4gICAgICBpbXBvcnRzOiBbXG4gICAgICAgICd2dWUnLFxuICAgICAgICAndnVlLXJvdXRlcicsXG4gICAgICAgIC8vIFx1NTNFRlx1OTg5RFx1NTkxNlx1NkRGQlx1NTJBMFx1OTcwMFx1ODk4MSBhdXRvSW1wb3J0IFx1NzY4NFx1N0VDNFx1NEVGNlxuICAgICAgICB7XG4gICAgICAgICAgJ0AvaG9va3Mvd2ViL3VzZUkxOG4nOiBbJ3VzZUkxOG4nXSxcbiAgICAgICAgICAnQC9ob29rcy93ZWIvdXNlTWVzc2FnZSc6IFsndXNlTWVzc2FnZSddLFxuICAgICAgICAgICdAL2hvb2tzL3dlYi91c2VUYWJsZSc6IFsndXNlVGFibGUnXSxcbiAgICAgICAgICAnQC9ob29rcy93ZWIvdXNlQ3J1ZFNjaGVtYXMnOiBbJ3VzZUNydWRTY2hlbWFzJ10sXG4gICAgICAgICAgJ0AvdXRpbHMvZm9ybVJ1bGVzJzogWydyZXF1aXJlZCddLFxuICAgICAgICAgICdAL3V0aWxzL2RpY3QnOiBbJ0RJQ1RfVFlQRSddXG4gICAgICAgIH1cbiAgICAgIF0sXG4gICAgICBkdHM6ICdzcmMvdHlwZXMvYXV0by1pbXBvcnRzLmQudHMnLFxuICAgICAgcmVzb2x2ZXJzOiBbRWxlbWVudFBsdXNSZXNvbHZlcigpXSxcbiAgICAgIGVzbGludHJjOiB7XG4gICAgICAgIGVuYWJsZWQ6IGZhbHNlLCAvLyBEZWZhdWx0IGBmYWxzZWBcbiAgICAgICAgZmlsZXBhdGg6ICcuLy5lc2xpbnRyYy1hdXRvLWltcG9ydC5qc29uJywgLy8gRGVmYXVsdCBgLi8uZXNsaW50cmMtYXV0by1pbXBvcnQuanNvbmBcbiAgICAgICAgZ2xvYmFsc1Byb3BWYWx1ZTogdHJ1ZSAvLyBEZWZhdWx0IGB0cnVlYCwgKHRydWUgfCBmYWxzZSB8ICdyZWFkb25seScgfCAncmVhZGFibGUnIHwgJ3dyaXRhYmxlJyB8ICd3cml0ZWFibGUnKVxuICAgICAgfVxuICAgIH0pLFxuICAgIENvbXBvbmVudHMoe1xuICAgICAgLy8gXHU3NTFGXHU2MjEwXHU4MUVBXHU1QjlBXHU0RTQ5IGBhdXRvLWNvbXBvbmVudHMuZC50c2AgXHU1MTY4XHU1QzQwXHU1OEYwXHU2NjBFXG4gICAgICBkdHM6ICdzcmMvdHlwZXMvYXV0by1jb21wb25lbnRzLmQudHMnLFxuICAgICAgLy8gXHU4MUVBXHU1QjlBXHU0RTQ5XHU3RUM0XHU0RUY2XHU3Njg0XHU4OUUzXHU2NzkwXHU1NjY4XG4gICAgICByZXNvbHZlcnM6IFtFbGVtZW50UGx1c1Jlc29sdmVyKCldLFxuICAgICAgZ2xvYnM6IFsnc3JjL2NvbXBvbmVudHMvKiovKioue3Z1ZSwgbWR9JywgJyFzcmMvY29tcG9uZW50cy9EaXlFZGl0b3IvY29tcG9uZW50cy9tb2JpbGUvKionXVxuICAgIH0pLFxuICAgIEVzbGludFBsdWdpbih7XG4gICAgICBjYWNoZTogZmFsc2UsXG4gICAgICBpbmNsdWRlOiBbJ3NyYy8qKi8qLnZ1ZScsICdzcmMvKiovKi50cycsICdzcmMvKiovKi50c3gnXSAvLyBcdTY4QzBcdTY3RTVcdTc2ODRcdTY1ODdcdTRFRjZcbiAgICB9KSxcbiAgICBWdWVJMThuUGx1Z2luKHtcbiAgICAgIHJ1bnRpbWVPbmx5OiB0cnVlLFxuICAgICAgY29tcG9zaXRpb25Pbmx5OiB0cnVlLFxuICAgICAgc3RyaWN0TWVzc2FnZTogZmFsc2UsXG4gICAgICBpbmNsdWRlOiBbcGF0aFJlc29sdmUoJ3NyYy9sb2NhbGVzLyoudHMnKV1cbiAgICB9KSxcbiAgICBjcmVhdGVTdmdJY29uc1BsdWdpbih7XG4gICAgICBpY29uRGlyczogW3BhdGhSZXNvbHZlKCdzcmMvYXNzZXRzL3N2Z3MnKV0sXG4gICAgICBzeW1ib2xJZDogJ2ljb24tW2Rpcl0tW25hbWVdJyxcbiAgICAgIHN2Z29PcHRpb25zOiB0cnVlXG4gICAgfSksXG4gICAgdml0ZUNvbXByZXNzaW9uKHtcbiAgICAgIHZlcmJvc2U6IHRydWUsIC8vIFx1NjYyRlx1NTQyNlx1NTcyOFx1NjNBN1x1NTIzNlx1NTNGMFx1OEY5M1x1NTFGQVx1NTM4Qlx1N0YyOVx1N0VEM1x1Njc5Q1xuICAgICAgZGlzYWJsZTogZmFsc2UsIC8vIFx1NjYyRlx1NTQyNlx1Nzk4MVx1NzUyOFxuICAgICAgdGhyZXNob2xkOiAxMDI0MCwgLy8gXHU0RjUzXHU3OUVGXHU1OTI3XHU0RThFIHRocmVzaG9sZCBcdTYyNERcdTRGMUFcdTg4QUJcdTUzOEJcdTdGMjksXHU1MzU1XHU0RjREIGJcbiAgICAgIGFsZ29yaXRobTogJ2d6aXAnLCAvLyBcdTUzOEJcdTdGMjlcdTdCOTdcdTZDRDUsXHU1M0VGXHU5MDA5IFsgJ2d6aXAnICwgJ2Jyb3RsaUNvbXByZXNzJyAsJ2RlZmxhdGUnICwgJ2RlZmxhdGVSYXcnXVxuICAgICAgZXh0OiAnLmd6JywgLy8gXHU3NTFGXHU2MjEwXHU3Njg0XHU1MzhCXHU3RjI5XHU1MzA1XHU1NDBFXHU3RjAwXG4gICAgICBkZWxldGVPcmlnaW5GaWxlOiBmYWxzZSAvL1x1NTM4Qlx1N0YyOVx1NTQwRVx1NjYyRlx1NTQyNlx1NTIyMFx1OTY2NFx1NkU5MFx1NjU4N1x1NEVGNlxuICAgIH0pLFxuICAgIFZpdGVFanNQbHVnaW4oKSxcbiAgICB0b3BMZXZlbEF3YWl0KHtcbiAgICAgIC8vIGh0dHBzOi8vanVlamluLmNuL3Bvc3QvNzE1MjE5MTc0MjUxMzUxMjQ4NVxuICAgICAgLy8gVGhlIGV4cG9ydCBuYW1lIG9mIHRvcC1sZXZlbCBhd2FpdCBwcm9taXNlIGZvciBlYWNoIGNodW5rIG1vZHVsZVxuICAgICAgcHJvbWlzZUV4cG9ydE5hbWU6ICdfX3RsYScsXG4gICAgICAvLyBUaGUgZnVuY3Rpb24gdG8gZ2VuZXJhdGUgaW1wb3J0IG5hbWVzIG9mIHRvcC1sZXZlbCBhd2FpdCBwcm9taXNlIGluIGVhY2ggY2h1bmsgbW9kdWxlXG4gICAgICBwcm9taXNlSW1wb3J0TmFtZTogKGkpID0+IGBfX3RsYV8ke2l9YFxuICAgIH0pXG4gIF1cbn1cbiIsICJjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfZGlybmFtZSA9IFwiL1VzZXJzL2x4cy9hcHBsaWNhdGlvbkFwcC9naXQvcGVyc29uL2ZseWNsb3VkLXNlcnZlci9mbHljbG91ZC11aS9mbHljbG91ZC1hZG1pbi11aS9idWlsZC92aXRlXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ZpbGVuYW1lID0gXCIvVXNlcnMvbHhzL2FwcGxpY2F0aW9uQXBwL2dpdC9wZXJzb24vZmx5Y2xvdWQtc2VydmVyL2ZseWNsb3VkLXVpL2ZseWNsb3VkLWFkbWluLXVpL2J1aWxkL3ZpdGUvb3B0aW1pemUudHNcIjtjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfaW1wb3J0X21ldGFfdXJsID0gXCJmaWxlOi8vL1VzZXJzL2x4cy9hcHBsaWNhdGlvbkFwcC9naXQvcGVyc29uL2ZseWNsb3VkLXNlcnZlci9mbHljbG91ZC11aS9mbHljbG91ZC1hZG1pbi11aS9idWlsZC92aXRlL29wdGltaXplLnRzXCI7Y29uc3QgaW5jbHVkZSA9IFtcbiAgJ3FzJyxcbiAgJ3VybCcsXG4gICd2dWUnLFxuICAnc2FzcycsXG4gICdtaXR0JyxcbiAgJ2F4aW9zJyxcbiAgJ3BpbmlhJyxcbiAgJ2RheWpzJyxcbiAgJ3FyY29kZScsXG4gICd1bm9jc3MnLFxuICAndnVlLXJvdXRlcicsXG4gICd2dWUtdHlwZXMnLFxuICAndnVlLWkxOG4nLFxuICAnY3J5cHRvLWpzJyxcbiAgJ2Nyb3BwZXJqcycsXG4gICdsb2Rhc2gtZXMnLFxuICAnbnByb2dyZXNzJyxcbiAgJ3dlYi1zdG9yYWdlLWNhY2hlJyxcbiAgJ0BpY29uaWZ5L2ljb25pZnknLFxuICAnQHZ1ZXVzZS9jb3JlJyxcbiAgJ0B6eGN2Ym4tdHMvY29yZScsXG4gICdlY2hhcnRzL2NvcmUnLFxuICAnZWNoYXJ0cy9jaGFydHMnLFxuICAnZWNoYXJ0cy9jb21wb25lbnRzJyxcbiAgJ2VjaGFydHMvcmVuZGVyZXJzJyxcbiAgJ2VjaGFydHMtd29yZGNsb3VkJyxcbiAgJ0B3YW5nZWRpdG9yL2VkaXRvcicsXG4gICdAd2FuZ2VkaXRvci9lZGl0b3ItZm9yLXZ1ZScsXG4gICdAbWljcm9zb2Z0L2ZldGNoLWV2ZW50LXNvdXJjZScsXG4gICdtYXJrZG93bi1pdCcsXG4gICdtYXJrbWFwLXZpZXcnLFxuICAnbWFya21hcC1saWInLFxuICAnbWFya21hcC10b29sYmFyJyxcbiAgJ2hpZ2hsaWdodC5qcycsXG4gICdlbGVtZW50LXBsdXMnLFxuICAnZWxlbWVudC1wbHVzL2VzJyxcbiAgJ2VsZW1lbnQtcGx1cy9lcy9sb2NhbGUvbGFuZy96aC1jbicsXG4gICdlbGVtZW50LXBsdXMvZXMvbG9jYWxlL2xhbmcvZW4nLFxuICAnZWxlbWVudC1wbHVzL2VzL2NvbXBvbmVudHMvYXZhdGFyL3N0eWxlL2NzcycsXG4gICdlbGVtZW50LXBsdXMvZXMvY29tcG9uZW50cy9zcGFjZS9zdHlsZS9jc3MnLFxuICAnZWxlbWVudC1wbHVzL2VzL2NvbXBvbmVudHMvYmFja3RvcC9zdHlsZS9jc3MnLFxuICAnZWxlbWVudC1wbHVzL2VzL2NvbXBvbmVudHMvZm9ybS9zdHlsZS9jc3MnLFxuICAnZWxlbWVudC1wbHVzL2VzL2NvbXBvbmVudHMvcmFkaW8tZ3JvdXAvc3R5bGUvY3NzJyxcbiAgJ2VsZW1lbnQtcGx1cy9lcy9jb21wb25lbnRzL3JhZGlvL3N0eWxlL2NzcycsXG4gICdlbGVtZW50LXBsdXMvZXMvY29tcG9uZW50cy9jaGVja2JveC9zdHlsZS9jc3MnLFxuICAnZWxlbWVudC1wbHVzL2VzL2NvbXBvbmVudHMvY2hlY2tib3gtZ3JvdXAvc3R5bGUvY3NzJyxcbiAgJ2VsZW1lbnQtcGx1cy9lcy9jb21wb25lbnRzL3N3aXRjaC9zdHlsZS9jc3MnLFxuICAnZWxlbWVudC1wbHVzL2VzL2NvbXBvbmVudHMvdGltZS1waWNrZXIvc3R5bGUvY3NzJyxcbiAgJ2VsZW1lbnQtcGx1cy9lcy9jb21wb25lbnRzL2RhdGUtcGlja2VyL3N0eWxlL2NzcycsXG4gICdlbGVtZW50LXBsdXMvZXMvY29tcG9uZW50cy9kZXNjcmlwdGlvbnMvc3R5bGUvY3NzJyxcbiAgJ2VsZW1lbnQtcGx1cy9lcy9jb21wb25lbnRzL2Rlc2NyaXB0aW9ucy1pdGVtL3N0eWxlL2NzcycsXG4gICdlbGVtZW50LXBsdXMvZXMvY29tcG9uZW50cy9saW5rL3N0eWxlL2NzcycsXG4gICdlbGVtZW50LXBsdXMvZXMvY29tcG9uZW50cy90b29sdGlwL3N0eWxlL2NzcycsXG4gICdlbGVtZW50LXBsdXMvZXMvY29tcG9uZW50cy9kcmF3ZXIvc3R5bGUvY3NzJyxcbiAgJ2VsZW1lbnQtcGx1cy9lcy9jb21wb25lbnRzL2RpYWxvZy9zdHlsZS9jc3MnLFxuICAnZWxlbWVudC1wbHVzL2VzL2NvbXBvbmVudHMvY2hlY2tib3gtYnV0dG9uL3N0eWxlL2NzcycsXG4gICdlbGVtZW50LXBsdXMvZXMvY29tcG9uZW50cy9vcHRpb24tZ3JvdXAvc3R5bGUvY3NzJyxcbiAgJ2VsZW1lbnQtcGx1cy9lcy9jb21wb25lbnRzL3JhZGlvLWJ1dHRvbi9zdHlsZS9jc3MnLFxuICAnZWxlbWVudC1wbHVzL2VzL2NvbXBvbmVudHMvY2FzY2FkZXIvc3R5bGUvY3NzJyxcbiAgJ2VsZW1lbnQtcGx1cy9lcy9jb21wb25lbnRzL2NvbG9yLXBpY2tlci9zdHlsZS9jc3MnLFxuICAnZWxlbWVudC1wbHVzL2VzL2NvbXBvbmVudHMvaW5wdXQtbnVtYmVyL3N0eWxlL2NzcycsXG4gICdlbGVtZW50LXBsdXMvZXMvY29tcG9uZW50cy9yYXRlL3N0eWxlL2NzcycsXG4gICdlbGVtZW50LXBsdXMvZXMvY29tcG9uZW50cy9zZWxlY3QtdjIvc3R5bGUvY3NzJyxcbiAgJ2VsZW1lbnQtcGx1cy9lcy9jb21wb25lbnRzL3RyZWUtc2VsZWN0L3N0eWxlL2NzcycsXG4gICdlbGVtZW50LXBsdXMvZXMvY29tcG9uZW50cy9zbGlkZXIvc3R5bGUvY3NzJyxcbiAgJ2VsZW1lbnQtcGx1cy9lcy9jb21wb25lbnRzL3RpbWUtc2VsZWN0L3N0eWxlL2NzcycsXG4gICdlbGVtZW50LXBsdXMvZXMvY29tcG9uZW50cy9hdXRvY29tcGxldGUvc3R5bGUvY3NzJyxcbiAgJ2VsZW1lbnQtcGx1cy9lcy9jb21wb25lbnRzL2ltYWdlLXZpZXdlci9zdHlsZS9jc3MnLFxuICAnZWxlbWVudC1wbHVzL2VzL2NvbXBvbmVudHMvdXBsb2FkL3N0eWxlL2NzcycsXG4gICdlbGVtZW50LXBsdXMvZXMvY29tcG9uZW50cy9jb2wvc3R5bGUvY3NzJyxcbiAgJ2VsZW1lbnQtcGx1cy9lcy9jb21wb25lbnRzL2Zvcm0taXRlbS9zdHlsZS9jc3MnLFxuICAnZWxlbWVudC1wbHVzL2VzL2NvbXBvbmVudHMvYWxlcnQvc3R5bGUvY3NzJyxcbiAgJ2VsZW1lbnQtcGx1cy9lcy9jb21wb25lbnRzL2JyZWFkY3J1bWIvc3R5bGUvY3NzJyxcbiAgJ2VsZW1lbnQtcGx1cy9lcy9jb21wb25lbnRzL3NlbGVjdC9zdHlsZS9jc3MnLFxuICAnZWxlbWVudC1wbHVzL2VzL2NvbXBvbmVudHMvaW5wdXQvc3R5bGUvY3NzJyxcbiAgJ2VsZW1lbnQtcGx1cy9lcy9jb21wb25lbnRzL2JyZWFkY3J1bWItaXRlbS9zdHlsZS9jc3MnLFxuICAnZWxlbWVudC1wbHVzL2VzL2NvbXBvbmVudHMvdGFnL3N0eWxlL2NzcycsXG4gICdlbGVtZW50LXBsdXMvZXMvY29tcG9uZW50cy9wYWdpbmF0aW9uL3N0eWxlL2NzcycsXG4gICdlbGVtZW50LXBsdXMvZXMvY29tcG9uZW50cy90YWJsZS9zdHlsZS9jc3MnLFxuICAnZWxlbWVudC1wbHVzL2VzL2NvbXBvbmVudHMvdGFibGUtdjIvc3R5bGUvY3NzJyxcbiAgJ2VsZW1lbnQtcGx1cy9lcy9jb21wb25lbnRzL3RhYmxlLWNvbHVtbi9zdHlsZS9jc3MnLFxuICAnZWxlbWVudC1wbHVzL2VzL2NvbXBvbmVudHMvY2FyZC9zdHlsZS9jc3MnLFxuICAnZWxlbWVudC1wbHVzL2VzL2NvbXBvbmVudHMvcm93L3N0eWxlL2NzcycsXG4gICdlbGVtZW50LXBsdXMvZXMvY29tcG9uZW50cy9idXR0b24vc3R5bGUvY3NzJyxcbiAgJ2VsZW1lbnQtcGx1cy9lcy9jb21wb25lbnRzL21lbnUvc3R5bGUvY3NzJyxcbiAgJ2VsZW1lbnQtcGx1cy9lcy9jb21wb25lbnRzL3N1Yi1tZW51L3N0eWxlL2NzcycsXG4gICdlbGVtZW50LXBsdXMvZXMvY29tcG9uZW50cy9tZW51LWl0ZW0vc3R5bGUvY3NzJyxcbiAgJ2VsZW1lbnQtcGx1cy9lcy9jb21wb25lbnRzL29wdGlvbi9zdHlsZS9jc3MnLFxuICAnZWxlbWVudC1wbHVzL2VzL2NvbXBvbmVudHMvZHJvcGRvd24vc3R5bGUvY3NzJyxcbiAgJ2VsZW1lbnQtcGx1cy9lcy9jb21wb25lbnRzL2Ryb3Bkb3duLW1lbnUvc3R5bGUvY3NzJyxcbiAgJ2VsZW1lbnQtcGx1cy9lcy9jb21wb25lbnRzL2Ryb3Bkb3duLWl0ZW0vc3R5bGUvY3NzJyxcbiAgJ2VsZW1lbnQtcGx1cy9lcy9jb21wb25lbnRzL3NrZWxldG9uL3N0eWxlL2NzcycsXG4gICdlbGVtZW50LXBsdXMvZXMvY29tcG9uZW50cy9za2VsZXRvbi9zdHlsZS9jc3MnLFxuICAnZWxlbWVudC1wbHVzL2VzL2NvbXBvbmVudHMvYmFja3RvcC9zdHlsZS9jc3MnLFxuICAnZWxlbWVudC1wbHVzL2VzL2NvbXBvbmVudHMvbWVudS9zdHlsZS9jc3MnLFxuICAnZWxlbWVudC1wbHVzL2VzL2NvbXBvbmVudHMvc3ViLW1lbnUvc3R5bGUvY3NzJyxcbiAgJ2VsZW1lbnQtcGx1cy9lcy9jb21wb25lbnRzL21lbnUtaXRlbS9zdHlsZS9jc3MnLFxuICAnZWxlbWVudC1wbHVzL2VzL2NvbXBvbmVudHMvZHJvcGRvd24vc3R5bGUvY3NzJyxcbiAgJ2VsZW1lbnQtcGx1cy9lcy9jb21wb25lbnRzL3RyZWUvc3R5bGUvY3NzJyxcbiAgJ2VsZW1lbnQtcGx1cy9lcy9jb21wb25lbnRzL2Ryb3Bkb3duLW1lbnUvc3R5bGUvY3NzJyxcbiAgJ2VsZW1lbnQtcGx1cy9lcy9jb21wb25lbnRzL2Ryb3Bkb3duLWl0ZW0vc3R5bGUvY3NzJyxcbiAgJ2VsZW1lbnQtcGx1cy9lcy9jb21wb25lbnRzL2JhZGdlL3N0eWxlL2NzcycsXG4gICdlbGVtZW50LXBsdXMvZXMvY29tcG9uZW50cy9icmVhZGNydW1iL3N0eWxlL2NzcycsXG4gICdlbGVtZW50LXBsdXMvZXMvY29tcG9uZW50cy9icmVhZGNydW1iLWl0ZW0vc3R5bGUvY3NzJyxcbiAgJ2VsZW1lbnQtcGx1cy9lcy9jb21wb25lbnRzL2ltYWdlL3N0eWxlL2NzcycsXG4gICdlbGVtZW50LXBsdXMvZXMvY29tcG9uZW50cy9jb2xsYXBzZS10cmFuc2l0aW9uL3N0eWxlL2NzcycsXG4gICdlbGVtZW50LXBsdXMvZXMvY29tcG9uZW50cy90aW1lbGluZS9zdHlsZS9jc3MnLFxuICAnZWxlbWVudC1wbHVzL2VzL2NvbXBvbmVudHMvdGltZWxpbmUtaXRlbS9zdHlsZS9jc3MnLFxuICAnZWxlbWVudC1wbHVzL2VzL2NvbXBvbmVudHMvY29sbGFwc2Uvc3R5bGUvY3NzJyxcbiAgJ2VsZW1lbnQtcGx1cy9lcy9jb21wb25lbnRzL2NvbGxhcHNlLWl0ZW0vc3R5bGUvY3NzJyxcbiAgJ2VsZW1lbnQtcGx1cy9lcy9jb21wb25lbnRzL2J1dHRvbi1ncm91cC9zdHlsZS9jc3MnLFxuICAnZWxlbWVudC1wbHVzL2VzL2NvbXBvbmVudHMvdGV4dC9zdHlsZS9jc3MnLFxuICAnZWxlbWVudC1wbHVzL2VzL2NvbXBvbmVudHMvc2VnbWVudGVkL3N0eWxlL2NzcycsXG4gICdAZWxlbWVudC1wbHVzL2ljb25zLXZ1ZScsXG4gICdlbGVtZW50LXBsdXMvZXMvY29tcG9uZW50cy9mb290ZXIvc3R5bGUvY3NzJyxcbiAgJ2VsZW1lbnQtcGx1cy9lcy9jb21wb25lbnRzL2VtcHR5L3N0eWxlL2Nzcydcbl1cblxuY29uc3QgZXhjbHVkZSA9IFsnQGljb25pZnkvanNvbiddXG5cbmV4cG9ydCB7IGluY2x1ZGUsIGV4Y2x1ZGUgfVxuIl0sCiAgIm1hcHBpbmdzIjogIjtBQUVBLFNBQVEsZUFBYzs7O0FDRnVhLFNBQVMsZUFBZTtBQUNyZCxTQUFTLHFCQUFxQjtBQUM5QixPQUFPLFNBQVM7QUFDaEIsT0FBTyxZQUFZO0FBQ25CLE9BQU8sY0FBYztBQUNyQixPQUFPLGtCQUFrQjtBQUN6QixTQUFTLHFCQUFxQjtBQUU5QixPQUFPLGlCQUFpQjtBQUN4QixPQUFPLGdCQUFnQjtBQUN2QixPQUFPLGdCQUFnQjtBQUN2QixTQUFTLDJCQUEyQjtBQUNwQyxPQUFPLHFCQUFxQjtBQUM1QixPQUFPLG1CQUFtQjtBQUMxQixPQUFPLG1CQUFtQjtBQUMxQixTQUFTLDRCQUE0QjtBQUNyQyxPQUFPLFlBQVk7QUFoQnlRLElBQU0sMkNBQTJDO0FBa0I3VSxJQUFNQSxXQUFV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QUEsRUFDQTtBQUFBLEVBQ0E7QUFBQSxFQUNBO0FBQUEsRUFDQTtBQUFBLEVBQ0E7QUFBQSxFQUNBO0FBQUEsRUFDQTtBQUFBLEVBQ0E7QUFBQSxFQUNBO0FBQUEsRUFDQTtBQUFBLEVBQ0E7QUFBQSxFQUNBO0FBQUEsRUFDQTtBQUFBLEVBQ0E7QUFBQSxFQUNBO0FBQUEsRUFDQTtBQUFBLEVBQ0E7QUFBQSxFQUNBO0FBQUEsRUFDQTtBQUFBLEVBQ0E7QUFBQSxFQUNBO0FBQUEsRUFDQTtBQUFBLEVBQ0E7QUFBQSxFQUNBO0FBQUEsRUFDQTtBQUFBLEVBQ0E7QUFBQSxFQUNBO0FBQUEsRUFDQTtBQUFBLEVBQ0E7QUFBQSxFQUNBO0FBQUEsRUFDQTtBQUFBLEVBQ0E7QUFBQSxFQUNBO0FBQUEsRUFDQTtBQUFBLEVBQ0E7QUFBQSxFQUNBO0FBQUEsRUFDQTtBQUFBLEVBQ0E7QUFBQSxFQUNBO0FBQUEsRUFDQTtBQUFBLEVBQ0E7QUFBQSxFQUNBO0FBQUEsRUFDQTtBQUFBLEVBQ0E7QUFBQSxFQUNBO0FBQUEsRUFDQTtBQUFBLEVBQ0E7QUFBQSxFQUNBO0FBQUEsRUFDQTtBQUFBLEVBQ0E7QUFBQSxFQUNBO0FBQUEsRUFDQTtBQUFBLEVBQ0E7QUFBQSxFQUNBO0FBQUEsRUFDQTtBQUFBLEVBQ0E7QUFBQSxFQUNBO0FBQUEsRUFDQTtBQUFBLEVBQ0E7QUFBQSxFQUNBO0FBQUEsRUFDQTtBQUFBLEVBQ0E7QUFBQSxFQUNBO0FBQUEsRUFDQTtBQUFBLEVBQ0E7QUFBQSxFQUNBO0FBQUEsRUFDQTtBQUFBLEVBQ0E7QUFBQSxFQUNBO0FBQUEsRUFDQTtBQUFBLEVBQ0E7QUFBQSxFQUNBO0FBQUEsRUFDQTtBQUFBLEVBQ0E7QUFBQSxFQUNBO0FBQUEsRUFDQTtBQUFBLEVBQ0E7QUFBQSxFQUNBO0FBQUEsRUFDQTtBQUFBLEVBQ0E7QUFBQSxFQUNBO0FBQUEsRUFDQTtBQUFBLEVBQ0E7QUFBQSxFQUNBO0FBQUEsRUFDQTtBQUFBLEVBQ0E7QUFBQSxFQUNBO0FBQUEsRUFDQTtBQUFBLEVBQ0E7QUFBQSxFQUNBO0FBQUEsRUFDQTtBQUNGO0FBRUEsSUFBTSxVQUFVLENBQUMsZUFBZTs7O0FGbEhoQyxTQUFRLGVBQWUsV0FBVTtBQUwyTyxJQUFNQyw0Q0FBMkM7QUFRN1QsSUFBTSxPQUFPLFFBQVEsSUFBSTtBQWdCekIsSUFBTyxzQkFBUSxDQUFDLEVBQUMsU0FBUyxLQUFJLE1BQTZCO0FBQ3ZELE1BQUksTUFBTSxDQUFDO0FBQ1gsUUFBTSxVQUFVLFlBQVk7QUFDNUIsTUFBSSxDQUFDLFNBQVM7QUFDVixVQUFNLFFBQVMsUUFBUSxLQUFLLENBQUMsTUFBTSxXQUFXLFFBQVEsS0FBSyxDQUFDLElBQUksUUFBUSxLQUFLLENBQUMsR0FBSSxJQUFJO0FBQUEsRUFDMUYsT0FBTztBQUNILFVBQU0sUUFBUSxNQUFNLElBQUk7QUFBQSxFQUM1QjtBQUNBLFNBQU87QUFBQSxJQUVILE1BQU0sSUFBSTtBQUFBLElBQ1Y7QUFBQTtBQUFBLElBR0EsUUFBUTtBQUFBLE1BQ0osTUFBTSxJQUFJO0FBQUE7QUFBQSxNQUNWLE1BQU07QUFBQSxNQUNOLE1BQU0sSUFBSSxjQUFjO0FBQUEsTUFDeEIsWUFBWTtBQUFBLE1BQ1osT0FBTztBQUFBO0FBQUEsUUFHTCxDQUFDLGdCQUFnQixHQUFHO0FBQUE7QUFBQSxVQUVsQixRQUFRO0FBQUE7QUFBQSxVQUVSLElBQUk7QUFBQSxVQUNKLGNBQWM7QUFBQSxVQUNkLFNBQVMsQ0FBQyxTQUFTLEtBQUssUUFBUSxJQUFJLE9BQU8saUJBQWlCLEdBQUcsRUFBRTtBQUFBLFFBQ25FO0FBQUE7QUFBQSxRQUdBLENBQUMsa0JBQWtCLEdBQUc7QUFBQSxVQUNwQixRQUFRO0FBQUE7QUFBQSxVQUVSLElBQUk7QUFBQSxVQUNKLGNBQWM7QUFBQSxVQUNkLFNBQVMsQ0FBQyxTQUFTLEtBQUssUUFBUSxJQUFJLE9BQU8sbUJBQW1CLEdBQUcsRUFBRTtBQUFBLFFBQ3JFO0FBQUE7QUFBQSxRQUdBLENBQUMsZUFBZSxHQUFHO0FBQUEsVUFDakIsUUFBUTtBQUFBO0FBQUEsVUFFUixJQUFJO0FBQUEsVUFDSixjQUFjO0FBQUEsVUFDZCxTQUFTLENBQUMsU0FBUyxLQUFLLFFBQVEsSUFBSSxPQUFPLGdCQUFnQixHQUFHLEVBQUU7QUFBQSxRQUNsRTtBQUFBO0FBQUEsUUFHQSxDQUFDLGdCQUFnQixHQUFHO0FBQUEsVUFDbEIsUUFBUTtBQUFBO0FBQUEsVUFFUixJQUFJO0FBQUEsVUFDSixjQUFjO0FBQUEsVUFDZCxTQUFTLENBQUMsU0FBUyxLQUFLLFFBQVEsSUFBSSxPQUFPLGlCQUFpQixHQUFHLEVBQUU7QUFBQSxRQUNuRTtBQUFBO0FBQUEsUUFHQSxDQUFDLHFCQUFxQixHQUFHO0FBQUEsVUFDdkIsUUFBUTtBQUFBO0FBQUEsVUFFUixJQUFJO0FBQUEsVUFDSixjQUFjO0FBQUEsVUFDZCxTQUFTLENBQUMsU0FBUyxLQUFLLFFBQVEsSUFBSSxPQUFPLHNCQUFzQixHQUFHLEVBQUU7QUFBQSxRQUN4RTtBQUFBLE1BQ0Y7QUFBQSxJQUNKO0FBQUE7QUFBQSxJQUdBLFNBQVMsa0JBQWtCO0FBQUEsSUFDM0IsS0FBSztBQUFBLE1BQ0gscUJBQXFCO0FBQUEsUUFDbkIsTUFBTTtBQUFBLFVBQ0osZ0JBQWdCO0FBQUEsVUFDaEIsS0FBSztBQUFBLFVBQ0wsbUJBQW1CO0FBQUEsUUFDckI7QUFBQSxNQUNGO0FBQUEsSUFDRjtBQUFBLElBRUEsU0FBUztBQUFBLE1BQ0wsWUFBWSxDQUFDLFFBQVEsT0FBTyxPQUFPLFFBQVEsUUFBUSxTQUFTLFNBQVMsTUFBTTtBQUFBLE1BQzNFLE9BQU87QUFBQSxRQUNIO0FBQUEsVUFDSSxNQUFNO0FBQUEsVUFDTixhQUFhO0FBQUEsUUFDakI7QUFBQSxRQUNGO0FBQUEsVUFDRSxNQUFNO0FBQUEsVUFDTixhQUFhLGNBQWMsSUFBSSxJQUFJLFNBQVNBLHlDQUFlLENBQUM7QUFBQSxRQUM5RDtBQUFBLE1BQ0Y7QUFBQSxJQUNKO0FBQUE7QUFBQSxJQUdBLE9BQU87QUFBQSxNQUNILFFBQVE7QUFBQSxNQUNSLFFBQVEsSUFBSSxnQkFBZ0I7QUFBQSxNQUM1QixXQUFXLElBQUksbUJBQW1CLFNBQVMsV0FBVztBQUFBO0FBQUEsTUFFdEQsZUFBZTtBQUFBLFFBQ1gsVUFBVTtBQUFBLFVBQ04sZUFBZSxJQUFJLHVCQUF1QjtBQUFBLFVBQzFDLGNBQWMsSUFBSSxzQkFBc0I7QUFBQSxRQUM1QztBQUFBLE1BQ0o7QUFBQSxNQUNBLGVBQWU7QUFBQSxRQUNYLFFBQVE7QUFBQSxVQUNKLGNBQWM7QUFBQSxZQUNWLFNBQVMsQ0FBQyxTQUFTO0FBQUE7QUFBQSxVQUN2QjtBQUFBLFFBQ0o7QUFBQSxNQUNKO0FBQUEsSUFDSjtBQUFBLElBQ0EsY0FBYyxFQUFDLFNBQVMsUUFBTztBQUFBLEVBQ25DO0FBQ0o7IiwKICAibmFtZXMiOiBbInJlcXVpcmUiLCAicm9vdCIsICJfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfaW1wb3J0X21ldGFfdXJsIl0KfQo=
