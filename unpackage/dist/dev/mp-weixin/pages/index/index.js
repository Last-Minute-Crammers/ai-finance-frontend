"use strict";
const common_vendor = require("../../common/vendor.js");
const _sfc_main = common_vendor.defineComponent(new UTSJSONObject({
  data() {
    return {
      balance: 8542.36,
      expense: 2260.45,
      features: [
        new UTSJSONObject({ name: "记账", desc: "记录每一笔收支", icon: "/static/icons/book.png", path: "/pages/transaction/transaction" }),
        new UTSJSONObject({ name: "财务分析", desc: "可视化您的消费", icon: "/static/icons/chart.png", path: "/pages/report/report" }),
        new UTSJSONObject({ name: "AI理财宠物", desc: "陪伴式理财体验", icon: "/static/icons/pet.png", path: "/pages/pet/pet" }),
        new UTSJSONObject({ name: "财务报告", desc: "智能分析建议", icon: "/static/icons/report.png", path: "/pages/report/report" }),
        new UTSJSONObject({ name: "社交互动", desc: "与好友一起理财", icon: "/static/icons/social.png", path: "/pages/social/social" }),
        new UTSJSONObject({ name: "设置", desc: "个性化您的体验", icon: "/static/icons/settings.png", path: "/pages/settings/settings" })
      ]
    };
  },
  methods: new UTSJSONObject({
    goPage(path = null) {
      common_vendor.index.navigateTo({ url: path });
    }
  })
}));
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: common_vendor.t($data.balance.toFixed(2)),
    b: common_vendor.t($data.expense.toFixed(2)),
    c: common_vendor.f($data.features, (item, index, i0) => {
      return {
        a: item.icon,
        b: common_vendor.t(item.name),
        c: common_vendor.t(item.desc),
        d: index,
        e: common_vendor.o(($event) => $options.goPage(item.path), index)
      };
    }),
    d: common_vendor.sei(common_vendor.gei(_ctx, ""), "view")
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-00a60067"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/index/index.js.map
