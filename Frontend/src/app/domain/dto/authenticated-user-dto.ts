import { UserRole } from "../model/user-role";

export interface AuthenticatedUserDto {
    email: string,
    // password: string,
    fullname: string,
    token: string,
    role: UserRole
}