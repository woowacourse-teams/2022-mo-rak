import React from 'react';
import theme from '../../../styles/theme';
import Button from './Button';

export default {
  title: 'Reusable Components/Button',
  component: Button
};

function OutlinedTemplate(args) {
  return <Button {...args}>하나만 투표</Button>;
}

export const Outlined = OutlinedTemplate.bind({});
Outlined.args = {
  color: theme.colors.PURPLE_100,
  colorScheme: theme.colors.PURPLE_100,
  width: '9.2rem',
  height: '3.6rem',
  borderRadius: '20px',
  variant: 'outlined',
  fontSize: '0.5rem'
};

function FilledTemplate(args) {
  return <Button {...args}>투표 만들기</Button>;
}

export const Filled = FilledTemplate.bind({});
Filled.args = {
  colorScheme: theme.colors.PURPLE_100,
  width: '46rem',
  height: '6.4rem',
  borderRadius: '10px',
  variant: 'filled',
  color: theme.colors.WHITE_100,
  fontSize: '2rem'
};
