"use strict";
const common_vendor = require("../../common/vendor.js");
const _sfc_main = /* @__PURE__ */ common_vendor.defineComponent(new UTSJSONObject({
  __name: "analyze",
  setup(__props) {
    const tabs = ["日", "周", "月", "年", "自定义"];
    const activeTab = common_vendor.ref("周");
    return (_ctx = null, _cache = null) => {
      const __returned__ = {
        a: common_vendor.f(tabs, (tab = null, k0 = null, i0 = null) => {
          return {
            a: common_vendor.t(tab),
            b: tab,
            c: common_vendor.n(activeTab.value === tab ? "active" : ""),
            d: common_vendor.o(($event = null) => {
              return activeTab.value = tab;
            }, tab)
          };
        }),
        b: common_vendor.sei(common_vendor.gei(_ctx, ""), "view")
      };
      return __returned__;
    };
  }
}));
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-c1d51998"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/analyze/analyze.js.map
