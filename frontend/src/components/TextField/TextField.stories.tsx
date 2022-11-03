import { Meta, Story } from '@storybook/react';
import TextField from '@/components/TextField/TextField';
import theme from '@/styles/theme';
import Input from '@/components/Input/Input';

export default {
  title: 'Reusable Components/TextField',
  component: TextField
} as Meta;

const OutlinedTemplate: Story = (args) => (
  <TextField variant="outlined" {...args}>
    <Input placeholder="투표 제목을 입력해주세요🧐" />
  </TextField>
);

export const Outlined = OutlinedTemplate.bind({});
Outlined.args = {
  color: theme.colors.BLACK_100,
  colorScheme: theme.colors.PURPLE_100,
  borderRadius: '10px'
};

const FilledTemplate: Story = (args) => (
  <TextField variant="filled" {...args}>
    <Input placeholder="투표 제목을 입력해주세요🧐" />
  </TextField>
);

export const Filled = FilledTemplate.bind({});
Filled.args = {
  color: theme.colors.BLACK_100,
  colorScheme: theme.colors.PURPLE_100,
  borderRadius: '10px'
};

const UnstyledTemplate: Story = (args) => (
  <TextField variant="unstyled" {...args}>
    <Input placeholder="투표 제목을 입력해주세요🧐" />
  </TextField>
);

export const Unstyled = UnstyledTemplate.bind({});
Unstyled.args = {
  color: theme.colors.BLACK_100,
  colorScheme: theme.colors.PURPLE_100,
  borderRadius: '10px',
  variant: 'unstyled'
};
