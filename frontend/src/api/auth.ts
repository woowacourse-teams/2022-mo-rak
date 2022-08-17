import { authInstance as axios } from './axios';

const signin = (code: string) => axios.post('/signin', code);

export { signin };
