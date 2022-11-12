import Progress from '@/components/Progress/Progress';

import { Meta, Story } from '@storybook/react';

export default {
  title: 'Reusable Components/Progress',
  component: Progress
} as Meta;

const DefaultTemplate: Story = (args) => <Progress {...args} />;

const Default = DefaultTemplate.bind({});
Default.args = {
  value: '0',
  max: '100',
  width: '200px'
};

export { Default };
