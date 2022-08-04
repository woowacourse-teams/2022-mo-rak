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

const closeAppointment = (
  groupCode: GroupInterface['code'],
  appointmentCode: AppointmentInfoInterface['code']
) =>
  // TODO: appointment, poll, group등 따로 api url을 만들어서 중복을 줄여줄 수도 있겠다. 해보자
  fetcher({ method: 'PATCH', path: `groups/${groupCode}/appointments/${appointmentCode}/close` });

export {
  getAppointment,
  createAppointment,
  progressAppointment,
  getAppointmentRecommendation,
  closeAppointment
};
