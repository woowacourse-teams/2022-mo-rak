import { CSSProperties } from 'react';
import { Member } from '@/types/group';
import {
  StyledContainer,
  StyledUserImage,
  StyledUserName
} from '@/components/Avatar/Avatar.styles';

type Props = {
  profileUrl: Member['profileUrl'];
  name?: Member['name'];
} & CSSProperties;

function Avatar({ profileUrl, name, width, fontSize }: Props) {
  return (
    <StyledContainer width={width}>
      <StyledUserImage src={profileUrl} />
      {name && <StyledUserName fontSize={fontSize}>{name}</StyledUserName>}
    </StyledContainer>
  );
}

export default Avatar;
