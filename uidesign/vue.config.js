const { defineConfig } = require('@vue/cli-service')
const loader = require('sass-loader')
module.exports = defineConfig({
  transpileDependencies: true,
  css: {
    loaderOptions: {
      sass: {
        /**
         * 注意：在 sass-loader v8 中，这个选项名是 "prependData"
         */
        //  prependData: `
        // @import "@/styles/motion-ui.scss";
        // @import "@/styles/_settings.scss";
        // @import "@/styles/_classes.scss";
        // @import "@/styles/transitions";
        // `
      }
    }
},

    // loader: 'sass-loader',
    // options: {
    //   data: `
    //     @import "@/styles/motion-ui.scss";
    //     @import "@/styles/_settings.scss";
    //     @import "@/styles/_classes.scss";
    //     @import "@/styles/transitions";
    //   `
    // }



})


