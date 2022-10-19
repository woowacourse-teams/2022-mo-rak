import Modal from '../../../../components/Modal/Modal';
import {
  StyledTop,
  StyledLogo,
  StyledBottom,
  StyledTriangle,
  StyledCloseIcon,
  StyledTitle,
  StyledForm
} from './SidebarEditUsernameModal.styles';
import Close from '../../../../assets/close-button.svg';
import EditWithSmile from '../../../../assets/edit-with-smile.svg';
import { FormEvent } from 'react';
import useInput from '../../../../hooks/useInput';
import { editUsername } from '../../../../api/auth';
import { AxiosError } from 'axios';
import useAuthDispatchContext from '../../../../hooks/useAuthDispatchContext';
import SidebarEditUsernameModalButtonGroup from '../SidebarEditUsernameModalButtonGroup/SidebarEditUsernameModalButtonGroup';
import SidebarEditUsernameModalUsernameInput from '../SidebarEditUsernameModalUsernameInput/SidebarEditUsernameModalUsernameInput';

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
          alert('닉네임은 공백일 수 없습니다');
          resetUsername();
        }
      }
    }
  };

  return (
    // TODO: props drilling 발생
    <Modal isVisible={isVisible} close={close}>
      <StyledForm onSubmit={handleEditUserName}>
        <StyledTop>
          {/* TODO: Icon vs logo?? */}
          <StyledLogo src={EditWithSmile} alt="edit-logo" />
          <StyledTitle>닉네임 수정하기</StyledTitle>
          <StyledCloseIcon onClick={close} src={Close} alt="close-button" />
          <StyledTriangle />
        </StyledTop>
        <StyledBottom>
          <SidebarEditUsernameModalUsernameInput
            username={username}
            onChangeUsername={handleUsername}
          />
          <SidebarEditUsernameModalButtonGroup />
        </StyledBottom>
      </StyledForm>
    </Modal>
  );
}

export default SidebarEditUsernameModal;
