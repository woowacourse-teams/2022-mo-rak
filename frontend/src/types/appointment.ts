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
  durationHour: number;
  durationMinute: number;
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

export { AppointmentInfoInterface, AppointmentResultInterface, AppointmentInterface };
