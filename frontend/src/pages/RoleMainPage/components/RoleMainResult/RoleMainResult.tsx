import Box from '../../../../components/Box/Box';
import Avatar from '../../../../components/Avatar/Avatar';
import FlexContainer from '../../../../components/FlexContainer/FlexContainer';

import {
  StyledTitle,
  StyledRole,
  StyledRoleContainer,
  StyledDateWrapper,
  StyledRoleResultWrapper,
  StyledDate,
  StyledEmptyText
} from './RoleMainResult.styles';
import Divider from '../../../../components/Divider/Divider';
import { useEffect, useState } from 'react';
import { GetRolesHistoriesResponse } from '../../../../types/role';
import { getGroupMembers } from '../../../../api/group';
import { useParams } from 'react-router-dom';
import { GroupInterface } from '../../../../types/group';
import { MemberInterface } from '../../../../types/group';

type Props = { rolesHistories: GetRolesHistoriesResponse };

function RoleMainResult({ rolesHistories }: Props) {
  const [activeIdx, setActiveIdx] = useState(0);
  // TODO: 임시
  const [groupMembers, setGroupMembers] = useState<Array<MemberInterface>>([]);
  const { groupCode } = useParams() as { groupCode: GroupInterface['code'] };
  const handleShowResult = (idx: number) => () => {
    setActiveIdx(idx);
  };

  // TODO: 임시
  useEffect(() => {
    (async () => {
      const res = await getGroupMembers(groupCode);
      setGroupMembers(res.data);
    })();
  }, []);

  return (
    <FlexContainer gap="3rem">
      <Box width="70%" height="50rem" padding="2rem">
        <StyledTitle>역할 정하기</StyledTitle>
        <Divider />
        <StyledRoleResultWrapper>
          {rolesHistories.roles.length > 0 ? (
            (() => {
              const currentRole = rolesHistories.roles[activeIdx].role;

              return currentRole.map(({ memberId, name }) => {
                const [currentMember] = groupMembers.filter(({ id }) => id === memberId);

                return (
                  <StyledRoleContainer key={memberId}>
                    <StyledRole>{name}</StyledRole>
                    <Avatar
                      profileUrl={currentMember.profileUrl}
                      name={currentMember.name}
                      width="8rem"
                    />
                  </StyledRoleContainer>
                );
              });
            })()
          ) : (
            <StyledEmptyText>첫 역할 정하기를 진행해보세요</StyledEmptyText>
          )}
        </StyledRoleResultWrapper>
      </Box>

      <Box width="30%" height="50rem" padding="2rem">
        <>
          <StyledTitle>이전 결과</StyledTitle>
          <Divider />
          {rolesHistories.roles.map((history, idx) => (
            <StyledDateWrapper key={`${idx}-${history}`} onClick={handleShowResult(idx)}>
              <StyledDate isActive={idx === activeIdx}>{history.date}</StyledDate>
            </StyledDateWrapper>
          ))}
        </>
      </Box>
    </FlexContainer>
  );
}

export default RoleMainResult;
