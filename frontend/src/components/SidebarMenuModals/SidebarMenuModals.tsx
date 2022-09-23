import { FormEvent } from 'react';
import styled from '@emotion/styled';

import { useNavigate } from 'react-router-dom';
import { AxiosError } from 'axios';
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
import useMenuDispatchContext from '../../hooks/useMenuDispatchContext';
import { linkSlack } from '../../api/slack';
import { SlackInterface } from '../../types/slack';
import useInput from '../../hooks/useInput';

interface Props {
  activeModalMenu: string | null;
  closeModal: () => void;
  groupCode: GroupInterface['code'];
}

function SidebarMenuModals({ activeModalMenu, closeModal, groupCode }: Props) {
  const [groupName, handleGroupName, resetGroupName] = useInput('');
  const [invitationCode, handleInvitationCode, resetInvitationCode] = useInput('');
  const [slackUrl, handleSlackUrl, resetSlackUrl] = useInput('');
  const dispatch = useMenuDispatchContext();
  const navigate = useNavigate();

  // 그룹 생성
  const handleCreateGroup = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    try {
      const res = await createGroup(groupName);
      const groupCode = res.headers.location.split('groups/')[1];

      navigate(`/groups/${groupCode}`);
      dispatch({ type: 'SET_IS_VISIBLE_GROUPS_MODAL', payload: false });
      resetGroupName();
      closeModal();
    } catch (err) {
      alert(err);
    }
  };

  // 그룹 참가
  const handleParticipateGroup = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    try {
      const res = await participateGroup(invitationCode);
      const groupCode = res.headers.location.split('/groups/')[1];

      navigate(`/groups/${groupCode}`);
      dispatch({ type: 'SET_IS_VISIBLE_GROUPS_MODAL', payload: false });
      resetInvitationCode();
      closeModal();
    } catch (err) {
      if (err instanceof AxiosError) {
        const errCode = err.response?.data.codeNumber;

        if (errCode === '1101') {
          alert('이미 참여하고 있는 그룹입니다!');
          resetInvitationCode();
        }
      }
    }
  };

  // slack url 등록
  const handleLinkSlack = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const slackUrlData: SlackInterface = {
      url: slackUrl
    };

    try {
      await linkSlack(slackUrlData, groupCode);
      alert('슬랙 채널과 연동이 완료되었습니다 🎉');
      resetSlackUrl();
      closeModal();
    } catch (err) {
      console.log(err);
    }
  };

  return (
    <>
      {/* 슬랙 연동 */}
      <Modal isVisible={activeModalMenu === 'slack'} close={closeModal}>
        {/* 슬랙 메뉴 */}
        <StyledModalFormContainer onSubmit={handleLinkSlack}>
          <StyledTop>
            <StyledSlackLogo src={Slack} alt="slack-logo" />
            <StyledHeaderText>슬랙 채널과 연동해보세요!</StyledHeaderText>
            <StyledGuideText>
              슬랙 채널과 연동하면, 그룹의 새소식을 슬랙으로 받아볼 수 있어요
            </StyledGuideText>
            <StyledCloseButton onClick={closeModal} src={Close} alt="close-button" />
            <StyledTriangle />
          </StyledTop>
          <StyledBottom>
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
                <StyledLinkIcon src={LinkIcon} alt="link-icon" />
              </TextField>
              <StyledButton>확인</StyledButton>
            </FlexContainer>
          </StyledBottom>
        </StyledModalFormContainer>
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

const StyledModalFormContainer = styled.form(
  ({ theme }) => `
    position: relative;
    background-color: ${theme.colors.WHITE_100};
    border-radius: 1.2rem;
    width: 68rem;
    height: 41.6rem;
  `
);

const StyledSlackLogo = styled.img`
  width: 6.8rem;
  display: block;
  margin: 0 auto;
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
  width: 1.6rem;
  height: 1.6rem;

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
  transform: translate(50%, -50%);
`;

const StyledBottom = styled.div(
  ({ theme }) => `
    background: ${theme.colors.YELLOW_50};
    height: 50%;
    padding-top: 4.4rem;
    border-bottom-left-radius: 1.2rem;
    border-bottom-right-radius: 1.2rem;
  `
);

const StyledLinkIcon = styled.img`
  position: absolute;
  left: 1.2rem;
  top: 1.2rem;
  width: 2.4rem;
  height: 2.4rem;
`;

const StyledButton = styled.button`
  background-color: ${theme.colors.YELLOW_200};
  color: ${theme.colors.WHITE_100};
  width: 14rem;
  padding: 1.6rem 3.2rem;
  border-radius: 1.2rem;
  font-size: 1.6rem;
  position: relative;
  text-align: center;

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

export default SidebarMenuModals;
