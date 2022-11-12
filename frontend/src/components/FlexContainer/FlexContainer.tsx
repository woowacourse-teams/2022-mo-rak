import { CSSProperties, HTMLAttributes, PropsWithChildren } from 'react';

import { StyledContainer } from '@/components/FlexContainer/FlexContainer.styles';

type Props = PropsWithChildren<HTMLAttributes<HTMLDivElement>> & CSSProperties;

function FlexContainer({ children, ...props }: Props) {
  return <StyledContainer {...props}>{children}</StyledContainer>;
}

export default FlexContainer;
