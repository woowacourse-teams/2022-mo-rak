import { PollCreateFormDataInterface } from '../components/PollCreateForm/PollCreateForm';
import fetcher from '../utils/fetcher';

const createPoll = (data: PollCreateFormDataInterface) =>
  fetcher({ method: 'POST', path: 'polls', body: data });

export { createPoll };
