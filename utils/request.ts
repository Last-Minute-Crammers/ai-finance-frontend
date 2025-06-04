// Removed unused import as '@dcloudio/types' is not a module

// 更新默认URL，可能后端在不同端口上运行
const DEFAULT_URL = 'http://localhost:8080'; // 或其他可能的端口
const getBackendUrl = () => {
  // Try to get from storage first (allows runtime configuration)
  const configuredUrl = uni.getStorageSync('backend_url');
  return configuredUrl || DEFAULT_URL;
};

// Get the active backend URL - this allows changing it at runtime
const BASE_URL = getBackendUrl();

interface RequestOptions {
  url: string;
  method?: 'GET' | 'POST' | 'PUT' | 'DELETE';
  data?: any;
  params?: any;
  requireAuth?: boolean;
  // Add timeout and retries
  timeout?: number;
  retries?: number;
}

// 自定义错误接口
interface UniRequestError {
  errMsg?: string;
  message?: string;
  [key: string]: any;
}

// 获取存储的 token
// Ensure uni is globally available or imported from the appropriate library
declare const uni: any;

const getToken = () => {
  return uni.getStorageSync('token') || '';
};

// 设置后端URL
export const setBackendUrl = (url: string) => {
  uni.setStorageSync('backend_url', url);
  console.log('Backend URL set to:', url);
};

// 健康检查函数
export const healthCheck = async () => {
  try {
    const response = await request({
      url: '/api/public/health',
      method: 'GET',
      timeout: 3000 // shorter timeout for health check
    });
    console.log('Backend health check:', response);
    return response;
  } catch (error) {
    const errMsg = (typeof error === 'object' && error !== null && 'message' in error)
      ? (error as any).message
      : String(error);
    console.error('Backend connection failed:', errMsg);
    throw error;
  }
};

// 检查后端连接状态 - 优化版，支持连接诊断
export const checkBackendConnection = async () => {
  try {
    console.log('检查后端连接状态...', BASE_URL);
    const startTime = Date.now();
    
    const response = await uni.request({
      url: BASE_URL + '/api/test',
      method: 'GET',
      timeout: 5000
    });
    
    const endTime = Date.now();
    console.log('后端连接检查结果:', response, `响应时间: ${endTime - startTime}ms`);
    
    return {
      connected: response.statusCode === 200,
      statusCode: response.statusCode,
      responseTime: endTime - startTime,
      serverInfo: response.data || {}
    };
  } catch (error: any) {
    console.error('后端连接检查失败:', error);
    
    // 处理错误对象，确保类型安全
    const errMsg = typeof error === 'object' && error !== null && 'errMsg' in error 
      ? String(error.errMsg) 
      : '未知错误';

    const isConnectionRefused = typeof errMsg === 'string' && errMsg.includes('CONNECTION_REFUSED');
    
    // 增强的诊断信息
    const diagnostics = {
      serverUrl: BASE_URL,
      errorType: errMsg,
      isConnectionRefused: isConnectionRefused,
      possibleCauses: [] as string[]
    };
    
    if (isConnectionRefused) {
      diagnostics.possibleCauses = [
        "后端服务器未启动",
        '端口8080可能被其他应用占用',
        '检查防火墙设置是否允许连接'
      ];
    }
    
    console.log('连接诊断:', diagnostics);
    return { connected: false, error: errMsg, diagnostics };
  }
};

// 测试连接函数 - 带有重试功能
export const testConnection = async (maxRetries = 1) => {
  let retries = 0;
  let lastError;
  
  while (retries <= maxRetries) {
    try {
      console.log(`测试连接到 ${BASE_URL}/api/test (尝试 ${retries + 1}/${maxRetries + 1})`);
      const response = await request({
        url: '/api/test',
        method: 'GET',
        timeout: 3000
      });
      console.log('Connection test successful:', response);
      return response;
    } catch (error) {
      const errMsg = (typeof error === 'object' && error !== null && 'message' in error)
        ? (error as any).message
        : String(error);
      console.error(`Connection test failed (attempt ${retries + 1}):`, errMsg);
      lastError = error;
      retries++;
      
      // 如果要继续重试，等待一会
      if (retries <= maxRetries) {
        await new Promise(resolve => setTimeout(resolve, 1000));
      }
    }
  }
  
  throw lastError;
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
  const { 
    url, 
    method = 'GET', 
    data, 
    params, 
    requireAuth = false,
    timeout = 30000,
    retries = 0
  } = options;
  
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

  let currentRetry = 0;
  
  while (true) {
    try {
      console.log(`Making ${method} request to: ${fullUrl} (attempt ${currentRetry + 1})`);
      console.log('Request headers:', headers);
      console.log('Request data:', data);
      
      const response = await uni.request({
        url: fullUrl,
        method,
        data,
        header: headers,
        timeout
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
    } catch (error: any) {
      const errMsg = (typeof error === 'object' && error !== null && 'message' in error)
        ? (error as any).message
        : String(error);
      console.error('请求错误详情:', errMsg, error);
      
      // 检查是否应该重试
      if (currentRetry < retries) {
        console.log(`重试请求 (${currentRetry + 1}/${retries})...`);
        currentRetry++;
        // 等待一会再重试
        await new Promise(resolve => setTimeout(resolve, 1000));
        continue;
      }
      
      // 网络连接错误 - 提供更详细的诊断
      // 安全检查error对象结构
      if (typeof error === 'object' && error !== null && 'errMsg' in error) {
        const errMsgStr = String(error.errMsg);
        
        if (errMsgStr.includes('request:fail')) {
          if (errMsgStr.includes('timeout')) {
            uni.showToast({
              title: '网络超时，请检查网络连接',
              icon: 'none'
            });
            throw new Error('网络超时');
          } else if (errMsgStr.includes('CONNECTION_REFUSED')) {
            uni.showToast({
              title: '无法连接到后端服务器',
              icon: 'none'
            });
            console.error('连接诊断: 后端服务器可能未启动或不在端口8080上运行');
            throw new Error('后端服务器连接失败');
          } else {
            console.error('网络错误详情:', errMsgStr);
            throw new Error('网络连接错误: ' + errMsgStr);
          }
        }
      }
      
      if (errMsg !== 'Unauthorized') {
        uni.showToast({
          title: errMsg || '网络连接失败',
          icon: 'none'
        });
      }
      throw error;
    }
  }
};