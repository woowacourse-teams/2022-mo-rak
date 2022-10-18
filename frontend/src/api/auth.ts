import { EditUserNameRequest } from '../types/auth';
import { publicAuthAxiosInstance, privateAuthAxiosInstance } from './axios';

const signin = (code: string) => publicAuthAxiosInstance.post('/signin', code);
const getUser = () => privateAuthAxiosInstance.get('/me');
const editUserName = (userName: EditUserNameRequest) =>
  privateAuthAxiosInstance.patch('/me/name', userName);

export { signin, getUser, editUserName };
