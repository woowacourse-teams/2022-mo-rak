import { Group } from '../types/group';
import { EditRolesRequest } from '../types/role';
import { privateGroupsAxiosInstance } from './axios';

const getRoles = (groupCode: Group['code']) =>
  privateGroupsAxiosInstance.get(`/${groupCode}/roles/names`);
const editRoles = (groupCode: Group['code'], roles: EditRolesRequest) =>
  privateGroupsAxiosInstance.put(`/${groupCode}/roles/names`, roles);
const allocateRoles = (groupCode: Group['code']) =>
  privateGroupsAxiosInstance.post(`/${groupCode}/roles`);
const getRolesHistories = (groupCode: Group['code']) =>
  privateGroupsAxiosInstance.get(`/${groupCode}/roles/histories`);

export { getRoles, editRoles, allocateRoles, getRolesHistories };
