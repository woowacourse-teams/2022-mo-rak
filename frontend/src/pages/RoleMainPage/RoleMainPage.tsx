import { StyledContainer } from '@/pages/RoleMainPage/RoleMainPage.styles';
import RoleMainHeader from '@/pages/RoleMainPage/components/RoleMainHeader/RoleMainHeader';
import RoleMainProgress from '@/pages/RoleMainPage/components/RoleMainProgress/RoleMainProgress';
import RoleMainResult from '@/pages/RoleMainPage/components/RoleMainResult/RoleMainResult';
import { getRolesHistories } from '@/api/role';
import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { Group } from '@/types/group';

function RoleMainPage() {
  const { groupCode } = useParams() as { groupCode: Group['code'] };
  const [rolesHistories, setRolesHistories] = useState({
    roles: []
  });

  const fetchRolesHistories = async () => {
    const res = await getRolesHistories(groupCode);
    setRolesHistories(res.data);
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
