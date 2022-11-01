import { Meta, Story } from '@storybook/react';
import Checkbox from './Checkbox';

export default {
  title: 'Reusable Components/Checkbox',
  component: Checkbox
} as Meta;

const DefaultTemplate: Story = (args) => <Checkbox {...args} />;
export const Default = DefaultTemplate.bind({});
