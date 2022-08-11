import React from 'react';
import { Meta, Story } from '@storybook/react';
import Calendar from './Calendar';

export default {
  title: 'Reusable Components/Calendar',
  component: Calendar
} as Meta;

const Template: Story = () => <Calendar />;

export const Default = Template.bind({});
