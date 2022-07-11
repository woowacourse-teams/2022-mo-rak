import React from 'react';

import styled from '@emotion/styled';

// TODO: interface와 type의 차이와 대부분 어떤 것을 사용하는지?
interface Props {
  title: string;
}

// TODO: 재사용 되는데, common에 넣어야하나?...

function PollTitle({ title }: Props) {
  return <StyledTitle>{title}</StyledTitle>;
}

const StyledTitle = styled.h1(
  ({ theme }) => `
  color: ${theme.colors.BLACK_100};
  font-size: 32px;
`
);

export default PollTitle;
