import styled from '@emotion/styled';

const StyledFeatureMenuContainer = styled.div`
  cursor: pointer;
`;

const StyledImageWrapper = styled.div(
  ({ theme }) => `
  width: 32rem;
  height: 32rem;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  border-radius: 8rem;
  font-size: 3.2rem;
  background: ${theme.colors.WHITE_100};
  box-shadow: rgba(149, 157, 165, 0.1) 0px 8px 24px;
  transition: 0.5s;

  &:hover {
    background: ${theme.colors.YELLOW_50};
    box-shadow: rgba(250, 222, 144, 0.7) 0px 50px 100px -20px, rgba(250, 222, 144, 0.3) 0px 30px 60px -30px;
  }

  &:hover > img {
    filter: invert(100%) sepia(100%) saturate(0%) hue-rotate(194deg) brightness(102%) contrast(101%);
  }
  `
);

const StyledImage = styled.img`
  width: 10rem;
  height: 10rem;
  filter: invert(96%) sepia(69%) saturate(7252%) hue-rotate(320deg) brightness(100%) contrast(96%);
`;

const StyledFeatureMenuName = styled.div`
  margin: 2rem 0 0;
  text-align: center;
  font-size: 2.4rem;
`;

export { StyledImageWrapper, StyledFeatureMenuContainer, StyledFeatureMenuName, StyledImage };
