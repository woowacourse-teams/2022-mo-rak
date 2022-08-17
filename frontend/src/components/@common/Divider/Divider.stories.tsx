import React from 'react';
import { Meta, Story } from '@storybook/react';
import theme from '../../../styles/theme';
import Divider from './Divider';

export default {
  title: 'Reusable Components/Divider',
  component: Divider
} as Meta;

const Template: Story = (args) => <Divider {...args} />;

export const Default = Template.bind({});
Default.args = {
  borderColor: theme.colors.GRAY_200
};
