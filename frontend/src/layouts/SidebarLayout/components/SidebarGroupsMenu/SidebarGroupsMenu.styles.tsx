import { CSSProperties } from 'react';
import styled from '@emotion/styled';

const StyledMenuHeader = styled.div`
  font-size: 1.6rem;
  text-align: left;
  padding-bottom: 2rem;
`;

const StyledGroupsModalContainer = styled.div<{ isVisible: boolean }>(
  ({ theme, isVisible }) => `
  position: absolute;
  right: -25.2rem;
  top: 4.4rem;
  visibility: hidden;
  width: 24rem;
  max-height: 55.2rem;
  border-radius: 1.2rem;
  background: ${theme.colors.WHITE_100};
  opacity: 0;
  transition: visibility 0s, opacity 0.2s ease-in-out;

  ${
    isVisible &&
    `
      visibility: visible;
      opacity: 1;
    `
  }
`
);
const StyledGroupsModalBox = styled.div`
  overflow-y: auto;
  max-height: 32.4rem;
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
  width: 10rem;
  height: 10rem;
  border-radius: 1.2rem;
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
  font-size: 1.6rem;
`;

const StyledGroupContainer = styled.div`
  position: relative;
  width: 100%;
  height: 15%;
`;

const StyledGroup = styled.div<{ isActive: boolean }>(
  ({ theme, isActive }) => `
  display: flex;
  gap: 2rem;
  align-items: center;
  padding: 2rem;
  margin: 1.2rem;
  text-decoration: none;
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
  font-size: 1.6rem;
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
  font-size: 1.6rem;
  width: 100%;
  padding: 2rem;

  &:hover {
    transform: scale(1.05);
    transition: all 0.2s linear;
  }
`;

const StyledButtonText = styled.p`
  font-size: 2rem;
`;

const StyledLeaveGroupButton = styled.button`
  display: flex;
  align-items: center;
  cursor: pointer;
  gap: 1.2rem;
  font-size: 1.6rem;
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
  StyledGroupsModalContainer,
  StyledGroupsModalBox,
  StyledSettingIcon,
  StyledGroupsModalIcon,
  StyledGroupProfile,
  StyledGroupFirstCharacter,
  StyledGroupName,
  StyledGroupContainer,
  StyledGroup,
  StyledGroupIconGroup,
  StyledParticipateNewGroupButton,
  StyledCreateNewGroupButton,
  StyledButtonText,
  StyledLeaveGroupButton,
  StyledPlusImage,
  StyledLeaveImage
};
