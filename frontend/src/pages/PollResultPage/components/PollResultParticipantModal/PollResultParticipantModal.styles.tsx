import styled from '@emotion/styled';

const StyledParticipantsContainer = styled.div(
  ({ theme }) => `
  width: 68rem;
  height: 56rem;
  border-radius: 1.2rem;
  background-color: ${theme.colors.WHITE_100};
  `
);

const StyledIcon = styled.img`
  width: 4rem;
  margin-bottom: 2rem;
  /* TODO: 이렇게 filter를 통해서 바꿔주는 것 vs 애초에 이미지를 가지는 것 */
  filter: invert(96%) sepia(69%) saturate(7252%) hue-rotate(320deg) brightness(100%) contrast(96%);
`;

const StyledTitle = styled.p`
  font-size: 2rem;
  text-align: center;
  margin-bottom: 0.8rem;
  overflow-y: auto;
  width: 90%;
  padding: 0.4rem 0;
`;

const StyledDescription = styled.p`
  font-size: 1.2rem;
  text-align: center;
  margin-bottom: 1.2rem;
`;

const StyledTop = styled.div`
  display: flex;
  flex-direction: column;
  position: relative;
  height: 25%;
  padding-top: 2.4rem;
  align-items: center;
`;

const StyledCloseIcon = styled.img`
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

const StyledBottom = styled.div(
  ({ theme }) => `
    background-color: ${theme.colors.YELLOW_50};
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

const StyledParticipantContainer = styled.div`
  width: 70%;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
  gap: 2rem;
`;

const StyledParticipantDescription = styled.p(
  ({ theme }) => `
  width: 80%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: ${theme.colors.WHITE_100};
  border-radius: 2rem;
  font-size: 1.6rem;
  padding: 2rem;
  word-break: break-all;
`
);

export {
  StyledParticipantsContainer,
  StyledIcon,
  StyledTitle,
  StyledTop,
  StyledCloseIcon,
  StyledBottom,
  StyledDescription,
  StyledParticipantContainer,
  StyledParticipantDescription
};
