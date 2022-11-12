import responsive from '@/utils/responsive';
import styled from '@emotion/styled';

const StyledContainer = styled.form`
  width: 64rem;
  display: flex;
  flex-direction: column;
  gap: 3.2rem;

  ${responsive.mobile(`
    width: 52rem;
  `)}
`;

export { StyledContainer };
