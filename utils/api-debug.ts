import { request } from './request'

// API调试工具
export const debugAPI = {
  // 测试token有效性
  async testToken() {
    try {
      const response = await request({
        url: '/api/user/profile',
        method: 'GET',
        requireAuth: true
      });
      console.log('Token验证成功:', response);
      return { success: true, data: response };
    } catch (error) {
      console.error('Token验证失败:', error);
      return { success: false, error };
    }
  },

  // 测试交易API - 不同的参数组合
  async testTransactionAPI() {
    const testCases = [
      // 测试用例1：基本参数
      { offset: 0, limit: 10 },
      // 测试用例2：最小参数
      { offset: 0, limit: 1 },
      // 测试用例3：无参数
      {}
    ];

    for (let i = 0; i < testCases.length; i++) {
      const params = testCases[i];
      console.log(`测试用例 ${i + 1}:`, params);
      
      try {
        const response = await request({
          url: '/api/user/transaction/list',
          method: 'GET',
          params,
          requireAuth: true
        });
        console.log(`测试用例 ${i + 1} 成功:`, response);
      } catch (error) {
        console.error(`测试用例 ${i + 1} 失败:`, error);
      }
    }
  },

  // 手动测试网络请求
  async manualRequest(url: string, params?: any) {
    try {
      console.log('手动测试请求:', { url, params });
      const response = await request({
        url,
        method: 'GET',
        params,
        requireAuth: true
      });
      console.log('手动请求成功:', response);
      return response;
    } catch (error) {
      console.error('手动请求失败:', error);
      throw error;
    }
  }
};

// 在浏览器控制台中可以使用：
// window.debugAPI = debugAPI;
