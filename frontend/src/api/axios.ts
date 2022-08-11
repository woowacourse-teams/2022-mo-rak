import axios from 'axios';
import { getLocalStorageItem } from '../utils/storage';

const axiosInstanceGenerator = (path: string, isAuthRequired: boolean = true) => {
  const instance = axios.create({
    baseURL: `${process.env.BASE_API_URL}/${path}`,
    headers: { 'Content-Type': 'application/json' }
  });

  if (isAuthRequired) {
    instance.interceptors.request.use((config) => {
      const newConfig = { ...config };

      newConfig.headers = {
        Authorization: `Bearer ${getLocalStorageItem('token')} `
      };

      return newConfig;
    });
  }

  return instance;
};

const authInstance = axiosInstanceGenerator('auth', false);
const groupInstance = axiosInstanceGenerator('groups');
// NOTE: poll, appointment의 instance를 만들어주려면, groupCode를 넘겨받아서 instance를 생성해줘야한다.
// 생각을 해보면, 매번 code가 달라질텐데, 그럴때마다 instance를 만들어서 활용해주는 것보다는 사용하는 곳에서 code를
// 넘겨주는 것이 훨씬 효율적이어보임
// const pollInstance = axiosInstanceGenerator('group');
// const appointmentInstance = axiosInstanceGenerator('appointment');

export { authInstance, groupInstance };
