import styled from '@emotion/styled';
import responsive from '../../utils/responsive';
import { LAYOUT } from '@/constants/style';

const StyledContainer = styled.div`
  width: ${LAYOUT.PAGE_WIDTH};
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6rem;

  ${responsive.mobile(`
    align-items: flex-start;
    width: 100%;
    padding: ${LAYOUT.MOBILE_PAGE_PADDING};
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
`;

const StyledContentContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 2rem;

  ${responsive.mobile(`
    width: 100%;
  `)}
`;

export { StyledContainer, StyledLeftContainer, StyledRightContainer, StyledContentContainer };
