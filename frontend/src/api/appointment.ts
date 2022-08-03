import fetcher from '../utils/fetcher';
import { GroupInterface } from '../types/group';
import {
  AppointmentInfoInterface,
  CreateAppointmentRequest,
  ProgressAppointmentRequest
} from '../types/appointment';

const createAppointment = (
  groupCode: GroupInterface['code'],
  appointment: CreateAppointmentRequest
) => fetcher({ method: 'POST', path: `groups/${groupCode}/appointments`, body: appointment });

const getAppointmentRecommendation = (
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

const progressAppointment = (
  groupCode: GroupInterface['code'],
  appointmentCode: AppointmentInfoInterface['code'],
  availableTimes: ProgressAppointmentRequest
) =>
  fetcher({
    method: 'PUT',
    path: `groups/${groupCode}/appointments/${appointmentCode}`,
    body: availableTimes
  });

export { getAppointment, createAppointment, progressAppointment, getAppointmentRecommendation };
