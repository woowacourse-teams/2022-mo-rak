import Calendar from '@/components/Calendar/Calendar';

import { Meta, Story } from '@storybook/react';

export default {
  title: 'Reusable Components/Calendar',
  component: Calendar
} as Meta;

const DefaultTemplate: Story = () => <Calendar startDate="2022-08-22" endDate="2022-08-31" />;

const Default = DefaultTemplate.bind({});

export { Default };
