import React from 'react';
import { Route, Routes } from 'react-router-dom';
import Sidebar from './components/common/Sidebar/Sidebar';
import PollMainPage from './pages/PollMainPage/PollMainPage';
import PollCreatePage from './pages/PollCreatePage/PollCreatePage';
import PollProgressPage from './pages/PollProgressPage/PollProgressPage';
import PollResultPage from './pages/PollResultPage/PollResultPage';
import FlexContainer from './components/common/FlexContainer/FlexContainer';
import PollContext from './contexts/PollContext';

function App() {
  return (
    <FlexContainer>
      <Sidebar />
      <PollContext>
        <Routes>
          <Route path="/" element={<PollMainPage />} />
          <Route path="/create" element={<PollCreatePage />} />
          <Route path="/poll/:id/progress" element={<PollProgressPage />} />
          <Route path="/poll/:id/result" element={<PollResultPage />} />
        </Routes>
      </PollContext>
    </FlexContainer>
  );
}

export default App;
