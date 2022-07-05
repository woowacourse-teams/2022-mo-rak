import React from 'react';

import Box from './Box';

export default {
  title: 'Reusable Components/Box',
  component: Box,
  argTypes: {
    width: { controls: 'text' },
    height: { controls: 'text' },
    borderRadius: { controls: 'text' }
  }
};

function Template(args) {
  return <Box {...args} />;
}

export const Default = Template.bind({});
Default.args = {
  width: '844px',
  height: '652px',
  borderRadius: '15px'
};
