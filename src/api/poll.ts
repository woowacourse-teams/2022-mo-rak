import { PollInterface, PollCreateType } from '../types/poll';
import fetcher from '../utils/fetcher';

// TODO: getPollInfo => info suffix에 대해 생각해보기
const getPollInfo = (id: PollInterface['id']) => fetcher({ method: 'GET', path: `polls/${id}` });
const getPollItems = (id: PollInterface['id']) =>
  fetcher({ method: 'GET', path: `polls/${id}/items` });
const getPollResult = (id: PollInterface['id']) =>
  fetcher({ method: 'GET', path: `polls/${id}/result` });
const createPoll = (poll: PollCreateType) => fetcher({ method: 'POST', path: 'polls', body: poll });
// TODO: 백엔드 API 이상으로 인해 itemIds 임시 타입 적용
const progressPoll = (id: PollInterface['id'], itemIds: string[]) =>
  fetcher({ method: 'PUT', path: `polls/${id}`, body: itemIds });

export { createPoll, getPollInfo, getPollItems, getPollResult, progressPoll };
