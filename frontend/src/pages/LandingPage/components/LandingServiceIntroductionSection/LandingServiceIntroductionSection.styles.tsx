import styled from '@emotion/styled';

const StyledContainer = styled.section`
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  scroll-snap-align: start;
  position: relative;
  justify-content: space-evenly;
  flex-wrap: wrap;
`;

const StyledTitle = styled.h1(
  ({ theme }) => `
  position: relative;
  text-align: left;
  font-size: 4rem;
  margin: 10rem 0 2rem;
  font-weight: 700;
  line-height: 4.4rem;
  letter-spacing: 0.4rem;
  color: ${theme.colors.BLACK_100};
`
);

const StyledHighlightImage = styled.img`
  position: absolute;
  left: -1.2rem;
  top: 3.2rem;
  width: 10.4rem;
  height: 6.8rem;
`;

const StyledSubTitle = styled.span(
  ({ theme }) => `
  font-size: 2rem;
  text-align: left;
  letter-spacing: 0.1rem; 
  line-height: 2.8rem;
  color: ${theme.colors.BLACK_100};
`
);

const StyledTitleContainer = styled.div`
  padding: 4rem;
`;

const StyledServicesImage = styled.img`
  width: 67.6rem;
  height: 63.6rem;
`;

export {
  StyledHighlightImage,
  StyledContainer,
  StyledServicesImage,
  StyledSubTitle,
  StyledTitle,
  StyledTitleContainer
};
