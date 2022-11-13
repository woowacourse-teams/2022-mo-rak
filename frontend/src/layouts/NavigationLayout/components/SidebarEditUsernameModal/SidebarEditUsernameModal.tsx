import Modal from '@/components/Modal/Modal';
import {
  StyledTopContainer,
  StyledIcon,
  StyledBottomContainer,
  StyledTriangle,
  StyledCloseIcon,
  StyledTitle,
  StyledForm
} from '@/layouts/NavigationLayout/components/SidebarEditUsernameModal/SidebarEditUsernameModal.styles';
import closeButtonImg from '@/assets/close-button.svg';
import editWithSmileImg from '@/assets/edit-with-smile.svg';
import { FormEvent } from 'react';
import useInput from '@/hooks/useInput';
import { editUsername } from '@/api/auth';
import { AxiosError } from 'axios';
import useAuthDispatchContext from '@/hooks/useAuthDispatchContext';
import SidebarEditUsernameModalButtons from '@/layouts/NavigationLayout/components/SidebarEditUsernameModalButtons/SidebarEditUsernameModalButtons';
import SidebarEditUsernameModalUsernameInput from '@/layouts/NavigationLayout/components/SidebarEditUsernameModalUsernameInput/SidebarEditUsernameModalUsernameInput';
import { MODAL_ERROR } from '@/constants/errorMessage';

type Props = {
  isVisible: boolean;
  close: () => void;
};

function SidebarEditUsernameModal({ isVisible, close }: Props) {
  const authDispatch = useAuthDispatchContext();
  const [username, handleUsername, resetUsername] = useInput('');

  const handleEditUserName = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    try {
      await editUsername({ name: username });
      authDispatch({ type: 'SET_NAME', payload: username });
      alert('닉네임이 변경되었습니다.');
      resetUsername();
      close();
    } catch (err) {
      if (err instanceof AxiosError) {
        const errCode = err.response?.data.codeNumber;

        if (errCode === '4000') {
          alert(MODAL_ERROR.EMPTY_NICKNAME);
          resetUsername();
        }
      }
    }
  };

  return (
    // TODO: props drilling 발생
    <Modal isVisible={isVisible} close={close}>
      <StyledForm onSubmit={handleEditUserName}>
        <StyledTopContainer>
          <StyledIcon src={editWithSmileImg} alt="edit-logo" />
          <StyledTitle>닉네임 수정하기</StyledTitle>
          <StyledCloseIcon onClick={close} src={closeButtonImg} alt="close-button" />
          <StyledTriangle />
        </StyledTopContainer>
        <StyledBottomContainer>
          <SidebarEditUsernameModalUsernameInput
            username={username}
            onChangeUsername={handleUsername}
          />
          <SidebarEditUsernameModalButtons onClickCancelButton={close} />
        </StyledBottomContainer>
      </StyledForm>
    </Modal>
  );
}

export default SidebarEditUsernameModal;
