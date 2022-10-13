import { InputHTMLAttributes, CSSProperties } from 'react';

import { StyledInput } from './Input.styles';

interface Props extends InputHTMLAttributes<HTMLInputElement> {}

function Input({ ...props }: Props & CSSProperties) {
  return <StyledInput {...props} />;
}

export default Input;
