import fetcher from '../utils/fetcher';
import { GroupInterface } from '../types/group';
import { AppointmentInfoInterface } from '../types/appointment';

const getAppointmentResult = (
  groupCode: GroupInterface['code'],
  appointmentCode: AppointmentInfoInterface['code']
) =>
  fetcher({
    method: 'GET',
    path: `groups/${groupCode}/appointments/${appointmentCode}/recommendation`
  });

const getAppointment = (
  groupCode: GroupInterface['code'],
  appointmentCode: AppointmentInfoInterface['code']
) =>
  fetcher({
    method: 'GET',
    path: `groups/${groupCode}/appointments/${appointmentCode}`
  });

export { getAppointmentResult, getAppointment };
