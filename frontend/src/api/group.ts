import fetcher from '../utils/fetcher';

const createGroup = (name: any) => fetcher({ method: 'POST', path: 'groups', body: { name } });

export { createGroup };
