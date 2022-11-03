import { privateGroupsAxiosInstance } from './axios';
import { Group } from '@/types/group';

const getGroups = () => privateGroupsAxiosInstance.get('');

const getGroupMembers = (groupCode: Group['code']) =>
  privateGroupsAxiosInstance.get(`/${groupCode}/members`);

// TODO: '' 해결해야할듯
const createGroup = (name: Group['name']) => privateGroupsAxiosInstance.post('', { name });

const createInvitationCode = (groupCode: Group['code']) =>
  privateGroupsAxiosInstance.post(`/${groupCode}/invitation`);

const participateGroup = (invitationCode: string) =>
  privateGroupsAxiosInstance.post(`/in/${invitationCode}`);

const getIsJoinedGroup = (invitationCode: string) =>
  privateGroupsAxiosInstance.get(`/in/${invitationCode}`);

const getDefaultGroup = () => privateGroupsAxiosInstance.get('/default');

const leaveGroup = (groupCode: Group['code']) =>
  privateGroupsAxiosInstance.delete(`/out/${groupCode}`);

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
