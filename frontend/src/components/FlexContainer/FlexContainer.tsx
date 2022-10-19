import { HTMLAttributes, PropsWithChildren, CSSProperties } from 'react';
import { StyledContainer } from './FlexContainer.styles';

type Props = PropsWithChildren<HTMLAttributes<HTMLDivElement>> & CSSProperties;

function FlexContainer({ children, ...props }: Props) {
  return <StyledContainer {...props}>{children}</StyledContainer>;
}

export default FlexContainer;
