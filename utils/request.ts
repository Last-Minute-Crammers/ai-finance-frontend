const BACKEND_URLS = {
  local: 'http://localhost:8080',
  dockerHost: 'http://host.docker.internal:8080',
  ip: 'http://127.0.0.1:8080',
  external: 'http://192.168.1.100:8080'
};

const DEFAULT_URL = BACKEND_URLS.local;
declare const uni: any;

const getBackendUrl = () => {
  const configuredUrl = uni.getStorageSync('backend_url');
  return configuredUrl || DEFAULT_URL;
};
const BASE_URL = getBackendUrl();

const getToken = () => uni.getStorageSync('token') || '';

export const setBackendUrl = (url: string) => {
  uni.setStorageSync('backend_url', url);
};

export const getAvailableBackendUrls = () => BACKEND_URLS;

// 检查后端连接状态（兼容旧用法，简单实现）
export const checkBackendConnection = async (customUrl?: string) => {
  const url = customUrl || BASE_URL;
  try {
    await request({ url: '/api/public/test', method: 'GET', timeout: 2000 });
    return { connected: true, url };
  } catch (error: unknown) {
    const errMsg = (error instanceof Error) ? error.message : String(error);
    return { connected: false, error: errMsg, url };
  }
};

// 健康检查
export const healthCheck = async () => {
  return await request({ url: '/api/public/health', method: 'GET', timeout: 3000 });
};

// 简单ping
export const pingBackend = async () => {
  try {
    await request({ url: '/api/public/ping', method: 'GET', timeout: 2000 });
    return { success: true };
  } catch (error: unknown) {
    const errMsg = (error instanceof Error) ? error.message : String(error);
    return { success: false, error: errMsg };
  }
};

// 测试 /api/public/test 路由
export const testConnection = async () => {
  try {
    await request({ url: '/api/public/test', method: 'GET', timeout: 2000 });
    return { connected: true };
  } catch (error: unknown) {
    const errMsg = (error instanceof Error) ? error.message : String(error);
    return { connected: false, error: errMsg };
  }
};

// 基础请求
export const request = async <T = any>(options: {
  url: string;
  method?: 'GET' | 'POST' | 'PUT' | 'DELETE';
  data?: any;
  params?: any;
  requireAuth?: boolean;
  timeout?: number;
}): Promise<T> => {
  const {
    url,
    method = 'GET',
    data,
    params,
    requireAuth = false,
    timeout = 10000
  } = options;

  let fullUrl = BASE_URL + url;
  if (params) {
    const queryString = Object.entries(params)
      .map(([key, value]) => `${key}=${encodeURIComponent(String(value))}`)
      .join('&');
    fullUrl += `?${queryString}`;
  }

  const headers: Record<string, string> = {};
  let processedData = data;
  if (!(data instanceof FormData)) {
    headers['Content-Type'] = 'application/json';
    if (data && typeof data === 'object') {
      processedData = JSON.stringify(data);
    }
  }
  if (requireAuth) {
    const token = getToken();
    if (token) headers['Authorization'] = `Bearer ${token}`;
  }

  return await new Promise<T>((resolve, reject) => {
    uni.request({
      url: fullUrl,
      method,
      data: processedData,
      header: headers,
      timeout,
      success: (res: { statusCode: number; data: T }) => {
        if (!res || typeof res.statusCode !== 'number') {
          reject(new Error('无效的响应格式'));
          return;
        }
        if (res.statusCode === 200) {
          resolve(res.data);
        } else if (res.statusCode === 401) {
          uni.removeStorageSync('token');
          uni.showToast({ title: '请重新登录', icon: 'none' });
          reject(new Error('Unauthorized'));
        } else if (res.statusCode === 404) {
          reject(new Error(`API路径不存在: ${url}`));
        } else {
          reject(new Error(`请求失败: ${res.statusCode}`));
        }
      },
      fail: (err: unknown) => {
        reject(err || new Error('请求失败'));
      }
    });
  });
};