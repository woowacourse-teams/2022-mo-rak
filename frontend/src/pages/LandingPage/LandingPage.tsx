import styled from '@emotion/styled';
import React, { useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import {
  getLocalStorageItem,
  saveLocalStorageItem,
  removeLocalStorageItem,
  getSessionStorageItem,
  removeSessionStorageItem
} from '../../utils/storage';
import Logo from '../../assets/logo.svg';
import GithubLogo from '../../assets/github-logo.svg';
import { signin } from '../../api/auth';
import { getDefaultGroup } from '../../api/group';

// TODO: 페이지 추상화
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
      <StyledLogo src={Logo} alt="logo" />
      <StyledLink
        href={`https://github.com/login/oauth/authorize?client_id=${process.env.GITHUB_CLIENT_ID}`}
      >
        {/* TODO: 네이밍 고민  */}
        {/* TODO: button 컴포넌트와 레이아웃이 같지만, div 태그를 사용해야하기 때문에, 스타일 컴포넌트로 새로 만들어줌 이야기해봐야할듯  */}
        <StyledLoginContainer>
          <StyledGithubLogo src={GithubLogo} alt="github-logo" />
          <StyledTitle>Github으로 로그인</StyledTitle>
        </StyledLoginContainer>
      </StyledLink>
    </StyledContainer>
  );
}

const StyledContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  /* TODO: padding으로 주는 게 맞을까? height로 줘도 될 것 같은데... */
  padding: 26.4rem 0;
  gap: 5.4rem;
`;

const StyledLink = styled.a`
  text-decoration: none;
`;

const StyledLogo = styled.img`
  width: 34.8em;
`;

const StyledGithubLogo = styled.img`
  width: 3.8rem;
`;

const StyledTitle = styled.p`
  color: white;
  padding: 2rem 0;
  font-size: 1.6rem;
`;

const StyledLoginContainer = styled.div(
  ({ theme }) => `
  display: flex;
  justify-content: center;
  gap: 2.4rem;
  width: 33.6rem;
  color: ${theme.colors.WHITE_100};
  background-color: ${theme.colors.BLACK_100};
  border-radius: 15px;
`
);

export default LandingPage;
