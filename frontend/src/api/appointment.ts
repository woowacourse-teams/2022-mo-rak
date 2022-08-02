import fetcher from '../utils/fetcher';
import { GroupInterface } from '../types/group';
import { CreateAppointmentRequest } from '../types/appointment';

const createAppointment = (
  groupCode: GroupInterface['code'],
  appointment: CreateAppointmentRequest
) => fetcher({ method: 'POST', path: `groups/${groupCode}/appointments`, body: appointment });

export { createAppointment };
