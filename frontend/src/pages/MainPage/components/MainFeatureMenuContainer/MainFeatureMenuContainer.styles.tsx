import styled from '@emotion/styled';

const StyledFeatureMenuContainer = styled.div`
  cursor: pointer;
`;

const StyledTitle = styled.h1`
  display: block;
  font-size: 4.8rem;
  text-align: center;
  margin: 0 0 8rem;
`;

const StlyedImageWrapper = styled.div(
  ({ theme }) => `
  display: flex;
  justify-content: center;
  font-size: 3.2rem;
  border-radius: 100%;
  background: ${theme.colors.GRAY_200};
  padding: 8rem;  

  &:hover {
    background: ${theme.colors.GRAY_300};
  }
  `
);

const StyledImage = styled.img`
  width: 24rem;
  aspect-ratio: 1 / 1;
`;

const StyledFeatureMenuName = styled.div`
  margin: 2rem 0 0;
  text-align: center;
  font-size: 4rem;
`;

export {
  StlyedImageWrapper,
  StyledFeatureMenuContainer,
  StyledFeatureMenuName,
  StyledImage,
  StyledTitle
};
