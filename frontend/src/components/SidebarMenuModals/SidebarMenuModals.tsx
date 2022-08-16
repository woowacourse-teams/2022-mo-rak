import React, { useState, FormEvent, ChangeEvent } from 'react';
import styled from '@emotion/styled';

import { useNavigate } from 'react-router-dom';
import TextField from '../@common/TextField/TextField';
import Input from '../@common/Input/Input';
import Modal from '../@common/Modal/Modal';
import FlexContainer from '../@common/FlexContainer/FlexContainer';
import theme from '../../styles/theme';
import Slack from '../../assets/slack.svg';
import Plus from '../../assets/plus.svg';
import LinkIcon from '../../assets/link.svg';
import Close from '../../assets/close-button.svg';
import Logo from '../../assets/logo.svg';
import { GroupInterface } from '../../types/group';
import { createGroup, participateGroup } from '../../api/group';
import { useMenuDispatch } from '../../context/MenuProvider';
import useInput from '../../hooks/useInput';

interface Props {
  activeModalMenu: string | null;
  closeModal: () => void;
}

function SidebarMenuModals({ activeModalMenu, closeModal }:Props) {
  const [groupName, setGroupName] = useState<GroupInterface['name']>('');
  const [invitationCode, handleInvitationCode] = useInput('');

  const dispatch = useMenuDispatch();
  const navigate = useNavigate();

  // 그룹 생성
  const handleCreateGroup = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    try {
      const res = await createGroup(groupName);
      const groupCode = res.headers.location.split('groups/')[1];

      navigate(`/groups/${groupCode}`);
      closeModal();
      dispatch({ type: 'SET_SHOW_GROUP_LIST', isVisible: false });
    } catch (err) {
      alert(err);
    }
  };

  const handleGroupName = (e: ChangeEvent<HTMLInputElement>) => {
    setGroupName(e.target.value);
  };

  // 그룹 참가
  const handleParticipateGroup = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    try {
      await participateGroup(invitationCode);
      navigate(`/groups/${invitationCode}`);
    } catch (err) {
      console.log(err);
    }
  };

  return (
    <>
      {/* 슬랙 연동 */}
      <Modal isVisible={activeModalMenu === 'slack'} close={closeModal}>
        {/* 슬랙 메뉴 */}
        <StyledModalContainer>
          <StyledTop>
            <StyledSlackLogo src={Slack} alt="slack-logo" />
            <StyledHeaderText>슬랙 채널과 연동해보세요!</StyledHeaderText>
            <StyledGuideText>슬랙 채널과 연동하면, 그룹의 새소식을 슬랙으로 받아볼 수 있어요</StyledGuideText>
            <StyledCloseButton onClick={closeModal} src={Close} alt="close-button" />
            <StyledTriangle />
          </StyledTop>
          <StyledBottom>
            <FlexContainer flexDirection="column" alignItems="center" gap="2.4rem">
              <TextField
                variant="filled"
                colorScheme={theme.colors.WHITE_100}
                borderRadius="10px"
                padding="1.6rem 10rem"
                width="50.4rem"
              >
                <Input placeholder="슬랙 채널 url 입력 후, 확인버튼을 누르면 연동 끝!" fontSize="1.6REM" required autoFocus />
                <StyledLinkIcon src={LinkIcon} alt="link-icon" />
              </TextField>
              <StyledButton>확인</StyledButton>
            </FlexContainer>
          </StyledBottom>
        </StyledModalContainer>
      </Modal>

      {/* 그룹 생성 */}
      <Modal isVisible={activeModalMenu === 'create'} close={closeModal}>
        <StyledModalFormContainer onSubmit={handleCreateGroup}>
          <StyledTop>
            <StyledSmallLogo src={Logo} alt="logo" />
            <StyledHeaderText>그룹 생성</StyledHeaderText>
            <StyledGuideText>새로운 그룹을 빠르고 쉽게 생성해보세요</StyledGuideText>
            <StyledCloseButton onClick={closeModal} src={Close} alt="close-button" />
            <StyledTriangle />
          </StyledTop>
          <StyledBottom>
            <FlexContainer flexDirection="column" alignItems="center" gap="2.4rem">
              <TextField
                variant="filled"
                colorScheme={theme.colors.WHITE_100}
                borderRadius="10px"
                padding="1.6rem 10rem"
                width="50.4rem"
              >
                <StyledNameInput placeholder="그룹 이름을 입력하면 생성 완료!" value={groupName} onChange={handleGroupName} required autoFocus />
                <StyledLinkIcon src={Plus} alt="plus-icon" />
              </TextField>
              <StyledButton>생성하기</StyledButton>
            </FlexContainer>
          </StyledBottom>
        </StyledModalFormContainer>
      </Modal>

      {/* 그룹 참가 */}
      <Modal isVisible={activeModalMenu === 'participate'} close={closeModal}>
        <StyledModalFormContainer onSubmit={handleParticipateGroup}>
          <StyledTop>
            <StyledSmallLogo src={Logo} alt="logo" />
            <StyledHeaderText>그룹 참가</StyledHeaderText>
            <StyledGuideText>새로운 그룹에도 참가해보세요</StyledGuideText>
            <StyledCloseButton onClick={closeModal} src={Close} alt="close-button" />
            <StyledTriangle />
          </StyledTop>
          <StyledBottom>
            <FlexContainer flexDirection="column" alignItems="center" gap="2.4rem">
              <TextField
                variant="filled"
                colorScheme={theme.colors.WHITE_100}
                borderRadius="10px"
                padding="1.6rem 10rem"
                width="50.4rem"
              >
                <StyledNameInput placeholder="그룹 코드를 입력하면 참가 완료!" value={invitationCode} onChange={handleInvitationCode} required autoFocus />
                <StyledLinkIcon src={Plus} alt="plus-icon" />
              </TextField>
              <StyledButton>참가하기</StyledButton>
            </FlexContainer>
          </StyledBottom>
        </StyledModalFormContainer>
      </Modal>
    </>
  );
}

const StyledModalContainer = styled.div(({ theme }) => `
  position: relative;
  background-color: ${theme.colors.WHITE_100};
  border-radius: 12px;
  width: 68rem;
  height: 41.6rem;
`);

const StyledModalFormContainer = styled.form(({ theme }) => `
  position: relative;
  background-color: ${theme.colors.WHITE_100};
  border-radius: 12px;
  width: 68rem;
  height: 41.6rem;
`);

const StyledSlackLogo = styled.img`
  width: 8rem;
  display: block;
  margin: 0 auto ;
  margin-bottom: 2rem;
`;

const StyledHeaderText = styled.div`
  font-size: 2rem;
  text-align: center;
  margin-bottom: 1.2rem;
`;

const StyledGuideText = styled.div`
  font-size: 1.6rem;
  text-align: center;
`;

const StyledTop = styled.div`
  position: relative;
  height: 50%;
  padding-top: 2.4rem;
`;

const StyledCloseButton = styled.img`
  position: absolute;
  right: 2.4rem;
  top: 2.4rem;
  cursor: pointer;

  &:hover {
    transform: scale(1.1);
    transition: all 0.3s linear;
  }
`;

const StyledTriangle = styled.div`
  position: absolute;
  border-left: 2rem solid transparent;
  border-right: 2rem solid transparent;
  border-top: 2rem solid ${theme.colors.WHITE_100};
  width: 0;
  bottom: -2.8rem;
  right: 50%;
  transform: translate(50%,-50%);
`;

const StyledBottom = styled.div(({ theme }) => `
  background: ${theme.colors.YELLOW_50};
  height: 50%;
  padding-top: 4.4rem;
  border-bottom-left-radius: 12px;
  border-bottom-right-radius: 12px;
`);

const StyledLinkIcon = styled.img`
  position: absolute;
  left: 1.2rem;
  top: 1.2rem;
`;

const StyledButton = styled.button`
  background-color: ${theme.colors.YELLOW_200};
  color: ${theme.colors.WHITE_100};
  width: 14rem; 
  padding: 1.6rem 4rem;
  font-size: 1.6rem;
  position: relative;
  text-align: center;
  border-radius: 15px;

  &:hover {
    transform: scale(1.1);
    transition: all 0.3s linear;
  }
`;

const StyledSmallLogo = styled.img`
  display: block;
  margin: 2rem auto;  
  width: 8.8rem;
  cursor: pointer;
`;

const StyledNameInput = styled.input(({ theme }) => `
  width: 100%;
  color: ${theme.colors.BLACK_100};
  text-align: center;
  font-size: 1.6rem;
`);

export default SidebarMenuModals;
