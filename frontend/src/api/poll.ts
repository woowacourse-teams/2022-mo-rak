import { PollInterface, PollCreateType, SelectedPollItemInterface } from '../types/poll';
import fetcher from '../utils/fetcher';
import { GroupInterface } from '../types/group';

const getPolls = (groupCode: GroupInterface['code']) =>
  fetcher({ method: 'GET', path: `groups/${groupCode}/polls` });
const getPoll = (id: PollInterface['id'], groupCode: GroupInterface['code']) =>
  fetcher({ method: 'GET', path: `groups/${groupCode}/polls/${id}` });
const getPollItems = (id: PollInterface['id'], groupCode: GroupInterface['code']) =>
  fetcher({ method: 'GET', path: `groups/${groupCode}/polls/${id}/items` });
const getPollResult = (id: PollInterface['id'], groupCode: GroupInterface['code']) =>
  fetcher({ method: 'GET', path: `groups/${groupCode}/polls/${id}/result` });
const createPoll = (poll: PollCreateType, groupCode: GroupInterface['code']) =>
  fetcher({ method: 'POST', path: `groups/${groupCode}/polls`, body: poll });
const progressPoll = (
  id: PollInterface['id'],
  items: SelectedPollItemInterface[],
  groupCode: GroupInterface['code']
) => fetcher({ method: 'PUT', path: `groups/${groupCode}/polls/${id}`, body: items });
const deletePoll = (id: PollInterface['id'], groupCode: GroupInterface['code']) =>
  fetcher({ method: 'DELETE', path: `groups/${groupCode}/polls/${id}` });
const closePoll = (id: PollInterface['id'], groupCode: GroupInterface['code']) =>
  fetcher({ method: 'PATCH', path: `groups/${groupCode}/polls/${id}/close` });

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
