import React from 'react';
import styled from '@emotion/styled';
import { useTheme } from '@emotion/react';

function GroupCreateFormSubmitButton() {
  const theme = useTheme();

  return (
    // TODO: colorScheme 바꿔줘야함
    <StyledButton type="submit">
      생성하기
    </StyledButton>
  );
}

const StyledButton = styled.button(({ theme }) => `
  background-color: #ECB22E;
  color: ${theme.colors.WHITE_100};
  border-radius: 0 10px 10px 0;
  fontSize: 1.6rem;
  padding: 2.8rem 0; 
  width: 21.2rem;
  font-size: 2.4rem;

  &: hover {
    background-color: #eaa917; // TODO: theme 으로 수정 
    transition: all 0.2s linear;
  }
`);

export default GroupCreateFormSubmitButton;
