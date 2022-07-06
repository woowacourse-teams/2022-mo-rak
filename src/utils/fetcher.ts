interface Props {
  method: 'POST' | 'PATCH' | 'PUT' | 'DELETE' | 'GET';
  path: string;
  body: object;
  token?: string;
}

type Headers = {
  [key: string]: string;
};

const fetcher = async ({ method, path, body = {}, token }: Props) => {
  const headers: Headers = { 'Content-Type': 'application/json' };
  if (token) headers.Authorization = `Bearer ${token}`;
  const response = await fetch(`${process.env.BASE_API_URL}${path}`, {
    method,
    body: JSON.stringify(body),
    headers
  });

  if (!response.ok) {
    const errorMessage = await response.json();
    throw Error(errorMessage);
  }

  if (response.status === 201) return response;

  return response.json();
};

export default fetcher;
