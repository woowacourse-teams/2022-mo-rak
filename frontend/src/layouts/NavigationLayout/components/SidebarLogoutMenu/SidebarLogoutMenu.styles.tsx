import styled from '@emotion/styled';

const StyledContainer = styled.button`
  display: flex;
  align-items: center;
  cursor: pointer;
  gap: 1.2rem;
  font-size: 1.6rem;
`;

const StyledLeaveImage = styled.img`
  width: 2rem;
  filter: invert(65%) sepia(7%) saturate(12%) hue-rotate(1deg) brightness(92%) contrast(84%);
`;

const StyledText = styled.p(
  ({ theme }) => `
  font-size: 1.6rem; 
  color: ${theme.colors.GRAY_400}
`
);

export { StyledContainer, StyledLeaveImage, StyledText };
