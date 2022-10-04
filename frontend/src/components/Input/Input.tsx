import { InputHTMLAttributes, CSSProperties } from 'react';

import styled from '@emotion/styled';

interface Props extends InputHTMLAttributes<HTMLInputElement> {}

function Input({ ...props }: Props & CSSProperties) {
  return <StyledInput {...props} />;
}

const StyledInput = styled.input<CSSProperties>(
  ({ color, fontSize, textAlign, theme }) => `
    width: 100%;
    color: ${color || theme.colors.BLACK_100};
    text-align: ${textAlign || 'center'};
    ${fontSize && `font-size: ${fontSize}`};
  `
);

export default Input;
