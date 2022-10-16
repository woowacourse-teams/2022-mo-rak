import { InputHTMLAttributes, CSSProperties } from 'react';

import { StyledInput } from './Input.styles';

type Props = InputHTMLAttributes<HTMLInputElement> & CSSProperties;

function Input({ ...props }: Props & CSSProperties) {
  return <StyledInput {...props} />;
}

export default Input;
