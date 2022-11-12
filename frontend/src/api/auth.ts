import { privateAuthAxiosInstance, publicAuthAxiosInstance } from '@/api/axios';
import { EditUsernameRequest } from '@/types/auth';

const signin = (code: string) => publicAuthAxiosInstance.post('/signin', code);
const getUser = () => privateAuthAxiosInstance.get('/me');
const editUsername = (userName: EditUsernameRequest) =>
  privateAuthAxiosInstance.patch('/me/name', userName);

export { signin, getUser, editUsername };
