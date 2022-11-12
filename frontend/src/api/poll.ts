import { privateGroupsAxiosInstance } from '@/api/axios';
import { Group } from '@/types/group';
import { Poll, SelectedPollItem, createPollRequest } from '@/types/poll';

const getPolls = (groupCode: Group['code']) =>
  privateGroupsAxiosInstance.get(`/${groupCode}/polls`);

const getPoll = (pollCode: Poll['code'], groupCode: Group['code']) =>
  privateGroupsAxiosInstance.get(`/${groupCode}/polls/${pollCode}`);

const getPollItems = (pollCode: Poll['code'], groupCode: Group['code']) =>
  privateGroupsAxiosInstance.get(`/${groupCode}/polls/${pollCode}/items`);

const getPollResult = (pollCode: Poll['code'], groupCode: Group['code']) =>
  privateGroupsAxiosInstance.get(`/${groupCode}/polls/${pollCode}/result`);

const createPoll = (poll: createPollRequest, groupCode: Group['code']) =>
  privateGroupsAxiosInstance.post(`/${groupCode}/polls`, poll);

const progressPoll = (
  groupCode: Group['code'],
  pollCode: Poll['code'],
  items: Array<SelectedPollItem>
) => privateGroupsAxiosInstance.put(`/${groupCode}/polls/${pollCode}`, items);

const deletePoll = (pollCode: Poll['code'], groupCode: Group['code']) =>
  privateGroupsAxiosInstance.delete(`/${groupCode}/polls/${pollCode}`);

const closePoll = (pollCode: Poll['code'], groupCode: Group['code']) =>
  privateGroupsAxiosInstance.patch(`/${groupCode}/polls/${pollCode}/close`);

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
