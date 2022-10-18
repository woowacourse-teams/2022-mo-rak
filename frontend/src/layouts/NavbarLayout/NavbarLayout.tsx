import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { GroupInterface } from '../../types/group';
import NavbarHeader from './components/NavbarHeader/NavbarHeader';
import NavbarMenu from './components/NavbarMenu/NavbarMenu';
import { getGroups } from '../../api/group';

function NavbarLayout() {
  const [isLoading, setIsLoading] = useState(true);
  const [groups, setGroups] = useState<Array<GroupInterface>>([]);

  const { groupCode } = useParams() as {
    groupCode: GroupInterface['code'];
  };

  useEffect(() => {
    (async () => {
      try {
        const res = await getGroups();
        setGroups(res.data);
        setIsLoading(false);
      } catch (err) {
        setIsLoading(true);
      }
    })();
  }, [groupCode]);

  if (isLoading) return <div>로딩중</div>;

  return (
    <>
      <NavbarHeader groups={groups} groupCode={groupCode} />
      <NavbarMenu groups={groups} groupCode={groupCode} />
    </>
  );
}

export default NavbarLayout;
