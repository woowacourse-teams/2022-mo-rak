import fetcher from '../utils/fetcher';
import { GroupInterface } from '../types/group';
import { AppointmentInterface } from '../types/appointment';

const getAppointmentResult = (
  groupCode: GroupInterface['code'],
  appointmentCode: AppointmentInterface['code']
) =>
  fetcher({
    method: 'GET',
    path: `groups/${groupCode}/appointments/${appointmentCode}/recommendation`
  });

const getAppointment = (
  groupCode: GroupInterface['code'],
  appointmentCode: AppointmentInterface['code']
) =>
  fetcher({
    method: 'GET',
    path: `groups/${groupCode}/appointments/${appointmentCode}`
  });

export { getAppointmentResult, getAppointment };
