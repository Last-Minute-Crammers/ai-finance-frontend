import { request } from '../../utils/request'

// AI聊天对话
export const sendAIMessage = (message: string) => {
  return request({
    url: '/api/public/ai/chat',
    method: 'POST',
    data: { message },
    requireAuth: false
  });
}

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
