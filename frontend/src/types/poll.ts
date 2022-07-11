interface PollInterface {
  id?: string;
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
  id: string;
  count: number;
  memebers: [];
  subject: string;
}

// TODO: 네이밍
type PollCreateType = Pick<
  PollInterface,
  'id' | 'title' | 'allowedPollCount' | 'isAnonymous' | 'closedAt'
> & { subjects: string[] };

interface PollItemInterface {
  id: string;
  count: number;
  subject: string;
}

type PollProgressType = {
  [key: string]: Array<PollItemInterface['id']>;
};

export { PollInterface, PollCreateType, PollItemInterface, PollProgressType, PollResultInterface };
