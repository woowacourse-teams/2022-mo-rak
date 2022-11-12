import Divider from '@/components/Divider/Divider';

import theme from '@/styles/theme';
import { Meta, Story } from '@storybook/react';

export default {
  title: 'Reusable Components/Divider',
  component: Divider
} as Meta;

const DefaultTemplate: Story = (args) => <Divider {...args} />;

const Default = DefaultTemplate.bind({});
Default.args = {
  borderColor: theme.colors.GRAY_200
};

export { Default };
