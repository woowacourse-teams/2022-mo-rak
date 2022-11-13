import { FormEvent } from 'react';

import { useNavigate } from 'react-router-dom';
import { AxiosError } from 'axios';
import TextField from '@/components/TextField/TextField';
import Input from '@/components/Input/Input';
import Modal from '@/components/Modal/Modal';
import FlexContainer from '@/components/FlexContainer/FlexContainer';
import theme from '@/styles/theme';
import slackImg from '@/assets/slack.svg';
import plusImg from '@/assets/plus.svg';
import linkImg from '@/assets/link.svg';
import closeButtonImg from '@/assets/close-button.svg';
import serviceLogoImg from '@/assets/service-logo.svg';
import { Group } from '@/types/group';
import { createGroup, participateGroup } from '@/apis/group';
import useNavigationBarDispatchContext from '@/hooks/useNavigationBarDispatchContext';
import { linkSlack } from '@/apis/slack';
import { LinkSlackRequest } from '@/types/slack';
import useInput from '@/hooks/useInput';
import {
  StyledModalFormContainer,
  StyledSlackLogo,
  StyledHeaderText,
  StyledGuideText,
  StyledTopContainer,
  StyledCloseButton,
  StyledTriangle,
  StyledBottomContainer,
  StyledLinkIcon,
  StyledButton,
  StyledSmallLogo
} from '@/layouts/NavigationLayout/components/SidebarModals/SidebarModals.styles';

type Props = {
  activeModal: string | null;
  closeModal: () => void;
  groupCode: Group['code'];
};

function SidebarModals({ activeModal, closeModal, groupCode }: Props) {
  const [groupName, handleGroupName, resetGroupName] = useInput('');
  const [invitationCode, handleInvitationCode, resetInvitationCode] = useInput('');
  const [slackUrl, handleSlackUrl, resetSlackUrl] = useInput('');
  const navigationBarDispatch = useNavigationBarDispatchContext();
  const navigate = useNavigate();

  const handleCreateGroup = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    try {
      const res = await createGroup(groupName);
      const groupCode = res.headers.location.split('groups/')[1];

      navigate(`/groups/${groupCode}`);
      navigationBarDispatch({ type: 'SET_IS_GROUPS_MODAL_VISIBLE', payload: false });
      resetGroupName();
      closeModal();
    } catch (err) {
      if (err instanceof AxiosError) {
        const errCode = err.response?.data.codeNumber;

        if (errCode === '4000') {
          alert('팀 이름은 공백일 수 없습니다');
          resetGroupName();
        }
      }
    }
  };

  const handleParticipateGroup = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    try {
      const res = await participateGroup(invitationCode);
      const groupCode = res.headers.location.split('/groups/')[1];

      navigate(`/groups/${groupCode}`);
      navigationBarDispatch({ type: 'SET_IS_GROUPS_MODAL_VISIBLE', payload: false });
      resetInvitationCode();
      closeModal();
    } catch (err) {
      if (err instanceof AxiosError) {
        const errCode = err.response?.data.codeNumber;

        switch (errCode) {
          case '1101': {
            alert('이미 참여하고 있는 그룹입니다!');
            resetInvitationCode();

            break;
          }

          case '1301':
            alert('찾으시는 그룹이 없습니다. 코드를 다시 확인해주세요!');
            resetInvitationCode();

            break;
        }
      }
    }
  };

  const handleLinkSlack = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const slack: LinkSlackRequest = {
      url: slackUrl
    };

    try {
      await linkSlack(slack, groupCode);
      alert('슬랙 채널과 연동이 완료되었습니다 🎉');
      resetSlackUrl();
      closeModal();
    } catch (err) {
      if (err instanceof AxiosError) {
        const errCode = err.response?.data.codeNumber;

        if (errCode === '4000') {
          alert('http로 시작하는 올바른 url을 입력해주세요!');
          resetSlackUrl();
        }
      }
    }
  };

  return (
    <>
      {/* TODO: 모달 컴포넌트 3개로 나눠줘야할듯 */}
      {/* 슬랙 연동 */}
      <Modal isVisible={activeModal === 'slack'} close={closeModal}>
        {/* 슬랙 메뉴 */}
        <StyledModalFormContainer onSubmit={handleLinkSlack}>
          <StyledTopContainer>
            <StyledSlackLogo src={slackImg} alt="slack-logo" />
            <StyledHeaderText>슬랙 채널과 연동해보세요!</StyledHeaderText>
            <StyledGuideText>
              슬랙 채널과 연동하면, 그룹의 새소식을 슬랙으로 받아볼 수 있어요
            </StyledGuideText>
            <StyledCloseButton onClick={closeModal} src={closeButtonImg} alt="close-button" />
            <StyledTriangle />
          </StyledTopContainer>
          <StyledBottomContainer>
            <FlexContainer flexDirection="column" alignItems="center" gap="2.4rem">
              <TextField
                variant="filled"
                colorScheme={theme.colors.WHITE_100}
                borderRadius="1.2rem"
                padding="1.6rem 6rem"
                width="50.4rem"
              >
                <Input
                  value={slackUrl}
                  onChange={handleSlackUrl}
                  placeholder="슬랙 채널 url 입력 후, 확인버튼을 누르면 연동 끝!"
                  fontSize="1.6rem"
                  required
                  autoFocus
                />
                <StyledLinkIcon src={linkImg} alt="link-icon" />
              </TextField>
              <StyledButton>확인</StyledButton>
            </FlexContainer>
          </StyledBottomContainer>
        </StyledModalFormContainer>
      </Modal>

      {/* 그룹 생성 */}
      <Modal isVisible={activeModal === 'create'} close={closeModal}>
        <StyledModalFormContainer onSubmit={handleCreateGroup}>
          <StyledTopContainer>
            <StyledSmallLogo src={serviceLogoImg} alt="logo" />
            <StyledHeaderText>그룹 생성</StyledHeaderText>
            <StyledGuideText>새로운 그룹을 빠르고 쉽게 생성해보세요</StyledGuideText>
            <StyledCloseButton onClick={closeModal} src={closeButtonImg} alt="close-button" />
            <StyledTriangle />
          </StyledTopContainer>
          <StyledBottomContainer>
            <FlexContainer flexDirection="column" alignItems="center" gap="2.4rem">
              <TextField
                variant="filled"
                colorScheme={theme.colors.WHITE_100}
                borderRadius="1.2rem"
                padding="1.6rem 10rem"
                width="50.4rem"
              >
                <Input
                  placeholder="그룹 이름을 입력하면 생성 완료!"
                  value={groupName}
                  onChange={handleGroupName}
                  color={theme.colors.BLACK_100}
                  textAlign="center"
                  fontSize="1.6rem"
                  required
                  autoFocus
                />
                <StyledLinkIcon src={plusImg} alt="plus-icon" />
              </TextField>
              <StyledButton>생성하기</StyledButton>
            </FlexContainer>
          </StyledBottomContainer>
        </StyledModalFormContainer>
      </Modal>

      {/* 그룹 참가 */}
      <Modal isVisible={activeModal === 'participate'} close={closeModal}>
        <StyledModalFormContainer onSubmit={handleParticipateGroup}>
          <StyledTopContainer>
            <StyledSmallLogo src={serviceLogoImg} alt="logo" />
            <StyledHeaderText>그룹 참가</StyledHeaderText>
            <StyledGuideText>새로운 그룹에도 참가해보세요</StyledGuideText>
            <StyledCloseButton onClick={closeModal} src={closeButtonImg} alt="close-button" />
            <StyledTriangle />
          </StyledTopContainer>
          <StyledBottomContainer>
            <FlexContainer flexDirection="column" alignItems="center" gap="2.4rem">
              <TextField
                variant="filled"
                colorScheme={theme.colors.WHITE_100}
                borderRadius="1.2rem"
                padding="1.6rem 10rem"
                width="50.4rem"
              >
                <Input
                  placeholder="그룹 코드를 입력하면 참가 완료!"
                  value={invitationCode}
                  onChange={handleInvitationCode}
                  color={theme.colors.BLACK_100}
                  textAlign="center"
                  fontSize="1.6rem"
                  required
                  autoFocus
                />
                <StyledLinkIcon src={plusImg} alt="plus-icon" />
              </TextField>
              <StyledButton>참가하기</StyledButton>
            </FlexContainer>
          </StyledBottomContainer>
        </StyledModalFormContainer>
      </Modal>
    </>
  );
}

export default SidebarModals;
