import styled from '@emotion/styled';
import responsive from '../../utils/responsive';
import { LAYOUT } from '@/constants/style';

const StyledContainer = styled.div`
  width: ${LAYOUT.PAGE_WIDTH};
  display: flex;
  align-items: center;
  justify-content: center;

  ${responsive.mobile(`
    width: 100%;
    padding: ${LAYOUT.MOBILE_PAGE_PADDING};
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
