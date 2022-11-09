import { CSSProperties, PropsWithChildren, HTMLAttributes } from 'react';

import { StyledContainer } from '@/components/Box/Box.styles';

type Props = PropsWithChildren<HTMLAttributes<HTMLDivElement>> & CSSProperties;

function Box({ children, ...props }: Props) {
  return <StyledContainer {...props}>{children}</StyledContainer>;
}

export default Box;
