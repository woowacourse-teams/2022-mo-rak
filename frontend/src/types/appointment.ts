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

interface AppointmentResultInterface {
  rank: number;
  recommendStartDateTime: string;
  recommendEndDateTime: string;
  availableMembers: Array<AppointmentMemberInterface>;
  unavailableMembers: Array<AppointmentMemberInterface>;
}

type CreateAppointmentRequest = Omit<AppointmentInterface, 'id' | 'isClosed'>;

interface Time {
  period: 'AM' | 'PM';
  hour: string;
  minute: string;
}

export {
  Time,
  CreateAppointmentRequest,
  AppointmentInfoInterface,
  AppointmentResultInterface,
  AppointmentInterface
};
