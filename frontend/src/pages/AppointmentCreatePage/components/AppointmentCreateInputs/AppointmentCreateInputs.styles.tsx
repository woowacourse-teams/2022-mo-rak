import styled from '@emotion/styled';
import responsive from '@/utils/responsive';

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
`;

export { StyledLeftContainer, StyledRightContainer };
