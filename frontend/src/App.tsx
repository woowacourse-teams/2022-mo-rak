import React from 'react';
import { Route, Routes } from 'react-router-dom';
import PollCreatePage from './pages/PollCreatePage/PollCreatePage';
import PollProgressPage from './pages/PollProgressPage/PollProgressPage';
import HomePage from './pages/HomePage/HomePage';
import PollResultPage from './pages/PollResultPage/PollResultPage';
import GroupInitPage from './pages/GroupInitPage/GroupInitPage';
import FlexContainer from './components/common/FlexContainer/FlexContainer';
import PollContext from './contexts/PollContext';
import SidebarLayout from './components/SidebarLayout/SidebarLayout';
import MainPage from './pages/MainPage/MainPage';
import InvitationPage from './pages/InvitationPage/InvitationPage';

function App() {
  return (
    <FlexContainer>
      <PollContext>
        <Routes>
          <Route element={<SidebarLayout />}>
            <Route path="/create" element={<PollCreatePage />} />
            <Route path="/progress" element={<PollProgressPage />} />
            <Route path="/result" element={<PollResultPage />} />
            <Route path="/groups" element={<MainPage />}>
              <Route path=":groupCode" element={<MainPage />} />
            </Route>
          </Route>
          <Route path="/" element={<HomePage />} />
          <Route path="/init" element={<GroupInitPage />} />
          <Route path="/invite/:invitationCode" element={<InvitationPage />} />
          <Route path="/create" element={<PollCreatePage />} />
          <Route path="/progress" element={<PollProgressPage />} />
          <Route path="/result" element={<PollResultPage />} />
          <Route path="*" element={<div>error</div>} />
        </Routes>
      </PollContext>
    </FlexContainer>
  );
}

export default App;
