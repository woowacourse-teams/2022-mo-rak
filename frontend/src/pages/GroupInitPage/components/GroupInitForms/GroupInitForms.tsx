import { StyledBottomContainer } from '@/pages/GroupInitPage/components/GroupInitForms/GroupInitForms.styles';
import GroupCreateForm from '@/pages/GroupInitPage/components/GroupCreateForm/GroupCreateForm';
import GroupParticipateForm from '@/pages/GroupInitPage/components/GroupParticipateForm/GroupParticipateForm';

function GroupInitForms() {
  return (
    <StyledBottomContainer>
      <GroupCreateForm />
      <GroupParticipateForm />
    </StyledBottomContainer>
  );
}

export default GroupInitForms;
