import { Member } from '@/types/group';

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
// TODO: 타입명 변경해야할듯... Member에 description이 들어가는데 그냥 Members?!
type Members = Array<Member & Pick<SelectedPollItem, 'description'>>;

type getPollResultResponse = Array<
  PollItem & {
    members: Members;
    count: number;
  }
>;

type getPollItemsResponse = Array<PollItem & { isSelected: boolean; description: string }>;

export {
  Poll,
  Members,
  createPollRequest,
  PollItem,
  getPollResponse,
  getPollsResponse,
  getPollResultResponse,
  getPollItemsResponse,
  SelectedPollItem
};
