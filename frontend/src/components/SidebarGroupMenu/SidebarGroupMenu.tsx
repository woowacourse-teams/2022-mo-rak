import React, { useState, useEffect } from 'react';
import styled from '@emotion/styled';
import { Link, useNavigate } from 'react-router-dom';
import Setting from '../../assets/setting.svg';
import Menu from '../../assets/menu.svg';
import Plus from '../../assets/plus.svg';
import Leave from '../../assets/leave.svg';
import FlexContainer from '../@common/FlexContainer/FlexContainer';
import { getDefaultGroup, leaveGroup } from '../../api/group';
import { getLocalStorageItem, removeLocalStorageItem } from '../../utils/storage';
import { GroupInterface } from '../../types/group';

interface Props {
  groupCode: GroupInterface['code'];
  groups: Array<GroupInterface>;
}

function SidebarGroupMenu({ groupCode, groups }: Props) {
  const [isShowGroupList, setIsShowGroupList] = useState(false);
  const [defaultGroup, setDefaultGroup] = useState<GroupInterface>();

  const navigate = useNavigate();

  useEffect(() => {
    const fetchGetDefaultGroup = async () => {
      try {
        const res = await getDefaultGroup();
        setDefaultGroup(res.data);
      } catch (err) {
        if (err instanceof Error) {
          const statusCode = err.message;
          if (statusCode === '401') {
            removeLocalStorageItem('token');
            navigate('/');

            return;
          }

          if (statusCode === '404') {
            navigate('/init');
            console.log('속해있는 그룹이 없습니다.');
          }
        }
      }
    };
    const token = getLocalStorageItem('token');

    if (token) {
      fetchGetDefaultGroup();
    }
  }, []);

  const handleShowGroupList = () => {
    setIsShowGroupList(!isShowGroupList);
  };

  const handleLeaveGroup = async () => {
    // TODO: 이후에 모달창으로 변경하기
    if (window.confirm('그룹을 나가시겠습니까?')) {
      try {
        await leaveGroup(groupCode);
        navigate('/init');
      } catch (err) {
        alert(err);
      }
    }
  };

  return (
    <StyledGroupContainer>
      <StyledMenuHeader>그룹</StyledMenuHeader>

      {/* default group */}
      <FlexContainer justifyContent="space-between">
        <FlexContainer gap="2rem">
          <StyledGroupImage src="https://us.123rf.com/450wm/zoomzoom/zoomzoom1803/zoomzoom180300055/97726350-%EB%B9%9B-%EA%B5%AC%EB%A6%84%EA%B3%BC-%ED%91%B8%EB%A5%B8-%EB%B4%84-%ED%95%98%EB%8A%98.jpg?ver=6" />
          <FlexContainer flexDirection="column">
            <StyledGroupTitle>{defaultGroup && defaultGroup.name}</StyledGroupTitle>
          </FlexContainer>
        </FlexContainer>

        <StyledGroupIconGroup>
          {/* TODO: setting 아이콘은, host만 보이도록 해줘야한다. */}
          <StyledSettingIcon src={Setting} />
          <StyledGroupListIcon src={Menu} onClick={handleShowGroupList} />
        </StyledGroupIconGroup>
      </FlexContainer>

      {/* group list */}
      <StyledGroupListBox isShowGroupList={isShowGroupList}>
        <StyledGroupListContainer>
          {groups.map((group) => (
            <StyledGroupList to={`groups/${group.code}`} isDefaultGroup={groupCode === group.code}>
              <StyledGroupListImage src="https://us.123rf.com/450wm/zoomzoom/zoomzoom1803/zoomzoom180300055/97726350-%EB%B9%9B-%EA%B5%AC%EB%A6%84%EA%B3%BC-%ED%91%B8%EB%A5%B8-%EB%B4%84-%ED%95%98%EB%8A%98.jpg?ver=6" />
              <FlexContainer flexDirection="column">
                <StyledGroupTitle>{group.name}</StyledGroupTitle>
              </FlexContainer>
            </StyledGroupList>
          ))}
        </StyledGroupListContainer>
        <StyledParticipateNewGroup>
          <img src={Plus} alt="participate-new-group-button" />
          <StyledGroupText>새로운 그룹 참가</StyledGroupText>
        </StyledParticipateNewGroup>
        <StyledCreateNewGroup>
          <img src={Plus} alt="create-new-group-button" />
          <StyledGroupText>새로운 그룹 생성</StyledGroupText>
        </StyledCreateNewGroup>
        <StyledLeaveGroup onClick={handleLeaveGroup}>
          <img src={Leave} alt="group-exit-button" />
          <StyledGroupText>그룹 나가기</StyledGroupText>
        </StyledLeaveGroup>
      </StyledGroupListBox>
    </StyledGroupContainer>
  );
}

const StyledMenuHeader = styled.div`
  width: 100%;
  font-size: 2rem;
  text-align: left;
  margin-bottom: 2rem;
`;

const StyledGroupListBox = styled.div<{isShowGroupList: boolean}>(({ theme, isShowGroupList }) => `
  visibility: hidden;
  opacity: 0;
  transition: visibility 0s, opacity 0.2s ease-in-out;
  width: 24rem;
  background: ${theme.colors.WHITE_100};
  position: absolute;
  right: -25.2rem;
  top: 4.4rem;
  border-radius: 12px;
  max-height: 45.2rem;

  ${isShowGroupList && `
  visibility: visible;
  opacity: 1;
  `}
`);

const StyledGroupListContainer = styled.div`
  overflow-y: auto;
  max-height: 32.4rem;
`;

const StyledSettingIcon = styled.img`
  width: 2.4rem;
  cursor: pointer;

  &:hover {
    transform: rotate(0.5turn);
    transition: all 0.3s linear;
  }
`;

const StyledGroupListIcon = styled.img`
  width: 2.4rem;
  cursor: pointer;
  &:hover {
    transform: scale(1.1, 1.1);
    transition: all 0.3s linear;
  }
`;

const StyledGroupImage = styled.img`
  width: 8rem;
  height: 8rem;
  border-radius: 1.2rem;
`;

const StyledGroupListImage = styled.img`
  width: 5.2rem;
  height: 5.2rem;
  border-radius: 0.8rem;
`;

const StyledGroupTitle = styled.div`
  font-size: 1.6rem;
  margin-bottom: 1.2rem;
`;

const StyledGroupContainer = styled.div`
position: relative;
  width: 100%;
  margin-bottom: 2.8rem;
`;

const StyledGroupList = styled(Link)<{ isDefaultGroup: boolean }>(
  ({ theme, isDefaultGroup }) => `
  display: flex;
  gap: 2rem;
  padding: 2rem;
  text-decoration: none;
  margin: 1.2rem;
  color: ${theme.colors.BLACK_100};

  ${isDefaultGroup
    && `
    background: ${theme.colors.GRAY_100}; 
    border-radius: 10px;
    `
}
`
);

const StyledGroupIconGroup = styled.div`
  display: flex;
  align-items: flex-start;
  gap: 0.4rem;
  padding-right: 4rem;
`;

const StyledParticipateNewGroup = styled.button`
  display: flex;
  align-items: center;
  cursor: pointer;
  gap: 1.2rem;
  font-size: 1.6rem;
  padding: 2rem;

  &:hover {
    transform: scale(1.05);
    transition: all 0.2s linear;
  }
`;

const StyledCreateNewGroup = styled.button`
  display: flex;
  align-items: center;
  cursor: pointer;
  gap: 1.2rem;
  font-size: 1.6rem;
  width: 100%;
  padding: 2rem;

  &:hover {
    transform: scale(1.05);
    transition: all 0.2s linear;
  }
`;

const StyledGroupText = styled.p`
  font-size: 2rem;
`;

const StyledLeaveGroup = styled.button`
  display: flex;
  align-items: center;
  cursor: pointer;
  gap: 1.2rem;
  font-size: 1.6rem;
  width: 100%;
  padding: 2rem;

  &:hover {
    transform: scale(1.05);
    transition: all 0.2s linear;
  }
`;

export default SidebarGroupMenu;
