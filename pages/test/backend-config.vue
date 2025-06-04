<template>
  <view class="container">
    <view class="header">
      <text class="title">后端连接配置</text>
    </view>
    
    <view class="form">
      <view class="form-item">
        <text class="label">当前后端地址</text>
        <text class="value">{{ currentBackendUrl }}</text>
      </view>
      
      <view class="form-item">
        <text class="label">新后端地址</text>
        <input v-model="newBackendUrl" class="input" placeholder="例如: http://192.168.1.100:8080" />
      </view>
      
      <view class="button-group">
        <button @click="testConnection" class="primary-button" :disabled="loading">测试连接</button>
        <button @click="saveConnection" class="secondary-button" :disabled="loading || !connectionSuccess">保存设置</button>
      </view>
    </view>
    
    <view class="results">
      <text class="section-title">连接测试结果</text>
      <view class="status-box" :class="connectionStatus">
        <text>{{ statusMessage }}</text>
      </view>
      <view v-if="showAdvanced" class="log-box">
        <text v-for="(log, index) in logs" :key="index" class="log-entry">{{ log }}</text>
      </view>
    </view>
    
    <view class="quick-options">
      <text class="section-title">快速选择</text>
      <view class="options-grid">
        <button @click="selectOption('http://localhost:8080')" class="option-button">本地 :8080</button>
        <button @click="selectOption('http://localhost:3000')" class="option-button">本地 :3000</button>
        <button @click="selectOption('http://localhost:5000')" class="option-button">本地 :5000</button>
        <button @click="selectOption('http://192.168.1.100:8080')" class="option-button">局域网</button>
      </view>
    </view>
    
    <view class="advanced-toggle">
      <button @click="toggleAdvanced" class="text-button">
        {{ showAdvanced ? '隐藏高级信息' : '显示高级信息' }}
      </button>
    </view>
    
    <view class="troubleshooting">
      <text class="section-title">常见问题解决</text>
      <view class="tip-item">
        <text class="tip-title">连接被拒绝</text>
        <text class="tip-content">- 确认后端服务已启动</text>
        <text class="tip-content">- 检查端口是否正确</text>
        <text class="tip-content">- 检查防火墙设置</text>
      </view>
      <view class="tip-item">
        <text class="tip-title">Docker环境</text>
        <text class="tip-content">- 使用 host.docker.internal 而不是 localhost</text>
        <text class="tip-content">- 确保正确映射了容器端口</text>
      </view>
    </view>
  </view>
</template>

<script>
import { ref, onMounted } from 'vue';
import { setBackendUrl } from '../../utils/request';

export default {
  setup() {
    const currentBackendUrl = ref('');
    const newBackendUrl = ref('');
    const loading = ref(false);
    const connectionStatus = ref('neutral');
    const statusMessage = ref('未测试');
    const connectionSuccess = ref(false);
    const logs = ref([]);
    const showAdvanced = ref(false);
    
    onMounted(() => {
      // 获取当前后端URL
      currentBackendUrl.value = uni.getStorageSync('backend_url') || 'http://localhost:8080';
    });
    
    const addLog = (message) => {
      const timestamp = new Date().toLocaleTimeString();
      logs.value.push(`[${timestamp}] ${message}`);
    };
    
    const testConnection = async () => {
      if (!newBackendUrl.value) {
        uni.showToast({ title: '请输入后端地址', icon: 'none' });
        return;
      }
      
      loading.value = true;
      connectionSuccess.value = false;
      connectionStatus.value = 'pending';
      statusMessage.value = '测试中...';
      logs.value = [];
      
      addLog(`开始测试连接到: ${newBackendUrl.value}`);
      
      try {
        const startTime = Date.now();
        const response = await uni.request({
          url: newBackendUrl.value + '/test',
          method: 'GET',
          timeout: 5000
        });
        
        const endTime = Date.now();
        const responseTime = endTime - startTime;
        
        if (response.statusCode === 200) {
          connectionStatus.value = 'success';
          statusMessage.value = `连接成功! (${responseTime}ms)`;
          connectionSuccess.value = true;
          addLog(`连接成功，响应时间: ${responseTime}ms`);
          addLog(`服务器响应: ${JSON.stringify(response.data)}`);
        } else {
          connectionStatus.value = 'warning';
          statusMessage.value = `服务器返回: ${response.statusCode}`;
          addLog(`服务器返回非200状态码: ${response.statusCode}`);
        }
      } catch (error) {
        connectionStatus.value = 'error';
        // 安全获取错误信息
        const errMsg = typeof error === 'object' && error !== null && 'errMsg' in error 
          ? String(error.errMsg) 
          : '未知错误';
        
        statusMessage.value = `连接失败: ${errMsg}`;
        addLog(`连接错误: ${errMsg}`);
        
        if (errMsg.includes('CONNECTION_REFUSED')) {
          addLog('诊断: 服务器未启动或端口不正确');
        } else if (errMsg.includes('timeout')) {
          addLog('诊断: 连接超时，服务器可能响应缓慢或不可达');
        }
      } finally {
        loading.value = false;
      }
    };
    
    const saveConnection = () => {
      if (!connectionSuccess.value) {
        uni.showToast({ title: '请先测试连接成功', icon: 'none' });
        return;
      }
      
      setBackendUrl(newBackendUrl.value);
      currentBackendUrl.value = newBackendUrl.value;
      
      uni.showToast({ title: '设置已保存', icon: 'success' });
      addLog('后端地址已保存: ' + newBackendUrl.value);
      
      // 更新状态可能需要重启应用，提示用户
      uni.showModal({
        title: '设置已保存',
        content: '后端地址已更改。建议重启应用以确保所有请求使用新地址。',
        showCancel: false
      });
    };
    
    const selectOption = (url) => {
      newBackendUrl.value = url;
    };
    
    const toggleAdvanced = () => {
      showAdvanced.value = !showAdvanced.value;
    };
    
    return {
      currentBackendUrl,
      newBackendUrl,
      loading,
      connectionStatus,
      statusMessage,
      connectionSuccess,
      logs,
      showAdvanced,
      testConnection,
      saveConnection,
      selectOption,
      toggleAdvanced
    };
  }
};
</script>

<style>
.container {
  padding: 30rpx;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.header {
  margin-bottom: 30rpx;
  text-align: center;
}

.title {
  font-size: 36rpx;
  font-weight: bold;
  color: #333;
}

.form {
  background-color: #fff;
  border-radius: 12rpx;
  padding: 20rpx;
  margin-bottom: 30rpx;
  box-shadow: 0 2rpx 10rpx rgba(0,0,0,0.05);
}

.form-item {
  margin-bottom: 20rpx;
}

.label {
  font-size: 28rpx;
  color: #666;
  margin-bottom: 10rpx;
  display: block;
}

.value {
  font-size: 30rpx;
  color: #333;
  padding: 10rpx 0;
  word-break: break-all;
}

.input {
  border: 1px solid #ddd;
  padding: 15rpx;
  font-size: 28rpx;
  border-radius: 6rpx;
  width: 100%;
  box-sizing: border-box;
}

.button-group {
  display: flex;
  justify-content: space-between;
  margin-top: 20rpx;
}

.primary-button, .secondary-button {
  flex: 1;
  margin: 0 10rpx;
  border-radius: 6rpx;
  padding: 15rpx 0;
}

.primary-button {
  background-color: #3498db;
  color: white;
}

.secondary-button {
  background-color: #2ecc71;
  color: white;
}

.primary-button:disabled, .secondary-button:disabled {
  background-color: #ccc;
  color: #888;
}

.results {
  background-color: #fff;
  border-radius: 12rpx;
  padding: 20rpx;
  margin-bottom: 30rpx;
  box-shadow: 0 2rpx 10rpx rgba(0,0,0,0.05);
}

.section-title {
  font-size: 30rpx;
  color: #333;
  margin-bottom: 15rpx;
  display: block;
}

.status-box {
  padding: 15rpx;
  border-radius: 6rpx;
  margin-bottom: 15rpx;
  text-align: center;
}

.neutral {
  background-color: #f8f9fa;
  color: #666;
}

.pending {
  background-color: #e9f5fe;
  color: #3498db;
}

.success {
  background-color: #e9fef2;
  color: #2ecc71;
}

.warning {
  background-color: #fff9e6;
  color: #f39c12;
}

.error {
  background-color: #feecec;
  color: #e74c3c;
}

.log-box {
  background-color: #282c34;
  border-radius: 6rpx;
  padding: 15rpx;
  max-height: 300rpx;
  overflow-y: auto;
}

.log-entry {
  color: #ddd;
  font-size: 24rpx;
  font-family: monospace;
  display: block;
  padding: 5rpx 0;
}

.quick-options {
  background-color: #fff;
  border-radius: 12rpx;
  padding: 20rpx;
  margin-bottom: 30rpx;
  box-shadow: 0 2rpx 10rpx rgba(0,0,0,0.05);
}

.options-grid {
  display: flex;
  flex-wrap: wrap;
  margin: 0 -10rpx;
}

.option-button {
  flex: 1 1 calc(50% - 20rpx);
  margin: 10rpx;
  background-color: #f0f0f0;
  color: #333;
  border: 1px solid #ddd;
  min-width: 0;
}

.advanced-toggle {
  margin-bottom: 30rpx;
  text-align: center;
}

.text-button {
  background: none;
  color: #3498db;
  font-size: 28rpx;
}

.troubleshooting {
  background-color: #fff;
  border-radius: 12rpx;
  padding: 20rpx;
  box-shadow: 0 2rpx 10rpx rgba(0,0,0,0.05);
}

.tip-item {
  margin-bottom: 15rpx;
}

.tip-title {
  font-weight: bold;
  color: #333;
  font-size: 28rpx;
  margin-bottom: 10rpx;
  display: block;
}

.tip-content {
  color: #666;
  font-size: 26rpx;
  display: block;
  padding-left: 20rpx;
  line-height: 1.5;
}
</style>
