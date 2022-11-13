import { StyledContainer } from '@/pages/LandingPage/LandingPage.styles';

import { useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import {
  getLocalStorageItem,
  saveLocalStorageItem,
  getSessionStorageItem,
  removeSessionStorageItem
} from '@/utils/storage';
import { signin } from '@/api/auth';
import { getDefaultGroup } from '@/api/group';
import LandingMainSection from '@/pages/LandingPage/components/LandingMainSection/LandingMainSection';
import LandingServiceIntroductionSection from '@/pages/LandingPage/components/LandingServiceIntroductionSection/LandingServiceIntroductionSection';
import LandingFeatureIntroductionSection from '@/pages/LandingPage/components/LandingFeatureIntroductionSection/LandingFeatureIntroductionSection';
import { AxiosError } from 'axios';
import { AUTH_ERROR } from '@/constants/errorMessage';

function LandingPage() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();

  useEffect(() => {
    // TODO: 중복 로직해결
    // TODO: 다시 한 번 token을 가져오는 중복 로직해결
    const token = getLocalStorageItem<string>('token');
    if (token) {
      (async () => {
        try {
          const res = await getDefaultGroup();
          const { code: groupCode } = res.data;

          navigate(`/groups/${groupCode}`);
        } catch (err) {
          if (err instanceof AxiosError) {
            const statusCode = err.response?.status;

            if (statusCode === 404) {
              navigate('/init');
            }
          }
        }
      })();
    }
  }, []);

  useEffect(() => {
    const code = searchParams.get('code');
    if (code) {
      (async (code: string) => {
        try {
          const response = await signin(code);
          const { token } = response.data;

          // TODO: 위의 token을 string으로 타이핑 해주면 아래에서 안줘도 됨
          saveLocalStorageItem<string>('token', token);

          const redirectUrl = getSessionStorageItem<string>('redirectUrl');

          if (redirectUrl) {
            navigate(redirectUrl);
            removeSessionStorageItem('redirectUrl');

            return;
          }

          navigate('/init');
        } catch (err) {
          alert(AUTH_ERROR.FAILED_LOGIN);
          navigate('/');
        }
      })(code);
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

export default LandingPage;
