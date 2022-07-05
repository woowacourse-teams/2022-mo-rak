import React from 'react';
import Sidebar from './components/common/Sidebar/Sidebar';
import PollCreatePage from './pages/PollCreatePage/PollCreatePage';
import FlexContainer from './components/common/FlexContainer/FlexContainer';

function App() {
  return (
    <FlexContainer>
      <Sidebar />
      <PollCreatePage />
    </FlexContainer>
  );
}

export default App;
