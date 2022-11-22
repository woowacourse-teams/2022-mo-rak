import { StyledContainer } from '@/pages/GroupInitPage/components/GroupInitForms/GroupInitForms.styles';
import GroupInitFormsCreateForm from '@/pages/GroupInitPage/components/GroupInitFormsCreateForm/GroupInitFormsCreateForm';
import GroupInitFormsParticipateForm from '@/pages/GroupInitPage/components/GroupInitFormsParticipateForm/GroupInitFormsParticipateForm';

function GroupInitForms() {
  return (
    <StyledContainer>
      <GroupInitFormsCreateForm />
      <GroupInitFormsParticipateForm />
    </StyledContainer>
  );
}

export default GroupInitForms;
