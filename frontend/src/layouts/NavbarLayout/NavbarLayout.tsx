import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { Group } from '../../types/group';
import NavbarHeader from './components/NavbarHeader/NavbarHeader';
import NavbarFooter from './components/NavbarFooter/NavbarFooter';
import { getGroups } from '../../api/group';

function NavbarLayout() {
  const [isLoading, setIsLoading] = useState(true);
  const [groups, setGroups] = useState<Array<Group>>([]);

  const { groupCode } = useParams() as {
    groupCode: Group['code'];
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
      <NavbarFooter groups={groups} groupCode={groupCode} />
    </>
  );
}

export default NavbarLayout;
