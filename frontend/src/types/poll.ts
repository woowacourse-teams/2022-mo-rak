interface PollInterface {
  id: number;
  title: string;
  allowedPollCount: number | string;
  isAnonymous: boolean;
  status: 'OPEN' | 'CLOSED';
  createdAt: string;
  closedAt: string;
  code: string;
  isHost: boolean;
}

interface PollItemInterface {
  id: number;
  count: number;
  subject: string;
}

// TODO: 고민해보자...
interface SelectedPollItemInterface {
  itemId: PollItemInterface['id'];
  description: string;
}

type PollItemResultType = PollItemInterface & {
  members: [];
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
