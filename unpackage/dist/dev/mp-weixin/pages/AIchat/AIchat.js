"use strict";
const common_vendor = require("../../common/vendor.js");
const aiAvatar = "/static/icons/pet.png";
const userAvatar = "/static/icons/user.png";
const bottomAnchor = "bottom-anchor";
const _sfc_main = /* @__PURE__ */ common_vendor.defineComponent(new UTSJSONObject({
  __name: "AIchat",
  setup(__props) {
    const inputText = common_vendor.ref("");
    const messages = common_vendor.ref([
      { role: "ai", text: "你好，我是你的AI理财助手，有什么可以帮您？" }
    ]);
    const scrollIntoView = common_vendor.ref(bottomAnchor);
    function sendMessage() {
      const content = inputText.value.trim();
      if (!content)
        return null;
      messages.value.push({ role: "user", text: content });
      inputText.value = "";
      scrollToBottom();
      setTimeout(() => {
        messages.value.push({ role: "ai", text: `收到：${content}
我会为您分析一下～` });
        scrollToBottom();
      }, 500);
    }
    function scrollToBottom() {
      common_vendor.nextTick$1(() => {
        scrollIntoView.value = bottomAnchor;
      });
    }
    return (_ctx = null, _cache = null) => {
      const __returned__ = {
        a: common_vendor.f(messages.value, (msg = null, index = null, i0 = null) => {
          return {
            a: msg.role === "ai" ? aiAvatar : userAvatar,
            b: common_vendor.t(msg.text),
            c: common_vendor.sei("msg-" + index, "view"),
            d: index,
            e: common_vendor.n(msg.role)
          };
        }),
        b: common_vendor.sei(bottomAnchor, "view"),
        c: scrollIntoView.value,
        d: common_vendor.o(sendMessage),
        e: inputText.value,
        f: common_vendor.o(($event = null) => {
          return inputText.value = $event.detail.value;
        }),
        g: common_vendor.o(sendMessage),
        h: common_vendor.sei(common_vendor.gei(_ctx, ""), "view")
      };
      return __returned__;
    };
  }
}));
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-9f4e77f0"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/AIchat/AIchat.js.map
