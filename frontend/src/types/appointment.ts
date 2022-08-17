import { MemberInterface } from './group';

interface AppointmentInterface {
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
}

interface AppointmentRecommendationInterface {
  rank: number;
  recommendStartDateTime: string;
  recommendEndDateTime: string;
  availableMembers: Array<MemberInterface>;
  unavailableMembers: Array<MemberInterface>;
}

interface Time {
  period: 'AM' | 'PM';
  hour: string;
  minute: string;
}

type AvailableTimes = Array<{ start: string; end: string }>;

type createAppointmentData = Omit<AppointmentInterface, 'id' | 'code' | 'isClosed'>;

type getAppointmentsResponse = Array<
  Omit<AppointmentInterface, 'startDate' | 'endDate' | 'startTime' | 'endTime'> & {
    count: number;
  }
>;

export {
  Time,
  createAppointmentData,
  getAppointmentsResponse,
  AvailableTimes,
  AppointmentRecommendationInterface,
  AppointmentInterface
};
