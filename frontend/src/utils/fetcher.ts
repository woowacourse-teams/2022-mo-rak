interface Props {
  method: 'POST' | 'PATCH' | 'PUT' | 'DELETE' | 'GET';
  path: string;
  body?: object;
  token?: string;
}

// TODO: type명 이게 최선일까?
type Headers = {
  [key: string]: string;
};

const fetcher = async ({ method, path, body = {}, token = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjU3OTYxMzE3LCJleHAiOjE2ODk1Mjk3MTd9.NrQzpKPjKTyfLZFNsL90KBqV_E_ps0ofp3Ne81fSgoU' }: Props) => {
  const headers: Headers = { 'Content-Type': 'application/json' };
  if (token) headers.Authorization = `Bearer ${token}`;

  // TODO: refactoring 필요
  const config = Object.keys(body).length === 0
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
    const errorMessage = await response.json();

    throw Error(errorMessage);
  }

  if (response.headers.get('content-type') === 'application/json') {
    return response.json();
  }

  return response;
};

export default fetcher;
