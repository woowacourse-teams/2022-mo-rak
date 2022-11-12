import Radio from '@/components/Radio/Radio';

import { Meta, Story } from '@storybook/react';

export default {
  title: 'Reusable Components/Radio',
  component: Radio
} as Meta;

const DefaultTemplate: Story = (args) => <Radio name="story" {...args} />;

const Default = DefaultTemplate.bind({});

export { Default };
