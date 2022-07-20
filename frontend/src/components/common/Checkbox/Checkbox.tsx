import React, { InputHTMLAttributes, PropsWithChildren } from 'react';
import styled from '@emotion/styled';
import FlexContainer from '../FlexContainer/FlexContainer';

interface Props extends PropsWithChildren<InputHTMLAttributes<HTMLInputElement>> {}

function Checkbox({ id, children, ...props }: Props) {
  return (
    <FlexContainer alignItems="center">
      <label htmlFor={id}>
        <StyledInput type="checkbox" id={id} {...props} />
        <span />
        <StyledLabel>{children}</StyledLabel>
      </label>
    </FlexContainer>
  );
}

const StyledInput = styled.input`
  cursor: pointer;
`;
const StyledLabel = styled.span`
  cursor: pointer;
`;

export default Checkbox;
