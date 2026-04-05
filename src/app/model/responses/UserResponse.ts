import { UserRole } from '../requests/UserRequest';

export interface UserResponse {
  name: string;
  login: string;
  role: UserRole;
  ativo: Boolean;
}
