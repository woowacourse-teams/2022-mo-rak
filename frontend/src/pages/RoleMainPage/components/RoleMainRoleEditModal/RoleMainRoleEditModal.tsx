import { ChangeEvent, FormEvent, useState } from 'react';
import Close from '../../../../assets/close-button.svg';
import Edit from '../../../../assets/edit.svg';
import Bin from '../../../../assets/bin.svg';
import {
  StyledModalFormContainer,
  StyledLogo,
  StyledHeader,
  StyledRolesContainer,
  StyledTop,
  StyledCloseButton,
  StyledTriangle,
  StyledBottom,
  StyledBinIcon
} from './RoleMainRoleEditModal.styles';
import Modal from '../../../../components/Modal/Modal';
import FlexContainer from '../../../../components/FlexContainer/FlexContainer';
import TextField from '../../../../components/TextField/TextField';
import { useTheme } from '@emotion/react';
import Input from '../../../../components/Input/Input';
import Button from '../../../../components/Button/Button';

type Props = {
  isVisible: boolean;
  close: () => void;
};

function RoleMainRoleEditModal({ isVisible, close }: Props) {
  const theme = useTheme();
  const [roles, setRoles] = useState(['데일리 마스터']);

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

  const handleAllocateRoles = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    console.log(roles);
  };

  return (
    <Modal isVisible={isVisible} close={close}>
      <StyledModalFormContainer onSubmit={handleAllocateRoles}>
        <StyledTop>
          <StyledLogo src={Edit} alt="edit-logo" />
          <StyledHeader>역할 목록 수정하기</StyledHeader>
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
