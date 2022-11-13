import axios from 'axios';
import {
  getLocalStorageItem,
  removeLocalStorageItem,
  saveSessionStorageItem
} from '@/utils/storage';
import { AUTH_ERROR } from '@/constants/errorMessage';

type Path = 'auth' | 'groups';

const axiosInstanceGenerator = (path: Path) => (isAuthRequired: boolean) => {
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
        const url = err.response.config.url;
        const statusCode = err.response.status;

        if (!is401ErrorProcessing && statusCode === 401) {
          is401ErrorProcessing = true;

          const isInvitationPage = url.includes('in');
          if (isInvitationPage) {
            saveSessionStorageItem('redirectUrl', window.location.pathname);
          }

          alert(AUTH_ERROR.EMPTY_ACCESS_TOKEN);
          removeLocalStorageItem('token');
          // TODO: react에서는 anti-pattern인 리다이렉트 방법, 수정 필요
          // TODO: 강제로 html을 다시 받아오는 거라, 추후 SPA 방식의 navigation을 하게 되면, 여러 개의 alert가 뜰 듯, 수정 필요...
          window.location.href = '/';
        }

        if (!is403ErrorProcessing && statusCode === 403) {
          is403ErrorProcessing = true;

          alert(AUTH_ERROR.NO_ACCESS_AUTHORITY);
          // TODO: react에서는 anti-pattern인 리다이렉트 방법, 수정 필요
          window.location.href = '/error';
        }

        return Promise.reject(err);
      }
    );
  }

  return instance;
};

const publicAuthAxiosInstance = axiosInstanceGenerator('auth')(false);
const privateAuthAxiosInstance = axiosInstanceGenerator('auth')(true);
const privateGroupsAxiosInstance = axiosInstanceGenerator('groups')(true);

export { publicAuthAxiosInstance, privateAuthAxiosInstance, privateGroupsAxiosInstance };
