import { request } from '../../utils/request'
import { ApiResponse } from './types'

// 本地存储键名
const CATEGORY_STORAGE_KEY = 'local_categories'

// 本地存储工具函数
const saveCategoriesToLocal = (categories: Category[]) => {
  try {
    uni.setStorageSync(CATEGORY_STORAGE_KEY, JSON.stringify(categories))
    console.log('分类数据已保存到本地存储:', categories.length, '个分类')
  } catch (error) {
    console.error('保存分类到本地存储失败:', error)
  }
}

const getCategoriesFromLocal = (): Category[] => {
  try {
    const data = uni.getStorageSync(CATEGORY_STORAGE_KEY)
    if (data) {
      const categories = JSON.parse(data)
      console.log('从本地存储获取分类数据:', categories.length, '个分类')
      return categories
    }
  } catch (error) {
    console.error('从本地存储获取分类失败:', error)
  }
  return []
}

// 根据类型过滤本地分类
const filterCategoriesByType = (categories: Category[], type: 'income' | 'expense'): Category[] => {
  return categories.filter(category => category.incomeExpense === type)
}

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
export const getCategoryList = async (data: {
  income_expense: 'income' | 'expense';
}): Promise<ApiResponse<Category[]>> => {
  try {
    // 先尝试从本地存储获取数据
    const localCategories = getCategoriesFromLocal()
    const filteredCategories = filterCategoriesByType(localCategories, data.income_expense)
    
    // 如果有本地数据，先返回本地数据
    if (filteredCategories.length > 0) {
      console.log('使用本地分类数据:', filteredCategories.length, '个分类')
      return {
        code: 200,
        message: 'success',
        data: filteredCategories
      }
    }
    
    // 本地没有数据，从服务器获取
    console.log('本地无数据，从服务器获取分类列表...')
    const rawResponse = await request({
      url: '/api/user/category/list',
      method: 'GET',
      params: data,
      requireAuth: true
    })
    
    // 转换后端数据格式为前端期望的格式
    const transformedData = (rawResponse.Data || []).map((item: any) => ({
      id: item.ID, // 后端返回 ID，转换为前端期望的 id
      name: item.Name,
      icon: item.Icon,
      color: item.Color,
      incomeExpense: item.IncomeExpense,
      createTime: item.CreatedAt,
      updateTime: item.UpdatedAt
    }))
    
    const response = {
      code: 200,
      message: rawResponse.Msg || 'success',
      data: transformedData
    }
    
    console.log('转换后的响应格式:', response)
    
    // 如果服务器请求成功，同步到本地存储
    if (response.code === 200) {
      // 确保 response.data 是数组
      const newCategories = response.data || []
      
      if (newCategories.length > 0) {
        // 合并现有本地数据和新的服务器数据
        const existingCategories = getCategoriesFromLocal()
        
        // 创建ID映射，避免重复
        const existingIds = new Set(existingCategories.map(cat => cat.id))
        const uniqueNewCategories = newCategories.filter(cat => !existingIds.has(cat.id))
        
        const allCategories = [...existingCategories, ...uniqueNewCategories]
        saveCategoriesToLocal(allCategories)
        
        console.log('分类数据已同步到本地存储，总计:', allCategories.length, '个分类')
      } else {
        console.log('服务器返回空分类列表，无需更新本地存储')
      }
    }
    
    return response
  } catch (error) {
    console.error('获取分类列表失败:', error)
    
    // 如果网络请求失败，尝试使用本地数据
    const localCategories = getCategoriesFromLocal()
    const filteredCategories = filterCategoriesByType(localCategories, data.income_expense)
    
    if (filteredCategories.length > 0) {
      console.log('网络请求失败，使用本地缓存数据:', filteredCategories.length, '个分类')
      return {
        code: 200,
        message: 'success (cached)',
        data: filteredCategories
      }
    }
    
    // 本地也没有数据，抛出错误
    throw error
  }
}

// 创建分类
export const createCategory = async (data: {
  name: string;
  icon?: string;
  color?: string;
  incomeExpense: 'income' | 'expense';
}): Promise<ApiResponse<Category>> => {
  // 转换字段名以匹配后端API
  const requestData = {
    name: data.name,
    icon: data.icon,
    color: data.color,
    income_expense: data.incomeExpense  // 转换为后端期望的字段名
  }
  
  console.log('发送创建分类请求:', requestData)
  
  const rawResponse = await request({
    url: '/api/user/category',
    method: 'POST',
    data: requestData,
    requireAuth: true
  });
  
  // 转换后端数据格式为前端期望的格式
  return {
    code: 200,
    message: rawResponse.Msg || 'success',
    data: rawResponse.Data || rawResponse
  };
}

// 快速创建分类（使用当前页面类型）- 本地优先策略
export const createCategoryWithCurrentType = async (name: string, icon?: string, color?: string): Promise<ApiResponse<Category> | null> => {
  // 从页面状态获取当前类型
  const { getCurrentIncomeExpense } = await import('../../utils/pageState')
  const currentIncomeExpense = getCurrentIncomeExpense()
  
  if (!currentIncomeExpense) {
    console.error('无法获取当前页面类型，请确保在记账页面中')
    uni.showToast({
      title: '请先在记账页面选择类型',
      icon: 'none'
    })
    return null
  }
  
  // 1. 先创建本地临时分类
  const tempCategory: Category = {
    id: Date.now(), // 临时ID
    name,
    icon: icon || '📁',
    color: color || '#4e54c8',
    incomeExpense: currentIncomeExpense,
    createTime: new Date().toISOString(),
    updateTime: new Date().toISOString()
  }
  
  try {
    // 2. 先保存到本地存储
    const localCategories = getCategoriesFromLocal()
    const allCategories = [...localCategories, tempCategory]
    saveCategoriesToLocal(allCategories)
    console.log('分类已保存到本地:', tempCategory)
    
    // 3. 尝试同步到后端
    try {
      const response = await createCategory({
        name,
        icon,
        color,
        incomeExpense: currentIncomeExpense
      })
      
      if (response.code === 200 && response.data) {
        // 4. 后端创建成功，用真实数据替换临时数据
        const updatedCategories = localCategories.map(cat => 
          cat.id === tempCategory.id ? response.data : cat
        ).filter((cat): cat is Category => cat !== undefined)
        saveCategoriesToLocal(updatedCategories)
        console.log('分类已同步到后端:', response.data)
        
        return {
          code: 200,
          message: '分类创建成功',
          data: response.data
        }
      } else {
        throw new Error(response.message || '后端创建失败')
      }
    } catch (serverError) {
      console.error('后端同步失败，但本地已保存:', serverError)
      
      // 5. 后端失败，但本地已保存，返回成功状态
      uni.showToast({
        title: '分类已保存到本地，网络同步失败',
        icon: 'none',
        duration: 3000
      })
      
      return {
        code: 200,
        message: 'success (local only)',
        data: tempCategory
      }
    }
  } catch (localError) {
    console.error('本地保存失败:', localError)
    
    // 6. 本地保存也失败
    uni.showToast({
      title: '保存失败，请重试',
      icon: 'none'
    })
    
    throw localError
  }
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

// 强制刷新分类列表（从服务器获取最新数据）
export const refreshCategoryList = async (data: {
  income_expense: 'income' | 'expense';
}): Promise<ApiResponse<Category[]>> => {
  try {
    console.log('强制从服务器刷新分类列表...')
    const rawResponse = await request({
      url: '/api/user/category/list',
      method: 'GET',
      params: data,
      requireAuth: true
    })
    
    // 转换后端数据格式为前端期望的格式
    const transformedData = (rawResponse.Data || []).map((item: any) => ({
      id: item.ID, // 后端返回 ID，转换为前端期望的 id
      name: item.Name,
      icon: item.Icon,
      color: item.Color,
      incomeExpense: item.IncomeExpense,
      createTime: item.CreatedAt,
      updateTime: item.UpdatedAt
    }))
    
    const response = {
      code: 200,
      message: rawResponse.Msg || 'success',
      data: transformedData
    }
    
    console.log('转换后的响应格式:', response)
    
    // 如果服务器请求成功，更新本地存储
    if (response.code === 200) {
      // 确保 response.data 是数组
      const newCategories = response.data || []
      
      // 获取现有本地数据
      const existingCategories = getCategoriesFromLocal()
      
      // 移除同类型的旧数据，添加新数据
      const otherTypeCategories = existingCategories.filter(
        cat => cat.incomeExpense !== data.income_expense
      )
      const allCategories = [...otherTypeCategories, ...newCategories]
      
      saveCategoriesToLocal(allCategories)
      console.log('分类数据已强制刷新并保存到本地存储，总计:', allCategories.length, '个分类')
    }
    
    return response
  } catch (error) {
    console.error('强制刷新分类列表失败:', error)
    throw error
  }
}

// 清除本地分类缓存
export const clearLocalCategories = () => {
  try {
    uni.removeStorageSync(CATEGORY_STORAGE_KEY)
    console.log('本地分类缓存已清除')
  } catch (error) {
    console.error('清除本地分类缓存失败:', error)
  }
}
