const POLL_ERROR = {
  NOT_EXIST: '존재하지 않는 투표입니다.',
  ALREADY_CLOSED: '마감된 투표입니다.',
  INVALID_CLOSE_TIME: '투표 마감시간은 현재보다 미래여야합니다.',
  SELECT_AT_LEAST_ONE_ITEM: '최소 1개의 선택항목을 선택해주세요.',
  EXCEED_MAX_ITEM_COUNT: '최대 10개의 선택항목만 가능합니다.',
  UNDER_MIN_ITEM_COUNT: '선택항목은 최소 2개이상이여야합니다.'
} as const;

const APPOINTMENT_ERROR = {
  NOT_EXIST: '존재하지 않는 약속잡기입니다.',
  ALREADY_CLOSED: '마감된 약속잡기입니다.',
  PAST_TIME: '현재보다 과거의 시간을 선택할 수 없습니다.',
  EMPTY_DATE: '달력을 활용하여 약속잡기 날짜를 지정해주세요.',
  INVALID_DURATION_INPUT: '약속잡기 진행시간은 약속잡기 시간(가능시간제한)보다 짧아야 합니다.',
  UNFORMATTED_DURATION_INPUT: '약속잡기 진행시간은 30분에서 24시간 사이여야 합니다.',
  INVALID_CLOSE_TIME_INPUT: '마감 시간은 현재 시간과 마지막 날짜의 최대 가능 시간사이여야합니다.',
  INVALID_LAST_DATE_AND_TIME: '약속잡기의 마지막 날짜와 시간은 현재보다 과거일 수 없습니다.',
  EMPTY_TITLE_AND_DESCRIPTION: '제목과 설명은 공백일 수 없습니다.',
  DISABLED_DELETE:
    '현재 사이트 이용이 원활하지 않아 삭제가 진행되지 않았습니다. 잠시후 이용해주세요.'
} as const;

const ROLE_ERROR = {
  INVALID_COUNT: '역할의 개수는 1개 이상, 100개 이하여야합니다.',
  EXCEED_MEMBER_COUNT: '역할은 팀 멤버 수 이하여야합니다.',
  NOT_EXIST: '역할이 존재하지 않습니다.',
  UNDER_MIN_COUNT: '역할은 최소 1개 이상 필요합니다.',
  EXCEED_MAX_NAME_LENGTH: '역할의 이름은 20자를 넘을 수 없습니다.'
};

const AUTH_ERROR = {
  EMPTY_ACCESS_TOKEN: '로그인 해주세요 😀',
  NO_ACCESS_AUTHORITY: '접근 권한이 없습니다.',
  FAILED_LOGIN: '로그인에 실패하였습니다. 다시 시도해주세요.'
} as const;

const MODAL_ERROR = {
  EMPTY_GROUP_NAME: '팀 이름은 공백일 수 없습니다.',
  CAN_NOT_FIND_GROUP: '찾으시는 그룹이 없습니다. 코드를 다시 확인해주세요.',
  ALREADY_PARTICIPATED_GROUP: '이미 참여하고 있는 그룹입니다.',
  INVALID_SLACK_URL: 'http로 시작하는 올바른 url을 입력해주세요.',
  EMPTY_NICKNAME: '닉네임은 공백일 수 없습니다.'
} as const;

const GROUP_ERROR = {
  EMPTY_NAME: '그룹이름을 입력해주세요.',
  EMPTY_INVITATION_CODE: '그룹 코드를 입력해주세요.',
  INVALID_INVITATION_CODE: '그룹 코드를 다시 한 번 확인해주세요.',
  ALREADY_PARTICIPATED: '이미 가입된 모임입니다.',
  INVALID_INVITATION_URL: '유효하지 않은 초대장입니다.',
  NOT_PARTICIPATED: '잘못된 접근입니다.'
} as const;

const CONTEXT_ERROR = {
  NO_AUTH_CONTEXT: 'AuthContext를 찾을 수 없습니다.',
  NO_AUTH_DISPATCH_CONTEXT: 'AuthDispatchContext를 찾을 수 없습니다.',
  NO_GROUP_MEMBERS_CONTEXT: 'GroupMembersContext를 찾을 수 없습니다.',
  NO_GROUP_MEMBERS_DISPATCH_CONTEXT: 'GroupMembersDispatchContext를 찾을 수 없습니다.',
  NO_NAVIGATION_BAR_CONTEXT: 'NavigationBarContext를 찾을 수 없습니다.',
  NO_NAVIGATION_BAR_DISPATCH_CONTEXT: 'NavigationBarDispatchContext를 찾을 수 없습니다.'
} as const;

export {
  POLL_ERROR,
  APPOINTMENT_ERROR,
  ROLE_ERROR,
  AUTH_ERROR,
  MODAL_ERROR,
  GROUP_ERROR,
  CONTEXT_ERROR
};
