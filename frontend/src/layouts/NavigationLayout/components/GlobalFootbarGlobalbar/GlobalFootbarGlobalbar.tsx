import {
  StyledContainer,
  StyledLogo,
  StyledGroupFirstCharacter,
  StyledProfileContainer,
  StyledName,
  StyledCurrentGroupContainer
} from './GlobalFootbarGlobalbar.styles';
import Logo from '../../../../assets/logo.svg';
import { useNavigate } from 'react-router-dom';
import { Group } from '../../../../types/group';

type Props = {
  groupCode: Group['code'];
  groups: Array<Group>;
};

function GlobalFootbarGlobalbar({ groups, groupCode }: Props) {
  const navigate = useNavigate();
  const currentGroup = groups.find((group) => group.code === groupCode);

  const handleNavigate = (location: string) => () => {
    navigate(location);
  };

  return (
    <StyledContainer>
      <StyledLogo src={Logo} alt={Logo} onClick={handleNavigate(`/groups/${groupCode}`)} />
      {currentGroup && (
        <StyledCurrentGroupContainer>
          <StyledName>🎉 {currentGroup.name}</StyledName>
          <StyledProfileContainer>
            <StyledGroupFirstCharacter>{currentGroup.name[0]}</StyledGroupFirstCharacter>
          </StyledProfileContainer>
        </StyledCurrentGroupContainer>
      )}
    </StyledContainer>
  );
}

export default GlobalFootbarGlobalbar;
