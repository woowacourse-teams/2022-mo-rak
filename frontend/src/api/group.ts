import { groupInstance as axios } from './axios';
import { GroupInterface } from '../types/group';

const getGroups = () => axios.get('');

// TODO: 여기서 response에 대한 data 타입을 건네줘야 나중에 사용할 때도 편할듯하다.
const getGroupMembers = (groupCode: GroupInterface['code']) => axios.get(`/${groupCode}/members`);

// TODO: '' 해결해야할듯
const createGroup = (name: GroupInterface['name']) => axios.post('', { name });

const createInvitationCode = (groupCode: GroupInterface['code']) =>
  axios.post(`/${groupCode}/invitation`);

const participateGroup = (invitationCode: string) => axios.post(`/in/${invitationCode}`);

const getIsJoinedGroup = (invitationCode: string) => axios.get(`/in/${invitationCode}`);

const getDefaultGroup = () => axios.get('/default');

const leaveGroup = (groupCode: GroupInterface['code']) => axios.delete(`/out/${groupCode}`);

export {
  getGroups,
  createGroup,
  getGroupMembers,
  createInvitationCode,
  participateGroup,
  getIsJoinedGroup,
  getDefaultGroup,
  leaveGroup
};
