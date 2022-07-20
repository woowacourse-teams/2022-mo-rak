import { PollInterface, PollCreateType, SelectedPollItemInterface } from '../types/poll';
import fetcher from '../utils/fetcher';

const getPolls = () => fetcher({ method: 'GET', path: 'polls/' });
const getPoll = (id: PollInterface['id']) => fetcher({ method: 'GET', path: `polls/${id}` });
const getPollItems = (id: PollInterface['id']) =>
  fetcher({ method: 'GET', path: `polls/${id}/items` });
const getPollResult = (id: PollInterface['id']) =>
  fetcher({ method: 'GET', path: `polls/${id}/result` });
const createPoll = (poll: PollCreateType) => fetcher({ method: 'POST', path: 'polls', body: poll });
// TODO: 백엔드 API 이상으로 인해 itemIds 임시 타입 적용
const progressPoll = (id: PollInterface['id'], items: SelectedPollItemInterface[]) =>
  fetcher({ method: 'PUT', path: `polls/${id}`, body: items });
const deletePoll = (id: PollInterface['id']) => fetcher({ method: 'DELETE', path: `polls/${id}` });
const closePoll = (id: PollInterface['id']) =>
  fetcher({ method: 'PATCH', path: `polls/${id}/close` });

export {
  getPolls,
  createPoll,
  getPoll,
  getPollItems,
  getPollResult,
  progressPoll,
  deletePoll,
  closePoll
};
