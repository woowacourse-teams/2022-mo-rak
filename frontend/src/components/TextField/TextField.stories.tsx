import { Meta, Story } from '@storybook/react';
import TextField from '@/components/TextField/TextField';
import theme from '@/styles/theme';
import Input from '@/components/Input/Input';

export default {
  title: 'Reusable Components/TextField',
  component: TextField
} as Meta;

const defaultArgs = {
  color: theme.colors.BLACK_100,
  colorScheme: theme.colors.PURPLE_100,
  borderRadius: '10px'
};

const OutlinedTemplate: Story = (args) => (
  <TextField variant="outlined" {...args}>
    <Input placeholder="투표 제목을 입력해주세요🧐" />
  </TextField>
);

const Outlined = OutlinedTemplate.bind({});
Outlined.args = defaultArgs;

const FilledTemplate: Story = (args) => (
  <TextField variant="filled" {...args}>
    <Input placeholder="투표 제목을 입력해주세요🧐" />
  </TextField>
);

const Filled = FilledTemplate.bind({});
Filled.args = defaultArgs;

const UnstyledTemplate: Story = (args) => (
  <TextField variant="unstyled" {...args}>
    <Input placeholder="투표 제목을 입력해주세요🧐" />
  </TextField>
);

const Unstyled = UnstyledTemplate.bind({});
Unstyled.args = defaultArgs;

export { Outlined, Filled, Unstyled };
