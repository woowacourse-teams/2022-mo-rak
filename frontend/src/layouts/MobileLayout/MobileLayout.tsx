import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { GroupInterface } from '../../types/group';
import MobileHeader from './components/MobileHeader/MobileHeader';
import MobileMenu from './components/MobileMenu/MobileMenu';
import { getGroups } from '../../api/group';

function MobileLayout() {
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
      <MobileHeader groups={groups} groupCode={groupCode} />
      <MobileMenu groups={groups} groupCode={groupCode} />
    </>
  );
}

export default MobileLayout;
