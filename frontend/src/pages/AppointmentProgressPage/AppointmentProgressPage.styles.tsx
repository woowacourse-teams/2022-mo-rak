import styled from '@emotion/styled';
import responsive from '../../utils/responsive';
import { LAYOUT } from '@/constants/style';

const StyledContainer = styled.div`
  width: ${LAYOUT.PAGE_WIDTH};
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 8rem;

  ${responsive.mobile(`
    width: 100%;
    padding: ${LAYOUT.MOBILE_PAGE_PADDING};
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
