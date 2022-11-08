import { CSSProperties } from 'react';
import styled from '@emotion/styled';

const StyledMenuHeader = styled.div`
  font-size: 2.4rem;
  text-align: left;
  margin-bottom: 2rem;
`;

const StyledGroups = styled.div`
  overflow-y: auto;
  max-height: 26.4rem;
`;

const StyledSettingIcon = styled.img`
  width: 2.4rem;
  cursor: pointer;

  &:hover {
    transform: rotate(0.5turn);
    transition: all 0.3s linear;
  }
`;

const StyledGroupsModalIcon = styled.img`
  width: 2.4rem;
  cursor: pointer;
  filter: invert(59%) sepia(6%) saturate(17%) hue-rotate(314deg) brightness(103%) contrast(77%);

  &:hover {
    transform: scale(1.1, 1.1);
    transition: all 0.3s linear;
  }
`;

const StyledGroupProfile = styled.div<CSSProperties>(
  ({ backgroundColor }) => `
  display: flex;
  justify-content: center;
  align-items: center;
  flex-shrink: 0;
  width: 8rem;
  height: 8rem;
  border-radius: 1.2rem;
  font-family: 'Nanum Gothic', sans-serif;
  background: ${backgroundColor};
`
);

const StyledGroupFirstCharacter = styled.div(
  ({ theme }) => `
  color: ${theme.colors.WHITE_100};
  font-size: 4.8rem;
`
);

const StyledGroupName = styled.div`
  font-size: 2.4rem;
  margin-bottom: 1.2rem;
  word-break: break-all;
`;

const StyledContainer = styled.div`
  position: relative;
  width: 100%;
  margin-bottom: 2.8rem;
  margin-top: 2rem;
`;

const StyledGroup = styled.div<{ isActive: boolean }>(
  ({ theme, isActive }) => `
  display: flex;
  gap: 2rem;
  align-items: center;
  padding: 2rem;
  text-decoration: none;
  margin: 1.2rem;
  color: ${theme.colors.BLACK_100};
  cursor: pointer;

  &:hover {
    background: ${theme.colors.GRAY_100};
    border-radius: 1.2rem;
    transition: all 0.2s linear;
  }

  ${
    isActive &&
    `
      background: ${theme.colors.GRAY_100}; 
      border-radius: 1.2rem;
    `
  }
`
);

const StyledGroupIconGroup = styled.div`
  display: flex;
  align-items: flex-start;
  gap: 0.4rem;
  padding-right: 4rem;
`;

const StyledParticipateNewGroupButton = styled.button`
  display: flex;
  align-items: center;
  cursor: pointer;
  gap: 1.2rem;
  padding: 2rem;

  &:hover {
    transform: scale(1.05);
    transition: all 0.2s linear;
  }
`;

const StyledCreateNewGroupButton = styled.button`
  display: flex;
  align-items: center;
  cursor: pointer;
  gap: 1.2rem;
  width: 100%;
  padding: 2rem;

  &:hover {
    transform: scale(1.05);
    transition: all 0.2s linear;
  }
`;

const StyledButtonText = styled.p`
  font-size: 2.4rem;
`;

const StyledLeaveGroupButton = styled.button`
  display: flex;
  align-items: center;
  cursor: pointer;
  gap: 1.2rem;
  width: 100%;
  padding: 2rem;

  &:hover {
    transform: scale(1.05);
    transition: all 0.2s linear;
  }
`;

const StyledPlusImage = styled.img`
  width: 2.4rem;
`;

const StyledLeaveImage = styled.img`
  width: 2.4rem;
`;

export {
  StyledMenuHeader,
  StyledGroups,
  StyledSettingIcon,
  StyledGroupsModalIcon,
  StyledGroupProfile,
  StyledGroupFirstCharacter,
  StyledGroupName,
  StyledContainer,
  StyledGroup,
  StyledGroupIconGroup,
  StyledParticipateNewGroupButton,
  StyledCreateNewGroupButton,
  StyledButtonText,
  StyledLeaveGroupButton,
  StyledPlusImage,
  StyledLeaveImage
};
