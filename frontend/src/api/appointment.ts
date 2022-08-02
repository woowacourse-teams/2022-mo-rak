import fetcher from '../utils/fetcher';
import { GroupInterface } from '../types/group';
import { CreateAppointmentRequest, AppointmentInfoInterface } from '../types/appointment';

const createAppointment = (
  groupCode: GroupInterface['code'],
  appointment: CreateAppointmentRequest
) => fetcher({ method: 'POST', path: `groups/${groupCode}/appointments`, body: appointment });

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

export { getAppointmentResult, getAppointment, createAppointment };
