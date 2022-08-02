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

type CreateAppointmentRequest = Omit<AppointmentInterface, 'id' | 'isClosed'>;

interface Time {
  period: 'AM' | 'PM';
  hour: string;
  minute: string;
}
export { Time, AppointmentInterface, CreateAppointmentRequest };
