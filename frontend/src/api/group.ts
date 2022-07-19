import fetcher from '../utils/fetcher';
import { GroupInterface } from '../types/group';

const getGroups = () =>
  fetcher({
    method: 'GET',
    path: 'groups',
    token:
      'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjU3OTYxMzE3LCJleHAiOjE2ODk1Mjk3MTd9.NrQzpKPjKTyfLZFNsL90KBqV_E_ps0ofp3Ne81fSgoU'
  });

const getGroupMembers = (groupCode: GroupInterface['code']) =>
  fetcher({
    method: 'GET',
    path: `groups/${groupCode}/members`,
    token:
      'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjU3OTYxMzE3LCJleHAiOjE2ODk1Mjk3MTd9.NrQzpKPjKTyfLZFNsL90KBqV_E_ps0ofp3Ne81fSgoU'
  });

const createGroup = (name: GroupInterface['name']) =>
  fetcher({
    method: 'POST',
    path: 'groups',
    body: { name },
    // TODO: 임시 제거
    token:
      'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjU3OTYxMzE3LCJleHAiOjE2ODk1Mjk3MTd9.NrQzpKPjKTyfLZFNsL90KBqV_E_ps0ofp3Ne81fSgoU'
  });

const createInvitationCode = (groupCode: GroupInterface['code']) =>
  fetcher({
    method: 'POST',
    path: `groups/${groupCode}/invitation`,
    token:
      'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjU3OTYxMzE3LCJleHAiOjE2ODk1Mjk3MTd9.NrQzpKPjKTyfLZFNsL90KBqV_E_ps0ofp3Ne81fSgoU'
  });

const participateGroup = (invitationCode: string) =>
  fetcher({
    method: 'POST',
    path: `groups/in/${invitationCode}`,
    token:
      'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjU3OTYxMzE3LCJleHAiOjE2ODk1Mjk3MTd9.NrQzpKPjKTyfLZFNsL90KBqV_E_ps0ofp3Ne81fSgoU'
  });

// 그룹 가입 여부 확인
const getIsJoinedGroup = (invitationCode: string) =>
  fetcher({
    method: 'GET',
    path: `groups/in/${invitationCode}`,
    token:
      'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjU3OTYxMzE3LCJleHAiOjE2ODk1Mjk3MTd9.NrQzpKPjKTyfLZFNsL90KBqV_E_ps0ofp3Ne81fSgoU'
  });

const getDefaultGroup = () =>
  fetcher({
    method: 'GET',
    path: 'groups/default',
    token:
      'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjU3OTYxMzE3LCJleHAiOjE2ODk1Mjk3MTd9.NrQzpKPjKTyfLZFNsL90KBqV_E_ps0ofp3Ne81fSgoU'
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
