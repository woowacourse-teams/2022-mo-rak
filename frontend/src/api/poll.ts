// NOTE: 왜 axios로 치환해주었냐면, axios라는 것을 사용해서 http 요청을 하는 것을 알려주기 위해서 사용을 했고,
// groupInstance보다는 더 명확할 것 같아서 사용을 했습니다.
import { groupInstance as axios } from './axios';
import { PollInterface, createPollData, SelectedPollItem } from '../types/poll';
import { GroupInterface } from '../types/group';

const getPolls = (groupCode: GroupInterface['code']) => axios.get(`/${groupCode}/polls`);

const getPoll = (pollCode: PollInterface['code'], groupCode: GroupInterface['code']) =>
  axios.get(`/${groupCode}/polls/${pollCode}`);

const getPollItems = (pollCode: PollInterface['code'], groupCode: GroupInterface['code']) =>
  axios.get(`/${groupCode}/polls/${pollCode}/items`);

const getPollResult = (pollCode: PollInterface['code'], groupCode: GroupInterface['code']) =>
  axios.get(`/${groupCode}/polls/${pollCode}/result`);

const createPoll = (poll: createPollData, groupCode: GroupInterface['code']) =>
  axios.post(`/${groupCode}/polls`, poll);

const progressPoll = (
  pollCode: PollInterface['code'],
  items: Array<SelectedPollItem>,
  groupCode: GroupInterface['code']
) => axios.put(`/${groupCode}/polls/${pollCode}`, items);

const deletePoll = (pollCode: PollInterface['code'], groupCode: GroupInterface['code']) =>
  axios.delete(`/${groupCode}/polls/${pollCode}`);

const closePoll = (pollCode: PollInterface['code'], groupCode: GroupInterface['code']) =>
  axios.patch(`/${groupCode}/polls/${pollCode}/close`);

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
