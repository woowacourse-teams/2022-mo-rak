import React, { CSSProperties } from 'react';

import styled from '@emotion/styled';

interface Props extends CSSProperties {}

function Divider({ borderColor }: Props) {
  return <StyledDivider borderColor={borderColor} />;
}

const StyledDivider = styled.hr<Props>(
  ({ borderColor, theme }) => `
    border-color: ${borderColor || theme.colors.GRAY_200};
    width: 100%;
    border-width: 0 0 0.1rem;
  `
);

export default Divider;
