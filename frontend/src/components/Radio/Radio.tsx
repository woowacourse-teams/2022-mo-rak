import { InputHTMLAttributes, PropsWithChildren } from 'react';
import FlexContainer from '../FlexContainer/FlexContainer';
import checkImg from '@/assets/check.svg';
import notCheckImg from '@/assets/not-check.svg';
import { StyledContainerLabel, StyledInput, StyledRadio, StyledLabel } from './Radio.styles';

type Props = {
  name: string;
} & PropsWithChildren<InputHTMLAttributes<HTMLInputElement>>;

function Radio({ id, name, children, checked, ...props }: Props) {
  return (
    <FlexContainer alignItems="center">
      <StyledContainerLabel htmlFor={id}>
        <StyledInput type="radio" id={id} name={name} {...props} checked={checked} />
        <StyledRadio src={checked ? checkImg : notCheckImg} alt="bin" />
        <StyledLabel>{children}</StyledLabel>
      </StyledContainerLabel>
    </FlexContainer>
  );
}

export default Radio;
