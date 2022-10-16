import { groupInstance as axios } from './axios';

import { Group } from '../types/group';
import { SlackInterface } from '../types/slack';

const linkSlack = (slackUrl: SlackInterface, groupCode: Group['code']) =>
  axios.post(`/${groupCode}/slack`, slackUrl);

export { linkSlack };
