import styled from '@emotion/styled';
import responsive from '../../utils/responsive';

const StyledContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 4rem;
  width: calc(100% - 36.4rem);
  padding: 6.4rem 20rem;

  ${responsive.mobile(`
    width: 100%;
    padding: 0 8rem;
    margin-top: 8rem;
  `)}
`;

export { StyledContainer };
