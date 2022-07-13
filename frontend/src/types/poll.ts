interface PollInterface {
  id: number;
  title: string;
  allowedPollCount: number | string;
  isAnonymous: boolean;
  status: 'OPEN' | 'CLOSE';
  createdAt: string;
  closedAt: string;
  code: string;
  isHost: boolean;
}

interface PollResultInterface {
  id: number;
  count: number;
  memebers: [];
  subject: string;
}

// TODO: 네이밍
type PollCreateType = Pick<
  PollInterface,
  'title' | 'allowedPollCount' | 'isAnonymous' | 'closedAt'
> & { subjects: string[] };

interface PollItemInterface {
  id: number;
  count: number;
  subject: string;
}

type PollProgressType = {
  [key: string]: Array<PollItemInterface['id']>;
};

export { PollInterface, PollCreateType, PollItemInterface, PollProgressType, PollResultInterface };
