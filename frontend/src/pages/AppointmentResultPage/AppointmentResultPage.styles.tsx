import responsive from '@/utils/responsive';
import styled from '@emotion/styled';

const StyledContainer = styled.div`
  width: calc(100% - 36.4rem);
  display: flex;
  align-items: center;
  justify-content: center;

  ${responsive.mobile(`
    width: 100%;
    padding: 20rem 8rem;
  `)}
`;

const StyledContentContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 4rem;

  ${responsive.mobile(`
    width: 100%;
  `)}
`;

export { StyledContainer, StyledContentContainer };
