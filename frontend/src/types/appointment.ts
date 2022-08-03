// TODO: 변수명 고민해보기
interface AppointmentInfoInterface {
  code: string;
}

interface AppointmentInterface {
  id: number;
  title: string;
  description: string;
  startDate: string;
  endDate: string;
  startTime: string;
  endTime: string;
  durationHours: number;
  durationMinutes: number;
  isClosed: boolean;
}

interface AppointmentMemberInterface {
  id: number;
  name: string;
  profileUrl: string;
}

interface AppointmentRecommendationInterface {
  rank: number;
  recommendStartDateTime: string;
  recommendEndDateTime: string;
  availableMembers: Array<AppointmentMemberInterface>;
  unavailableMembers: Array<AppointmentMemberInterface>;
}

interface Time {
  period: 'AM' | 'PM';
  hour: string;
  minute: string;
}

type CreateAppointmentRequest = Omit<AppointmentInterface, 'id' | 'isClosed'>;
// TODO: 괜찮나?
type ProgressAppointmentRequest = Array<{ start: string; end: string }>;

export {
  Time,
  CreateAppointmentRequest,
  ProgressAppointmentRequest,
  AppointmentInfoInterface,
  AppointmentRecommendationInterface,
  AppointmentInterface
};
