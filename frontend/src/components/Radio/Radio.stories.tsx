import { Meta, Story } from '@storybook/react';
import Radio from '@/components/Radio/Radio';

export default {
  title: 'Reusable Components/Radio',
  component: Radio
} as Meta;

const DefaultTemplate: Story = (args) => <Radio name="story" {...args} />;

const Default = DefaultTemplate.bind({});

export { Default };
