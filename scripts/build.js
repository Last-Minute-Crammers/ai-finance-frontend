const fs = require('fs');
const path = require('path');

// 简化的构建脚本
console.log('开始构建前端项目...');

// 检查关键文件是否存在
const criticalFiles = [
  'pages/index/index.uvue',
  'components/AddCategoryModal.uvue',
  'components/SimpleChart.uvue',
  'common/api/types.ts',
  'common/api/category.ts',
  'utils/request.ts',
  'utils/pageState.ts'
];

console.log('检查关键文件...');
criticalFiles.forEach(file => {
  const filePath = path.join(__dirname, '..', file);
  if (fs.existsSync(filePath)) {
    console.log(`✓ ${file} 存在`);
  } else {
    console.log(`✗ ${file} 不存在`);
  }
});

console.log('构建脚本执行完成');
console.log('请使用HBuilderX进行云打包测试'); 