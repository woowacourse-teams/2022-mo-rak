import styled from '@emotion/styled';

import { useEffect } from 'react';
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
import LandingServiceIntroductionSection from '../../components/Landing/LandingServiceIntroductionSection/LandingServiceIntroductionSection';
import LandingFeatureIntroductionSection from '../../components/Landing/LandingFeatureIntroductionSection/LandingFeatureIntroductionSection';

function LandingPage() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();

  useEffect(() => {
    const fetchGetDefaultGroup = async () => {
      try {
        const res = await getDefaultGroup();
        const { code: groupCode } = res.data;

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
    // TODO: 중복 로직해결
    // TODO: 다시 한 번 token을 가져오는
    const token = getLocalStorageItem('token');

    if (token) {
      fetchGetDefaultGroup();
    }
  }, []);

  useEffect(() => {
    // TODO: fetch하는 함수이지만 navigate도 해주고있다..근데 try..catch...사용하려면 이렇게밖에 못함
    // TODO: strict mode라서 로그인이 된 이후에도 요청을 다시 보내서 에러가 나온다.
    const fetchSignin = async (code: string) => {
      try {
        const response = await signin(code);
        const { token } = response.data;

        saveLocalStorageItem<string>('token', token);

        const redirectUrl = getSessionStorageItem('redirectUrl');

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

  return (
    <StyledContainer>
      <LandingMainSection id="main-section" />
      <LandingServiceIntroductionSection id="service-introduction-section" />
      <LandingFeatureIntroductionSection id="feature-introduction-section" />
    </StyledContainer>
  );
}

const StyledContainer = styled.div`
  scroll-snap-type: y mandatory;
  overflow-y: scroll;
  height: 100vh;
  width: 100%;
  font-family: 'Nanum Gothic', sans-serif;
  scroll-behavior: smooth;
`;

export default LandingPage;
