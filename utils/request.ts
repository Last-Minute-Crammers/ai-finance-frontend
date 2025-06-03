import type { RequestOptions as UniRequestOptions } from '@dcloudio/types';

const BASE_URL = 'http://localhost:8080'; // 替换为您的后端 API URL

interface RequestOptions {
  url: string;
  method?: 'GET' | 'POST' | 'PUT' | 'DELETE';
  data?: any;
  params?: any;
}

export const request = async (options: RequestOptions) => {
  const { url, method = 'GET', data, params } = options;
  
  let fullUrl = BASE_URL + url;
  if (params) {
    const queryString = Object.entries(params)
      .map(([key, value]) => `${key}=${encodeURIComponent(String(value))}`)
      .join('&');
    fullUrl += `?${queryString}`;
  }

  try {
    const response = await uni.request({
      url: fullUrl,
      method,
      data,
      header: {
        'Content-Type': 'application/json'
      }
    });

    if (response.statusCode === 200) {
      return response.data;
    } else {
      throw new Error(`请求失败: ${response.statusCode}`);
    }
  } catch (error) {
    console.error('请求错误:', error);
    throw error;
  }
};