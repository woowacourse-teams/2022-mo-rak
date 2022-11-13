import { Group } from '@/types/group';

const SUCCESS_MESSAGE = {
  CHANGE_NICKNAME: '닉네임이 변경되었습니다 👋',
  LINK_SLACK: '슬랙 채널과 연동이 완료되었습니다 🎉',
  COPY_SHARE_LINK: '약속잡기 결과를 공유할 수 있는 링크가 복사되었습니다 👋',
  COPY_PROCESS_SHARE_LINK: '투표를 진행할 수 있는 링크가 복사되었습니다 👋',
  COPY_RESULT_SHARE_LINK: '투표 결과를 공유할 수 있는 링크가 복사되었습니다 👋',
  COPY_INVITATION_LINK: '초대 링크가 복사되었습니다 💌'
} as const;

const CONFIRM_MESSAGE = {
  LOGOUT: '로그아웃을 하시겠습니까?',
  EXIT_GROUP: '그룹을 나가시겠습니까?',
  REFUSE_INVITATION: '초대를 거절하시겠습니까?',
  DELETE_POLL_ITEM: '해당 항목을 삭제하시겠습니까?',
  DELETE_POLL: '투표를 삭제하시겠습니까?',
  CLOSE_POLL: '투표를 마감하시겠습니까?',
  CLOSE_APPOINTMENT: '약속잡기를 마감하시겠습니까?',
  DELETE_APPOINTMENT: '약속잡기를 삭제하시겠습니까?',
  DELETE_ROLE: '역할을 삭제하시겠습니까?',
  MOVE_GROUP: (groupName: Group['name']) => `${groupName} 그룹으로 이동하시겠습니까?`
} as const;

export { SUCCESS_MESSAGE, CONFIRM_MESSAGE };
