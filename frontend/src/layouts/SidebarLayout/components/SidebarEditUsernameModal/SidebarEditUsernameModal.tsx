import Modal from '../../../../components/Modal/Modal';
import {
  StyledTop,
  StyledLogo,
  StyledBottom,
  StyledTriangle,
  StyledCloseIcon,
  StyledTitle,
  StyledForm,
  StyledButtonGroup
} from './SidebarEditUsernameModal.styles';
import Close from '../../../../assets/close-button.svg';
import EditWithSmile from '../../../../assets/edit-with-smile.svg';
import Input from '../../../../components/Input/Input';
import Button from '../../../../components/Button/Button';
import TextField from '../../../../components/TextField/TextField';
import { useTheme } from '@emotion/react';
import { FormEvent } from 'react';
import useInput from '../../../../hooks/useInput';
import { editUserName } from '../../../../api/auth';
import { AxiosError } from 'axios';

type Props = {
  isVisible: boolean;
  close: () => void;
};

function SidebarEditUsernameModal({ isVisible, close }: Props) {
  const theme = useTheme();
  const [username, handleUsername, resetUsername] = useInput('');

  const handleSubmitUserName = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    try {
      await editUserName({ name: username });
      alert('닉네임이 변경되었습니다.');
      resetUsername();
      close();
    } catch (err) {
      if (err instanceof AxiosError) {
        const errCode = err.response?.data.codeNumber;

        if (errCode === '4000') {
          alert('닉네임은 공백일 수 없습니다');
          resetUsername();
        }
      }
    }
  };

  return (
    // TODO: props drilling 발생
    <Modal isVisible={isVisible} close={close}>
      <StyledForm onSubmit={handleSubmitUserName}>
        <StyledTop>
          <StyledLogo src={EditWithSmile} alt="edit-logo" />
          <StyledTitle>닉네임 수정하기</StyledTitle>
          <StyledCloseIcon onClick={close} src={Close} alt="close-button" />
          <StyledTriangle />
        </StyledTop>
        <StyledBottom>
          <TextField
            variant="filled"
            colorScheme={theme.colors.WHITE_100}
            borderRadius="1.2rem"
            padding="1.6rem 5rem"
            width="70%"
          >
            <Input
              value={username}
              onChange={handleUsername}
              fontSize="1.6rem"
              required
              autoFocus
            />
          </TextField>
          <StyledButtonGroup>
            <Button
              variant="filled"
              colorScheme={theme.colors.GRAY_400}
              width="50%"
              padding="1.6rem 3.2rem"
              borderRadius="1.2rem"
              fontSize="1.6rem"
              onClick={close}
            >
              취소
            </Button>
            <Button
              variant="filled"
              colorScheme={theme.colors.YELLOW_200}
              padding="1.6rem 3.2rem"
              width="50%"
              borderRadius="1.2rem"
              fontSize="1.6rem"
              type="submit"
            >
              변경
            </Button>
          </StyledButtonGroup>
        </StyledBottom>
      </StyledForm>
    </Modal>
  );
}

export default SidebarEditUsernameModal;
