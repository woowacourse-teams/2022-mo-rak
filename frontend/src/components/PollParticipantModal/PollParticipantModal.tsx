import React from 'react';
import styled from '@emotion/styled';
import { PollMembersInterface } from '../../types/poll';

interface Props {
  participants: Array<PollMembersInterface>;
}

function PollParticipantModal({ participants }: Props) {
  return (
    <StyledModalContainer>
      {participants.map((participant) => (
        <StyledUserProfile>
          <StyledUserImage src={participant.profileUrl} />
          <StyledUserName>{participant.name}</StyledUserName>
          <div className="description">{participant.id}</div>
        </StyledUserProfile>
      ))}
    </StyledModalContainer>
  );
}

const StyledModalContainer = styled.div(({ theme }) => `  
  display: flex;
  flex-direction: 'row';
  justify-content: center;
  gap: 4rem;
  width: 30rem; 
  height: 9.2rem; 
  padding: 2rem;
  position: absolute;
  top: -17px;
  left: 100px;
  background: ${theme.colors.WHITE_100};
`);

// TODO: StyledDescription 사용해서 하기 (child component를 선택할 수 있는 방법을 생각해보기)
const StyledUserProfile = styled.div`
  width: 4.4rem;
  cursor: pointer;
  
  .description {
    display: none;
    width: 10rem;
    height: 4rem;
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
  margin-bottom: 4px;
`;

const StyledUserName = styled.div`
  text-align: center;

`;

export default PollParticipantModal;
