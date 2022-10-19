import styled from '@emotion/styled';

const StyledGroupMembersContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 1.6rem;
  overflow-y: auto;
  max-height: 36rem;
`;

const StyledMenuHeader = styled.div`
  font-size: 1.6rem;
  text-align: left;
  margin-bottom: 2rem;
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

export {
  StyledMenuHeader,
  StyledUsername,
  StyledGroupMembersContainer,
  StyledEditIcon,
  StyledUsernameContainer
};