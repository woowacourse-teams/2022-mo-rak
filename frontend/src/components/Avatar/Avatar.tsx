import styled from '@emotion/styled';
import { CSSProperties } from 'react';
import { MemberInterface } from '../../types/group';

interface Props extends CSSProperties {
  profileUrl: MemberInterface['profileUrl'];
  name: MemberInterface['name'];
}

function Avatar({ profileUrl, name, width, fontSize }: Props) {
  return (
    <StyledUserProfile width={width}>
      <StyledUserImage src={profileUrl} />
      <StyledUserName fontSize={fontSize}>{name}</StyledUserName>
    </StyledUserProfile>
  );
}

// TODO: StyledContainer 어떤가
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
  aspect-ratio: 1 / 1;
`;

const StyledUserName = styled.p<CSSProperties>(
  ({ fontSize }) => `
  text-align: center;
  font-size: ${fontSize || '1.6rem'};
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
`
);

export default Avatar;
