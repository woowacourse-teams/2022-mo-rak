import { Group } from '@/types/group';
import { privateGroupsAxiosInstance } from '@/api/axios';
import { Appointment, CreateAppointmentRequest, AvailableTimes } from '@/types/appointment';

const createAppointment = (groupCode: Group['code'], appointment: CreateAppointmentRequest) =>
  privateGroupsAxiosInstance.post(`/${groupCode}/appointments`, appointment);

const getAppointments = (groupCode: Group['code']) =>
  privateGroupsAxiosInstance.get(`/${groupCode}/appointments`);

const getAppointmentRecommendation = (
  groupCode: Group['code'],
  appointmentCode: Appointment['code']
) => privateGroupsAxiosInstance.get(`/${groupCode}/appointments/${appointmentCode}/recommendation`);

const getAppointment = (groupCode: Group['code'], appointmentCode: Appointment['code']) =>
  privateGroupsAxiosInstance.get(`/${groupCode}/appointments/${appointmentCode}`);

const progressAppointment = (
  groupCode: Group['code'],
  appointmentCode: Appointment['code'],
  availableTimes: AvailableTimes
) =>
  privateGroupsAxiosInstance.put(`/${groupCode}/appointments/${appointmentCode}`, availableTimes);

const closeAppointment = (groupCode: Group['code'], appointmentCode: Appointment['code']) =>
  privateGroupsAxiosInstance.patch(`/${groupCode}/appointments/${appointmentCode}/close`);

const deleteAppointment = (groupCode: Group['code'], appointmentCode: Appointment['code']) =>
  privateGroupsAxiosInstance.delete(`/${groupCode}/appointments/${appointmentCode}`);

const getAppointmentStatus = (groupCode: Group['code'], appointmentCode: Appointment['code']) =>
  privateGroupsAxiosInstance.get(`/${groupCode}/appointments/${appointmentCode}/status`);

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
