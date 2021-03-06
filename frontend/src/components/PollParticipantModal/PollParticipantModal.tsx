/* eslint-disable react/jsx-no-useless-fragment */
import React from 'react';
import styled from '@emotion/styled';
import { PollMembersInterface } from '../../types/poll';

interface Props {
  participants: Array<PollMembersInterface>;
}

function PollParticipantModal({ participants }: Props) {
  return (
    <>
      {participants.length ? (
        <StyledContainer>
          {participants.map(({ profileUrl, name, description }) => (
            <StyledUserProfile>
              <StyledUserImage src={profileUrl} />
              {/* TODO: 이후 api 바뀐 명세에 따라, name === ''으로 변경해야함 */}
              <StyledUserName>{name === null ? '익명' : name}</StyledUserName>
              {/* TODO: 컴포넌트로 변경하기 */}
              <div className="description">{description}</div>
            </StyledUserProfile>
          ))}
        </StyledContainer>
      ) : ''}
    </>
  );
}

const StyledContainer = styled.div(({ theme }) => `  
  display: flex;
  flex-direction: row;
  justify-content: center;
  gap: 4rem;
  width: 30rem; 
  padding: 2rem;
  position: absolute;
  top: -17px;
  left: 100px;
  background: ${theme.colors.WHITE_100};
  color: ${theme.colors.BLACK_100};
`);

// TODO: StyledDescription 사용해서 하기 (child component를 선택할 수 있는 방법을 생각해보기)
const StyledUserProfile = styled.div`
  width: 4.4rem;
  cursor: pointer;
  position: relative;
  
  .description {
    position: absolute;
    display: none;
    width: 10rem;
    padding: 1rem;
    text-align: center;
    border: 1px solid gray; // TODO: 임시 스타일
    background: white;
  }

  &:hover .description {
    display: block;
  }
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

export default PollParticipantModal;
