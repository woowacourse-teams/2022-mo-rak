import responsive from '@/utils/responsive';
import styled from '@emotion/styled';

const StyledContainer = styled.div`
  width: calc(100% - 36.4rem);
  display: flex;
  flex-direction: column;
  gap: 4rem;
  padding: 6.4rem 20rem;

  ${responsive.mobile(`
    width: 100%;
    padding: 20rem 8rem;
  `)}
`;

export { StyledContainer };
