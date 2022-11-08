import { PropsWithChildren, CSSProperties, SelectHTMLAttributes } from 'react';
import { StyledContainer } from './Select.styles';

type Props = PropsWithChildren<SelectHTMLAttributes<HTMLSelectElement>> & CSSProperties;

function Select({ children, ...props }: Props) {
  return <StyledContainer {...props}>{children}</StyledContainer>;
}

export default Select;
