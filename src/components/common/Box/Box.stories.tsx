import React from 'react';

import Box from './Box';

export default {
  title: 'Reusable Components/Box',
  component: Box
};

function Template(args) {
  return <Box {...args} />;
}

export const Default = Template.bind({});
Default.args = {
  width: '84.4rem',
  height: '65.2rem',
  borderRadius: '15px'
};
