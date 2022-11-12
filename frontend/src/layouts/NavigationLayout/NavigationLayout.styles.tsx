import responsive from '@/utils/responsive';
import styled from '@emotion/styled';

const StyledContainer = styled.div`
  position: relative;
  display: flex;

  ${responsive.mobile(`
    display: block;
  `)}
`;

export { StyledContainer };
