import styled from '@emotion/styled';
import React, { InputHTMLAttributes, PropsWithChildren } from 'react';
import FlexContainer from '../FlexContainer/FlexContainer';

interface Props extends PropsWithChildren<InputHTMLAttributes<HTMLInputElement>> {
  name: string;
}

function Radio({ id, name, children, ...props }: Props) {
  return (
    <FlexContainer alignItems="center">
      <label htmlFor={id}>
        <StyledInput type="radio" id={id} name={name} {...props} />
        <span />
        <StyledSpan>{children}</StyledSpan>
      </label>
    </FlexContainer>
  );
}

const StyledInput = styled.input`
  cursor: pointer;
`;

const StyledSpan = styled.span`
  cursor: pointer;
  width: 100%;
  font-size: 1.6rem;
  text-align: center;
`;

export default Radio;
