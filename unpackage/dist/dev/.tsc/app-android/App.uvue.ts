
const __sfc__ = defineApp({
  onLaunch() {
    console.log('App 启动', " at App.uvue:10");
  },
  onShow() {
    console.log('App 显示', " at App.uvue:13");
  },
  onHide() {
    console.log('App 隐藏', " at App.uvue:16");
  }
})

export default __sfc__
const GenAppStyles = [utsMapOf([["app-container", padStyleMapOf(utsMapOf([["backgroundColor", "#f4f4f4"], ["fontFamily", "Arial, sans-serif"], ["color", "#333333"]]))]])]
