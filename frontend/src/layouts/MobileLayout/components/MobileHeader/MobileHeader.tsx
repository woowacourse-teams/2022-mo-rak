import {
  StyledContainer,
  StyledLogo,
  StyledGroupFirstCharacter,
  StyledWrapper,
  StyledName,
  StyledCurrentGroupContainer
} from './MobileHeader.styles';
import Logo from '../../../../assets/logo.svg';
import { useNavigate } from 'react-router-dom';
import { GroupInterface } from '../../../../types/group';

interface Props {
  groupCode: GroupInterface['code'];
  groups: Array<GroupInterface>;
}

function MobileHeader({ groups, groupCode }: Props) {
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
          <StyledWrapper>
            <StyledGroupFirstCharacter>{currentGroup.name[0]}</StyledGroupFirstCharacter>
          </StyledWrapper>
        </StyledCurrentGroupContainer>
      )}
    </StyledContainer>
  );
}

export default MobileHeader;
