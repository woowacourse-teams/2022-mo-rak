import fetcher from '../utils/fetcher';
import { GroupInterface } from '../types/group';
import { AppointmentInterface, createAppointmentData, AvailableTimes } from '../types/appointment';

const createAppointment = (groupCode: GroupInterface['code'], appointment: createAppointmentData) =>
  fetcher({ method: 'POST', path: `groups/${groupCode}/appointments`, body: appointment });

const getAppointmentRecommendation = (
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

const progressAppointment = (
  groupCode: GroupInterface['code'],
  appointmentCode: AppointmentInterface['code'],
  availableTimes: AvailableTimes
) =>
  fetcher({
    method: 'PUT',
    path: `groups/${groupCode}/appointments/${appointmentCode}`,
    body: availableTimes
  });

const closeAppointment = (
  groupCode: GroupInterface['code'],
  appointmentCode: AppointmentInterface['code']
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
