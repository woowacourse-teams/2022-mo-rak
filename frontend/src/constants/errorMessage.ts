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
  PAST_TIME: '현재보다 과거의 시간을 선택할 수 없습니다',
  EMPTY_DATE: '달력을 활용하여 약속잡기 날짜를 지정해주세요',
  INVALID_DURATION_INPUT: '약속잡기 진행시간은 약속잡기 시간(가능시간제한)보다 짧아야 합니다.',
  UNFORMATTED_DURATION_INPUT: '약속잡기 진행시간은 30분에서 24시간 사이여야 합니다.',
  INVALID_CLOSE_TIME_INPUT: '마감 시간은 현재 시간과 마지막 날짜의 최대 가능 시간사이여야합니다',
  INVALID_LAST_DATE_AND_TIME: '약속잡기의 마지막 날짜와 시간은 현재보다 과거일 수 없습니다..',
  EMPTY_TITLE_AND_DESCRIPTION: '제목과 설명은 공백일 수 없습니다.',
  DISABLED_DELETE:
    '현재 사이트 이용이 원활하지 않아 삭제가 진행되지 않았습니다. 잠시후 이용해주세요'
} as const;

const ROLE_ERROR = {
  INVALID_COUNT: '역할의 개수는 1개 이상, 100개 이하여야합니다',
  EXCEED_MEMBER_COUNT: '역할은 팀 멤버 수 이하여야합니다',
  NOT_EXIST: '역할이 존재하지 않습니다',
  UNDER_MIN_COUNT: '역할은 최소 1개 이상 필요합니다!',
  EXCEED_MAX_NAME_LENGTH: '역할의 이름은 20자를 넘을 수 없습니다'
};

export { POLL_ERROR, APPOINTMENT_ERROR, ROLE_ERROR };
