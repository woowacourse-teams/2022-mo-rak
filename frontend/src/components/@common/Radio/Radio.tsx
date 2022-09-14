import styled from '@emotion/styled';
import { InputHTMLAttributes, PropsWithChildren } from 'react';
import FlexContainer from '../FlexContainer/FlexContainer';
import Check from '../../../assets/check.svg';
import NotCheck from '../../../assets/not-check.svg';

interface Props extends PropsWithChildren<InputHTMLAttributes<HTMLInputElement>> {
  name: string;
}

function Radio({ id, name, children, checked, ...props }: Props) {
  return (
    <FlexContainer alignItems="center">
      <StyledContainerLabel htmlFor={id}>
        <StyledInput type="radio" id={id} name={name} {...props} checked={checked} />
        <StyledRadio src={checked ? Check : NotCheck} alt="bin" />
        <StyledLabel>{children}</StyledLabel>
      </StyledContainerLabel>
    </FlexContainer>
  );
}

const StyledContainerLabel = styled.label`
  width: 100%;
  cursor: pointer;
`;

const StyledInput = styled.input`
  cursor: pointer;
  display: none;
`;

const StyledRadio = styled.img`
  width: 2rem;
  height: 2rem;
  position: absolute;
  left: 1.2rem;
  top: 1rem;
`;

const StyledLabel = styled.span`
  cursor: pointer;
  font-size: 1.6rem;
  text-align: center;
`;

export default Radio;
