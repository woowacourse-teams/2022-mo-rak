import { CSSProperties, PropsWithChildren, SelectHTMLAttributes } from 'react';

import { StyledContainer } from '@/components/Select/Select.styles';

type Props = PropsWithChildren<SelectHTMLAttributes<HTMLSelectElement>> & CSSProperties;

function Select({ children, ...props }: Props) {
  return <StyledContainer {...props}>{children}</StyledContainer>;
}

export default Select;
