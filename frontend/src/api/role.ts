import { GroupInterface } from '../types/group';
import { EditRolesRequest } from '../types/role';
import { groupInstance as axios } from './axios';

const getRoles = (groupCode: GroupInterface['code']) => axios.get(`/${groupCode}/roles/names`);
const editRoles = (groupCode: GroupInterface['code'], roles: EditRolesRequest) =>
  axios.put(`/${groupCode}/roles/names`, roles);
const allocateRoles = (groupCode: GroupInterface['code']) => axios.post(`/${groupCode}/roles`);
const getRolesHistories = (groupCode: GroupInterface['code']) =>
  axios.get(`/${groupCode}/roles/histories`);

export { getRoles, editRoles, allocateRoles, getRolesHistories };
