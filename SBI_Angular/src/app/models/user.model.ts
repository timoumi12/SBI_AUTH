import { Role } from './role.enum';

export class User {
  id_user: number | undefined;
  email: string = '';
  password: string = '';
  firstname: string = '';
  lastname: string = '';
  jwt: string = '';
  role: Role = Role.USER;
}
