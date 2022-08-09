import { MemberInterface } from './group';

interface AppointmentInterface {
  id: number;
  code: string;
  title: string;
  description: string;
  startDate: string;
  endDate: string;
  startTime: string;
  endTime: string;
  durationHours: number;
  durationMinutes: number;
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

type createAppointmentData = Omit<AppointmentInterface, 'id' | 'code'>;

type getAppointmentResponse = AppointmentInterface & {
  isClosed: boolean;
};

export {
  Time,
  createAppointmentData,
  getAppointmentResponse,
  AvailableTimes,
  AppointmentRecommendationInterface,
  AppointmentInterface
};
