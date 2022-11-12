import Box from '@/components/Box/Box';

import { Meta, Story } from '@storybook/react';

export default {
  title: 'Reusable Components/Box',
  component: Box
} as Meta;

const DefaultTemplate: Story = (args) => <Box {...args} />;
const Default = DefaultTemplate.bind({});
Default.args = {
  width: '84.4rem',
  height: '40rem',
  borderRadius: '15px'
};

export { Default };
