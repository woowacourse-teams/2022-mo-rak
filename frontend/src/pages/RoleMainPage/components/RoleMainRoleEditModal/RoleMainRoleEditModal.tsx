import { ChangeEvent, FormEvent, useState } from 'react';
import Close from '../../../../assets/close-button.svg';
import Edit from '../../../../assets/edit-with-smile.svg';
import {
  StyledForm,
  StyledLogo,
  StyledTitle,
  StyledTop,
  StyledCloseButton,
  StyledTriangle,
  StyledBottom,
  StyledDescription
} from './RoleMainRoleEditModal.styles';
import Modal from '../../../../components/Modal/Modal';
import { editRoles } from '../../../../api/role';
import { useParams } from 'react-router-dom';
import { Group } from '../../../../types/group';
import { AxiosError } from 'axios';
import { EditRolesRequest } from '../../../../types/role';
import useGroupMembersContext from '../../../../hooks/useGroupMembersContext';
import RoleMainRoleEditModalInputGroup from '../RoleMainRoleEditModalInputGroup/RoleMainRoleEditModalInputGroup';
import RoleMainRoleEditModalButtonGroup from '../RoleMainRoleEditModalButtonGroup/RoleMainRoleEditModalButtonGroup';

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
      alert('역할은 최소 1개 이상 필요합니다!');

      return;
    }

    if (window.confirm('역할을 삭제하시겠습니까?')) {
      const filteredRoles = roles.filter((_, idx) => idx !== targetIdx);

      setRoles(filteredRoles);
    }
  };

  const handleAllocateRoles = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    if (roles.length > groupMembers.length) {
      alert('역할의 개수가 멤버수보다 많을 수 없습니다!');

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
          alert('역할의 이름은 20자를 넘을 수 없습니다');
        }
      }
    }
  };

  return (
    <Modal isVisible={true} close={close}>
      <StyledForm onSubmit={handleAllocateRoles}>
        <StyledTop>
          <StyledLogo src={Edit} alt="edit-logo" />
          <StyledTitle>역할 목록 수정하기</StyledTitle>
          <StyledDescription>역할의 개수가 멤버수보다 많을 수는 없어요!</StyledDescription>
          <StyledCloseButton onClick={close} src={Close} alt="close-button" />
          <StyledTriangle />
        </StyledTop>
        <StyledBottom>
          <RoleMainRoleEditModalInputGroup
            roles={roles}
            onChangeRoleInput={handleSetRoles}
            onClickDeleteButton={handleDeleteRoleInput}
            onClickAddButton={handleAddRoleInput}
          />
          <RoleMainRoleEditModalButtonGroup onClickCancelButton={close} />
        </StyledBottom>
      </StyledForm>
    </Modal>
  );
}

export default RoleMainRoleEditModal;
