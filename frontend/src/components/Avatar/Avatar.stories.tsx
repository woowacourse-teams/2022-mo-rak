import { Meta, Story } from '@storybook/react';

import Avatar from './Avatar';

export default {
  title: 'Reusable Components/Avatar',
  component: Avatar
} as Meta;

const DefaultTemplate: Story = (args) => (
  <Avatar
    profileUrl="https://avatars.githubusercontent.com/u/64825713?v=4"
    name="al-bur"
    {...args}
  />
);
export const Default = DefaultTemplate.bind({});

const NoImageFoundTemplate: Story = (args) => (
  <Avatar profileUrl="not-found" name="al-bur" {...args} />
);

export const NoImageFound = NoImageFoundTemplate.bind({});
