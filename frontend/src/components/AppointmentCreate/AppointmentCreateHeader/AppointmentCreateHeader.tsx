import styled from '@emotion/styled';

function AppointmentCreateHeader() {
  return (
    <>
      <StyledHeader>약속을 생성해주세요 🥳</StyledHeader>
    </>
  );
}

const StyledHeader = styled.header`
  font-size: 4rem;
`;

export default AppointmentCreateHeader;
