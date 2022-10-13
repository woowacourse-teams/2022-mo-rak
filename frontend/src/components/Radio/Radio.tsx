import { InputHTMLAttributes, PropsWithChildren } from 'react';
import FlexContainer from '../FlexContainer/FlexContainer';
import Check from '../../assets/check.svg';
import NotCheck from '../../assets/not-check.svg';
import { StyledContainerLabel, StyledInput, StyledRadio, StyledLabel } from './Radio.styles';

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

export default Radio;
