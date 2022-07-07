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

// TODO: 네이밍
type PollCreateType = Pick<
  PollInterface,
  'id' | 'title' | 'allowedPollCount' | 'isAnonymous' | 'closedAt'
> & { subjects: string[] };

interface PollItemInterface {
  id: string;
  subject: string;
}

type PollProgressType = {
  [key: string]: Array<Pick<PollItemInterface, 'id'>>;
};

export { PollInterface, PollCreateType, PollItemInterface, PollProgressType };
