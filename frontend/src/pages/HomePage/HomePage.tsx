import styled from '@emotion/styled';
import React, { useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { saveLocalStorageItem, getLocalStorageItem } from '../../utils/localStorage';
import Logo from '../../assets/logo.svg';
import GithubLogo from '../../assets/githubLogo.svg';
import { signin } from '../../api/auth';

function HomePage() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const token = getLocalStorageItem('token');

  useEffect(() => {
    if (token) {
      navigate('/init');
    }
  }, []);

  useEffect(() => {
    // TODO: fetch하는 함수이지만 navigate도 해주고있다..근데 try..catch...사용하려면 이렇게밖에 못함
    // TODO: strict mode라서 로그인이 된 이후에도 요청을 다시 보내서 에러가 나온다.
    const fetchSignin = async (code: string) => {
      try {
        const { token } = await signin(code);

        saveLocalStorageItem<string>('token', token);
        navigate('/groups');
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
      <a href="https://github.com/login/oauth/authorize?client_id=f67a30d27afefe8b241f">
        {/* TODO: 네이밍 고민  */}
        {/* TODO: button 컴포넌트와 레이아웃이 같지만, div 태그를 사용해야하기 때문에, 스타일 컴포넌트로 새로 만들어줌 이야기해봐야할듯  */}
        <StyledLoginContainer>
          <StyledGithubLogo src={GithubLogo} alt="github-logo" />
          <StyledTitle>Github으로 로그인</StyledTitle>
        </StyledLoginContainer>
      </a>
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

export default HomePage;
