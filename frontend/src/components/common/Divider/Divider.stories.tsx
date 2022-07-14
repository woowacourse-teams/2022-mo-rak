import React from 'react';
import theme from '../../../styles/theme';
import Divider from './Divider';

export default {
  title: 'Reusable Components/Divider',
  component: Divider
};

function Template(args) {
  return <Divider {...args} />;
}

export const Default = Template.bind({});
Default.args = {
  borderColor: theme.colors.GRAY_200
};
