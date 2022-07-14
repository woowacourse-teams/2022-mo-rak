import React from 'react';
import { Route, Routes } from 'react-router-dom';
import PollCreatePage from './pages/PollCreatePage/PollCreatePage';
import PollProgressPage from './pages/PollProgressPage/PollProgressPage';
import PollResultPage from './pages/PollResultPage/PollResultPage';
import GroupInitPage from './pages/GroupInitPage/GroupInitPage';
import FlexContainer from './components/common/FlexContainer/FlexContainer';
import PollContext from './contexts/PollContext';
import SidebarLayout from './components/SidebarLayout/SidebarLayout';

function App() {
  return (
    <FlexContainer>
      <PollContext>
        <Routes>
          <Route element={<SidebarLayout />}>
            <Route path="/create" element={<PollCreatePage />} />
            <Route path="/progress" element={<PollProgressPage />} />
            <Route path="/result" element={<PollResultPage />} />
          </Route>
          <Route path="/groupInit" element={<GroupInitPage />} />
        </Routes>
      </PollContext>
    </FlexContainer>
  );
}

export default App;
