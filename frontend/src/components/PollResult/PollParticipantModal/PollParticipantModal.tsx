/* eslint-disable react/jsx-no-useless-fragment */
import React from 'react';
import styled from '@emotion/styled';
import { MemberInterface } from '../../../types/group';
import { SelectedPollItem } from '../../../types/poll';

interface Props {
  participants: Array<MemberInterface & Pick<SelectedPollItem, 'description'>>;
}

function PollParticipantModal({ participants }: Props) {
  return (
    <>
      {participants.length > 0 && (
        <StyledContainer>
          {participants.map(({ profileUrl, name, description }) => (
            <StyledUserProfile>
              <StyledUserImage src={profileUrl} />
              <StyledUserName>{name === '' ? '익명' : name}</StyledUserName>
              {/* TODO: 컴포넌트로 변경하기 */}
              {description && <div className="description">{description}</div>}
            </StyledUserProfile>
          ))}
        </StyledContainer>
      )}
    </>
  );
}

const StyledContainer = styled.div(
  ({ theme }) => `  
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
`
);

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
    border: 1px solid gray;
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
