import React from 'react';
import { Meta, Story } from '@storybook/react';
import theme from '../../../styles/theme';
import Button from './Button';

export default {
  title: 'Reusable Components/Button',
  component: Button
} as Meta;

const OutlinedTemplate: Story = (args) => (
  <Button {...args} colorScheme={theme.colors.PURPLE_100} variant="outlined">
    하나만 투표
  </Button>
);

export const Outlined = OutlinedTemplate.bind({});
Outlined.args = {
  color: theme.colors.PURPLE_100,
  width: '9.2rem',
  height: '3.6rem',
  borderRadius: '20px',
  fontSize: '0.5rem'
};

const FilledTemplate: Story = (args) => (
  <Button {...args} colorScheme={theme.colors.PURPLE_100} variant="filled">
    투표 만들기
  </Button>
);

export const Filled = FilledTemplate.bind({});
Filled.args = {
  width: '46rem',
  height: '6.4rem',
  borderRadius: '10px',
  color: theme.colors.WHITE_100,
  fontSize: '2rem'
};

const PollButtonTemplate: Story = (args) => (
  <Button {...args} colorScheme={theme.colors.PURPLE_100} variant="filled">
    투표 하기
  </Button>
);

export const PollButton = PollButtonTemplate.bind({});
PollButton.args = {
  width: '74.4rem',
  height: '3.6rem',
  borderRadius: '10px',
  color: theme.colors.WHITE_100,
  fontSize: '1.6rem'
};

const PollChoiceButtonTemplate: Story = (args) => (
  <Button {...args} colorScheme={theme.colors.PURPLE_100} variant="outlined">
    투표 선택지입니다
  </Button>
);

export const PollChoiceButton = PollChoiceButtonTemplate.bind({});
PollChoiceButton.args = {
  width: '74.4rem',
  height: '3.6rem',
  borderRadius: '10px',
  color: theme.colors.BLACK_100,
  fontSize: '1.6rem'
};
