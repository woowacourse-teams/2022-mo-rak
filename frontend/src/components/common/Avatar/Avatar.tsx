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
  width: ${width || '4.4rem'};
  cursor: pointer;
`
);

const StyledUserImage = styled.img`
  width: 100%;
  height: 4.4rem;
  border-radius: 100%;
`;

const StyledUserName = styled.div<CSSProperties>(
  ({ fontSize }) => `
  text-align: center;
  font-size: ${fontSize || '1.2rem'};
`
);

export default Avatar;
