import { StyledContainer } from './RoleMainPage.styles';
import RoleMainHeader from './components/RoleMainHeader/RoleMainHeader';
import RoleMainProgress from './components/RoleMainProgress/RoleMainProgress';
import RoleMainResult from './components/RoleMainResult/RoleMainResult';
import { getRolesHistories } from '../../api/role';
import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { GroupInterface } from '../../types/group';

function RoleMainPage() {
  const { groupCode } = useParams() as { groupCode: GroupInterface['code'] };
  const [rolesHistories, setRolesHistories] = useState({
    roles: []
  });

  const fetchRolesHistories = async () => {
    try {
      const res = await getRolesHistories(groupCode);
      setRolesHistories(res.data);
    } catch (err) {}
  };

  useEffect(() => {
    fetchRolesHistories();
  }, []);

  return (
    <StyledContainer>
      <RoleMainHeader />
      <RoleMainProgress onClickAllocateRolesButton={fetchRolesHistories} />
      <RoleMainResult rolesHistories={rolesHistories} />
    </StyledContainer>
  );
}

export default RoleMainPage;
