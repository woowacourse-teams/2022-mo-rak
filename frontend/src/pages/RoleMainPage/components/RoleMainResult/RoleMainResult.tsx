import Box from '@/components/Box/Box';
import Avatar from '@/components/Avatar/Avatar';
import FlexContainer from '@/components/FlexContainer/FlexContainer';

import {
  StyledTitle,
  StyledRole,
  StyledRoleContainer,
  StyledDatesContainer,
  StyledRoleResultContainer,
  StyledDate,
  StyledEmptyText
} from '@/pages/RoleMainPage/components/RoleMainResult/RoleMainResult.styles';
import Divider from '@/components/Divider/Divider';
import { useState } from 'react';
import { GetRolesHistoriesResponse } from '@/types/role';
import useGroupMembersContext from '@/hooks/useGroupMembersContext';
import deletedUserImg from '@/assets/deleted-user.svg';

type Props = { rolesHistories: GetRolesHistoriesResponse };

function RoleMainResult({ rolesHistories }: Props) {
  const [activeIdx, setActiveIdx] = useState(0);
  const { groupMembers } = useGroupMembersContext();

  const handleShowResult = (idx: number) => () => {
    setActiveIdx(idx);
  };

  return (
    <FlexContainer gap="3rem">
      <Box width="70%" height="50rem" padding="2rem">
        <StyledTitle>역할 정하기</StyledTitle>
        <Divider />
        <StyledRoleResultContainer>
          {rolesHistories.roles.length > 0 ? (
            (() => {
              const currentRole = rolesHistories.roles[activeIdx].role;

              return currentRole.map(({ memberId, name }) => {
                const currentMember = groupMembers.find(({ id }) => id === memberId);

                return (
                  <StyledRoleContainer key={memberId}>
                    <StyledRole>{name}</StyledRole>
                    <Avatar
                      profileUrl={currentMember?.profileUrl ?? deletedUserImg}
                      name={currentMember?.name ?? '탈퇴 회원'}
                      width="8rem"
                    />
                  </StyledRoleContainer>
                );
              });
            })()
          ) : (
            <StyledEmptyText>첫 역할 정하기를 진행해보세요</StyledEmptyText>
          )}
        </StyledRoleResultContainer>
      </Box>

      <Box width="30%" height="50rem" padding="2rem">
        <>
          <StyledTitle>이전 결과</StyledTitle>
          <Divider />
          <StyledDatesContainer>
            {rolesHistories.roles.map((history, idx) => (
              <StyledDate
                key={`${idx}-${history}`}
                onClick={handleShowResult(idx)}
                isActive={idx === activeIdx}
              >
                {history.date}
              </StyledDate>
            ))}
          </StyledDatesContainer>
        </>
      </Box>
    </FlexContainer>
  );
}

export default RoleMainResult;
