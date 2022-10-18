import { InputHTMLAttributes, PropsWithChildren } from 'react';
import FlexContainer from '../FlexContainer/FlexContainer';
import Check from '../../assets/check.svg';
import NotCheck from '../../assets/not-check.svg';
import { StyledContainerLabel, StyledInput, StyledCheckbox, StyledLabel } from './Checkbox.styles';

type Props = PropsWithChildren<InputHTMLAttributes<HTMLInputElement>>;

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

export default Checkbox;
