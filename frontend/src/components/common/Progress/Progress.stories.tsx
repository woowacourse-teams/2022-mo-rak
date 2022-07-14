import React from 'react';
import { Meta, Story } from '@storybook/react';
import Progress from './Progress';

export default {
  title: 'Reusable Components/Progress',
  component: Progress
} as Meta;

const Template: Story = (args) => <Progress {...args} />;

export const Default = Template.bind({});
Default.args = {
  value: '0',
  max: '100',
  width: '200px',
  height: '50px'
};
