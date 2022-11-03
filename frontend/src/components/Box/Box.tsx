import { CSSProperties, PropsWithChildren, HTMLAttributes } from 'react';

import { StyledBox } from '@/components/Box/Box.styles';

type Props = PropsWithChildren<HTMLAttributes<HTMLDivElement>> & CSSProperties;

function Box({ children, ...props }: Props) {
  return <StyledBox {...props}>{children}</StyledBox>;
}

export default Box;
