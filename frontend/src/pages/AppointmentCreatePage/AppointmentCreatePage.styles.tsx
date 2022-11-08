import styled from '@emotion/styled';
import responsive from '../../utils/responsive';

const StyledContainer = styled.div`
  width: calc(100% - 36.4rem);
  display: flex;
  padding: 6.4rem 20rem;

  ${responsive.mobile(`
    align-items: flex-start;
    width: 100%;
    padding: 20rem 8rem;
  `)}
`;

const StyledLeftContainer = styled.div`
  display: flex;
  width: 45.2rem;
  flex-direction: column;
  gap: 1.8rem;

  ${responsive.mobile(`
    align-items: center; 
  `)}
`;

const StyledRightContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 4rem;
  width: calc(100% - 49.2rem);
  min-width: 44rem;

  ${responsive.mobile(`
    width: 100%;
  `)}
`;

const StyledContentContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 2rem;
  width: 100%;
`;

export { StyledContainer, StyledLeftContainer, StyledRightContainer, StyledContentContainer };
