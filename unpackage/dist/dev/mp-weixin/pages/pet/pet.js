"use strict";
const common_vendor = require("../../common/vendor.js");
const common_assets = require("../../common/assets.js");
const _sfc_main = /* @__PURE__ */ common_vendor.defineComponent(new UTSJSONObject({
  __name: "pet",
  setup(__props) {
    const showAllBadges = common_vendor.ref(false);
    const allBadges = [
      new UTSJSONObject({ name: "目标达人", icon: "🎯" }),
      new UTSJSONObject({ name: "省钱小能手", icon: "💰" }),
      new UTSJSONObject({ name: "储蓄先锋", icon: "📈" }),
      new UTSJSONObject({ name: "社交达人", icon: "🗣️" }),
      new UTSJSONObject({ name: "理财王者", icon: "👑" }),
      new UTSJSONObject({ name: "早起打卡", icon: "⏰" }),
      new UTSJSONObject({ name: "账单清零", icon: "🧾" })
    ];
    const displayedBadges = common_vendor.computed(() => {
      return showAllBadges.value ? allBadges : allBadges.slice(0, 3);
    });
    const toggleShowAll = () => {
      showAllBadges.value = !showAllBadges.value;
    };
    const goToChat = () => {
      common_vendor.index.navigateTo({ url: "/pages/AIchat/AIchat" });
    };
    return (_ctx = null, _cache = null) => {
      const __returned__ = {
        a: common_assets._imports_0,
        b: common_vendor.o(goToChat),
        c: common_vendor.t(showAllBadges.value ? "收起" : "查看全部"),
        d: common_vendor.o(toggleShowAll),
        e: common_vendor.f(displayedBadges.value, (badge = null, k0 = null, i0 = null) => {
          return {
            a: common_vendor.t(badge.icon),
            b: common_vendor.t(badge.name),
            c: badge.name
          };
        }),
        f: common_vendor.sei(common_vendor.gei(_ctx, ""), "view")
      };
      return __returned__;
    };
  }
}));
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-44a33225"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/pet/pet.js.map
