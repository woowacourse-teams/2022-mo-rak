import { Member } from './group';

interface PollInterface {
  id: number;
  title: string;
  allowedPollCount: number;
  isAnonymous: boolean;
  status: 'OPEN' | 'CLOSED';
  createdAt: string;
  closedAt: string;
  code: string;
}

interface PollItemInterface {
  id: number;
  subject: string;
}

// TODO: description 분리
type SelectedPollItem = Pick<PollItemInterface, 'id'> & {
  description: string;
};

type createPollData = Pick<
  PollInterface,
  'title' | 'allowedPollCount' | 'isAnonymous' | 'closedAt'
> & { subjects: Array<PollItemInterface['subject']> };

type getPollResponse = PollInterface & {
  isHost: boolean;
  count: number;
};

type getPollsResponse = Array<getPollResponse>;

type getPollResultResponse = Array<
  PollItemInterface & {
    members: Array<Member & Pick<SelectedPollItem, 'description'>>;
    count: number;
  }
>;

type getPollItemsResponse = Array<PollItemInterface & { isSelected: boolean; description: string }>;

export {
  PollInterface,
  createPollData,
  PollItemInterface,
  getPollResponse,
  getPollsResponse,
  getPollResultResponse,
  getPollItemsResponse,
  SelectedPollItem
};
