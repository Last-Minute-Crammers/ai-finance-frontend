# 构建文件修复说明

## 问题描述

在 `unpackage/dist/dev/.tsc/app-android/utils/request.ts` 文件中出现了多个 "Cannot find name '__f__'" 的 TypeScript 错误。

## 问题原因

1. **uni-app 编译机制**：uni-app 在编译过程中会将 `console.log` 等调试函数转换为 `__f__` 函数
2. **TypeScript 编译阶段**：在 TypeScript 编译阶段，`__f__` 函数尚未定义，导致类型检查错误
3. **构建文件生成**：`unpackage/dist/` 目录下的文件是自动生成的构建产物

## 解决方案

### 方案1：修复生成的构建文件（已实施）

将所有 `__f__` 调用替换为标准的 `console` 方法：

```typescript
// 修复前
__f__('log','at utils/request.ts:45','Retrieved token:', token);

// 修复后  
console.log('Retrieved token:', token);
```

### 方案2：在源文件中添加类型声明

在 `utils/request.ts` 文件顶部添加：

```typescript
// 声明 __f__ 函数类型（用于构建时）
declare const __f__: (level: string, location: string, ...args: any[]) => void;
```

### 方案3：使用条件编译

```typescript
// 使用条件编译避免构建时错误
if (typeof __f__ !== 'undefined') {
  __f__('log', 'message');
} else {
  console.log('message');
}
```

## 已修复的文件

- `unpackage/dist/dev/.tsc/app-android/utils/request.ts`
- `unpackage/dist/build/.tsc/app-android/utils/request.ts`

## 修复内容

1. **日志函数替换**：
   - `__f__('log', ...)` → `console.log(...)`
   - `__f__('error', ...)` → `console.error(...)`

2. **保持功能不变**：
   - 所有日志输出功能保持不变
   - 调试信息仍然可用
   - 错误处理逻辑完整

## 注意事项

1. **重新构建**：如果重新构建项目，可能需要再次修复构建文件
2. **开发环境**：在 HBuilderX 中运行时，`__f__` 函数会正常工作
3. **生产环境**：构建后的代码会正确处理日志输出

## 预防措施

1. **定期检查**：在每次构建后检查构建文件是否有 `__f__` 错误
2. **自动化修复**：可以编写脚本自动修复构建文件中的 `__f__` 问题
3. **类型声明**：在项目中添加 `__f__` 的类型声明文件

## 相关文件

- `utils/request.ts` - 源文件
- `unpackage/dist/dev/.tsc/app-android/utils/request.ts` - 开发环境构建文件
- `unpackage/dist/build/.tsc/app-android/utils/request.ts` - 生产环境构建文件 