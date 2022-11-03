import { InputHTMLAttributes, CSSProperties } from 'react';

import { StyledInput } from '@/components/Input/Input.styles';

type Props = InputHTMLAttributes<HTMLInputElement> & CSSProperties;

function Input({ ...props }: Props) {
  return <StyledInput {...props} />;
}

export default Input;
