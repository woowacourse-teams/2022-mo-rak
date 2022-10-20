import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { Group } from '../../../../types/group';
import GlobalFootbarGlobalbar from '../GlobalFootbarGlobalbar/GlobalFootbarGlobalbar';
import GlobalFootbarFootbar from '../GlobalFootbarFootbar/GlobalFootbarFootbar';
import { getGroups } from '../../../../api/group';

function GlobalFootbar() {
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
      <GlobalFootbarGlobalbar groups={groups} groupCode={groupCode} />
      <GlobalFootbarFootbar groups={groups} groupCode={groupCode} />
    </>
  );
}

export default GlobalFootbar;
