import { HTMLAttributes, PropsWithChildren, CSSProperties } from 'react';
import styled from '@emotion/styled';

interface Props extends PropsWithChildren<HTMLAttributes<HTMLDivElement>> {}

function FlexContainer({ children, ...props }: Props & CSSProperties) {
  return <StyledContainer {...props}>{children}</StyledContainer>;
}

const StyledContainer = styled.div<CSSProperties>(
  ({ flexDirection, alignItems, justifyContent, gap }) => `
  display: flex;
  flex-direction: ${flexDirection || 'row'};
  ${alignItems && `align-items: ${alignItems}`};
  ${justifyContent && `justify-content: ${justifyContent}`};
  ${gap && `gap: ${gap}`};
`
);

export default FlexContainer;
