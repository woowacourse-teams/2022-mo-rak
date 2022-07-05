import React from 'react';

import styled from '@emotion/styled';
import { CSSObject } from '@emotion/react';

interface Props
  extends React.PropsWithChildren<Pick<CSSObject, 'borderColor'>> {}

function Divider({ borderColor }: Props) {
  return <StyledDivider borderColor={borderColor} />;
}

const StyledDivider = styled.hr<Pick<CSSObject, 'borderColor'>>(
  ({ borderColor, theme }) => `
    border-color: ${borderColor || theme.colors.GRAY_200};
    width: 100%;
    border-width: 0 0 1px;
  `
);

export default Divider;
