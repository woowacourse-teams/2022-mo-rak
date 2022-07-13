import React from 'react';
import { Meta, Story } from '@storybook/react';
import theme from '../../../styles/theme';

import Input from './Input';

export default {
  title: 'Reusable Components/Input',
  component: Input
} as Meta;

const Template: Story = (args) => <Input {...args} />;

export const Default = Template.bind({});
Default.args = {
  fontSize: '5rem',
  textAlign: 'center',
  color: theme.colors.BLACK_100
};
