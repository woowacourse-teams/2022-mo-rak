import styled from '@emotion/styled';
import responsive from '../../utils/responsive';

const StyledContainer = styled.div`
  width: calc(100% - 36.4rem);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6rem;

  ${responsive.mobile(`
    width: 100%;
    height: 100vh;
    padding: 0 8rem;
  `)}
`;

const StyledLeftContainer = styled.div`
  display: flex;
  width: 45.2rem;
  flex-direction: column;
  gap: 1.8rem;
`;

const StyledRightContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 4rem;
`;

export { StyledContainer, StyledLeftContainer, StyledRightContainer };
