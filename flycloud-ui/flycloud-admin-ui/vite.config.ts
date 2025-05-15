import {resolve} from 'path'
import type {ConfigEnv, UserConfig} from 'vite'
import {loadEnv} from 'vite'
import {createVitePlugins} from './build/vite'
import {exclude, include} from "./build/vite/optimize"

// 当前执行node命令时文件夹的地址(工作目录)
const root = process.cwd()

// 后端服务
// const AUTH_BASE_URL = import.meta.env.VITE_AUTH_SERVER
// const AUTH_BASE_URL = import.meta.env.VITE_AUTH_SERVER

// 路径查找
function pathResolve(dir: string) {
    return resolve(root, '.', dir)
}

// https://vitejs.dev/config/
export default ({command, mode}: ConfigEnv): UserConfig => {
    let env = {} as any
    const isBuild = command === 'build'
    if (!isBuild) {
        env = loadEnv((process.argv[3] === '--mode' ? process.argv[4] : process.argv[3]), root)
    } else {
        env = loadEnv(mode, root)
    }
    return {

        base: env.VITE_BASE_PATH,
        root: root,

        // todo 代理, 使用场景: 一部分接口使用测试服, 一部分接口使用本地服务
        server: {
            port: env.VITE_PORT, // 端口号
            host: "0.0.0.0",
            open: env.VITE_OPEN === 'true',
            proxy: {

              // 授权服务
              ['/flycloud-auth']: {
                // target: env.VITE_BASE_URL + '/' + env.VITE_AUTH_SERVER,
                // target: 'http://localhost:8080/flycloud-auth',
                target: 'http://101.34.33.33:8080/flycloud-auth',
                ws: false,
                changeOrigin: true,
                rewrite: (path) => path.replace(new RegExp(`^/flycloud-auth`), ''),
              },

              // 系统服务
              ['/flycloud-system']: {
                // target: 'http://localhost:8080/flycloud-system',
                target: 'http://101.34.33.33:8080/flycloud-system',
                ws: false,
                changeOrigin: true,
                rewrite: (path) => path.replace(new RegExp(`^/flycloud-system`), ''),
              },
              // 系统服务
              ['/bpm/manager/flycloud-system']: {
                // target: 'http://localhost:8080/flycloud-system',
                target: 'http://101.34.33.33:8080/flycloud-system',
                ws: false,
                changeOrigin: true,
                rewrite: (path) => path.replace(new RegExp(`^/bpm/manager/flycloud-system`), ''),
              },

              // 工作流服务
              ['/flycloud-bpm']: {
                // target: 'http://localhost:8080/flycloud-bpm',
                target: 'http://101.34.33.33:8080/flycloud-bpm',
                ws: false,
                changeOrigin: true,
                rewrite: (path) => path.replace(new RegExp(`^/flycloud-bpm`), ''),
              },

              // 工作流服务
              ['/bpm/manager/flycloud-bpm']: {
                // target: 'http://localhost:8080/flycloud-bpm',
                target: 'http://101.34.33.33:8080/flycloud-bpm',
                ws: false,
                changeOrigin: true,
                rewrite: (path) => path.replace(new RegExp(`^/bpm/manager/flycloud-bpm`), ''),
              },
            },
        },

        // 项目使用的vite插件。 单独提取到build/vite/plugin中管理
        plugins: createVitePlugins(),
        css: {
          preprocessorOptions: {
            scss: {
              additionalData: '@use "@/styles/variables.scss" as *;',
              javascriptEnabled: true
            }
          }
        },

        resolve: {
            extensions: ['.mjs', '.js', '.ts', '.jsx', '.tsx', '.json', '.scss', '.css'],
            alias: [
                {
                    find: 'vue-i18n',
                    replacement: 'vue-i18n/dist/vue-i18n.cjs.js'
                },
                {
                    find: /\@\//,
                    replacement: `${pathResolve('src')}/`
                }
            ]
        },

        // todo 构建
        build: {
            minify: 'terser',
            outDir: env.VITE_OUT_DIR || 'dist',
            sourcemap: env.VITE_SOURCEMAP === 'true' ? 'inline' : false,
            // brotliSize: false,
            terserOptions: {
                compress: {
                    drop_debugger: env.VITE_DROP_DEBUGGER === 'true',
                    drop_console: env.VITE_DROP_CONSOLE === 'true'
                }
            },
            rollupOptions: {
                output: {
                    manualChunks: {
                        echarts: ['echarts'] // 将 echarts 单独打包，参考 https://gitee.com/yudaocode/yudao-ui-admin-vue3/issues/IAB1SX 讨论
                    }
                },
            },
        },
        optimizeDeps: {include, exclude}
    }
}
