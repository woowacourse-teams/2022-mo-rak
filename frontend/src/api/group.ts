import fetcher from '../utils/fetcher';
import { GroupInterface } from '../types/group';

const getGroups = () =>
  fetcher({
    method: 'GET',
    path: 'groups'
  });

const getGroupMembers = (groupCode: GroupInterface['code']) =>
  fetcher({
    method: 'GET',
    path: `groups/${groupCode}/members`
  });

const createGroup = (name: GroupInterface['name']) =>
  fetcher({
    method: 'POST',
    path: 'groups',
    body: { name }
  });

const createInvitationCode = (groupCode: GroupInterface['code']) =>
  fetcher({
    method: 'POST',
    path: `groups/${groupCode}/invitation`
  });

const participateGroup = (invitationCode: string) =>
  fetcher({
    method: 'POST',
    path: `groups/in/${invitationCode}`
  });

// 그룹 가입 여부 확인
const getIsJoinedGroup = (invitationCode: string) =>
  fetcher({
    method: 'GET',
    path: `groups/in/${invitationCode}`
  });

const getDefaultGroup = () =>
  fetcher({
    method: 'GET',
    path: 'groups/default'
  });

export {
  getGroups,
  createGroup,
  getGroupMembers,
  createInvitationCode,
  participateGroup,
  getIsJoinedGroup,
  getDefaultGroup
};
