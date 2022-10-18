// NOTE: 왜 axios로 치환해주었냐면, axios라는 것을 사용해서 http 요청을 하는 것을 알려주기 위해서 사용을 했고,
// groupInstance보다는 더 명확할 것 같아서 사용을 했습니다.
import { groupInstance as axios } from './axios';
import { Poll, createPollRequest, SelectedPollItem } from '../types/poll';
import { Group } from '../types/group';

const getPolls = (groupCode: Group['code']) => axios.get(`/${groupCode}/polls`);

const getPoll = (pollCode: Poll['code'], groupCode: Group['code']) =>
  axios.get(`/${groupCode}/polls/${pollCode}`);

const getPollItems = (pollCode: Poll['code'], groupCode: Group['code']) =>
  axios.get(`/${groupCode}/polls/${pollCode}/items`);

const getPollResult = (pollCode: Poll['code'], groupCode: Group['code']) =>
  axios.get(`/${groupCode}/polls/${pollCode}/result`);

const createPoll = (poll: createPollRequest, groupCode: Group['code']) =>
  axios.post(`/${groupCode}/polls`, poll);

const progressPoll = (
  groupCode: Group['code'],
  pollCode: Poll['code'],
  items: Array<SelectedPollItem>
) => axios.put(`/${groupCode}/polls/${pollCode}`, items);

const deletePoll = (pollCode: Poll['code'], groupCode: Group['code']) =>
  axios.delete(`/${groupCode}/polls/${pollCode}`);

const closePoll = (pollCode: Poll['code'], groupCode: Group['code']) =>
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
