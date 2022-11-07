import styled from '@emotion/styled';

const StyledModalFormContainer = styled.form(
  ({ theme }) => `
    position: relative;
    background-color: ${theme.colors.WHITE_100};
    border-radius: 1.2rem;
    width: 68rem;
    height: 41.6rem;
  `
);

const StyledSlackLogo = styled.img`
  width: 6.8rem;
  display: block;
  margin: 0 auto;
  margin-bottom: 2rem;
`;

const StyledHeaderText = styled.div`
  font-size: 2rem;
  text-align: center;
  margin-bottom: 1.2rem;
`;

const StyledGuideText = styled.div`
  font-size: 1.6rem;
  text-align: center;
`;

const StyledTop = styled.div`
  position: relative;
  height: 50%;
  padding-top: 2.4rem;
`;

const StyledCloseButton = styled.img`
  position: absolute;
  right: 2.4rem;
  top: 2.4rem;
  cursor: pointer;
  width: 1.6rem;
  height: 1.6rem;

  &:hover {
    transform: scale(1.1);
    transition: all 0.3s linear;
  }
`;

const StyledTriangle = styled.div(
  ({ theme }) => `
  position: absolute;
  border-left: 2rem solid transparent;
  border-right: 2rem solid transparent;
  border-top: 2rem solid ${theme.colors.WHITE_100};
  width: 0;
  bottom: -2.8rem;
  right: 50%;
  transform: translate(50%, -50%);
`
);

const StyledBottom = styled.div(
  ({ theme }) => `
    background-color: ${theme.colors.YELLOW_50};
    height: 50%;
    padding-top: 4.4rem;
    border-bottom-left-radius: 1.2rem;
    border-bottom-right-radius: 1.2rem;
  `
);

const StyledLinkIcon = styled.img`
  position: absolute;
  left: 1.2rem;
  top: 1.6rem;
  width: 2.4rem;
  height: 2.4rem;
`;

const StyledButton = styled.button(
  ({ theme }) => `
  background-color: ${theme.colors.YELLOW_200};
  color: ${theme.colors.WHITE_100};
  width: 16rem;
  padding: 1.6rem 3.2rem;
  border-radius: 1.2rem;
  font-size: 1.6rem;
  position: relative;
  text-align: center;

  &:hover {
    transform: scale(1.1);
    transition: all 0.3s linear;
  }
`
);

const StyledSmallLogo = styled.img`
  display: block;
  margin: 2rem auto;
  width: 8.8rem;
  cursor: pointer;
`;

export {
  StyledModalFormContainer,
  StyledSlackLogo,
  StyledHeaderText,
  StyledGuideText,
  StyledTop,
  StyledCloseButton,
  StyledTriangle,
  StyledBottom,
  StyledLinkIcon,
  StyledButton,
  StyledSmallLogo
};
