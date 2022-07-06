import { PollDataInterface } from '../components/PollCreateForm/PollCreateForm';
import fetcher from '../utils/fetcher';

const createPoll = (pollData: PollDataInterface) =>
  fetcher({ method: 'POST', path: 'polls', body: pollData });

export { createPoll };
