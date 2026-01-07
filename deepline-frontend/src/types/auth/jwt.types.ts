export interface CustomJwtPayload {
  sub: string;
  role: string | string[];
  exp: number;
  iat: number;
}
