import React from 'react';
import { Route, Routes } from 'react-router-dom';
import PollMainPage from './pages/PollMainPage/PollMainPage';
import PollCreatePage from './pages/PollCreatePage/PollCreatePage';
import PollProgressPage from './pages/PollProgressPage/PollProgressPage';
import LandingPage from './pages/LandingPage/LandingPage';
import PollResultPage from './pages/PollResultPage/PollResultPage';
import GroupInitPage from './pages/GroupInitPage/GroupInitPage';
import FlexContainer from './components/common/FlexContainer/FlexContainer';
import PollContext from './contexts/PollContext';
import SidebarLayout from './components/SidebarLayout/SidebarLayout';
import MainPage from './pages/MainPage/MainPage';
import InvitationPage from './pages/InvitationPage/InvitationPage';
import { PrivateRoute } from './routes/PrivateRoute';

function App() {
  return (
    // TODO: default 그룹을 찾는 요청을 해서 그룹이 있는 지 없는 지 확인하는 Route 생성?
    <FlexContainer>
      <PollContext>
        <Routes>
          <Route path="/" element={<LandingPage />} />
          <Route element={<PrivateRoute />}>
            <Route element={<SidebarLayout />}>
              <Route path="/poll" element={<PollMainPage />} />
              <Route path="/poll/create" element={<PollCreatePage />} />
              <Route path="/poll/:id/progress" element={<PollProgressPage />} />
              <Route path="/poll/:id/result" element={<PollResultPage />} />
              <Route path="/groups" element={<MainPage />}>
                <Route path=":groupCode" element={<MainPage />} />
              </Route>
            </Route>
            <Route path="/init" element={<GroupInitPage />} />
          </Route>
          <Route path="/invite/:invitationCode" element={<InvitationPage />} />
          <Route path="*" element={<div>error</div>} />
        </Routes>
      </PollContext>
    </FlexContainer>
  );
}

export default App;
