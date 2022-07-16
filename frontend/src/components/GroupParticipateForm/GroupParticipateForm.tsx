import styled from '@emotion/styled';
import React from 'react';
import theme from '../../styles/theme';
import Box from '../common/Box/Box';
import Button from '../common/Button/Button';
import Input from '../common/Input/Input';
import TextField from '../common/TextField/TextField';

function GroupParticipateForm() {
  return (
    <Box width="60rem" height="21.6rem">
      <StyledForm>
        <StyledTitle>그룹참가</StyledTitle>
        <TextField
          variant="outlined"
          width="52rem"
          height="3.6rem"
          colorScheme={theme.colors.PURPLE_100}
          borderRadius="10px"
        >
          {/* TODO: Input 내부 레이아웃 수정 */}
          <Input placeholder="코드를 입력해주세요!" />
        </TextField>
        <Button
          variant="filled"
          width="52rem"
          colorScheme={theme.colors.PURPLE_100}
          color={theme.colors.WHITE_100}
          fontSize="1.6rem"
        >
          참가하기
        </Button>
      </StyledForm>
    </Box>
  );
}

const StyledForm = styled.form`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 1.2rem;
  height: 100%;
`;

const StyledTitle = styled.span`
  font-size: 2rem;
`;

export default GroupParticipateForm;
