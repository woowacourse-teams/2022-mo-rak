import { PropsWithChildren, CSSProperties, SelectHTMLAttributes } from 'react';
import { StyledSelect } from './Select.styles';

interface Props extends PropsWithChildren<SelectHTMLAttributes<HTMLSelectElement>> {}

function Select({ children, ...props }: Props & CSSProperties) {
  return <StyledSelect {...props}>{children}</StyledSelect>;
}

export default Select;
