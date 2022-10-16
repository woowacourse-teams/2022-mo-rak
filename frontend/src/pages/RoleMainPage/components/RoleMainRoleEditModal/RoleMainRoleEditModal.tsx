import { ChangeEvent, FormEvent, useState, useEffect, useContext } from 'react';
import Close from '../../../../assets/close-button.svg';
import Edit from '../../../../assets/edit.svg';
import Bin from '../../../../assets/bin.svg';
import {
  StyledModalFormContainer,
  StyledLogo,
  StyledTitle,
  StyledRolesContainer,
  StyledTop,
  StyledCloseButton,
  StyledTriangle,
  StyledBottom,
  StyledBinIcon,
  StyledDescription
} from './RoleMainRoleEditModal.styles';
import Modal from '../../../../components/Modal/Modal';
import FlexContainer from '../../../../components/FlexContainer/FlexContainer';
import TextField from '../../../../components/TextField/TextField';
import { useTheme } from '@emotion/react';
import Input from '../../../../components/Input/Input';
import Button from '../../../../components/Button/Button';
import { editRoles } from '../../../../api/role';
import { useParams } from 'react-router-dom';
import { GroupInterface } from '../../../../types/group';
import { AxiosError } from 'axios';
import { EditRolesRequest } from '../../../../types/role';
import useGroupMembersContext from '../../../../hooks/useGroupMembersContext';

type Props = {
  close: () => void;
  initialRoles: EditRolesRequest['roles'];
  onEditRoles: () => void;
};

function RoleMainRoleEditModal({ initialRoles, close, onEditRoles }: Props) {
  const theme = useTheme();
  const { groupMembers } = useGroupMembersContext();
  const { groupCode } = useParams() as { groupCode: GroupInterface['code'] };
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
      onEditRoles();
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
      <StyledModalFormContainer onSubmit={handleAllocateRoles}>
        <StyledTop>
          <StyledLogo src={Edit} alt="edit-logo" />
          <StyledTitle>역할 목록 수정하기</StyledTitle>
          <StyledDescription>역할의 개수가 멤버수보다 많을 수는 없어요!</StyledDescription>
          <StyledCloseButton onClick={close} src={Close} alt="close-button" />
          <StyledTriangle />
        </StyledTop>
        <StyledBottom>
          <StyledRolesContainer>
            {roles.map((role, idx) => (
              <TextField
                key={idx}
                variant="filled"
                colorScheme={theme.colors.WHITE_100}
                borderRadius="1.2rem"
                padding="1.6rem 5rem"
                width="50.4rem"
              >
                <Input
                  value={role}
                  onChange={handleSetRoles(idx)}
                  fontSize="1.6rem"
                  required
                  autoFocus
                />
                <StyledBinIcon src={Bin} alt="bin-icon" onClick={handleDeleteRoleInput(idx)} />
              </TextField>
            ))}
            <Button
              variant="filled"
              colorScheme={theme.colors.YELLOW_200}
              fontSize="2rem"
              width="50.4rem"
              onClick={handleAddRoleInput}
            >
              +
            </Button>
            <FlexContainer gap="2rem">
              <Button
                variant="filled"
                colorScheme={theme.colors.GRAY_400}
                width="14rem"
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
                width="14rem"
                padding="1.6rem 3.2rem"
                borderRadius="1.2rem"
                fontSize="1.6rem"
                type="submit"
              >
                확인
              </Button>
            </FlexContainer>
          </StyledRolesContainer>
        </StyledBottom>
      </StyledModalFormContainer>
    </Modal>
  );
}

export default RoleMainRoleEditModal;
