import styled from '@emotion/styled';

const StyledGroupMembersContainer = styled.div`
  overflow-y: auto;
  height: 80%;
`;

const StyledMenuHeader = styled.div`
  font-size: 1.6rem;
  text-align: left;
  padding: 2rem 0;
`;

const StyledUsername = styled.div`
  font-size: 1.6rem;
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
  padding: 0.4rem 0;
`;

const StyledUsernameContainer = styled.div`
  width: 60%;
  overflow: hidden;
  display: flex;
`;

const StyledEditIcon = styled.img`
  width: 1.6rem;
  position: absolute;
  right: 10%;
  cursor: pointer;
`;

const StyledContainer = styled.div`
  height: 35%;
`;

export {
  StyledContainer,
  StyledMenuHeader,
  StyledUsername,
  StyledGroupMembersContainer,
  StyledEditIcon,
  StyledUsernameContainer
};
