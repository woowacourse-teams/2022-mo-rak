import { MouseEventHandler } from 'react';
import {
  StyledImageWrapper,
  StyledFeatureMenuContainer,
  StyledFeatureMenuName,
  StyledImage
} from './MainFeatureMenu.style';

interface Props {
  onClick: MouseEventHandler<HTMLDivElement>;
  menu: '투표하기' | '약속잡기' | '역할 정하기';
  menuImg: string;
}

function MainFeatureMenu({ onClick, menu, menuImg }: Props) {
  return (
    <StyledFeatureMenuContainer onClick={onClick}>
      <StyledImageWrapper>
        {/* TODO: StyledImage 네이밍 고민 */}
        <StyledImage src={menuImg} alt={menu} />
        <StyledFeatureMenuName>{menu}</StyledFeatureMenuName>
      </StyledImageWrapper>
    </StyledFeatureMenuContainer>
  );
}

export default MainFeatureMenu;
