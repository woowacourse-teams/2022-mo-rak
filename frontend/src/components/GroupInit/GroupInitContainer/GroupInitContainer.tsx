import React from 'react';
import Box from '../../common/Box/Box';
import FlexContainer from '../../common/FlexContainer/FlexContainer';
import GroupCreateForm from '../GroupCreateForm/GroupCreateForm';
import GroupParticipateForm from '../GroupParticipateForm/GroupParticipateForm';

function GroupInitContainer() {
  return (
    <Box width="60rem" minHeight="51.6rem" padding="8.4rem 3.2rem">
      <FlexContainer flexDirection="column" gap="6.8rem">
        <GroupCreateForm />
        <GroupParticipateForm />
      </FlexContainer>
    </Box>
  );
}

export default GroupInitContainer;
