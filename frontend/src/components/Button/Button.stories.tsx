import Button from '@/components/Button/Button';

import theme from '@/styles/theme';
import { Meta, Story } from '@storybook/react';

export default {
  title: 'Reusable Components/Button',
  component: Button
} as Meta;

const OutlinedTemplate: Story = (args) => (
  <Button {...args} colorScheme={theme.colors.PURPLE_100} variant="outlined">
    하나만 투표
  </Button>
);

const Outlined = OutlinedTemplate.bind({});
Outlined.args = {
  color: theme.colors.PURPLE_100,
  width: '9.2rem',
  borderRadius: '20px',
  fontSize: '0.5rem'
};

const FilledTemplate: Story = (args) => (
  <Button {...args} colorScheme={theme.colors.PURPLE_100} variant="filled">
    투표 만들기
  </Button>
);

const Filled = FilledTemplate.bind({});
Filled.args = {
  width: '46rem',
  borderRadius: '10px',
  color: theme.colors.WHITE_100,
  fontSize: '2rem'
};

const PollButtonTemplate: Story = (args) => (
  <Button {...args} colorScheme={theme.colors.PURPLE_100} variant="filled">
    투표 하기
  </Button>
);

const PollButton = PollButtonTemplate.bind({});
PollButton.args = {
  width: '74.4rem',
  borderRadius: '10px',
  color: theme.colors.WHITE_100,
  fontSize: '1.6rem'
};

export { PollButton, Filled, Outlined };
