import fetcher from '../utils/fetcher';

const signin = (code: string) =>
  fetcher({
    method: 'POST',
    path: 'auth/signin',
    body: { code },
    isTokenNeeded: false
  });

export { signin };
