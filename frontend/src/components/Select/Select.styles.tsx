import styled from '@emotion/styled';
import { CSSProperties } from 'react';

const StyledContainer = styled.select<CSSProperties>(
  ({ fontSize }) => `
  appearance: none;
  width: 100%;
  font-size: ${fontSize || '2.8rem'};
  text-align: center;
  border: none;
  cursor: pointer;

  :focus {
    outline: none;
  }
`
);

export { StyledContainer };
