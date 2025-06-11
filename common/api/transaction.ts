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

// 获取交易列表
export const getTransactionList = (params: TransactionListParams = {}): Promise<ApiResponse<TransactionListResponse>> => {
  console.log('getTransactionList called with params:', params);
  
  // 构建查询参数，匹配后端期望的结构
  const queryParams: any = {};
  
  if (params.offset !== undefined) queryParams.offset = params.offset;
  if (params.limit !== undefined) queryParams.limit = params.limit;
  if (params.incomeExpense) queryParams.incomeExpense = params.incomeExpense;
  if (params.startTime) queryParams.startTime = params.startTime;
  if (params.endTime) queryParams.endTime = params.endTime;
  if (params.minimumAmount) queryParams.minimumAmount = params.minimumAmount;
  if (params.maximumAmount) queryParams.maximumAmount = params.maximumAmount;
  if (params.categoryIds && params.categoryIds.length > 0) {
    queryParams.categoryIds = params.categoryIds.join(',');
  }
  
  console.log('Query params:', queryParams);
  
  return request({
    url: '/api/user/transaction/list',
    method: 'GET',
    params: queryParams,
    requireAuth: true
  });
}

// 创建交易记录
export const createTransaction = (data: {
  amount: number;
  categoryId: number;
  incomeExpense: 'income' | 'expense';
  remark: string;
  tradeTime?: string;
}): Promise<ApiResponse<Transaction>> => {
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

// 获取月度统计
export const getMonthStatistic = (params?: {
  startTime?: string;
  endTime?: string;
}): Promise<ApiResponse<any>> => {
  return request({
    url: '/api/user/transaction/statistic/month',
    method: 'GET',
    params,
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
