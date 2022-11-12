import { CSSProperties, HTMLAttributes, PropsWithChildren } from 'react';

import { StyledContainer } from '@/components/MarginContainer/MarginContainer.styles';

type Props = PropsWithChildren<HTMLAttributes<HTMLDivElement>> & CSSProperties;

function MarginContainer({ children, ...props }: Props) {
  return <StyledContainer {...props}>{children}</StyledContainer>;
}

export default MarginContainer;
