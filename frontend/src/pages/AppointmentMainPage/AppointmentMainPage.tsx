import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

function AppointmentMainPage() {
  const navigate = useNavigate();

  // 임시
  useEffect(() => {
    navigate('create');
  }, []);

  return <div> 약속잡기 메인페이지입니다</div>;
}

export default AppointmentMainPage;
