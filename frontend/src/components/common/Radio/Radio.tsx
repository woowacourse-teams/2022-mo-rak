import styled from '@emotion/styled';
import React, { InputHTMLAttributes, PropsWithChildren } from 'react';
import FlexContainer from '../FlexContainer/FlexContainer';

interface Props extends PropsWithChildren<InputHTMLAttributes<HTMLInputElement>> {
  name: string;
}

function Radio({ id, name, children }: Props) {
  return (
    <FlexContainer alignItems="center">
      <StyledInput type="radio" id={id} name={name} />
      <StyledLabel htmlFor={id}>{children}</StyledLabel>
    </FlexContainer>
  );
}

const StyledInput = styled.input`
  cursor: pointer;
`;

const StyledLabel = styled.label`
  cursor: pointer;
  width: 100%;
  height: 100%;
  font-size: 1.6rem;
  text-align: center;
`;

export default Radio;
