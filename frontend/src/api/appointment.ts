import { GroupInterface } from '../types/group';
import { groupInstance as axios } from './axios';
import { AppointmentInterface, createAppointmentData, AvailableTimes } from '../types/appointment';

const createAppointment = (groupCode: GroupInterface['code'], appointment: createAppointmentData) =>
  axios.post(`/${groupCode}/appointments`, appointment);

const getAppointments = (groupCode: GroupInterface['code']) =>
  axios.get(`/${groupCode}/appointments`);

const getAppointmentRecommendation = (
  groupCode: GroupInterface['code'],
  appointmentCode: AppointmentInterface['code']
) => axios.get(`/${groupCode}/appointments/${appointmentCode}/recommendation`);

const getAppointment = (
  groupCode: GroupInterface['code'],
  appointmentCode: AppointmentInterface['code']
) => axios.get(`/${groupCode}/appointments/${appointmentCode}`);

const progressAppointment = (
  groupCode: GroupInterface['code'],
  appointmentCode: AppointmentInterface['code'],
  availableTimes: AvailableTimes
) => axios.put(`/${groupCode}/appointments/${appointmentCode}`, availableTimes);

const closeAppointment = (
  groupCode: GroupInterface['code'],
  appointmentCode: AppointmentInterface['code']
) => axios.patch(`/${groupCode}/appointments/${appointmentCode}/close`);

export {
  getAppointments,
  getAppointment,
  createAppointment,
  progressAppointment,
  getAppointmentRecommendation,
  closeAppointment
};
