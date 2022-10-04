import { InputHTMLAttributes, PropsWithChildren } from 'react';
import styled from '@emotion/styled';
import FlexContainer from '../FlexContainer/FlexContainer';
import Check from '../../assets/check.svg';
import NotCheck from '../../assets/not-check.svg';

interface Props extends PropsWithChildren<InputHTMLAttributes<HTMLInputElement>> {}

function Checkbox({ id, children, checked, ...props }: Props) {
  return (
    <FlexContainer alignItems="center">
      <StyledContainerLabel htmlFor={id}>
        <StyledInput type="checkbox" id={id} {...props} />
        <StyledCheckbox src={checked ? Check : NotCheck} alt="check" />
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

const StyledCheckbox = styled.img`
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

export default Checkbox;
