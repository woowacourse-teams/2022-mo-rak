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
  width: '744px',
  height: '36px',
  borderRadius: '10px',
  variant: 'outlined',
  fontSize: '10px',
  placeholder: '선택항목을 입력해주세요!'
};

function UnstyledTemplate(args) {
  return <Input {...args} />;
}

export const Unstyled = UnstyledTemplate.bind({});
Unstyled.args = {
  colorScheme: theme.colors.PURPLE_100,
  width: '744px',
  height: '36px',
  borderRadius: '10px',
  variant: 'unstyled',
  color: theme.colors.BLACK_100,
  fontSize: '32px',
  placeholder: '투표 제목을 입력해주세요🧐',
  textAlign: 'left'
};
