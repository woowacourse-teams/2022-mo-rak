import { MemberInterface } from './group';

type Appointment = {
  id: number;
  code: string;
  title: string;
  description: string;
  durationHours: number;
  durationMinutes: number;
  startDate: string;
  endDate: string;
  startTime: string;
  endTime: string;
  closedAt: string;
  isClosed: boolean;
};

type AppointmentRecommendation = {
  rank: number;
  recommendStartDateTime: string;
  recommendEndDateTime: string;
  availableMembers: Array<MemberInterface>;
  unavailableMembers: Array<MemberInterface>;
};

type Time = {
  period: 'AM' | 'PM';
  hour: string;
  minute: string;
};

type AvailableTimes = Array<{ start: string; end: string }>;
type CreateAppointmentRequest = Omit<Appointment, 'id' | 'code' | 'isClosed'>;
type GetAppointmentResponse = Appointment & {
  isHost: boolean;
};
type GetAppointmentsResponse = Array<
  Omit<Appointment, 'startDate' | 'endDate' | 'startTime' | 'endTime'> & {
    count: number;
  }
>;
type AppointmentStatus = 'CLOSED' | 'OPEN';

export {
  Time,
  CreateAppointmentRequest,
  GetAppointmentsResponse,
  GetAppointmentResponse,
  AvailableTimes,
  AppointmentRecommendation,
  Appointment,
  AppointmentStatus
};
