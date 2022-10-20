import styled from '@emotion/styled';
import Circle from '../../../../assets/half-circle.svg';

const StyledLink = styled.a`
  text-decoration: none;
  margin-top: 8rem;
`;

const StyledGithubLogo = styled.img`
  width: 4rem;
`;

const StyledLoginText = styled.p(
  ({ theme }) => `
  color: ${theme.colors.WHITE_100};
  padding: 2rem 0;
  font-size: 2rem;
`
);

const StyledLoginContainer = styled.div(
  ({ theme }) => `
  display: flex;
  justify-content: center;
  gap: 2.4rem;
  width: 33.6rem;
  border-radius: 1.6rem;
  padding: 1.2rem;
  margin: 0 auto;
  color: ${theme.colors.WHITE_100};
  background-color: ${theme.colors.BLACK_100};
`
);

const StyledLogo = styled.img`
  width: 10rem;
  position: absolute;
  top: 2rem;
  left: 4rem;
`;

const StyledContainer = styled.section`
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  scroll-snap-align: start;
  position: relative;
  flex-direction: column;
  background-image: url(${Circle});
  background-size: contain;
  background-repeat: no-repeat;
  background-position: bottom 0 right 0;
`;

const StyledSubTitle = styled.div(
  ({ theme }) => `
  font-size: 2.4rem;
  margin-bottom: 1.2rem;
  letter-spacing: 0.4rem;
  margin: 0 auto;
  padding-top: 20rem;
  color: ${theme.colors.BLACK_100};
`
);

const StyledTitle = styled.h1(
  ({ theme }) => `
  font-size: 9.6rem;
  font-weight: 700;
  text-align: center;
  position: relative;
  line-height: 12rem;
  letter-spacing: -0.4rem;
  color: ${theme.colors.BLACK_100};
`
);

const StyledSmileImage = styled.img`
  position: absolute;
  bottom: 0;
  left: -4.8rem;
  top: 7.6rem;
  width: 10rem;
`;

const StyledLineImage = styled.img`
  position: absolute;
  right: 4.4rem;
  bottom: -4.4rem;
  width: 26rem;
`;

const StyledGlitterImage = styled.img`
  position: absolute;
  bottom: 0;
  right: -3.2rem;
  top: 5.2rem;
  width: 6rem;
`;

const StyledNextSectionGuideContainer = styled.div`
  position: absolute;
  right: 14rem;
  bottom: 6rem;
`;

const StyledBlobContainer = styled.div`
  position: relative;
`;

const StyledBlobImage = styled.img`
  width: 18rem;
  height: 17.4rem;
`;

const StyledAnchor = styled.a(
  ({ theme }) => `
  position: absolute;
  top: 7.2rem;
  left: 4.2rem;
  text-decoration: none;
  color: ${theme.colors.BLACK_100};
  font-size: 2.4rem;
`
);

export {
  StyledBlobContainer,
  StyledBlobImage,
  StyledGithubLogo,
  StyledGlitterImage,
  StyledLineImage,
  StyledLink,
  StyledLoginContainer,
  StyledLoginText,
  StyledLogo,
  StyledContainer,
  StyledNextSectionGuideContainer,
  StyledSmileImage,
  StyledSubTitle,
  StyledTitle,
  StyledAnchor
};
