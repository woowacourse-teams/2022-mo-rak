import { CSSProperties } from 'react';
import { MemberInterface } from '../../types/group';
import { StyledUserProfile, StyledUserImage, StyledUserName } from './Avatar.styles';

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

export default Avatar;
