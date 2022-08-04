import { PollInterface, PollCreateType, SelectedPollItemInterface } from '../types/poll';
import fetcher from '../utils/fetcher';
import { GroupInterface } from '../types/group';

const getPolls = (groupCode: GroupInterface['code']) =>
  fetcher({ method: 'GET', path: `groups/${groupCode}/polls` });
const getPoll = (pollCode: PollInterface['code'], groupCode: GroupInterface['code']) =>
  fetcher({ method: 'GET', path: `groups/${groupCode}/polls/${pollCode}` });
const getPollItems = (pollCode: PollInterface['code'], groupCode: GroupInterface['code']) =>
  fetcher({ method: 'GET', path: `groups/${groupCode}/polls/${pollCode}/items` });
const getPollResult = (pollCode: PollInterface['code'], groupCode: GroupInterface['code']) =>
  fetcher({ method: 'GET', path: `groups/${groupCode}/polls/${pollCode}/result` });
const createPoll = (poll: PollCreateType, groupCode: GroupInterface['code']) =>
  fetcher({ method: 'POST', path: `groups/${groupCode}/polls`, body: poll });
const progressPoll = (
  pollCode: PollInterface['code'],
  items: SelectedPollItemInterface[],
  groupCode: GroupInterface['code']
) => fetcher({ method: 'PUT', path: `groups/${groupCode}/polls/${pollCode}`, body: items });
const deletePoll = (pollCode: PollInterface['code'], groupCode: GroupInterface['code']) =>
  fetcher({ method: 'DELETE', path: `groups/${groupCode}/polls/${pollCode}` });
const closePoll = (pollCode: PollInterface['code'], groupCode: GroupInterface['code']) =>
  fetcher({ method: 'PATCH', path: `groups/${groupCode}/polls/${pollCode}/close` });

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
