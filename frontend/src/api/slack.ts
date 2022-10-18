import { groupInstance as axios } from './axios';

import { Group } from '../types/group';
import { LinkSlackRequest } from '../types/slack';

const linkSlack = (slack: LinkSlackRequest, groupCode: Group['code']) =>
  axios.post(`/${groupCode}/slack`, slack);

export { linkSlack };
