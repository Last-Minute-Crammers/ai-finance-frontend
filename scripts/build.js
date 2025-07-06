// uni-app x 构建检查脚本
const fs = require('fs');
const path = require('path');

console.log('=== uni-app x 项目检查 ===');

// 检查必要文件
function checkRequiredFiles() {
  const requiredFiles = [
    'manifest.json',
    'pages.json',
    'App.uvue',
    'main.uts'
  ];
  
  console.log('\n📁 检查必要文件:');
  for (const file of requiredFiles) {
    const filePath = path.join(__dirname, '..', file);
    if (fs.existsSync(filePath)) {
      console.log(`✅ ${file}`);
    } else {
      console.log(`❌ ${file} - 文件不存在`);
      return false;
    }
  }
  return true;
}

// 检查 manifest.json 配置
function checkManifest() {
  console.log('\n📋 检查 manifest.json 配置:');
  
  try {
    const manifestPath = path.join(__dirname, '..', 'manifest.json');
    const manifest = JSON.parse(fs.readFileSync(manifestPath, 'utf8'));
    
    // 检查 uni-app-x 配置
    if (manifest['uni-app-x']) {
      console.log('✅ uni-app-x 配置存在');
    } else {
      console.log('❌ 缺少 uni-app-x 配置');
      return false;
    }
    
    // 检查应用配置
    if (manifest.app && manifest.app.distribute) {
      console.log('✅ app.distribute 配置存在');
    } else {
      console.log('❌ 缺少 app.distribute 配置');
      return false;
    }
    
    // 检查图标配置
    if (manifest.app.distribute.icons) {
      console.log('✅ 图标配置存在');
    } else {
      console.log('⚠️  图标配置为空，建议添加应用图标');
    }
    
    return true;
  } catch (error) {
    console.log(`❌ manifest.json 解析失败: ${error.message}`);
    return false;
  }
}

// 检查页面配置
function checkPages() {
  console.log('\n📄 检查页面配置:');
  
  try {
    const pagesPath = path.join(__dirname, '..', 'pages.json');
    const pages = JSON.parse(fs.readFileSync(pagesPath, 'utf8'));
    
    if (pages.pages && pages.pages.length > 0) {
      console.log(`✅ 找到 ${pages.pages.length} 个页面配置`);
      
      // 检查页面文件是否存在
      for (const page of pages.pages) {
        const pagePath = path.join(__dirname, '..', page.path + '.uvue');
        if (fs.existsSync(pagePath)) {
          console.log(`  ✅ ${page.path}.uvue`);
        } else {
          console.log(`  ❌ ${page.path}.uvue - 文件不存在`);
        }
      }
    } else {
      console.log('❌ 没有找到页面配置');
      return false;
    }
    
    return true;
  } catch (error) {
    console.log(`❌ pages.json 解析失败: ${error.message}`);
    return false;
  }
}

// 检查静态资源
function checkStaticResources() {
  console.log('\n🖼️  检查静态资源:');
  
  const staticDir = path.join(__dirname, '..', 'static');
  if (fs.existsSync(staticDir)) {
    console.log('✅ static 目录存在');
    
    // 检查图标目录
    const iconsDir = path.join(staticDir, 'icons');
    if (fs.existsSync(iconsDir)) {
      console.log('✅ static/icons 目录存在');
      
      const iconFiles = fs.readdirSync(iconsDir);
      if (iconFiles.length > 0) {
        console.log(`  📁 找到 ${iconFiles.length} 个图标文件`);
      } else {
        console.log('  ⚠️  图标目录为空，建议添加应用图标');
      }
    } else {
      console.log('⚠️  static/icons 目录不存在，建议创建');
    }
  } else {
    console.log('⚠️  static 目录不存在，建议创建');
  }
  
  return true;
}

// 检查 API 配置
function checkApiConfig() {
  console.log('\n🔗 检查 API 配置:');
  
  const apiDir = path.join(__dirname, '..', 'common', 'api');
  if (fs.existsSync(apiDir)) {
    console.log('✅ common/api 目录存在');
    
    const apiFiles = fs.readdirSync(apiDir);
    if (apiFiles.length > 0) {
      console.log(`  📁 找到 ${apiFiles.length} 个 API 文件`);
    }
  } else {
    console.log('⚠️  common/api 目录不存在');
  }
  
  return true;
}

// 检查工具函数
function checkUtils() {
  console.log('\n🛠️  检查工具函数:');
  
  const utilsDir = path.join(__dirname, '..', 'utils');
  if (fs.existsSync(utilsDir)) {
    console.log('✅ utils 目录存在');
    
    const utilFiles = fs.readdirSync(utilsDir);
    if (utilFiles.length > 0) {
      console.log(`  📁 找到 ${utilFiles.length} 个工具文件`);
    }
  } else {
    console.log('⚠️  utils 目录不存在');
  }
  
  return true;
}

// 主检查函数
function main() {
  let allPassed = true;
  
  // 检查必要文件
  if (!checkRequiredFiles()) {
    allPassed = false;
  }
  
  // 检查 manifest.json
  if (!checkManifest()) {
    allPassed = false;
  }
  
  // 检查页面配置
  if (!checkPages()) {
    allPassed = false;
  }
  
  // 检查静态资源
  checkStaticResources();
  
  // 检查 API 配置
  checkApiConfig();
  
  // 检查工具函数
  checkUtils();
  
  // 输出总结
  console.log('\n📊 检查总结:');
  if (allPassed) {
    console.log('✅ 项目配置基本正确，可以进行打包');
    console.log('\n📋 下一步操作:');
    console.log('1. 在 HBuilderX 中打开项目');
    console.log('2. 选择 "发行" → "原生App-云打包"');
    console.log('3. 选择目标平台 (Android/iOS)');
    console.log('4. 配置应用信息和签名');
    console.log('5. 点击 "打包" 按钮');
  } else {
    console.log('❌ 项目配置存在问题，请修复后再进行打包');
  }
  
  return allPassed;
}

// 运行检查
main(); 