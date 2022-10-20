import { PropsWithChildren, CSSProperties, SelectHTMLAttributes } from 'react';
import { StyledSelect } from './Select.styles';

type Props = PropsWithChildren<SelectHTMLAttributes<HTMLSelectElement>> & CSSProperties;

function Select({ children, ...props }: Props) {
  return <StyledSelect {...props}>{children}</StyledSelect>;
}

export default Select;
