export interface TokenPayloadResponse {
  sub: string;
  role: string;
  name: string;
  email: string;
  type: 'access' | string;
  exp: number;
}
