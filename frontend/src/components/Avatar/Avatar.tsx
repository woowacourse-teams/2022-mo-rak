import { CSSProperties } from 'react';
import { Member } from '../../types/group';
import { StyledUserProfile, StyledUserImage, StyledUserName } from './Avatar.styles';

type Props = {
  profileUrl: Member['profileUrl'];
  name: Member['name'];
} & CSSProperties;

function Avatar({ profileUrl, name, width, fontSize }: Props) {
  return (
    <StyledUserProfile width={width}>
      <StyledUserImage src={profileUrl} />
      <StyledUserName fontSize={fontSize}>{name}</StyledUserName>
    </StyledUserProfile>
  );
}

export default Avatar;
