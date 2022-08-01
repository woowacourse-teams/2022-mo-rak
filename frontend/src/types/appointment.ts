interface AppointmentInterface {
  code: string;
}

interface AppointmentInfoInterface {
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

interface AppointmentMembersInterface {
  id: number;
  name: string;
  profileUrl: string;
}

interface AppointmentResultInterface {
  rank: number;
  recommendStartDateTime: string;
  recommendEndDateTime: string;
  availableMembers: Array<AppointmentMembersInterface>;
  unavailableMembers: Array<AppointmentMembersInterface>;
}

export { AppointmentInterface, AppointmentResultInterface, AppointmentInfoInterface };
