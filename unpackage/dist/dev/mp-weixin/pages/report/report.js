"use strict";
const common_vendor = require("../../common/vendor.js");
const _sfc_main = /* @__PURE__ */ common_vendor.defineComponent(new UTSJSONObject({
  __name: "report",
  setup(__props) {
    const tabs = ["周报", "月报", "年报"];
    const activeTab = common_vendor.ref("月报");
    const reportData = [
      new UTSJSONObject({ label: "总收入", value: "¥8,500", trend: "↑5% 较上月", trendType: "green" }),
      new UTSJSONObject({ label: "总支出", value: "¥4,200", trend: "↓12% 较上月", trendType: "red" }),
      new UTSJSONObject({ label: "储蓄率", value: "50.6%", trend: "↑3% 较上月", trendType: "green" }),
      new UTSJSONObject({ label: "预算执行", value: "82%", trend: "↓8% 较上月", trendType: "red" })
    ];
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
        b: common_vendor.f(reportData, (item = null, k0 = null, i0 = null) => {
          return {
            a: common_vendor.t(item.label),
            b: common_vendor.t(item.value),
            c: common_vendor.t(item.trend),
            d: common_vendor.n(item.trendType),
            e: item.label
          };
        }),
        c: common_vendor.sei(common_vendor.gei(_ctx, ""), "view")
      };
      return __returned__;
    };
  }
}));
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-08613d42"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/report/report.js.map
