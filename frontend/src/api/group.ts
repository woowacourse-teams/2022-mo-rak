import { groupInstance as axios } from './axios';
import { GroupInterface } from '../types/group';

const getGroups = () => axios.get('');

const getGroupMembers = (groupCode: GroupInterface['code']) => axios.get(`/${groupCode}/members`);

// TODO: '' 해결해야할듯
const createGroup = (name: GroupInterface['name']) => axios.post('', { name });

const createInvitationCode = (groupCode: GroupInterface['code']) =>
  axios.post(`/${groupCode}/invitation`);

const participateGroup = (invitationCode: string) => axios.post(`/in/${invitationCode}`);

const getIsJoinedGroup = (invitationCode: string) => axios.get(`/in/${invitationCode}`);

const getDefaultGroup = () => axios.get('/default');

export {
  getGroups,
  createGroup,
  getGroupMembers,
  createInvitationCode,
  participateGroup,
  getIsJoinedGroup,
  getDefaultGroup
};
