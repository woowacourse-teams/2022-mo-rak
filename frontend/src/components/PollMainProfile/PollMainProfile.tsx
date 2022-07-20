import styled from '@emotion/styled';
import React from 'react';

// TODO: 그룹 멤버조회 api 연결
function PollMainProfile() {
  const members = [{ id: 1, name: '우영우', profileUrl: 'https://spnimage.edaily.co.kr/images/Photo/files/NP/S/2022/06/PS22062800115.jpg' }, { id: 2, name: '태연', profileUrl: 'https://cdnweb01.wikitree.co.kr/webdata/editor/202202/03/img_20220203152221_f00e3cfa.webp' }];
  return (
    <StyledContainer>
      {members.map(({ profileUrl, name }) => (
        <StyledUserProfile>
          <StyledUserImage src={profileUrl} />
          <StyledUserName>{name}</StyledUserName>
        </StyledUserProfile>
      ))}
    </StyledContainer>
  );
}

const StyledContainer = styled.div`
  display: flex;
  gap: 1.2rem;
  margin: 2rem 0; 
`;

const StyledUserProfile = styled.div`
  width: 4.4rem;
  cursor: pointer;
  position: relative;
`;

const StyledUserImage = styled.img`
  width: 100%;
  height: 4.4rem; 
  border-radius: 100%;
  margin-bottom: 0.4rem;
`;

const StyledUserName = styled.div`
  text-align: center;
`;

export default PollMainProfile;
