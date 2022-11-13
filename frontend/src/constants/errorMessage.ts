const POLL_ERROR = {
  NOT_EXIST: '존재하지 않는 투표입니다.',
  INVALID_CLOSE_TIME: '투표 마감시간은 현재보다 미래여야합니다.',
  SELECT_AT_LEAST_ONE_ITEM: '최소 1개의 선택항목을 선택해주세요.',
  ALREADY_CLOSED: '마감된 투표입니다.',
  EXCEED_MAX_ITEM_COUNT: '최대 10개의 선택항목만 가능합니다.',
  UNDER_MIN_ITEM_COUNT: '선택항목은 최소 2개이상이여야합니다.'
} as const;

export { POLL_ERROR };
