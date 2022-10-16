import { Member } from './group';

type Poll = {
  id: number;
  title: string;
  allowedPollCount: number;
  isAnonymous: boolean;
  status: 'OPEN' | 'CLOSED';
  createdAt: string;
  closedAt: string;
  code: string;
};

type PollItem = {
  id: number;
  subject: string;
};

// TODO: description 분리
type SelectedPollItem = Pick<PollItem, 'id'> & {
  description: string;
};

type createPollRequest = Pick<Poll, 'title' | 'allowedPollCount' | 'isAnonymous' | 'closedAt'> & {
  subjects: Array<PollItem['subject']>;
};

type getPollResponse = Poll & {
  isHost: boolean;
  count: number;
};

type getPollsResponse = Array<getPollResponse>;

type getPollResultResponse = Array<
  PollItem & {
    members: Array<Member & Pick<SelectedPollItem, 'description'>>;
    count: number;
  }
>;

type getPollItemsResponse = Array<PollItem & { isSelected: boolean; description: string }>;

export {
  Poll,
  createPollRequest,
  PollItem,
  getPollResponse,
  getPollsResponse,
  getPollResultResponse,
  getPollItemsResponse,
  SelectedPollItem
};
