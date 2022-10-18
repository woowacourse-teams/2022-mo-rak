import { CSSProperties } from 'react';

import styled from '@emotion/styled';

const StyledDivider = styled.hr<CSSProperties>(
  ({ borderColor, theme }) => `
    border-color: ${borderColor || theme.colors.GRAY_200};
    width: 100%;
    border-width: 0 0 0.1rem;
  `
);

export { StyledDivider };
