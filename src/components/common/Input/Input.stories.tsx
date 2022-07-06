import React from 'react';
import theme from '../../../styles/theme';
import Input from './Input';

export default {
  title: 'Reusable Components/Input',
  component: Input
};

function OutlinedTemplate(args) {
  return <Input {...args} />;
}

export const Outlined = OutlinedTemplate.bind({});
Outlined.args = {
  color: theme.colors.BLACK_100,
  colorScheme: theme.colors.PURPLE_100,
  width: '74.4rem',
  height: '3.6rem',
  borderRadius: '10px',
  variant: 'outlined',
  fontSize: '1rem',
  placeholder: '선택항목을 입력해주세요!'
};

function UnstyledTemplate(args) {
  return <Input {...args} />;
}

export const Unstyled = UnstyledTemplate.bind({});
Unstyled.args = {
  colorScheme: theme.colors.PURPLE_100,
  width: '74.4rem',
  height: '3.6rem',
  variant: 'unstyled',
  color: theme.colors.BLACK_100,
  fontSize: '3.2rem',
  placeholder: '투표 제목을 입력해주세요🧐',
  textAlign: 'left'
};
