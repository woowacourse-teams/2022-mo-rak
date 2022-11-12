import Avatar from '@/components/Avatar/Avatar';

import { Meta, Story } from '@storybook/react';

export default {
  title: 'Reusable Components/Avatar',
  component: Avatar
} as Meta;

const DefaultTemplate: Story = (args) => (
  <Avatar profileUrl="https://avatars.githubusercontent.com/u/64825713?v=4" {...args} />
);
const Default = DefaultTemplate.bind({});

const NamedTemplate: Story = (args) => (
  <Avatar
    profileUrl="https://avatars.githubusercontent.com/u/64825713?v=4"
    name="al-bur"
    {...args}
  />
);
const Named = NamedTemplate.bind({});

const NoImageFoundTemplate: Story = (args) => (
  <Avatar profileUrl="not-found" name="al-bur" {...args} />
);
const NoImageFound = NoImageFoundTemplate.bind({});

export { Default, Named, NoImageFound };
