import { getLocalStorageItem } from './storage';

// TODO: interface와 type의 차이? 조사하고 정리해보자
interface Props {
  method: 'POST' | 'PATCH' | 'PUT' | 'DELETE' | 'GET';
  path: string;
  body?: object;
  isTokenNeeded?: boolean;
}

type Headers = {
  [key: string]: string;
};

const fetcher = async ({ method, path, body = {}, isTokenNeeded = true }: Props) => {
  const headers: Headers = { 'Content-Type': 'application/json' };
  if (isTokenNeeded) headers.Authorization = `Bearer ${getLocalStorageItem('token')}`;
  const config: {
    method: typeof method;
    headers: Headers;
    body?: string;
  } = { method, headers };

  if (Object.keys(body).length > 0) config.body = JSON.stringify(body);

  const response = await fetch(`${process.env.BASE_API_URL}/${path}`, config);

  if (!response.ok) {
    const statusCode = response.status;

    throw new Error(String(statusCode));
  }

  if (response.headers.get('content-type') === 'application/json') {
    return response.json();
  }

  return response;
};

export default fetcher;
