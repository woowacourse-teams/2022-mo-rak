import { Meta, Story } from '@storybook/react';
import Progress from './Progress';

export default {
  title: 'Reusable Components/Progress',
  component: Progress
} as Meta;

const DefaultTemplate: Story = (args) => <Progress {...args} />;

export const Default = DefaultTemplate.bind({});
Default.args = {
  value: '0',
  max: '100',
  width: '200px'
};
