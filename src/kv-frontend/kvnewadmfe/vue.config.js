const { defineConfig } = require('@vue/cli-service')
const fs = require('fs');
const path = require('path');

module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: process.env.PORT,
    historyApiFallback: true,
    allowedHosts: "all",
    https: {
      key: fs.readFileSync(path.join(__dirname, './cert/private.pem')),
      cert: fs.readFileSync(path.join(__dirname, './cert/fullchain.crt'))
    }
  }
})
