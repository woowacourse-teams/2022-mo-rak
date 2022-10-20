import { groupInstance as axios } from './axios';
import { Group } from '../types/group';

const getGroups = () => axios.get('');

const getGroupMembers = (groupCode: Group['code']) => axios.get(`/${groupCode}/members`);

// TODO: '' 해결해야할듯
const createGroup = (name: Group['name']) => axios.post('', { name });

const createInvitationCode = (groupCode: Group['code']) => axios.post(`/${groupCode}/invitation`);

const participateGroup = (invitationCode: string) => axios.post(`/in/${invitationCode}`);

const getIsJoinedGroup = (invitationCode: string) => axios.get(`/in/${invitationCode}`);

const getDefaultGroup = () => axios.get('/default');

const leaveGroup = (groupCode: Group['code']) => axios.delete(`/out/${groupCode}`);

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
