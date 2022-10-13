import { HTMLAttributes, PropsWithChildren, CSSProperties } from 'react';
import { StyledContainer } from './FlexContainer.styles';

interface Props extends PropsWithChildren<HTMLAttributes<HTMLDivElement>> {}

function FlexContainer({ children, ...props }: Props & CSSProperties) {
  return <StyledContainer {...props}>{children}</StyledContainer>;
}

export default FlexContainer;
