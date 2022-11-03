import { Meta, Story } from '@storybook/react';
import Radio from '@/components/Radio/Radio';

export default {
  title: 'Reusable Components/Radio',
  component: Radio
} as Meta;

const Template: Story = (args) => <Radio name="story" {...args} />;

export const Default = Template.bind({});
Default.args = {};
