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
    url: '/transaction/add',
    method: 'POST',
    data,
    requireAuth: true
  });
}

// 获取交易列表
export const getTransactionList = (params?: any) => {
  return request({
    url: '/transaction/list',
    method: 'GET',
    params,
    requireAuth: true
  });
}

// 获取分类列表
export const getCategoryList = () => {
  return request({
    url: '/category/list',
    method: 'GET',
    requireAuth: true
  });
}

// 用户登录
export const userLogin = (username: string, password: string) => {
  return request({
    url: '/public/user/login',
    method: 'POST',
    data: { username, password }
  });
}

// 用户注册
export const userRegister = (userData: any) => {
  return request({
    url: '/public/user/register',
    method: 'POST',
    data: userData
  });
}
