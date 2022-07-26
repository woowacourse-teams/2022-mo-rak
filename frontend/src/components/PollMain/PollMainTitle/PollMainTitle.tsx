import React, { PropsWithChildren } from 'react';

import styled from '@emotion/styled';

function PollMainTitle({ children }: PropsWithChildren) {
  return <StyledTitle>{children}</StyledTitle>;
}

const StyledTitle = styled.h1`
  font-size: 1.6rem;
  text-align: center;
`;

export default PollMainTitle;
