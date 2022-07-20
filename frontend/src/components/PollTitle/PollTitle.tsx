import React from 'react';

import styled from '@emotion/styled';
import { PollInterface } from '../../types/poll';

// TODO: interface와 type의 차이와 대부분 어떤 것을 사용하는지?
interface Props {
  title: PollInterface['title'];
}

function PollTitle({ title }: Props) {
  return <StyledTitle>{title}</StyledTitle>;
}

const StyledTitle = styled.h1(
  ({ theme }) => `
  color: ${theme.colors.BLACK_100};
  font-size: 3.2rem;
`
);

export default PollTitle;
