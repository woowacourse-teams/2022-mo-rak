import { Group } from '../types/group';
import { EditRolesRequest } from '../types/role';
import { groupInstance as axios } from './axios';

const getRoles = (groupCode: Group['code']) => axios.get(`/${groupCode}/roles/names`);
const editRoles = (groupCode: Group['code'], roles: EditRolesRequest) =>
  axios.put(`/${groupCode}/roles/names`, roles);
const allocateRoles = (groupCode: Group['code']) => axios.post(`/${groupCode}/roles`);
const getRolesHistories = (groupCode: Group['code']) => axios.get(`/${groupCode}/roles/histories`);

export { getRoles, editRoles, allocateRoles, getRolesHistories };
