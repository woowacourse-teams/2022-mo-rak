import React, { CSSProperties } from 'react';

import styled from '@emotion/styled';
import { PollInterface } from '../../../types/poll';

// TODO: interface와 type의 차이와 대부분 어떤 것을 사용하는지?
interface Props {
  title: PollInterface['title'];
}

// TODO: 재사용 되는데, common에 넣어야하나?...

function PollMainTitle({ title }: Props) {
  return <StyledTitle>{title}</StyledTitle>;
}

const StyledTitle = styled.h1`
  font-size: 1.6rem;
  text-align: center;
`;

export default PollMainTitle;
