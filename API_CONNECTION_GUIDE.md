# API连接配置指南

## 概述

前端应用已配置为连接到生产环境API：`http://47.109.194.39/api`

## 配置更改

### 1. 主要配置文件更新

已更新以下文件中的API配置：

- `utils/request.ts` - 主要请求工具
- `unpackage/dist/dev/.tsc/app-android/utils/request.ts` - 开发环境构建文件
- `unpackage/dist/build/.tsc/app-android/utils/request.ts` - 生产环境构建文件

### 2. 可用的API环境

```typescript
const BACKEND_URLS = {
  production: 'http://47.109.194.39/api',  // 生产环境 (默认)
  local: 'http://localhost:8080',          // 本地开发
  dockerHost: 'http://host.docker.internal:8080', // Docker环境
  ip: 'http://127.0.0.1:8080',            // 本地IP
  external: 'http://192.168.1.100:8080'   // 外部网络
};
```

## 使用方法

### 1. 自动连接

应用启动时会自动使用生产环境API (`http://47.109.194.39/api`)

### 2. 手动切换API环境

在设置页面中，您可以：

1. 点击"切换API"按钮
2. 选择不同的环境配置
3. 系统会自动测试新连接

### 3. 连接状态监控

在设置页面中，您可以：

- 查看当前API连接状态
- 测试连接响应时间
- 查看连接错误信息
- 手动触发连接测试

## 测试连接

### 1. 使用设置页面

1. 打开应用
2. 进入设置页面
3. 查看"API连接状态"卡片
4. 点击"测试连接"按钮

### 2. 使用测试脚本

运行 `test-api-connection.js` 脚本：

```bash
node test-api-connection.js
```

### 3. 浏览器控制台测试

在浏览器控制台中运行：

```javascript
runTests()
```

## 故障排除

### 常见问题

1. **连接超时**
   - 检查网络连接
   - 确认API服务器是否运行
   - 检查防火墙设置

2. **CORS错误**
   - 确认API服务器已配置CORS
   - 检查请求头设置

3. **认证错误**
   - 确认用户已登录
   - 检查token是否有效

### 诊断步骤

1. 检查网络连接
2. 测试API端点可访问性
3. 查看浏览器控制台错误
4. 使用设置页面的连接测试功能

## API端点

### 测试端点
- `GET /api/test` - 基础连接测试
- `GET /api/public/health` - 健康检查

### 用户相关
- `POST /api/user/login` - 用户登录
- `GET /api/user/profile` - 获取用户信息

### 交易相关
- `GET /api/transaction/list` - 获取交易列表
- `POST /api/transaction/create` - 创建交易

## 开发注意事项

1. **环境切换**：开发时可以在设置页面切换到本地环境
2. **调试模式**：查看控制台日志了解连接详情
3. **错误处理**：所有API请求都有完整的错误处理机制

## 更新日志

- 2024-01-XX: 添加生产环境API配置
- 2024-01-XX: 实现API连接状态监控
- 2024-01-XX: 添加环境切换功能 