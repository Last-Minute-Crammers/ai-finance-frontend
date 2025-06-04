import { request } from '../../utils/request'

interface Transaction {
  amount: number;
  category: string;
  date: string;
  description?: string;
}

interface ApiResponse<T = any> {
  code: number;
  message: string;
  data: T;
}

// 添加交易
export const addTransaction = (data: Transaction) => {
  return request({
    url: '/api/transaction/add',
    method: 'POST',
    data,
    requireAuth: true
  });
}

// 获取交易列表
export const getTransactionList = (params?: any) => {
  return request({
    url: '/api/transaction/list',
    method: 'GET',
    params,
    requireAuth: true
  });
}

// 获取分类列表
export const getCategoryList = () => {
  return request({
    url: '/api/category/list',
    method: 'GET',
    requireAuth: true
  });
}

// 用户登录
export const userLogin = (username: string, password: string) => {
  return request({
    url: '/api/public/user/login',
    method: 'POST',
    data: { username, password }
  });
}

// 用户注册
export const userRegister = (userData: any) => {
  return request({
    url: '/api/public/user/register',
    method: 'POST',
    data: userData
  });
}

// AI聊天对话
export const sendAIMessage = (data: { message: string }): Promise<{ success: boolean; data: string; error?: string }> => {
  return request<{ success: boolean; data: string; error?: string }>({
    url: '/api/public/ai/chat',
    method: 'POST',
    data,
    requireAuth: false,
  });
};

// 语音识别
export const voiceRecognition = (audioFile: File) => {
  const formData = new FormData();
  formData.append('audio', audioFile);
  
  return request({
    url: '/api/public/ai/voice',
    method: 'POST',
    data: formData,
    requireAuth: false
  });
}
