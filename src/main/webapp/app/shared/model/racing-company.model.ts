export interface IRacingCompany {
  id?: number;
  name?: string;
  gasPrice?: number;
  servicePrice?: number;
  street?: string;
  city?: string;
  state?: string;
  zipCode?: string;
  racingCode?: string;
  ownerLogin?: string;
  ownerId?: number;
}

export const defaultValue: Readonly<IRacingCompany> = {};
