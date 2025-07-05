// Removed unused import as '@dcloudio/types' is not a module

// 为不同环境提供后端URL选项
const BACKEND_URLS = {
  local: 'http://localhost:8080',
  dockerHost: 'http://host.docker.internal:8080',
  ip: 'http://127.0.0.1:8080',
  external: 'http://192.168.1.100:8080'
};

// 更新默认URL配置，正确连接本地运行的前端到Docker中的后端
const DEFAULT_URL = BACKEND_URLS.local; // 使用localhost而不是host.docker.internal

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
  timeout?: number;
  retries?: number;
  headers?: Record<string, string>; // 新增headers字段
}

// 自定义错误接口
interface UniRequestError {
  errMsg?: string;
  message?: string;
  [key: string]: any;
}

// 获取存储的 token - 添加调试日志
const getToken = () => {
  const token = uni.getStorageSync('token') || '';
  console.log('Retrieved token:', token ? `${token.substring(0, 20)}...` : 'No token found');
  return token;
};

// Ensure uni is globally available or imported from the appropriate library
declare const uni: any;

// 设置后端URL
export const setBackendUrl = (url: string) => {
  uni.setStorageSync('backend_url', url);
  console.log('Backend URL set to:', url);
};

// 增强版设置后端URL函数，支持预设环境选择
export const setBackendEnvironment = (envKey: keyof typeof BACKEND_URLS) => {
  if (BACKEND_URLS[envKey]) {
    const url = BACKEND_URLS[envKey];
    setBackendUrl(url);
    return url;
  }
  return null;
};

// 增加一个获取所有可能后端URL的函数
export const getAvailableBackendUrls = () => {
  return BACKEND_URLS;
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
export const checkBackendConnection = async (customUrl?: string) => {
  const targetUrl = customUrl || BASE_URL;
  try {
    console.log('检查后端连接状态...', targetUrl);
    const startTime = Date.now();
    
    // 修复Promise.race逻辑，确保适当处理undefined
    const requestPromise = new Promise((resolve, reject) => {
      uni.request({
        url: targetUrl + '/api/test',
        method: 'GET',
        timeout: 10000,
        success: (response) => {
          if (!response) {
            reject(new Error('未收到后端响应'));
            return;
          }
          resolve(response);
        },
        fail: (error) => {
          reject(error || new Error('请求失败'));
        }
      });
    });
    
    const timeoutPromise = new Promise((_, reject) => {
      setTimeout(() => reject(new Error('请求超时(10秒)')), 10000);
    });
    
    // 使用Promise.race正确处理响应
    const response = await Promise.race([requestPromise, timeoutPromise]) as any;
    const endTime = Date.now();
    
    // 安全检查response和statusCode
    if (!response || response.statusCode === undefined) {
      throw new Error('无效的响应格式');
    }
    
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
    const errMsg = typeof error === 'object' && error !== null && 'message' in error 
      ? String(error.message || error.errMsg) 
      : '未知错误';

    const isConnectionRefused = typeof errMsg === 'string' && errMsg.includes('CONNECTION_REFUSED');
    const isTimeout = typeof errMsg === 'string' && (errMsg.includes('timeout') || errMsg.includes('超时'));
    
    // 增强的诊断信息
    const diagnostics = {
      serverUrl: targetUrl,
      errorType: errMsg,
      isConnectionRefused: isConnectionRefused,
      isTimeout: isTimeout,
      possibleCauses: [] as string[]
    };
    
    if (isConnectionRefused) {
      diagnostics.possibleCauses = [
        "后端服务器未启动",
        '端口8080可能被其他应用占用',
        '检查防火墙设置是否允许连接'
      ];
    }
    else if (isTimeout) {
      diagnostics.possibleCauses = [
        "Docker容器端口映射不正确 - 检查docker-compose.yml",
        "Docker网络配置问题 - 尝试使用127.0.0.1而不是localhost",
        "防火墙阻止了连接 - 检查防火墙设置",
        "后端服务响应过慢 - 检查服务器负载",
        "后端服务未正确监听端口 - 检查后端日志"
      ];
    } else {
      // 添加通用错误原因
      diagnostics.possibleCauses = [
        "后端服务未正确启动",
        "API端点路径可能不正确",
        "请求处理过程中出现错误",
        "网络连接问题"
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

export const request = async <T = any>(options: RequestOptions): Promise<T> => {
  const token = getToken();
  const headers = {
    ...(options.headers || {}),
    'Content-Type': 'application/json',
  };
  if (options.requireAuth !== false && token) {
    headers['Authorization'] = 'Bearer ' + token;
  }

  // 处理GET请求的查询参数
  let url = BASE_URL + options.url;
  if (options.method === 'GET' && options.params) {
    const queryParams = new URLSearchParams();
    for (const [key, value] of Object.entries(options.params)) {
      if (value !== undefined && value !== null) {
        queryParams.append(key, String(value));
      }
    }
    const queryString = queryParams.toString();
    if (queryString) {
      url += (url.includes('?') ? '&' : '?') + queryString;
    }
  }

  return new Promise<T>((resolve, reject) => {
    uni.request({
      url: url,
      method: options.method || 'GET',
      data: options.data,
      header: headers,
      timeout: options.timeout || 30000,
      success: (res) => {
        if (!res) {
          reject(new Error('未收到响应数据'));
          return;
        }
        if (!res.statusCode || res.statusCode !== 200) {
          reject(new Error(`请求失败 (${res.statusCode})`));
          return;
        }
        resolve(res.data as T);
      },
      fail: (err) => {
        reject(err || new Error('请求失败'));
      }
    });
  });
};

// 添加一个检查token有效性的函数
export const validateToken = async () => {
  try {
    const response = await request({
      url: '/api/user/profile',
      method: 'GET',
      requireAuth: true
    });
    return { valid: true, user: response.data };
  } catch (error) {
    return { valid: false, error };
  }
};