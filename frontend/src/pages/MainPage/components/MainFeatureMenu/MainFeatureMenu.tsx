import { MouseEventHandler } from 'react';
import {
  StyledMenuContainer,
  StyledContainer,
  StyledName,
  StyledImage
} from './MainFeatureMenu.styles';

type Props = {
  onClick: MouseEventHandler<HTMLDivElement>;
  name: '투표하기' | '약속잡기' | '역할 정하기';
  img: string;
};

function MainFeatureMenu({ onClick, name, img }: Props) {
  return (
    <StyledContainer onClick={onClick}>
      <StyledMenuContainer>
        {/* TODO: StyledImage 네이밍 고민 */}
        <StyledImage src={img} alt={name} />
        <StyledName>{name}</StyledName>
      </StyledMenuContainer>
    </StyledContainer>
  );
}

export default MainFeatureMenu;
