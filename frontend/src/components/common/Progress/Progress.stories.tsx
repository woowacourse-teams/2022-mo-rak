import React from 'react';

import Progress from './Progress';

export default {
  title: 'Reusable Components/Progress',
  component: Progress
};

function Template(args) {
  return <Progress {...args} />;
}

export const Default = Template.bind({});
Default.args = {
  value: '0',
  max: '100',
  width: '200px',
  height: '50px'
};
