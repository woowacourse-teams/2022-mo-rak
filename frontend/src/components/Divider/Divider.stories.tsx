import { Meta, Story } from '@storybook/react';
import theme from '../../styles/theme';
import Divider from './Divider';

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
