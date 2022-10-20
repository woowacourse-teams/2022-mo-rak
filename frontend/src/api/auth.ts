import { EditUsernameRequest } from '../types/auth';
import { publicAuthAxiosInstance, privateAuthAxiosInstance } from './axios';

const signin = (code: string) => publicAuthAxiosInstance.post('/signin', code);
const getUser = () => privateAuthAxiosInstance.get('/me');
const editUsername = (userName: EditUsernameRequest) =>
  privateAuthAxiosInstance.patch('/me/name', userName);

export { signin, getUser, editUsername };
