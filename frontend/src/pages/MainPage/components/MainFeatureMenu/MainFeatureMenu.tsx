import {
  StyledImageWrapper,
  StyledFeatureMenuContainer,
  StyledFeatureMenuName,
  StyledImage
} from './MainFeatureMenu.style';

interface Props {
  onClick: () => void;
  menu: string;
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
