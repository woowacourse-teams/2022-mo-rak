import { Meta, Story } from '@storybook/react';
import theme from '../../styles/theme';

import Input from './Input';

export default {
  title: 'Reusable Components/Input',
  component: Input
} as Meta;

const DefaultTemplate: Story = (args) => <Input {...args} />;

const Default = DefaultTemplate.bind({});
Default.args = {
  fontSize: '5rem',
  textAlign: 'center',
  color: theme.colors.BLACK_100
};

export { Default };
