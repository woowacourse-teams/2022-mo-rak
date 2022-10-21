import styled from '@emotion/styled';
import { CSSProperties } from 'react';
import { Poll } from '../../../../types/poll';

const StyledParticipantCount = styled.div`
  position: absolute;
  right: 1.2rem;
  top: 1.2rem;
  cursor: pointer;
  display: flex;
`;

const StyledUserIcon = styled.img`
  width: 1.6rem;
  height: 1.6rem;
`;

const StyledCrownIcon = styled.img<
  CSSProperties & {
    isWinningPollItem: boolean;
  }
>(
  ({ isWinningPollItem }) => `
  display: ${isWinningPollItem ? 'block' : 'none'};
  position: absolute;
  top: 1.2rem;
  left: 2rem;
  width: 2rem;
  height: 2rem;
`
);

const StyledCheckIcon = styled.img<
  CSSProperties & {
    checked: boolean;
  }
>(
  ({ checked }) => `
  display: ${checked ? 'block' : 'none'};
  position: absolute;
  top: 1rem;
  left: 1.6rem;
  width: 2rem;
  height: 2rem;
`
);

const StyledUserCount = styled.span<{
  isWinningPollItem: boolean;
  status: Poll['status'];
}>(
  ({ theme, status, isWinningPollItem }) => `
  color: ${
    status === 'CLOSED' && isWinningPollItem ? theme.colors.WHITE_100 : theme.colors.BLACK_100
  };
  font-size: 1.6rem;
`
);

const StyledSubject = styled.span<{ isWinningPollItem: boolean; status: Poll['status'] }>(
  ({ theme, isWinningPollItem, status }) => `
  color: ${
    status === 'CLOSED' && isWinningPollItem ? theme.colors.WHITE_100 : theme.colors.BLACK_100
  };
  font-size: 1.6rem;
`
);

export {
  StyledParticipantCount,
  StyledUserIcon,
  StyledCrownIcon,
  StyledCheckIcon,
  StyledUserCount,
  StyledSubject
};
