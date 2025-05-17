"use strict";
const common_vendor = require("../../common/vendor.js");
const _sfc_main = common_vendor.defineComponent({
  data() {
    return {
      type: "支出",
      amount: "",
      selectedCategory: "餐饮",
      date: "",
      categories: [
        new UTSJSONObject({ name: "餐饮", icon: "🍔" }),
        new UTSJSONObject({ name: "购物", icon: "🛒" }),
        new UTSJSONObject({ name: "交通", icon: "🚗" }),
        new UTSJSONObject({ name: "住房", icon: "🏠" }),
        new UTSJSONObject({ name: "娱乐", icon: "🎬" }),
        new UTSJSONObject({ name: "数码", icon: "📱" }),
        new UTSJSONObject({ name: "服饰", icon: "👗" }),
        new UTSJSONObject({ name: "其他", icon: "➕" })
      ]
    };
  },
  onLoad() {
    this.date = this.formatDate(/* @__PURE__ */ new Date());
  },
  methods: {
    goBack() {
      common_vendor.index.navigateBack();
    },
    changeType(newType = null) {
      this.type = newType;
    },
    formatDate(date = null) {
      const y = date.getFullYear();
      const m = (date.getMonth() + 1).toString().padStart(2, "0");
      const d = date.getDate().toString().padStart(2, "0");
      return `${y}年${m}月${d}日`;
    },
    submitTransaction() {
      if (!this.amount) {
        common_vendor.index.showToast({ title: "请输入金额", icon: "none" });
        return null;
      }
      const data = new UTSJSONObject({
        type: this.type,
        amount: this.amount,
        category: this.selectedCategory,
        date: this.date
      });
      common_vendor.index.__f__("log", "at pages/transaction/transaction.uvue:112", "记录已保存：", data);
      common_vendor.index.showToast({ title: "记录已保存", icon: "success" });
    }
  }
});
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: common_vendor.o((...args) => $options.goBack && $options.goBack(...args)),
    b: $data.type === "支出" ? 1 : "",
    c: common_vendor.o(($event) => $options.changeType("支出")),
    d: $data.type === "收入" ? 1 : "",
    e: common_vendor.o(($event) => $options.changeType("收入")),
    f: $data.amount,
    g: common_vendor.o(($event) => $data.amount = $event.detail.value),
    h: common_vendor.f($data.categories, (item, index, i0) => {
      return {
        a: common_vendor.t(item.icon),
        b: common_vendor.t(item.name),
        c: index,
        d: $data.selectedCategory === item.name ? 1 : "",
        e: common_vendor.o(($event) => $data.selectedCategory = item.name, index)
      };
    }),
    i: common_vendor.t($data.date),
    j: common_vendor.o((...args) => $options.submitTransaction && $options.submitTransaction(...args)),
    k: common_vendor.sei(common_vendor.gei(_ctx, ""), "view")
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-690e881d"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/transaction/transaction.js.map
