import React, { InputHTMLAttributes, CSSProperties } from 'react';

import styled from '@emotion/styled';

interface Props extends InputHTMLAttributes<HTMLInputElement> {}

// TODO: 리팩토링
function Input({
  ...props
}: Props &
  // TODO: 깔끔하게 하고싶다!! 근데, 맞는 걸수도~?
  CSSProperties) {
  return <StyledInput {...props} />;
}

const StyledInput = styled.input<CSSProperties>(
  ({ color, fontSize, textAlign }) => `
    width: 100%;
    color: ${color};
    font-size: ${fontSize};
    text-align: ${textAlign || 'center'};
  `
);

export default Input;
