import { CSSProperties, PropsWithChildren, HTMLAttributes } from 'react';

import { StyledBox } from './Box.styles';

interface Props extends PropsWithChildren<HTMLAttributes<HTMLDivElement>> {}

function Box({ children, ...props }: Props & CSSProperties) {
  return <StyledBox {...props}>{children}</StyledBox>;
}

export default Box;
