// Removed unused import as '@dcloudio/types' is not a module

const BASE_URL = 'http://localhost:8080'; // 替换为您的后端 API URL

interface RequestOptions {
  url: string;
  method?: 'GET' | 'POST' | 'PUT' | 'DELETE';
  data?: any;
  params?: any;
  requireAuth?: boolean;
}

// 获取存储的 token
// Ensure uni is globally available or imported from the appropriate library
declare const uni: any;

const getToken = () => {
  return uni.getStorageSync('token') || '';
};

// 健康检查函数
export const healthCheck = async () => {
  try {
    const response = await request({
      url: '/api/public/health',
      method: 'GET'
    });
    console.log('Backend health check:', response);
    return response;
  } catch (error) {
    const errMsg = (typeof error === 'object' && error && 'message' in error)
      ? (error as any).message
      : String(error);
    console.error('Backend connection failed:', errMsg);
    throw error;
  }
};

// 检查后端连接状态
export const checkBackendConnection = async () => {
  try {
    console.log('检查后端连接状态...');
    const response = await uni.request({
      url: BASE_URL + '/api/test',
      method: 'GET',
      timeout: 5000
    });
    
    console.log('后端连接检查结果:', response);
    return response.statusCode === 200;
  } catch (error) {
    const errMsg = (typeof error === 'object' && error && 'message' in error)
      ? (error as any).message
      : String(error);
    console.error('后端连接检查失败:', errMsg);
    return false;
  }
};

// 测试连接函数
export const testConnection = async () => {
  try {
    console.log('测试连接到:', BASE_URL + '/api/test');
    const response = await request({
      url: '/api/test',
      method: 'GET'
    });
    console.log('Connection test successful:', response);
    return response;
  } catch (error) {
    const errMsg = (typeof error === 'object' && error && 'message' in error)
      ? (error as any).message
      : String(error);
    console.error('Connection test failed:', errMsg);
    throw error;
  }
};

// 全面的连接测试函数
export const runComprehensiveTest = async () => {
  const testResults = {
    basic: false,
    health: false,
    public: false,
    private: false,
    services: {},
    errors: [] as string[]
  };

  try {
    // 1. 基础连接测试
    await testConnection();
    testResults.basic = true;
  } catch (error) {
    const errMsg = (typeof error === 'object' && error && 'message' in error)
      ? (error as any).message
      : String(error);
    testResults.errors.push('基础连接失败: ' + errMsg);
  }

  try {
    // 2. 健康检查测试
    const healthResponse = await healthCheck();
    testResults.health = true;
    testResults.services = healthResponse.services || {};
  } catch (error) {
    const errMsg = (typeof error === 'object' && error && 'message' in error)
      ? (error as any).message
      : String(error);
    testResults.errors.push('健康检查失败: ' + errMsg);
  }

  try {
    // 3. 公开API测试
    await request({ url: '/api/public/ping', method: 'GET' });
    testResults.public = true;
  } catch (error) {
    const errMsg = (typeof error === 'object' && error && 'message' in error)
      ? (error as any).message
      : String(error);
    testResults.errors.push('公开API测试失败: ' + errMsg);
  }

  try {
    // 4. 私有API测试 (可能因未登录而失败，这是正常的)
    await request({ url: '/api/transaction/list', method: 'GET', requireAuth: true });
    testResults.private = true;
  } catch (error) {
    const errMsg = (typeof error === 'object' && error && 'message' in error)
      ? (error as any).message
      : String(error);
    if (errMsg !== 'Unauthorized') {
      testResults.errors.push('私有API测试失败: ' + errMsg);
    }
  }

  return testResults;
};

// 简单的ping测试
export const pingBackend = async () => {
  try {
    const startTime = Date.now();
    await request({ url: '/api/test', method: 'GET' });
    const endTime = Date.now();
    return {
      success: true,
      responseTime: endTime - startTime
    };
  } catch (error) {
    const errMsg = (typeof error === 'object' && error && 'message' in error)
      ? (error as any).message
      : String(error);
    return {
      success: false,
      error: errMsg
    };
  }
};

export const request = async (options: RequestOptions) => {
  const { url, method = 'GET', data, params, requireAuth = false } = options;
  
  let fullUrl = BASE_URL + url;
  if (params) {
    const queryString = Object.entries(params)
      .map(([key, value]) => `${key}=${encodeURIComponent(String(value))}`)
      .join('&');
    fullUrl += `?${queryString}`;
  }

  // 构建请求头
  const headers: Record<string, string> = {};

  // 如果数据不是FormData，设置Content-Type为JSON
  if (!(data instanceof FormData)) {
    headers['Content-Type'] = 'application/json';
  }

  // 如果需要认证，添加 Authorization header
  if (requireAuth) {
    const token = getToken();
    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }
  }

  try {
    console.log(`Making ${method} request to: ${fullUrl}`);
    console.log('Request headers:', headers);
    console.log('Request data:', data);
    
    const response = await uni.request({
      url: fullUrl,
      method,
      data,
      header: headers,
      timeout: 30000
    });

    console.log('Response status:', response.statusCode);
    console.log('Response data:', response.data);

    if (response.statusCode === 200) {
      return response.data;
    } else if (response.statusCode === 401) {
      // Token 过期或无效
      uni.removeStorageSync('token');
      uni.showToast({
        title: '请重新登录',
        icon: 'none'
      });
      throw new Error('Unauthorized');
    } else if (response.statusCode === 404) {
      throw new Error(`API路径不存在: ${url} (检查后端路由配置)`);
    } else {
      throw new Error(`请求失败: ${response.statusCode} - ${response.data?.error || JSON.stringify(response.data) || '未知错误'}`);
    }
  } catch (error) {
    const errMsg = (typeof error === 'object' && error && 'message' in error)
      ? (error as any).message
      : String(error);
    console.error('请求错误详情:', errMsg);
    
    // 网络连接错误
    if (errMsg.includes('timeout')) {
      uni.showToast({
        title: '网络超时，请检查网络连接',
        icon: 'none'
      });
      throw new Error('网络超时');
    } else if (errMsg.includes('fail')) {
      uni.showToast({
        title: '无法连接到后端服务器',
        icon: 'none'
      });
      throw new Error('后端服务器连接失败');
    }
    
    if (errMsg !== 'Unauthorized') {
      uni.showToast({
        title: errMsg || '网络连接失败',
        icon: 'none'
      });
    }
    throw error;
  }
};