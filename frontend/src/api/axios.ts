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
    instance.interceptors.response.use(
      (res) => {
        is401ErrorProcessing = false;

        return res;
      },
      (err) => {
        const errCode = err.response.data?.codeNumber;
        const url = err.response.config.url;

        if (!is401ErrorProcessing && errCode === ('0201' || '0202')) {
          const isInvitationPage = url.includes('in');
          if (isInvitationPage) {
            saveSessionStorageItem('redirectUrl', window.location.pathname);
          }

          is401ErrorProcessing = true;
          alert(`로그인 해주세요😀`);
          removeLocalStorageItem('token');
          // TODO: react에서는 anti-pattern인 리다이렉트 방법, 수정 필요
          window.location.href = '/';
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
