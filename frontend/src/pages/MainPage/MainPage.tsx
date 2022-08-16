import styled from '@emotion/styled';
import React from 'react';
import { useParams } from 'react-router-dom';
import MembersProfile from '../../components/MembersProfile/MembersProfile';
import MainFeatureMenuContainer from '../../components/Main/MainFeatureMenuContainer/MainFeatureMenuContainer';
import { GroupInterface } from '../../types/group';
import { useMenuDispatch } from '../../context/MenuProvider';

function MainPage() {
  const dispatch = useMenuDispatch();
  dispatch({ type: 'SET_CLICKED_MENU', menu: 'main' });

  const { groupCode } = useParams() as { groupCode: GroupInterface['code'] };

  return (
    <StyledContainer>
      <MembersProfile groupCode={groupCode} />
      <MainFeatureMenuContainer />
    </StyledContainer>
  );
}

const StyledContainer = styled.div`
  width: calc(100% - 36.4rem);
  padding: 6.4rem 20rem;
`;

export default MainPage;
