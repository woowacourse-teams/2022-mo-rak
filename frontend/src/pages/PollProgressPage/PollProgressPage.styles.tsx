import styled from '@emotion/styled';
import responsive from '../../utils/responsive';
import { LAYOUT } from '@/constants/style';

const StyledContainer = styled.div`
  width: ${LAYOUT.PAGE_WIDTH};
  display: flex;
  align-items: center;
  justify-content: center;

  ${responsive.mobile(`
    align-items: flex-start;
    width: 100%;
    padding: ${LAYOUT.MOBILE_PAGE_PADDING};
  `)}
`;

export { StyledContainer };
