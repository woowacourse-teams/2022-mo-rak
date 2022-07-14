import React from 'react';
import { Meta, Story } from '@storybook/react';
import Checkbox from './Checkbox';

export default {
  title: 'Reusable Components/Checkbox',
  component: Checkbox
} as Meta;

const Template: Story = (args) => <Checkbox {...args} />;
export const Default = Template.bind({});
Default.args = {};
