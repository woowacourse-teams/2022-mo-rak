import Checkbox from '@/components/Checkbox/Checkbox';

import { Meta, Story } from '@storybook/react';

export default {
  title: 'Reusable Components/Checkbox',
  component: Checkbox
} as Meta;

const DefaultTemplate: Story = (args) => <Checkbox {...args} />;
const Default = DefaultTemplate.bind({});

export { Default };
