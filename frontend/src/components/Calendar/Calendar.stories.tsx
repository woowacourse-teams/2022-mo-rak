import { Meta, Story } from '@storybook/react';
import Calendar from './Calendar';

export default {
  title: 'Reusable Components/Calendar',
  component: Calendar
} as Meta;

const Template: Story = () => <Calendar startDate="2022-08-22" endDate="2022-08-31" />;

export const Default = Template.bind({});
