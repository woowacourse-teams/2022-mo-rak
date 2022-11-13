import styled from '@emotion/styled';
import responsive from '../../utils/responsive';
import { LAYOUT } from '@/constants/style';

const StyledContainer = styled.div`
  width: ${LAYOUT.PAGE_WIDTH};
  display: flex;
  flex-direction: column;
  gap: 4rem;
  padding: ${LAYOUT.MAIN_PAGE_PADDING};

  ${responsive.mobile(`
    width: 100%;
    padding: ${LAYOUT.MOBILE_PAGE_PADDING};
  `)}
`;

export { StyledContainer };
