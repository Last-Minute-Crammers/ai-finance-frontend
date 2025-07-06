// API连接测试脚本
// 用于验证前端是否能成功连接到指定的API地址

const testApiConnection = async () => {
  const apiUrl = 'http://47.109.194.39/api';
  
  console.log('开始测试API连接...');
  console.log('目标API地址:', apiUrl);
  
  try {
    // 测试基础连接 - 使用健康检查端点
    const response = await fetch(`${apiUrl}/public/health`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json'
      }
    });
    
    if (response.ok) {
      const data = await response.json();
      console.log('✅ API连接成功!');
      console.log('响应状态:', response.status);
      console.log('响应数据:', data);
    } else {
      console.log('❌ API连接失败');
      console.log('响应状态:', response.status);
      console.log('响应文本:', await response.text());
    }
  } catch (error) {
    console.log('❌ API连接错误:', error.message);
    
    // 提供诊断信息
    if (error.message.includes('fetch')) {
      console.log('可能的原因:');
      console.log('- 网络连接问题');
      console.log('- API服务器未启动');
      console.log('- 防火墙阻止连接');
      console.log('- CORS配置问题');
    }
  }
};

// 测试健康检查端点
const testHealthCheck = async () => {
  const apiUrl = 'http://47.109.194.39/api';
  
  console.log('\n开始健康检查...');
  
  try {
    const response = await fetch(`${apiUrl}/public/health`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json'
      }
    });
    
    if (response.ok) {
      const data = await response.json();
      console.log('✅ 健康检查通过!');
      console.log('服务状态:', data);
    } else {
      console.log('❌ 健康检查失败');
      console.log('状态码:', response.status);
    }
  } catch (error) {
    console.log('❌ 健康检查错误:', error.message);
  }
};

// 运行测试
const runTests = async () => {
  console.log('=== API连接测试 ===\n');
  
  await testApiConnection();
  await testHealthCheck();
  
  console.log('\n=== 测试完成 ===');
};

// 如果在Node.js环境中运行
if (typeof window === 'undefined') {
  runTests();
} else {
  // 在浏览器环境中运行
  window.testApiConnection = testApiConnection;
  window.testHealthCheck = testHealthCheck;
  window.runTests = runTests;
  
  console.log('API测试函数已加载到全局作用域');
  console.log('使用方法: runTests()');
} 