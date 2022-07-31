import React from 'react';
import { Route, Routes } from 'react-router-dom';
import PollMainPage from './pages/PollMainPage/PollMainPage';
import PollCreatePage from './pages/PollCreatePage/PollCreatePage';
import PollProgressPage from './pages/PollProgressPage/PollProgressPage';
import LandingPage from './pages/LandingPage/LandingPage';
import PollResultPage from './pages/PollResultPage/PollResultPage';
import GroupInitPage from './pages/GroupInitPage/GroupInitPage';
import FlexContainer from './components/common/FlexContainer/FlexContainer';
import SidebarLayout from './components/SidebarLayout/SidebarLayout';
import MainPage from './pages/MainPage/MainPage';
import InvitationPage from './pages/InvitationPage/InvitationPage';
import { PrivateRoute } from './routes/PrivateRoute';
import AppointmentMainPage from './pages/AppointmentMainPage/AppointmentMainPage';
import AppointmentCreatePage from './pages/AppointmentCreatePage/AppointmentCreatePage';

function App() {
  return (
    // TODO: default 그룹을 찾는 요청을 해서 그룹이 있는 지 없는 지 확인하는 Route 생성? (PASS)
    <FlexContainer>
      <Routes>
        <Route path="/">
          <Route index element={<LandingPage />} />
          <Route element={<PrivateRoute />}>
            <Route element={<SidebarLayout />}>
              <Route path="groups/:groupCode">
                <Route index element={<MainPage />} />
                <Route path="poll">
                  <Route index element={<PollMainPage />} />
                  <Route path="create" element={<PollCreatePage />} />
                  <Route path=":pollId/progress" element={<PollProgressPage />} />
                  <Route path=":pollId/result" element={<PollResultPage />} />
                </Route>
                <Route path="appointment">
                  <Route index element={<AppointmentMainPage />} />
                  <Route path="create" element={<AppointmentCreatePage />} />
                </Route>
              </Route>
            </Route>
            <Route path="init" element={<GroupInitPage />} />
          </Route>
          <Route path="invite/:invitationCode" element={<InvitationPage />} />
          <Route path="*" element={<div>error</div>} />
        </Route>
      </Routes>
    </FlexContainer>
  );
}

export default App;
