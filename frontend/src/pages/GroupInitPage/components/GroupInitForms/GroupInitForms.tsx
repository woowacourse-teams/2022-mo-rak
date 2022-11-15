import { StyledBottomContainer } from '@/pages/GroupInitPage/components/GroupInitForms/GroupInitForms.styles';
import GroupInitFormsCreateForm from '@/pages/GroupInitPage/components/GroupInitFormsCreateForm/GroupInitFormsCreateForm';
import GroupInitFormsParticipateForm from '@/pages/GroupInitPage/components/GroupInitFormsParticipateForm/GroupInitFormsParticipateForm';

function GroupInitForms() {
  return (
    <StyledBottomContainer>
      <GroupInitFormsCreateForm />
      <GroupInitFormsParticipateForm />
    </StyledBottomContainer>
  );
}

export default GroupInitForms;
