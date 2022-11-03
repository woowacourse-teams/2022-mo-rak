import { PropsWithChildren, CSSProperties, SelectHTMLAttributes } from 'react';
import { StyledSelect } from '@/components/Select/Select.styles';

type Props = PropsWithChildren<SelectHTMLAttributes<HTMLSelectElement>> & CSSProperties;

function Select({ children, ...props }: Props) {
  return <StyledSelect {...props}>{children}</StyledSelect>;
}

export default Select;
