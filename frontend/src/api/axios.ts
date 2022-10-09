import axios from 'axios';
import {
  getLocalStorageItem,
  removeLocalStorageItem,
  saveSessionStorageItem
} from '../utils/storage';

type Path = 'auth' | 'groups';

const axiosInstanceGenerator = (path: Path, isAuthRequired = true) => {
  const instance = axios.create({
    baseURL: `${process.env.BASE_API_URL}/${path}`,
    headers: { 'Content-Type': 'application/json' }
  });

  if (isAuthRequired) {
    instance.interceptors.request.use((config) => {
      const newConfig = { ...config };

      newConfig.headers = {
        Authorization: `Bearer ${getLocalStorageItem('token')}`
      };

      return newConfig;
    });

    let is401ErrorProcessing = false;
    let is403ErrorProcessing = false;
    instance.interceptors.response.use(
      (res) => {
        is401ErrorProcessing = false;
        is403ErrorProcessing = false;

        return res;
      },
      (err) => {
        const errCode = err.response.data?.codeNumber;
        const url = err.response.config.url;
        const statusCode = err.response.status;

        if (!is401ErrorProcessing && statusCode === 401) {
          is401ErrorProcessing = true;

          const isInvitationPage = url.includes('in');
          if (isInvitationPage) {
            saveSessionStorageItem('redirectUrl', window.location.pathname);
          }

          alert(`로그인 해주세요😀`);
          removeLocalStorageItem('token');
          // TODO: react에서는 anti-pattern인 리다이렉트 방법, 수정 필요
          window.location.href = '/';
        }

        if (!is403ErrorProcessing && statusCode === 403) {
          is403ErrorProcessing = true;

          if (errCode === '1200') {
            alert('접근 권한이 없습니다');
            // TODO: react에서는 anti-pattern인 리다이렉트 방법, 수정 필요
            window.location.href = '/error';
          }
        }

        return Promise.reject(err);
      }
    );
  }

  return instance;
};

const authInstance = axiosInstanceGenerator('auth', false);
const groupInstance = axiosInstanceGenerator('groups');

export { authInstance, groupInstance };
