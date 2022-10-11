import { CSSProperties } from 'react';
import styled from '@emotion/styled';

const StyledContainer = styled.div<CSSProperties>(
  ({ flexDirection, alignItems, justifyContent, gap }) => `
  display: flex;
  flex-direction: ${flexDirection || 'row'};
  ${alignItems && `align-items: ${alignItems}`};
  ${justifyContent && `justify-content: ${justifyContent}`};
  ${gap && `gap: ${gap}`};
`
);

export { StyledContainer };
