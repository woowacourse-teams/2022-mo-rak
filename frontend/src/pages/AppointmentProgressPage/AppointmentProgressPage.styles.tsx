import responsive from '@/utils/responsive';
import styled from '@emotion/styled';

const StyledContainer = styled.div`
  width: calc(100% - 36.4rem);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 8rem;

  ${responsive.mobile(`
    width: 100%;
    padding: 20rem 8rem;
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
  gap: 1.2rem;
  align-items: center;
`;

export { StyledContainer, StyledLeftContainer, StyledRightContainer };
