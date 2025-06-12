import { request } from '../../utils/request'
import { ApiResponse } from './types'

// Import or define 'uni' if required by your environment
declare const uni: any;

// 定义登录响应类型
interface LoginResponse {
  token: string;
  user: {
    id: number;
    username: string;
    email: string;
  };
}

// 用户登录 - 使用邮箱和密码，并保存token
export const userLogin = async (email: string, password: string): Promise<ApiResponse<LoginResponse>> => {
  try {
    console.log('Attempting login with:', { email, password: '***' });
    
    const response = await request({
      url: '/api/public/user/login',
      method: 'POST',
      data: { email, password }
    });
    
    console.log('Login response received:', response);
    console.log('Response type:', typeof response);
    
    // 更robust的响应处理
    if (!response) {
      throw new Error('服务器未返回有效响应');
    }
    
    // 检查是否是成功的响应 - 适配后端返回的结构
    const isSuccess = response.code === 200 || 
                     response.code === 0 || 
                     (response.statusCode && response.statusCode === 200) ||
                     (response.Msg && response.Msg === '登录成功') || // 后端返回的成功标识
                     (response.message && response.message.includes('成功'));
    
    if (isSuccess) {
      // 寻找token的多种可能位置 - 适配后端的 Data.Token 结构
      let token: string | null = null;
      
      if (response.Data?.Token) {
        // 后端实际返回的结构：{ Data: { Token: "...", User: {...} }, Msg: "登录成功" }
        token = response.Data.Token;
      } else if (response.data?.token) {
        // 标准结构：{ code: 200, message: '', data: { token: '', user: {} } }
        token = response.data.token;
      } else if (response.token) {
        // 简化结构：{ code: 200, message: '', token: '', user: {} }
        token = response.token;
      } else if (response.access_token) {
        token = response.access_token;
      } else if (response.accessToken) {
        token = response.accessToken;
      }
      
      if (token) {
        uni.setStorageSync('token', token);
        console.log('Token saved successfully:', token.substring(0, 20) + '...');
        
        // 获取用户信息 - 适配后端结构
        const user = response.Data?.User || response.data?.user || response.user || {};
        
        // 返回标准化的响应格式
        return {
          code: 200,
          message: response.Msg || response.message || '登录成功',
          data: {
            token,
            user: {
              id: user.Id || user.id,
              username: user.Username || user.username,
              email: user.Email || user.email
            }
          }
        };
      } else {
        console.error('登录成功但未找到token，完整响应:', JSON.stringify(response, null, 2));
        throw new Error('登录成功但服务器未返回访问令牌');
      }
    } else {
      // 处理登录失败的情况
      const errorMessage = response.Msg || response.message || response.error || response.msg || '登录失败';
      console.error('登录失败:', errorMessage, response);
      throw new Error(errorMessage);
    }
    
  } catch (error) {
    console.error('Login error details:', error);
    
    // 检查是否是网络或服务器错误
    if (error instanceof Error) {
      if (error.message.includes('500') || error.message.includes('Internal Server Error') || error.message.includes('服务器错误')) {
        throw new Error('服务器内部错误，请检查用户账号是否存在或联系管理员');
      } else if (error.message.includes('Failed to fetch') || error.message.includes('network')) {
        throw new Error('网络连接失败，请检查网络连接');
      } else if (error.message.includes('timeout')) {
        throw new Error('请求超时，请稍后重试');
      } else if (error.message.includes('用户不存在') || error.message.includes('record not found')) {
        throw new Error('用户不存在，请检查邮箱地址或先注册账号');
      }
    }
    
    throw error;
  }
}

// 用户注册 - 需要用户名、邮箱、密码和验证码
export const userRegister = (userData: { username: string, email: string, password: string, captcha: string }) => {
  return request({
    url: '/api/public/user/register',
    method: 'POST',
    data: userData
  });
}

// 获取当前用户信息
export const getCurrentUser = () => {
  return request({
    url: '/api/user/profile',
    method: 'GET',
    requireAuth: true
  });
}

// 用户登出
export const userLogout = () => {
  // 清除本地存储的token
  uni.removeStorageSync('token');
  console.log('User logged out, token cleared');
}
