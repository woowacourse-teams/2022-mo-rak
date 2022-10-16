import { Group } from '../types/group';
import { groupInstance as axios } from './axios';
import { Appointment, CreateAppointmentRequest, AvailableTimes } from '../types/appointment';

const createAppointment = (groupCode: Group['code'], appointment: CreateAppointmentRequest) =>
  axios.post(`/${groupCode}/appointments`, appointment);

const getAppointments = (groupCode: Group['code']) => axios.get(`/${groupCode}/appointments`);

const getAppointmentRecommendation = (
  groupCode: Group['code'],
  appointmentCode: Appointment['code']
) => axios.get(`/${groupCode}/appointments/${appointmentCode}/recommendation`);

const getAppointment = (groupCode: Group['code'], appointmentCode: Appointment['code']) =>
  axios.get(`/${groupCode}/appointments/${appointmentCode}`);

const progressAppointment = (
  groupCode: Group['code'],
  appointmentCode: Appointment['code'],
  availableTimes: AvailableTimes
) => axios.put(`/${groupCode}/appointments/${appointmentCode}`, availableTimes);

const closeAppointment = (groupCode: Group['code'], appointmentCode: Appointment['code']) =>
  axios.patch(`/${groupCode}/appointments/${appointmentCode}/close`);

const deleteAppointment = (groupCode: Group['code'], appointmentCode: Appointment['code']) =>
  axios.delete(`/${groupCode}/appointments/${appointmentCode}`);

const getAppointmentStatus = (groupCode: Group['code'], appointmentCode: Appointment['code']) =>
  axios.get(`/${groupCode}/appointments/${appointmentCode}/status`);

export {
  getAppointments,
  getAppointment,
  createAppointment,
  progressAppointment,
  getAppointmentRecommendation,
  closeAppointment,
  deleteAppointment,
  getAppointmentStatus
};
