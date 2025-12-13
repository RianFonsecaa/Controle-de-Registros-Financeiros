export interface TokenPayloadResponse {
  iss: string;
  sub: string;
  role: string;
  name: string;
  type: 'access' | string;
  exp: number;
  iat: number;
}
