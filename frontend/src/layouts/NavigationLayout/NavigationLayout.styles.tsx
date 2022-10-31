import styled from '@emotion/styled';
import responsive from '../../utils/responsive';

const StyledContainer = styled.div`
  display: flex;

  ${responsive.mobile(`
    display: block;
  `)}
`;

export { StyledContainer };
