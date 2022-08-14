import React, { useState } from 'react';
import { Outlet, useNavigate, useParams } from 'react-router-dom';
import FlexContainer from '../@common/FlexContainer/FlexContainer';
import Sidebar from '../Sidebar/Sidebar';
import { GroupInterface } from '../../types/group';

function SidebarLayout() {
  const [clickedMenu, setClickedMenu] = useState('poll');

  const { groupCode } = useParams() as { groupCode: GroupInterface['code'] };
  const navigate = useNavigate();

  const handleSetClickedMenu = (menu: string) => () => {
    setClickedMenu(menu);
    navigate(`/groups/${groupCode}/${menu}`);
  };

  return (
    <FlexContainer>
      {/* TODO: handleSetClickedMenu 와 같이 넘겨줘도 될까? (on prefix) */}
      <Sidebar
        groupCode={groupCode}
        handleSetClickedMenu={handleSetClickedMenu}
        clickedMenu={clickedMenu}
      />
      <Outlet context={{ setClickedMenu }} />
    </FlexContainer>
  );
}

export default SidebarLayout;
