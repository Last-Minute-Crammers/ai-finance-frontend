"use strict";
Object.defineProperty(exports, Symbol.toStringTag, { value: "Module" });
const common_vendor = require("./common/vendor.js");
if (!Math) {
  "./pages/index/index.js";
  "./pages/transaction/transaction.js";
  "./pages/pet/pet.js";
  "./pages/AIchat/AIchat.js";
  "./pages/report/report.js";
  "./pages/analyze/analyze.js";
}
const _sfc_main = common_vendor.defineComponent(new UTSJSONObject({
  onLaunch() {
    common_vendor.index.__f__("log", "at App.uvue:10", "App 启动");
  },
  onShow() {
    common_vendor.index.__f__("log", "at App.uvue:13", "App 显示");
  },
  onHide() {
    common_vendor.index.__f__("log", "at App.uvue:16", "App 隐藏");
  }
}));
if (!Array) {
  const _component_router_view = common_vendor.resolveComponent("router-view");
  _component_router_view();
}
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: common_vendor.sei(common_vendor.gei(_ctx, ""), "view")
  };
}
const App = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render]]);
function createApp() {
  const app = common_vendor.createSSRApp(App);
  return {
    app
  };
}
const addTransaction = (data) => {
  return common_vendor.index.request({ url: "/api/transaction", method: "POST", data });
};
createApp().app.mount("#app");
exports.addTransaction = addTransaction;
exports.createApp = createApp;
//# sourceMappingURL=../.sourcemap/mp-weixin/app.js.map
