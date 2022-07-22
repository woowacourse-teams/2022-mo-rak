interface PollInterface {
  id: number;
  title: string;
  allowedPollCount: number;
  isAnonymous: boolean;
  status: 'OPEN' | 'CLOSED';
  createdAt: string;
  closedAt: string;
  code: string;
  isHost: boolean;
}

interface PollItemInterface {
  // TODO: 다음주 월요일에 이야기해보고 바꾸자
  id: number;
  subject: string;
}

// TODO: 고민해보자...PollItem에 대한 선택 항목..
interface SelectedPollItemInterface {
  itemId: PollItemInterface['id'];
  description: string;
}

type PollItemResultType = PollItemInterface & {
  members: Array<PollMembersInterface>;
  count: number;
};

interface PollMembersInterface {
  id: number;
  name: string;
  profileUrl: string;
  description: string;
}

// TODO: 네이밍
type PollCreateType = Pick<
  PollInterface,
  'title' | 'allowedPollCount' | 'isAnonymous' | 'closedAt'
> & { subjects: string[] };

type PollProgressType = {
  [key: string]: Array<PollItemInterface['id']>;
};

export {
  PollInterface,
  PollCreateType,
  PollItemInterface,
  PollProgressType,
  PollItemResultType,
  PollMembersInterface,
  SelectedPollItemInterface
};
