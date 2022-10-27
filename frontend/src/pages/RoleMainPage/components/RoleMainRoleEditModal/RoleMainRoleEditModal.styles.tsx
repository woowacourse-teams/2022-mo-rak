import styled from '@emotion/styled';

const StyledForm = styled.form(
  ({ theme }) => `
  width: 68rem;
  height: 56rem;
  border-radius: 1.2rem;
  background-color: ${theme.colors.WHITE_100};
  `
);

const StyledLogo = styled.img`
  width: 4rem;
  margin: 0 auto;
  margin-bottom: 2rem;
`;

const StyledTitle = styled.p`
  font-size: 2rem;
  text-align: center;
  margin-bottom: 0.8rem;
`;

const StyledDescription = styled.p`
  font-size: 1.2rem;
  text-align: center;
  margin-bottom: 1.2rem;
`;

const StyledTop = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  position: relative;
  height: 25%;
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
    background: ${theme.colors.YELLOW_50};
    height: 75%;
    padding: 4.4rem 0 2rem 0;
    border-bottom-left-radius: 1.2rem;
    border-bottom-right-radius: 1.2rem;
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 2.4rem;
    overflow-y: auto;
  `
);

export {
  StyledForm,
  StyledLogo,
  StyledTitle,
  StyledTop,
  StyledCloseButton,
  StyledTriangle,
  StyledBottom,
  StyledDescription
};
