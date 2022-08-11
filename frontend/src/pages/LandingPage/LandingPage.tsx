import styled from '@emotion/styled';

import React, { RefObject, useEffect, useRef } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import {
  getLocalStorageItem,
  saveLocalStorageItem,
  removeLocalStorageItem,
  getSessionStorageItem,
  removeSessionStorageItem
} from '../../utils/storage';
import { signin } from '../../api/auth';
import { getDefaultGroup } from '../../api/group';
import LandingMainSection from '../../components/Landing/LandingMainSection/LandingMainSection';
import LandingIntroduceSection from '../../components/Landing/LandingIntroduceSection/LandingIntroduceSection';
import LandingServiceIntroduceSection from '../../components/Landing/LandingServiceIntroduceSection/LandingServiceIntroduceSection';

// TODO: 페이지 추상화
function LandingPage() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();

  const firstSection = useRef<HTMLTableSectionElement>(null);
  const secondSection = useRef<HTMLTableSectionElement>(null);
  const thirdSection = useRef<HTMLTableSectionElement>(null);

  const redirectUrl = getSessionStorageItem('redirectUrl');
  // TODO: 중복 로직해결
  // TODO: 다시 한 번 token을 가져오는
  const token = getLocalStorageItem('token');
  useEffect(() => {
    const fetchGetDefaultGroup = async () => {
      try {
        const { code: groupCode } = await getDefaultGroup();

        navigate(`/groups/${groupCode}`);
      } catch (err) {
        if (err instanceof Error) {
          const statusCode = err.message;
          if (statusCode === '401') {
            removeLocalStorageItem('token');
            navigate('/');

            return;
          }

          if (statusCode === '404') {
            navigate('/init');
            console.log('랜딩페이지에서 로그인을 했지만, 속해있는 그룹이 없습니다.');
          }
        }
      }
    };

    if (token) {
      fetchGetDefaultGroup();
    }
  }, []);

  useEffect(() => {
    // TODO: fetch하는 함수이지만 navigate도 해주고있다..근데 try..catch...사용하려면 이렇게밖에 못함
    // TODO: strict mode라서 로그인이 된 이후에도 요청을 다시 보내서 에러가 나온다.
    const fetchSignin = async (code: string) => {
      try {
        const { token } = await signin(code);

        saveLocalStorageItem<string>('token', token);

        if (redirectUrl) {
          navigate(redirectUrl);
          removeSessionStorageItem('redirectUrl');

          return;
        }

        navigate('/init');
      } catch (err) {
        alert('로그인에 실패하였습니다. 다시 시도해주세요');
        navigate('/');
      }
    };

    const code = searchParams.get('code');

    if (code) {
      fetchSignin(code);
    }
  }, []);

  const handleMoveToSection = (section: RefObject<HTMLTableSectionElement>) => () => {
    section.current?.scrollIntoView({ behavior: 'smooth' });
  };

  return (
    <StyledContainer>
      <LandingMainSection />
      <LandingIntroduceSection />
      <LandingServiceIntroduceSection />
    </StyledContainer>
  );
}

const StyledContainer = styled.div`
  scroll-snap-type: y mandatory;
  overflow-y: scroll;
  height: 100vh;
  width: 100%;
  font-family: 'Nanum Gothic', sans-serif;
`;

export default LandingPage;
