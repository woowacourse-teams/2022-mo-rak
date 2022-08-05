import styled from '@emotion/styled';
import React, { CSSProperties } from 'react';
import { PollMembersInterface } from '../../../types/poll';

interface Props extends CSSProperties {
  profileUrl: PollMembersInterface['profileUrl'];
  name: PollMembersInterface['name'];
}

function Avatar({ profileUrl, name, width, fontSize }: Props) {
  return (
    <StyledUserProfile width={width}>
      <StyledUserImage src={profileUrl} />
      <StyledUserName fontSize={fontSize}>{name}</StyledUserName>
    </StyledUserProfile>
  );
}

const StyledUserProfile = styled.div<CSSProperties>(
  ({ width }) => `
  display: flex;
  flex-direction: column;
  gap: 0.8rem;
  width: ${width || '8rem'};
  cursor: pointer;
`
);

const StyledUserImage = styled.img`
  border-radius: 100%;
`;

const StyledUserName = styled.p<CSSProperties>(
  ({ fontSize }) => `
  text-align: center;
  font-size: ${fontSize || '1.6rem'};
`
);

export default Avatar;
