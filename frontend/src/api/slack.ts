import { privateGroupsAxiosInstance } from '@/api/axios';
import { Group } from '@/types/group';
import { LinkSlackRequest } from '@/types/slack';

const linkSlack = (slack: LinkSlackRequest, groupCode: Group['code']) =>
  privateGroupsAxiosInstance.post(`/${groupCode}/slack`, slack);

export { linkSlack };
