import { groupInstance as axios } from './axios';

import { GroupInterface } from '../types/group';
import { SlackInterface } from '../types/slack';

const resisterSlackUrl = (slackUrl: SlackInterface, groupCode: GroupInterface['code']) =>
  axios.post(`/${groupCode}/slack`, slackUrl);

export { resisterSlackUrl };
