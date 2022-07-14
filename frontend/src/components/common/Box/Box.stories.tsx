import React from 'react';
import { Meta, Story } from '@storybook/react';

import Box from './Box';

export default {
  title: 'Reusable Components/Box',
  component: Box
} as Meta;

const Template: Story = (args) => <Box {...args} />;
export const Default = Template.bind({});
Default.args = {
  width: '84.4rem',
  height: '65.2rem',
  borderRadius: '15px'
};
