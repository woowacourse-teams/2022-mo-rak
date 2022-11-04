import { Meta, Story } from '@storybook/react';
import Calendar from './Calendar';

export default {
  title: 'Reusable Components/Calendar',
  component: Calendar
} as Meta;

const DefaultTemplate: Story = () => <Calendar startDate="2022-08-22" endDate="2022-08-31" />;

const Default = DefaultTemplate.bind({});

export { Default };
