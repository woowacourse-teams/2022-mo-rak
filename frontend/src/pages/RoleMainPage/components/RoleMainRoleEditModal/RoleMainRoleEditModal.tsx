import { ChangeEvent, FormEvent, useState } from 'react';
import closeButtonImg from '@/assets/close-button.svg';
import editWithSmileImg from '@/assets/edit-with-smile.svg';
import {
  StyledForm,
  StyledLogo,
  StyledTitle,
  StyledTopContainer,
  StyledCloseButton,
  StyledTriangle,
  StyledBottomContainer,
  StyledDescription
} from '@/pages/RoleMainPage/components/RoleMainRoleEditModal/RoleMainRoleEditModal.styles';
import Modal from '@/components/Modal/Modal';
import { editRoles } from '@/api/role';
import { useParams } from 'react-router-dom';
import { Group } from '@/types/group';
import { AxiosError } from 'axios';
import { EditRolesRequest } from '@/types/role';
import useGroupMembersContext from '@/hooks/useGroupMembersContext';
import RoleMainRoleEditModalInputs from '@/pages/RoleMainPage/components/RoleMainRoleEditModalInputs/RoleMainRoleEditModalInputs';
import RoleMainRoleEditModalButtons from '@/pages/RoleMainPage/components/RoleMainRoleEditModalButtons/RoleMainRoleEditModalButtons';
import { ROLE_ERROR } from '@/constants/errorMessage';
import { CONFIRM_MESSAGE } from '@/constants/message';

type Props = {
  close: () => void;
  initialRoles: EditRolesRequest['roles'];
  onSubmit: () => void;
};

function RoleMainRoleEditModal({ initialRoles, close, onSubmit }: Props) {
  const { groupMembers } = useGroupMembersContext();
  const { groupCode } = useParams() as { groupCode: Group['code'] };
  const [roles, setRoles] = useState(initialRoles);

  const handleSetRoles = (targetIdx: number) => (e: ChangeEvent<HTMLInputElement>) => {
    const copiedRoles = [...roles];

    copiedRoles[targetIdx] = e.target.value;
    setRoles(copiedRoles);
  };

  const handleAddRoleInput = () => {
    const copiedRoles = [...roles];

    copiedRoles.push('');
    setRoles(copiedRoles);
  };

  const handleDeleteRoleInput = (targetIdx: number) => () => {
    if (roles.length <= 1) {
      alert(ROLE_ERROR.UNDER_MIN_COUNT);

      return;
    }

    if (window.confirm(CONFIRM_MESSAGE.DELETE_ROLE)) {
      const filteredRoles = roles.filter((_, idx) => idx !== targetIdx);

      setRoles(filteredRoles);
    }
  };

  const handleAllocateRoles = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    if (roles.length > groupMembers.length) {
      alert(ROLE_ERROR.EXCEED_MEMBER_COUNT);

      return;
    }

    try {
      await editRoles(groupCode, { roles });
      onSubmit();
      close();
    } catch (err) {
      if (err instanceof AxiosError) {
        const errCode = err.response?.data.codeNumber;

        if (errCode === '5100') {
          alert(ROLE_ERROR.EXCEED_MAX_NAME_LENGTH);
        }
      }
    }
  };

  return (
    <Modal isVisible={true} close={close}>
      <StyledForm onSubmit={handleAllocateRoles}>
        <StyledTopContainer>
          <StyledLogo src={editWithSmileImg} alt="edit-logo" />
          <StyledTitle>역할 목록 수정하기</StyledTitle>
          <StyledDescription>역할의 개수가 멤버수보다 많을 수는 없어요!</StyledDescription>
          <StyledCloseButton onClick={close} src={closeButtonImg} alt="close-button" />
          <StyledTriangle />
        </StyledTopContainer>
        <StyledBottomContainer>
          <RoleMainRoleEditModalInputs
            roles={roles}
            onChangeRoleInput={handleSetRoles}
            onClickDeleteButton={handleDeleteRoleInput}
            onClickAddButton={handleAddRoleInput}
          />
          <RoleMainRoleEditModalButtons onClickCancelButton={close} />
        </StyledBottomContainer>
      </StyledForm>
    </Modal>
  );
}

export default RoleMainRoleEditModal;
