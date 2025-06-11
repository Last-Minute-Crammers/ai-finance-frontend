import { request } from '../../utils/request'
import { ApiResponse } from './types'

// 分类类型定义
export interface Category {
  id: number;
  name: string;
  icon?: string;
  color?: string;
  incomeExpense: 'income' | 'expense';
  createTime: string;
  updateTime: string;
}

// 获取分类列表
export const getCategoryList = (params?: {
  incomeExpense?: 'income' | 'expense';
}): Promise<ApiResponse<Category[]>> => {
  return request({
    url: '/api/user/category/list',
    method: 'GET',
    params,
    requireAuth: true
  });
}

// 创建分类
export const createCategory = (data: {
  name: string;
  icon?: string;
  color?: string;
  incomeExpense: 'income' | 'expense';
}): Promise<ApiResponse<Category>> => {
  return request({
    url: '/api/user/category',
    method: 'POST',
    data,
    requireAuth: true
  });
}

// 更新分类
export const updateCategory = (id: number, data: {
  name?: string;
  icon?: string;
  color?: string;
  incomeExpense?: 'income' | 'expense';
}): Promise<ApiResponse<Category>> => {
  return request({
    url: `/api/user/category/${id}`,
    method: 'PUT',
    data,
    requireAuth: true
  });
}

// 删除分类
export const deleteCategory = (id: number): Promise<ApiResponse<null>> => {
  return request({
    url: `/api/user/category/${id}`,
    method: 'DELETE',
    requireAuth: true
  });
}
