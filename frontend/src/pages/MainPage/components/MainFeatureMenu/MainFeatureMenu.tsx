import { MouseEventHandler } from 'react';
import {
  StyledImageContainer,
  StyledFeatureMenuWrapper,
  StyledFeatureMenuName,
  StyledImage
} from './MainFeatureMenu.style';

interface Props {
  onClick: MouseEventHandler<HTMLDivElement>;
  name: '투표하기' | '약속잡기' | '역할 정하기';
  img: string;
}

function MainFeatureMenu({ onClick, name, img }: Props) {
  return (
    <StyledFeatureMenuWrapper onClick={onClick}>
      <StyledImageContainer>
        {/* TODO: StyledImage 네이밍 고민 */}
        <StyledImage src={img} alt={name} />
        <StyledFeatureMenuName>{name}</StyledFeatureMenuName>
      </StyledImageContainer>
    </StyledFeatureMenuWrapper>
  );
}

export default MainFeatureMenu;
