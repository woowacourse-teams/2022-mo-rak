import { InputHTMLAttributes, PropsWithChildren } from 'react';

import FlexContainer from '@/components/FlexContainer/FlexContainer';
import {
  StyledContainerLabel,
  StyledInput,
  StyledLabel,
  StyledRadio
} from '@/components/Radio/Radio.styles';

import checkImg from '@/assets/check.svg';
import notCheckImg from '@/assets/not-check.svg';

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
