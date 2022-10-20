import { useTheme } from '@emotion/react';
import TextField from '../../../../components/TextField/TextField';
import { StyledName, StyledContainer, StyledRoleContainer } from './RoleMainRoles.styles';
import { EditRolesRequest } from '../../../../types/role';
import Spinner from '../../../../components/Spinner/Spinner';

type Props = {
  roles: EditRolesRequest['roles'];
};

function RoleMainRoles({ roles }: Props) {
  const theme = useTheme();

  return (
    <StyledContainer>
      {roles.length > 0 ? (
        roles.map((role, idx) => {
          return (
            <StyledRoleContainer key={`${idx}-${role}`}>
              <TextField
                padding="1.8rem 2.6rem"
                borderRadius="4rem"
                variant="outlined"
                colorScheme={theme.colors.PURPLE_100}
              >
                <StyledName>{role}</StyledName>
              </TextField>
            </StyledRoleContainer>
          );
        })
      ) : (
        <Spinner width="5%" placement="center" />
      )}
    </StyledContainer>
  );
}

export default RoleMainRoles;
