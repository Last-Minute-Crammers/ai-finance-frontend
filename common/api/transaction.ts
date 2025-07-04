import { request } from '../../utils/request'
import { ApiResponse } from './types'

// 交易类型定义 - 匹配后端响应结构
export interface Transaction {
  Id: number;
  UserId: number;
  Amount: number; // 以分为单位
  CategoryId: number;
  CategoryName?: string;
  IncomeExpense: 'income' | 'expense';
  Remark: string;
  TradeTime: string;
  CreateTime: string;
  UpdateTime: string;
}

// 交易列表请求参数 - 匹配后端 TransactionGetList 结构
export interface TransactionListParams {
  offset?: number;
  limit?: number;
  categoryIds?: number[];
  incomeExpense?: 'income' | 'expense';
  startTime?: string;
  endTime?: string;
  minimumAmount?: number;
  maximumAmount?: number;
}

// 交易列表响应 - 匹配后端 TransactionGetList 结构
export interface TransactionListResponse {
  List: Transaction[];
  Total: number;
  Page: number;
  PageSize: number;
}

// 获取交易列表 - 使用POST方法支持复杂查询参数
export const getTransactionList = (params: TransactionListParams = {}): Promise<ApiResponse<TransactionListResponse>> => {
  console.log('getTransactionList called with params:', params);
  
  return request({
    url: '/api/user/transaction/list',
    method: 'POST', // 改为POST方法
    data: params,   // 使用data传递JSON body
    requireAuth: true
  });
}

// 创建交易记录 - 更新参数类型定义
export const createTransaction = (data: {
  amount: number;
  categoryId: number;
  incomeExpense: 'income' | 'expense';
  remark: string;
  tradeTime?: string;
}): Promise<ApiResponse<Transaction>> => {
  console.log('createTransaction called with data:', data);
  
  return request({
    url: '/api/user/transaction',
    method: 'POST',
    data,
    requireAuth: true
  });
}

// 获取单个交易记录
export const getTransaction = (id: number): Promise<ApiResponse<Transaction>> => {
  return request({
    url: `/api/user/transaction/${id}`,
    method: 'GET',
    requireAuth: true
  });
}

// 获取月度统计 - 使用POST方法支持复杂查询参数
export const getMonthStatistic = (params?: {
  incomeExpense?: 'income' | 'expense';
  startTime?: string;
  endTime?: string;
}): Promise<ApiResponse<any>> => {
  return request({
    url: '/api/user/transaction/statistic/month',
    method: 'POST', // 改为POST方法
    data: params,   // 使用data传递JSON body
    requireAuth: true
  });
}

// 更新交易记录
export const updateTransaction = (id: number, data: {
  amount?: number;
  description?: string;
  categoryId?: number;
  incomeExpense?: 'income' | 'expense';
  tradeTime?: string;
}): Promise<ApiResponse<Transaction>> => {
  return request({
    url: `/user/transaction/${id}`,
    method: 'PUT',
    data,
    requireAuth: true
  });
}

// 删除交易记录
export const deleteTransaction = (id: number): Promise<ApiResponse<null>> => {
  return request({
    url: `/user/transaction/${id}`,
    method: 'DELETE',
    requireAuth: true
  });
}

// 获取交易统计信息
export const getTransactionStats = (params?: {
  startTime?: string;
  endTime?: string;
}): Promise<ApiResponse<{
  totalIncome: number;
  totalExpense: number;
  balance: number;
  monthlyIncome: number;
  monthlyExpense: number;
}>> => {
  return request({
    url: '/api/user/transaction/stats',
    method: 'GET',
    params,
    requireAuth: true
  });
}
// 获取总统计信息
export const getTotalStatistic = (): Promise<ApiResponse<{
  income: { amount: number; count: number };
  expense: { amount: number; count: number };
  total_assets: number;
}>> => {
  return request({
    url: '/api/user/transaction/statistic/total',
    method: 'GET',
    requireAuth: true
  });
}

// 获取月度统计信息 - 使用POST方法
export const getMonthlyStatistic = (params?: {
  startTime?: string;
  endTime?: string;
  incomeExpense?: 'income' | 'expense' | 'both';
}): Promise<ApiResponse<{
  List: Array<{
    income: { amount: number; count: number };
    expense: { amount: number; count: number };
    StartTime: string;
    EndTime: string;
  }>;
}>> => {
  return request({
    url: '/api/user/transaction/statistic/month',
    method: 'POST', // 改为POST方法
    data: params,   // 使用data传递JSON body
    requireAuth: true
  });
}

// 获取类别统计
export const getCategoryStatistic = (params: {
  startTime?: string;
  endTime?: string;
  incomeExpense?: 'income' | 'expense';
  limit?: number;
}): Promise<ApiResponse<any>> => {
  return request<ApiResponse<any>>({
    url: '/api/user/transaction/statistic/category_rank',
    method: 'POST',
    data: params,
    requireAuth: true
  });
}

// 获取单日统计
export const getDailyStatistic = (params: {
  startTime?: string;
  endTime?: string;
  incomeExpense?: 'income' | 'expense';
}): Promise<ApiResponse<any>> => {
  return request<ApiResponse<any>>({
    url: '/api/user/transaction/statistic/day',
    method: 'POST',
    data: params,
    requireAuth: true
  });
}

// 获取单周统计
export const getWeeklyStatistic = (params: {
  startTime?: string;
  endTime?: string;
  incomeExpense?: 'income' | 'expense';
}): Promise<ApiResponse<any>> => {
  return request<ApiResponse<any>>({
    url: '/api/user/transaction/statistic/week',
    method: 'POST',
    data: params,
    requireAuth: true
  });
}
