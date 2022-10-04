import { useEffect, useState } from 'react';
import Progress from '../../../../components/Progress/Progress';
import FlexContainer from '../../../../components/FlexContainer/FlexContainer';
import { GroupInterface, MemberInterface } from '../../../../types/group';
import { getGroupMembers } from '../../../../api/group';
import { StyledParticipantsStatus } from './AppointmentMainProgress.style';

interface Props {
  count: number;
  groupCode: GroupInterface['code'];
}

function AppointmentMainProgress({ count, groupCode }: Props) {
  const [groupMembers, setGroupMembers] = useState<Array<MemberInterface>>([]);
  const totalParticipants = groupMembers.length;
  const currentParticipants = count;

  useEffect(() => {
    const fetchGroupMembers = async () => {
      try {
        if (groupCode) {
          const res = await getGroupMembers(groupCode);

          setGroupMembers(res.data);
        }
      } catch (err) {
        if (err instanceof Error) {
          console.log(err);
        }
      }
    };

    fetchGroupMembers();
  }, [groupCode]);

  return (
    <FlexContainer flexDirection="column" alignItems="end">
      <Progress max={totalParticipants} value={currentParticipants} width="100%" />
      {/* // TODO: prettier 설정 보기 */}
      <StyledParticipantsStatus>
        {currentParticipants}
        명/
        {totalParticipants}명
      </StyledParticipantsStatus>
    </FlexContainer>
  );
}

export default AppointmentMainProgress;
