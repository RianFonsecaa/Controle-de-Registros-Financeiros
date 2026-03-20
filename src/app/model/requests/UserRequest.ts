export type UserRole = 'ADMIN' | 'USER';

export interface UserRequest {
  name: string;
  login: string;
  password?: string;
  role: UserRole;
}
