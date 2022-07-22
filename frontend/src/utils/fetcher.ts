import { getLocalStorageItem } from './storage';

// TODO: interface와 type의 차이? 조사하고 정리해보자
interface Props {
  method: 'POST' | 'PATCH' | 'PUT' | 'DELETE' | 'GET';
  path: string;
  body?: object;
  isTokenNeeded?: boolean;
}

// TODO: type명 이게 최선일까?
type Headers = {
  [key: string]: string;
};

const fetcher = async ({ method, path, body = {}, isTokenNeeded = true }: Props) => {
  const headers: Headers = { 'Content-Type': 'application/json' };
  if (isTokenNeeded) headers.Authorization = `Bearer ${getLocalStorageItem('token')}`;

  // TODO: refactoring 필요
  const config =
    Object.keys(body).length === 0
      ? {
          method,
          headers
        }
      : {
          method,
          headers,
          body: JSON.stringify(body)
        };

  const response = await fetch(`${process.env.BASE_API_URL}${path}`, config);

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
