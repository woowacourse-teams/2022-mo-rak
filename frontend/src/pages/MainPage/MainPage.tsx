import styled from '@emotion/styled';
import React, { useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { getDefaultGroup } from '../../api/group';
import FlexContainer from '../../components/common/FlexContainer/FlexContainer';
import Poll from '../../assets/poll.svg';
import Appointment from '../../assets/appointment.svg';
import MembersProfile from '../../components/MembersProfile/MembersProfile';

// TODO: 페이지 추상화
function MainPage() {
  const navigate = useNavigate();
  // TODO: 100% 들어올까??
  const { groupCode } = useParams() as { groupCode: string };

  // TODO: util로 빼기
  const handleNavigate = (location: string) => () => {
    navigate(location);
  };

  useEffect(() => {
    const fetchDefaultGroup = async () => {
      try {
        const { code } = await getDefaultGroup();
        navigate(`/groups/${code}`);
      } catch (err) {
        alert('속해있는 그룹이 없습니다. 오류입니다 삐빅');
        navigate('/init');
      }
    };

    if (!groupCode) {
      fetchDefaultGroup();
    }
  }, []);

  return (
    <StyledContainer>
      <MembersProfile groupCode={groupCode} />
      <FlexContainer flexDirection="column">
        <StyledTitle>사용할 메뉴를 선택하세요!</StyledTitle>
        <FlexContainer justifyContent="center" gap="8rem">
          {/* TODO: 스프린트2 리팩토링때 컴포넌트로 나누기 */}
          <StyledMenuContainer onClick={handleNavigate('poll')}>
            <StyledMenu>
              <StyledImage src={Poll} alt="poll menu" />
            </StyledMenu>
            <StyledName>투표하기</StyledName>
          </StyledMenuContainer>

          <StyledMenuContainer onClick={handleNavigate('/')}>
            <StyledMenu>
              <StyledImage src={Appointment} alt="appointment-menu" />
            </StyledMenu>
            <StyledName>약속잡기</StyledName>
          </StyledMenuContainer>
        </FlexContainer>
      </FlexContainer>
    </StyledContainer>
  );
}

const StyledContainer = styled.div`
  width: calc(100% - 36.4rem);
  padding: 6.4rem 20rem;
`;

const StyledMenuContainer = styled.div`
  cursor: pointer;
`;

const StyledTitle = styled.h1`
  display: block;
  font-size: 4.8rem;
  text-align: center;
  margin: 0 0 8rem;
`;

const StyledMenu = styled.div(
  ({ theme }) => `
  display: flex;
  justify-content: center;
  font-size: 3.2rem;
  border-radius: 100%;
  background: ${theme.colors.GRAY_200};
  width: 50rem;
  height: 50rem;

  &:hover {
    background: ${theme.colors.GRAY_300};
  }
  `
);
const StyledImage = styled.img`
  width: 32rem;
`;

const StyledName = styled.div`
  margin: 2rem 0 0;
  text-align: center;
  font-size: 4rem;
`;

export default MainPage;
